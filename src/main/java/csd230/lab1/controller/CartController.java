package csd230.lab1.controller;

import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.OrderEntity;
import csd230.lab1.entities.ProductEntity;
import csd230.lab1.repositories.CartRepository;
import csd230.lab1.repositories.OrderRepository;
import csd230.lab1.repositories.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Controller handling cart management and order creation using session persistence.
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

    /**
     * Retrieves existing session cart or persists a new one in the database.
     */
    private CartEntity getOrCreateCart(HttpSession session) {
        Long cartId = (Long) session.getAttribute("cartId");
        CartEntity cart = null;

        if (cartId != null) {
            cart = cartRepository.findById(cartId).orElse(null);
        }

        if (cart == null) {
            cart = new CartEntity();
            cart = cartRepository.save(cart);
            session.setAttribute("cartId", cart.getId());
        }

        return cart;
    }

    /**
     * Renders shopping cart items.
     */
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        CartEntity cart = getOrCreateCart(session);
        cart.getProducts().size(); // Forces initialization of eager/lazy collection

        // (added) running total shown on the cart page
        double total = 0;
        for (ProductEntity product : cart.getProducts()) {
            total += product.getPrice();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("total", Math.round(total * 100.0) / 100.0);
        return "cartDetails";
    }

    /**
     * Adds a product to the session cart and persists the junction table record.
     */
    // (added) the class already maps to /cart, so the full URL is POST /cart/add
    @PostMapping("/add")
    @Transactional
    public String addToCart(@RequestParam("productId") Long productId, HttpSession session) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found ID: " + productId));

        CartEntity cart = getOrCreateCart(session);
        cart.getProducts().add(product);
        cartRepository.save(cart);

        return "redirect:/cart";
    }

    /**
     * Removes an item from the cart.
     */
    @GetMapping("/remove/{id}")
    @Transactional
    public String removeFromCart(@PathVariable("id") Long id, HttpSession session) {
        CartEntity cart = getOrCreateCart(session);
        cart.getProducts().removeIf(product -> product.getId().equals(id));
        cartRepository.save(cart);

        return "redirect:/cart";
    }

    /**
     * Processes checkout, builds OrderEntity, persists order details, and clears current cart.
     */
    @PostMapping("/checkout")
    @Transactional
    public String checkout(HttpSession session) {
        CartEntity cart = getOrCreateCart(session);

        if (cart.getProducts().isEmpty()) {
            return "redirect:/cart";
        }

        OrderEntity order = new OrderEntity();
        order.setOrderDate(LocalDateTime.now()); // (added) LocalDateTime, same type as OrderEntity

        double total = 0; // (added) sum of the item prices
        for (ProductEntity product : cart.getProducts()) {
            // Reduce stock: each product type (publication, laptop...) handles its own
            product.sellItem();
            total += product.getPrice();
            order.getProducts().add(product);
        }
        order.setTotalAmount(Math.round(total * 100.0) / 100.0); // (added) rounded to cents

        OrderEntity savedOrder = orderRepository.save(order);

        cart.getProducts().clear();
        cartRepository.save(cart);

        // (added) persists the new stock, then redirects so a refresh won't buy everything twice
        productRepository.saveAll(savedOrder.getProducts());
        return "redirect:/orders/" + savedOrder.getId();
    }
}