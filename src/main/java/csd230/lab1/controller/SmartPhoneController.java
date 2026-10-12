package csd230.lab1.controller;

import csd230.lab1.entities.SmartPhoneEntity;
import csd230.lab1.repositories.SmartPhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller class handling HTTP requests for viewing available smartphones.
 * The add, edit and delete pages are restricted to admins in WebSecurityConfig.
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

    /**
     * Shows the empty form to create a smartphone.
     */
    @GetMapping("/add")
    public String showAddForm() {
        return "smartphoneAdd";
    }

    /**
     * Receives the form and saves the new smartphone.
     */
    @PostMapping("/add")
    public String addSmartPhone(@RequestParam("description") String description,
                                @RequestParam("brand") String brand,
                                @RequestParam("screenSize") double screenSize,
                                @RequestParam("batteryCapacity") int batteryCapacity,
                                @RequestParam("price") double price,
                                @RequestParam("copies") int copies) {
        smartPhoneRepository.save(new SmartPhoneEntity(price, description, brand, screenSize, batteryCapacity, copies));
        return "redirect:/smartphones";
    }

    /**
     * Shows the form already filled with the data of the smartphone being edited.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        SmartPhoneEntity phone = smartPhoneRepository.findById(id).orElse(null);

        // Unknown id: just go back to the list
        if (phone == null) {
            return "redirect:/smartphones";
        }

        model.addAttribute("phone", phone);
        return "smartphoneEdit";
    }

    /**
     * Receives the edited data and updates the existing smartphone.
     */
    @PostMapping("/edit/{id}")
    public String updateSmartPhone(@PathVariable("id") Long id,
                                   @RequestParam("description") String description,
                                   @RequestParam("brand") String brand,
                                   @RequestParam("screenSize") double screenSize,
                                   @RequestParam("batteryCapacity") int batteryCapacity,
                                   @RequestParam("price") double price,
                                   @RequestParam("copies") int copies) {
        SmartPhoneEntity phone = smartPhoneRepository.findById(id).orElse(null);

        if (phone != null) {
            phone.setDescription(description);
            phone.setBrand(brand);
            phone.setScreenSize(screenSize);
            phone.setBatteryCapacity(batteryCapacity);
            phone.setPrice(price);
            phone.setCopies(copies);
            smartPhoneRepository.save(phone);
        }

        return "redirect:/smartphones";
    }

    /**
     * Deletes a smartphone. It has to be a POST, never a plain link.
     */
    @PostMapping("/delete/{id}")
    public String deleteSmartPhone(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            smartPhoneRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            // The smartphone is still referenced by a cart or an order, so the database refuses to delete it.
            redirectAttributes.addFlashAttribute("error",
                    "This smartphone cannot be deleted because it is in a cart or in an order.");
        }
        return "redirect:/smartphones";
    }
}