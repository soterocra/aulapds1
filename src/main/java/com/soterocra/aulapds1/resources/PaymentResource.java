package com.soterocra.aulapds1.resources;

import com.soterocra.aulapds1.dto.PaymentDTO;
import com.soterocra.aulapds1.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/payments")
public class PaymentResource {

    @Autowired
    private PaymentService service;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> findAll() {
        List<PaymentDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<PaymentDTO> findById(@PathVariable Long id) {
        PaymentDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

}
