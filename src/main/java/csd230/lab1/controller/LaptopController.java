package csd230.lab1.controller;

import csd230.lab1.entities.LaptopEntity;
import csd230.lab1.repositories.LaptopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller class handling HTTP requests for viewing available laptops.
 */
@Controller
@RequestMapping("/laptops")
public class LaptopController {

    @Autowired
    private LaptopRepository laptopRepository;

    /**
     * Renders the list of laptops available in the database.
     */
    @GetMapping
    public String listLaptops(Model model) {
        List<LaptopEntity> laptops = laptopRepository.findAll();
        model.addAttribute("laptops", laptops);
        return "laptops";
    }
}