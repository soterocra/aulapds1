package com.soterocra.aulapds1.resources;

import com.soterocra.aulapds1.dto.CategoryDTO;
import com.soterocra.aulapds1.dto.OrderDTO;
import com.soterocra.aulapds1.dto.OrderItemDTO;
import com.soterocra.aulapds1.entities.Order;
import com.soterocra.aulapds1.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderResource {

    @Autowired
    private OrderService service;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> findAll() {
        List<OrderDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
        OrderDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value = "/{id}/items")
    public ResponseEntity<List<OrderItemDTO>> findItems(@PathVariable Long id) {
        List<OrderItemDTO> list = service.findItems(id);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/myorders")
    public ResponseEntity<List<OrderDTO>> findByClient() {
        List<OrderDTO> dto = service.findByClient();
        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/client/{clientId}")
    public ResponseEntity<List<OrderDTO>> findByClientId(@PathVariable Long clientId) {
        List<OrderDTO> dto = service.findByClientId(clientId);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody List<OrderItemDTO> dto) {
        OrderDTO orderDTO = service.placeOrder(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(orderDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(orderDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> update(@PathVariable Long id, @RequestBody OrderDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }
}