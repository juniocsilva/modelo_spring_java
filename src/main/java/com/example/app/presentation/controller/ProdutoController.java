package com.example.app.presentation.controller;

import com.example.app.application.dto.ProdutoDTO;
import com.example.app.application.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.DeserializationFeature;

import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;

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

	@PatchMapping("/{id}")
	public ProdutoDTO atualizarParcial(@PathVariable Long id,
									@RequestBody Map<String, Object> campos, HttpServletRequest request) {
		ProdutoDTO produtoAtual = produtoService.buscarPorId(id);

		merge(campos, produtoAtual, request);

		return atualizar(id, produtoAtual);
	}

	private void merge(Map<String, Object> dadosOrigem, ProdutoDTO produtoDestino,
					   HttpServletRequest request) {
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

			ProdutoDTO produtoOrigem = objectMapper.convertValue(dadosOrigem, ProdutoDTO.class);

			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(ProdutoDTO.class, nomePropriedade);
				field.setAccessible(true);

				Object novoValor = ReflectionUtils.getField(field, produtoOrigem);

				ReflectionUtils.setField(field, produtoDestino, novoValor);
			});
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}
}
