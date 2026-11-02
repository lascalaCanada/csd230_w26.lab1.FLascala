package csd230.lab1.controller;

import csd230.lab1.entities.MagazineEntity;
import csd230.lab1.exception.MagazineNotFoundException;
import csd230.lab1.repositories.MagazineRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for magazines. It returns JSON instead of Thymeleaf pages.
 */
@RestController
@RequestMapping("/api/rest/magazines")
@Tag(name = "Magazines", description = "CRUD operations for magazines")
public class MagazineRestController {

    private final MagazineRepository magazineRepository;

    public MagazineRestController(MagazineRepository magazineRepository) {
        this.magazineRepository = magazineRepository;
    }

    @GetMapping
    @Operation(summary = "List all magazines", description = "Also includes disc magazines, because a DiscMag is a Magazine")
    @ApiResponse(responseCode = "200", description = "List returned")
    public List<MagazineEntity> getAll() {
        return magazineRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one item by id")
    @ApiResponse(responseCode = "200", description = "Item found")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public MagazineEntity getOne(@PathVariable Long id) {
        return magazineRepository.findById(id).orElseThrow(() -> new MagazineNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new item")
    @ApiResponse(responseCode = "201", description = "Item created")
    public MagazineEntity create(@RequestBody MagazineEntity newItem) {
        // Clear the id so the database always creates a new row
        newItem.setId(null);
        return magazineRepository.save(newItem);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing item")
    @ApiResponse(responseCode = "200", description = "Item updated")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public MagazineEntity update(@PathVariable Long id, @RequestBody MagazineEntity newItem) {
        MagazineEntity magazine = magazineRepository.findById(id).orElseThrow(() -> new MagazineNotFoundException(id));
        magazine.setTitle(newItem.getTitle());
        magazine.setPrice(newItem.getPrice());
        magazine.setCopies(newItem.getCopies());
        magazine.setOrderQty(newItem.getOrderQty());
        magazine.setCurrentIssue(newItem.getCurrentIssue());
        return magazineRepository.save(magazine);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an item")
    @ApiResponse(responseCode = "204", description = "Item deleted")
    @ApiResponse(responseCode = "404", description = "Item not found")
    @ApiResponse(responseCode = "409", description = "Item is still used by a cart or an order")
    public void delete(@PathVariable Long id) {
        // deleteById does nothing when the id is missing, so check first to return a 404
        if (!magazineRepository.existsById(id)) {
            throw new MagazineNotFoundException(id);
        }
        magazineRepository.deleteById(id);
    }
}
