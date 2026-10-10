package csd230.lab1.controller;

import csd230.lab1.entities.OrderEntity;
import csd230.lab1.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller handling the order confirmation page.
 */
@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Renders a finished order by its ID. This is where the checkout redirect lands.
     */
    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        OrderEntity order = orderRepository.findById(id).orElse(null);

        // Unknown ID: send the user back to the shop instead of an empty page
        if (order == null) {
            return "redirect:/books";
        }

        model.addAttribute("order", order);
        return "orderDetails"; // looks for orderDetails.html in templates
    }
}