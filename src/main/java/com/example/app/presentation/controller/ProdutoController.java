package com.example.app.presentation.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.application.dto.ProdutoDTO;
import com.example.app.application.exception.EntidadeEmUsoException;
import com.example.app.application.exception.EntidadeNaoEncontradaException;
import com.example.app.application.service.ProdutoService;
import com.example.app.domain.model.Produto;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
	
	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping
	public ResponseEntity<List<ProdutoDTO>> getProduto() {

		return ResponseEntity.ok(produtoService.getTodosProdutos());
	}
	
	@GetMapping("/{id}")
	public ProdutoDTO buscar(@PathVariable Long id) {
		
			return produtoService.buscarPorId(id);
			
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoDTO salvar(@RequestBody ProdutoDTO produtoDTO) {

		return produtoService.salvar(produtoDTO);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		produtoService.excluir(id);
			
	}
	
	@PutMapping("/{id}")
	public ProdutoDTO atualizar(@PathVariable Long id,
			@RequestBody ProdutoDTO produto) {
		
			ProdutoDTO produtoAtual = produtoService.buscarPorId(id);
			BeanUtils.copyProperties(produto, produtoAtual, "id");
			return produtoService.salvar(produtoAtual);
	}
}
