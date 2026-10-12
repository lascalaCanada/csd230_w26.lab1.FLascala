package csd230.lab1.controller;

import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.OrderEntity;
import csd230.lab1.entities.ProductEntity;
import csd230.lab1.entities.UserEntity;
import csd230.lab1.repositories.CartRepository;
import csd230.lab1.repositories.OrderRepository;
import csd230.lab1.repositories.ProductRepository;
import csd230.lab1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

/**
 * Controller handling cart management and order creation.
 * Each logged-in user has their own cart, linked to the users table.
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves the cart of the logged-in user or creates and persists a new one.
     * The Principal holds the username of whoever is logged in.
     */
    private CartEntity getOrCreateCart(Principal principal) {
        UserEntity user = userRepository.findByUsername(principal.getName());
        CartEntity cart = cartRepository.findByUser(user);

        // First visit of this user: create an empty cart and link it to them
        if (cart == null) {
            cart = new CartEntity();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        return cart;
    }

    /**
     * Renders shopping cart items.
     */
    @GetMapping
    public String viewCart(Principal principal, Model model) {
        CartEntity cart = getOrCreateCart(principal);
        cart.getProducts().size(); // Forces initialization of eager/lazy collection

        // Running total shown on the cart page
        double total = 0;
        for (ProductEntity product : cart.getProducts()) {
            total += product.getPrice();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("total", Math.round(total * 100.0) / 100.0);
        return "cartDetails";
    }

    /**
     * Adds a product to the user's cart and persists the junction table record.
     */
    // The class already maps to /cart, so the full URL is POST /cart/add
    @PostMapping("/add")
    @Transactional
    public String addToCart(@RequestParam("productId") Long productId, Principal principal) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found ID: " + productId));

        CartEntity cart = getOrCreateCart(principal);
        cart.getProducts().add(product);
        cartRepository.save(cart);

        return "redirect:/cart";
    }

    /**
     * Removes an item from the cart.
     */
    @GetMapping("/remove/{id}")
    @Transactional
    public String removeFromCart(@PathVariable("id") Long id, Principal principal) {
        CartEntity cart = getOrCreateCart(principal);
        cart.getProducts().removeIf(product -> product.getId().equals(id));
        cartRepository.save(cart);

        return "redirect:/cart";
    }

    /**
     * Processes checkout, builds OrderEntity, persists order details, and clears current cart.
     */
    @PostMapping("/checkout")
    @Transactional
    public String checkout(Principal principal) {
        CartEntity cart = getOrCreateCart(principal);

        if (cart.getProducts().isEmpty()) {
            return "redirect:/cart";
        }

        OrderEntity order = new OrderEntity();
        order.setOrderDate(LocalDateTime.now()); // LocalDateTime, same type as OrderEntity

        double total = 0; // Sum of the item prices
        for (ProductEntity product : cart.getProducts()) {
            // Reduce stock: each product type (publication, laptop...) handles its own
            product.sellItem();
            total += product.getPrice();
            order.getProducts().add(product);
        }
        order.setTotalAmount(Math.round(total * 100.0) / 100.0); // Rounded to cents

        OrderEntity savedOrder = orderRepository.save(order);

        cart.getProducts().clear();
        cartRepository.save(cart);

        // Persists the new stock, then redirects so a refresh won't buy everything twice
        productRepository.saveAll(savedOrder.getProducts());
        return "redirect:/orders/" + savedOrder.getId();
    }
}