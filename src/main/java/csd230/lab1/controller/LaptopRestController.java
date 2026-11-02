package csd230.lab1.controller;

import csd230.lab1.entities.LaptopEntity;
import csd230.lab1.exception.LaptopNotFoundException;
import csd230.lab1.repositories.LaptopRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for laptops. It returns JSON instead of Thymeleaf pages.
 */
@RestController
@RequestMapping("/api/rest/laptops")
@Tag(name = "Laptops", description = "CRUD operations for laptops")
public class LaptopRestController {

    private final LaptopRepository laptopRepository;

    public LaptopRestController(LaptopRepository laptopRepository) {
        this.laptopRepository = laptopRepository;
    }

    @GetMapping
    @Operation(summary = "List all laptops")
    @ApiResponse(responseCode = "200", description = "List returned")
    public List<LaptopEntity> getAll() {
        return laptopRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one item by id")
    @ApiResponse(responseCode = "200", description = "Item found")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public LaptopEntity getOne(@PathVariable Long id) {
        return laptopRepository.findById(id).orElseThrow(() -> new LaptopNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new item")
    @ApiResponse(responseCode = "201", description = "Item created")
    public LaptopEntity create(@RequestBody LaptopEntity newItem) {
        // Clear the id so the database always creates a new row
        newItem.setId(null);
        return laptopRepository.save(newItem);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing item")
    @ApiResponse(responseCode = "200", description = "Item updated")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public LaptopEntity update(@PathVariable Long id, @RequestBody LaptopEntity newItem) {
        LaptopEntity laptop = laptopRepository.findById(id).orElseThrow(() -> new LaptopNotFoundException(id));
        laptop.setBrand(newItem.getBrand());
        laptop.setDescription(newItem.getDescription());
        laptop.setPrice(newItem.getPrice());
        laptop.setCopies(newItem.getCopies());
        laptop.setRamGb(newItem.getRamGb());
        laptop.setStorageGb(newItem.getStorageGb());
        laptop.setProcessor(newItem.getProcessor());
        return laptopRepository.save(laptop);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an item")
    @ApiResponse(responseCode = "204", description = "Item deleted")
    @ApiResponse(responseCode = "404", description = "Item not found")
    @ApiResponse(responseCode = "409", description = "Item is still used by a cart or an order")
    public void delete(@PathVariable Long id) {
        // deleteById does nothing when the id is missing, so check first to return a 404
        if (!laptopRepository.existsById(id)) {
            throw new LaptopNotFoundException(id);
        }
        laptopRepository.deleteById(id);
    }
}
