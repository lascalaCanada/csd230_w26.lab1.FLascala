package csd230.lab1.controller;

import csd230.lab1.entities.SmartPhoneEntity;
import csd230.lab1.exception.SmartPhoneNotFoundException;
import csd230.lab1.repositories.SmartPhoneRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for smartphones. It returns JSON instead of Thymeleaf pages.
 */
@RestController
@RequestMapping("/api/rest/smartphones")
@Tag(name = "Smartphones", description = "CRUD operations for smartphones")
public class SmartPhoneRestController {

    private final SmartPhoneRepository smartPhoneRepository;

    public SmartPhoneRestController(SmartPhoneRepository smartPhoneRepository) {
        this.smartPhoneRepository = smartPhoneRepository;
    }

    @GetMapping
    @Operation(summary = "List all smartphones")
    @ApiResponse(responseCode = "200", description = "List returned")
    public List<SmartPhoneEntity> getAll() {
        return smartPhoneRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one item by id")
    @ApiResponse(responseCode = "200", description = "Item found")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public SmartPhoneEntity getOne(@PathVariable Long id) {
        return smartPhoneRepository.findById(id).orElseThrow(() -> new SmartPhoneNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new item")
    @ApiResponse(responseCode = "201", description = "Item created")
    public SmartPhoneEntity create(@RequestBody SmartPhoneEntity newItem) {
        // Clear the id so the database always creates a new row
        newItem.setId(null);
        return smartPhoneRepository.save(newItem);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing item")
    @ApiResponse(responseCode = "200", description = "Item updated")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public SmartPhoneEntity update(@PathVariable Long id, @RequestBody SmartPhoneEntity newItem) {
        SmartPhoneEntity smartPhone = smartPhoneRepository.findById(id).orElseThrow(() -> new SmartPhoneNotFoundException(id));
        smartPhone.setBrand(newItem.getBrand());
        smartPhone.setDescription(newItem.getDescription());
        smartPhone.setPrice(newItem.getPrice());
        smartPhone.setCopies(newItem.getCopies());
        smartPhone.setScreenSize(newItem.getScreenSize());
        smartPhone.setBatteryCapacity(newItem.getBatteryCapacity());
        return smartPhoneRepository.save(smartPhone);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an item")
    @ApiResponse(responseCode = "204", description = "Item deleted")
    @ApiResponse(responseCode = "404", description = "Item not found")
    @ApiResponse(responseCode = "409", description = "Item is still used by a cart or an order")
    public void delete(@PathVariable Long id) {
        // deleteById does nothing when the id is missing, so check first to return a 404
        if (!smartPhoneRepository.existsById(id)) {
            throw new SmartPhoneNotFoundException(id);
        }
        smartPhoneRepository.deleteById(id);
    }
}
