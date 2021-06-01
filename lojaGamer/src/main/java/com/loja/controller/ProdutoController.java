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

import com.loja.model.Produto;
import com.loja.repository.ProdutoRepository;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping
	public ResponseEntity<List<Produto>> listar() {
		return ResponseEntity.ok(produtoRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
		return produtoRepository.findById(id)
				.map(produto -> ResponseEntity.ok(produto))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produto>> listarPorNome(@PathVariable String nome) {
		return ResponseEntity.ok(produtoRepository.findByNomeContainingIgnoreCase(nome));
	}
	
	@PostMapping
	public ResponseEntity<Produto> criarNovoProduto(@RequestBody @Valid Produto produto) {
		if(produto.getId() != null) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody @Valid Produto produto) {
		if(!produtoRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		
		produto.setId(id);
		
		return ResponseEntity.ok(produtoRepository.save(produto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		if(!produtoRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		
		produtoRepository.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}
}
