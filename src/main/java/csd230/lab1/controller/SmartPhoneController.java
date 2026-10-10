package csd230.lab1.controller;

import csd230.lab1.entities.SmartPhoneEntity;
import csd230.lab1.repositories.SmartPhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller class handling HTTP requests for viewing available smartphones.
 */
@Controller
@RequestMapping("/smartphones")
public class SmartPhoneController {

    @Autowired
    private SmartPhoneRepository smartPhoneRepository;

    /**
     * Renders the list of smartphones available in the database.
     */
    @GetMapping
    public String listSmartPhones(Model model) {
        List<SmartPhoneEntity> smartphones = smartPhoneRepository.findAll();
        model.addAttribute("smartphones", smartphones);
        return "smartphones";
    }
}