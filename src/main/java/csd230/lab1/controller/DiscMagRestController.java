package csd230.lab1.controller;

import csd230.lab1.entities.DiscMagEntity;
import csd230.lab1.exception.DiscMagNotFoundException;
import csd230.lab1.repositories.DiscMagRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for disc magazines. It returns JSON instead of Thymeleaf pages.
 */
@RestController
@RequestMapping("/api/rest/discmags")
@Tag(name = "Disc Magazines", description = "CRUD operations for disc magazines")
public class DiscMagRestController {

    private final DiscMagRepository discMagRepository;

    public DiscMagRestController(DiscMagRepository discMagRepository) {
        this.discMagRepository = discMagRepository;
    }

    @GetMapping
    @Operation(summary = "List all disc magazines")
    @ApiResponse(responseCode = "200", description = "List returned")
    public List<DiscMagEntity> getAll() {
        return discMagRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one item by id")
    @ApiResponse(responseCode = "200", description = "Item found")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public DiscMagEntity getOne(@PathVariable Long id) {
        return discMagRepository.findById(id).orElseThrow(() -> new DiscMagNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new item")
    @ApiResponse(responseCode = "201", description = "Item created")
    public DiscMagEntity create(@RequestBody DiscMagEntity newItem) {
        // Clear the id so the database always creates a new row
        newItem.setId(null);
        return discMagRepository.save(newItem);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing item")
    @ApiResponse(responseCode = "200", description = "Item updated")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public DiscMagEntity update(@PathVariable Long id, @RequestBody DiscMagEntity newItem) {
        DiscMagEntity discMag = discMagRepository.findById(id).orElseThrow(() -> new DiscMagNotFoundException(id));
        discMag.setTitle(newItem.getTitle());
        discMag.setPrice(newItem.getPrice());
        discMag.setCopies(newItem.getCopies());
        discMag.setOrderQty(newItem.getOrderQty());
        discMag.setCurrentIssue(newItem.getCurrentIssue());
        discMag.setHasDisc(newItem.isHasDisc());
        return discMagRepository.save(discMag);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an item")
    @ApiResponse(responseCode = "204", description = "Item deleted")
    @ApiResponse(responseCode = "404", description = "Item not found")
    @ApiResponse(responseCode = "409", description = "Item is still used by a cart or an order")
    public void delete(@PathVariable Long id) {
        // deleteById does nothing when the id is missing, so check first to return a 404
        if (!discMagRepository.existsById(id)) {
            throw new DiscMagNotFoundException(id);
        }
        discMagRepository.deleteById(id);
    }
}
