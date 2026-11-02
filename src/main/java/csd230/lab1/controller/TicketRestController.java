package csd230.lab1.controller;

import csd230.lab1.entities.TicketEntity;
import csd230.lab1.exception.TicketNotFoundException;
import csd230.lab1.repositories.TicketRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for tickets. It returns JSON instead of Thymeleaf pages.
 */
@RestController
@RequestMapping("/api/rest/tickets")
@Tag(name = "Tickets", description = "CRUD operations for tickets")
public class TicketRestController {

    private final TicketRepository ticketRepository;

    public TicketRestController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    @Operation(summary = "List all tickets")
    @ApiResponse(responseCode = "200", description = "List returned")
    public List<TicketEntity> getAll() {
        return ticketRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one item by id")
    @ApiResponse(responseCode = "200", description = "Item found")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public TicketEntity getOne(@PathVariable Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new item")
    @ApiResponse(responseCode = "201", description = "Item created")
    public TicketEntity create(@RequestBody TicketEntity newItem) {
        // Clear the id so the database always creates a new row
        newItem.setId(null);
        return ticketRepository.save(newItem);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing item")
    @ApiResponse(responseCode = "200", description = "Item updated")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public TicketEntity update(@PathVariable Long id, @RequestBody TicketEntity newItem) {
        TicketEntity ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
        ticket.setDescription(newItem.getDescription());
        ticket.setPrice(newItem.getPrice());
        return ticketRepository.save(ticket);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an item")
    @ApiResponse(responseCode = "204", description = "Item deleted")
    @ApiResponse(responseCode = "404", description = "Item not found")
    @ApiResponse(responseCode = "409", description = "Item is still used by a cart or an order")
    public void delete(@PathVariable Long id) {
        // deleteById does nothing when the id is missing, so check first to return a 404
        if (!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException(id);
        }
        ticketRepository.deleteById(id);
    }
}
