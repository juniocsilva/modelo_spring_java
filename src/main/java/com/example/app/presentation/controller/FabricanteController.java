package com.example.app.presentation.controller;

import com.example.app.application.dto.FabricanteDTO;
import com.example.app.application.dto.ProdutoDTO;
import com.example.app.application.service.FabricanteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fabricante")
public class FabricanteController {
	
	@Autowired
	private FabricanteService fabricanteService;
	
	@GetMapping
	public ResponseEntity<List<FabricanteDTO>> getFabricante() {

		return ResponseEntity.ok(fabricanteService.getTodosFabricantes());
	}
	
	@GetMapping("/{id}")
	public FabricanteDTO buscar(@PathVariable Long id) {
		
			return fabricanteService.buscarPorId(id);
			
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FabricanteDTO salvar(@RequestBody @Valid FabricanteDTO fabricanteDTO) {

		return fabricanteService.salvar(fabricanteDTO);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		fabricanteService.excluir(id);
			
	}
	
	@PutMapping("/{id}")
	public FabricanteDTO atualizar(@PathVariable Long id,
			@RequestBody @Valid FabricanteDTO fabricante) {
		
			FabricanteDTO fabricanteAtual = fabricanteService.buscarPorId(id);
			BeanUtils.copyProperties(fabricante, fabricanteAtual, "id");
			return fabricanteService.salvar(fabricanteAtual);
	}

	@PatchMapping("/{id}")
	public FabricanteDTO atualizarParcial(@PathVariable Long id,
									   @RequestBody Map<String, Object> campos, HttpServletRequest request) {
		FabricanteDTO fabricanteAtual = fabricanteService.buscarPorId(id);

		merge(campos, fabricanteAtual, request);

		return atualizar(id, fabricanteAtual);
	}

	private void merge(Map<String, Object> dadosOrigem, FabricanteDTO fabricanteDestino,
					   HttpServletRequest request) {
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

			FabricanteDTO fabricanteOrigem = objectMapper.convertValue(dadosOrigem, FabricanteDTO.class);

			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(FabricanteDTO.class, nomePropriedade);
				field.setAccessible(true);

				Object novoValor = ReflectionUtils.getField(field, fabricanteOrigem);

				ReflectionUtils.setField(field, fabricanteDestino, novoValor);
			});
		} catch (IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}
}
