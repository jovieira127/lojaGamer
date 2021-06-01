package com.loja.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.loja.model.Categoria;
import com.loja.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping
	public ResponseEntity<List<Categoria>> listar() {
		return ResponseEntity.ok(categoriaRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
		return categoriaRepository.findById(id)
				.map(categoria -> ResponseEntity.ok(categoria))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/descricao/{descricao}")
	public ResponseEntity<List<Categoria>> buscarPorCategoria(@PathVariable String descricao) {
		return ResponseEntity.ok(categoriaRepository.findByDescricaoContainingIgnoreCase(descricao));
	}
	
	@PostMapping
	public ResponseEntity<Categoria> criarNovaCategoria(@RequestBody @Valid Categoria categoria) {
		if(categoria.getId() != null) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaRepository.save(categoria));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Categoria> atualizar(@RequestBody @Valid Categoria categoria, @PathVariable Long id) {
		if(!categoriaRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		
		categoria.setId(id);
		
		return ResponseEntity.ok(categoriaRepository.save(categoria));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		if(!categoriaRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		
		categoriaRepository.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}
}
