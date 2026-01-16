package com.bachoco.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
/*
import com.bachoco.model.ProveedoresRequest;
import com.bachoco.model.ProveedoresResponse;
import com.bachoco.service.usecase.ProveedoresUseCase;*/

@RestController
@RequestMapping("/v1/proveedores")
public class ProveedoresController {
/*
    private final ProveedoresUseCase proveedoresUseCase;

    public ProveedoresController(ProveedoresUseCase proveedoresUseCase) {
        this.proveedoresUseCase = proveedoresUseCase;
    }

    @PostMapping
    public ResponseEntity<ProveedoresResponse> save(@RequestBody ProveedoresRequest req) {
        ProveedoresResponse response = this.proveedoresUseCase.save(req);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody ProveedoresRequest req) {
        this.proveedoresUseCase.update(id, req);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProveedoresResponse>> findAll() {
        List<ProveedoresResponse> response = this.proveedoresUseCase.findAll();
        return ResponseEntity.ok(response);
    }

    /**
     * Filtra proveedores por silo (igual que tu EmpleadoExternoController)
     * Ej: GET /v1/proveedores/filter-silo?siloId=2
     */
    /*@GetMapping("/filter-silo")
    public ResponseEntity<List<ProveedoresResponse>> findAllBySilo(@RequestParam Integer siloId) {
        List<ProveedoresResponse> response = this.proveedoresUseCase.findAllBySilo(siloId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        this.proveedoresUseCase.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }*/
}
