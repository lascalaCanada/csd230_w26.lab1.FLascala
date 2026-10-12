package csd230.lab1.controller;

import csd230.lab1.entities.LaptopEntity;
import csd230.lab1.repositories.LaptopRepository;
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
 * Controller class handling HTTP requests for viewing available laptops.
 * The add, edit and delete pages are restricted to admins in WebSecurityConfig.
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

    /**
     * Shows the empty form to create a laptop.
     */
    @GetMapping("/add")
    public String showAddForm() {
        return "laptopAdd";
    }

    /**
     * Receives the form and saves the new laptop.
     */
    @PostMapping("/add")
    public String addLaptop(@RequestParam("description") String description,
                            @RequestParam("brand") String brand,
                            @RequestParam("processor") String processor,
                            @RequestParam("ramGb") int ramGb,
                            @RequestParam("storageGb") int storageGb,
                            @RequestParam("price") double price,
                            @RequestParam("copies") int copies) {
        laptopRepository.save(new LaptopEntity(price, description, brand, ramGb, storageGb, processor, copies));
        return "redirect:/laptops";
    }

    /**
     * Shows the form already filled with the data of the laptop being edited.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        LaptopEntity laptop = laptopRepository.findById(id).orElse(null);

        // Unknown id: just go back to the list
        if (laptop == null) {
            return "redirect:/laptops";
        }

        model.addAttribute("laptop", laptop);
        return "laptopEdit";
    }

    /**
     * Receives the edited data and updates the existing laptop.
     */
    @PostMapping("/edit/{id}")
    public String updateLaptop(@PathVariable("id") Long id,
                               @RequestParam("description") String description,
                               @RequestParam("brand") String brand,
                               @RequestParam("processor") String processor,
                               @RequestParam("ramGb") int ramGb,
                               @RequestParam("storageGb") int storageGb,
                               @RequestParam("price") double price,
                               @RequestParam("copies") int copies) {
        LaptopEntity laptop = laptopRepository.findById(id).orElse(null);

        if (laptop != null) {
            laptop.setDescription(description);
            laptop.setBrand(brand);
            laptop.setProcessor(processor);
            laptop.setRamGb(ramGb);
            laptop.setStorageGb(storageGb);
            laptop.setPrice(price);
            laptop.setCopies(copies);
            laptopRepository.save(laptop);
        }

        return "redirect:/laptops";
    }

    /**
     * Deletes a laptop. It has to be a POST, never a plain link.
     */
    @PostMapping("/delete/{id}")
    public String deleteLaptop(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            laptopRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            // The laptop is still referenced by a cart or an order, so the database refuses to delete it.
            redirectAttributes.addFlashAttribute("error",
                    "This laptop cannot be deleted because it is in a cart or in an order.");
        }
        return "redirect:/laptops";
    }
}