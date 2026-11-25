package csd230.lab1.controller;

import csd230.lab1.entities.CartEntity;
import csd230.lab1.entities.UserEntity;
import csd230.lab1.repositories.CartRepository;
import csd230.lab1.repositories.ProductRepository;
import csd230.lab1.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

/**
 * REST controller for the cart of the signed-in user. It returns JSON instead of Thymeleaf pages.
 * The browser only sends a product id: the owner always comes from the logged-in user (Principal).
 * The MVC CartController (HTML redirects) is kept as it was.
 */
@RestController
@RequestMapping("/api/rest/carts")
@Tag(name = "Carts", description = "Cart of the signed-in user")
public class CartRestController {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartRestController(CartRepository cartRepository,
                              UserRepository userRepository,
                              ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // POST /api/rest/carts/mine/products/{productId}: add a product to my cart
    @PostMapping("/mine/products/{productId}")
    @Transactional
    @Operation(summary = "Add an existing product to the signed-in user's cart")
    public CartEntity addMine(@PathVariable Long productId, Principal principal) {
        UserEntity user = currentUser(principal, true);
        // Look up the real product first, so a wrong id gives 404 without creating a cart
        var product = productRepository.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        CartEntity cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new CartEntity();
            cart.setUser(user);
        }
        // The cart holds a Set, so adding the same product twice keeps just one
        cart.addProduct(product);
        return cartRepository.save(cart);
    }

    // The authenticated username is the owner; no client cart/user id is accepted.
    private UserEntity currentUser(Principal principal, boolean forUpdate) {
        UserEntity user = principal == null ? null : forUpdate
                ? userRepository.findForCartUpdate(principal.getName())
                : userRepository.findByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return user;
    }

    // GET /api/rest/carts/mine: read my cart (204 when I do not have one yet; nothing is created)
    @GetMapping("/mine")
    @Transactional(readOnly = true)
    @Operation(summary = "Read the signed-in user's cart; 204 if none has been created")
    public ResponseEntity<CartEntity> mine(Principal principal) {
        CartEntity cart = cartRepository.findByUser(currentUser(principal, false));
        return cart == null ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(cart);
    }

    // DELETE /api/rest/carts/mine/products/{productId}: remove a product from my cart
    @DeleteMapping("/mine/products/{productId}")
    @Transactional
    @Operation(summary = "Remove a product from the signed-in user's cart")
    public ResponseEntity<CartEntity> removeMine(@PathVariable Long productId, Principal principal) {
        UserEntity user = currentUser(principal, true);
        var product = productRepository.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        CartEntity cart = cartRepository.findByUser(user);
        if (cart == null) {
            return ResponseEntity.noContent().build();
        }
        // Only the membership is removed; the product itself stays in the inventory.
        // The cart owns the join table, so the change is saved when the transaction ends.
        cart.getProducts().remove(product);
        return ResponseEntity.ok(cart);
    }
}
