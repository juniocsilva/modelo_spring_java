package com.example.app.presentation.controller;

import com.example.app.application.dto.FabricanteDTO;
import com.example.app.application.service.FabricanteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	public FabricanteDTO salvar(@RequestBody FabricanteDTO fabricanteDTO) {

		return fabricanteService.salvar(fabricanteDTO);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		fabricanteService.excluir(id);
			
	}
	
	@PutMapping("/{id}")
	public FabricanteDTO atualizar(@PathVariable Long id,
			@RequestBody FabricanteDTO fabricante) {
		
			FabricanteDTO fabricanteAtual = fabricanteService.buscarPorId(id);
			BeanUtils.copyProperties(fabricante, fabricanteAtual, "id");
			return fabricanteService.salvar(fabricanteAtual);
	}
}
