package com.example.app.application.service;

import com.example.app.application.dto.FabricanteDTO;
import com.example.app.application.exception.EntidadeEmUsoException;
import com.example.app.application.exception.EntidadeNaoEncontradaException;
import com.example.app.domain.model.Fabricante;
import com.example.app.domain.repository.FabricanteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FabricanteService {
	
	private static final String MSG_FABRICANTE_EM_USO = "Fabricante de código %d não pode ser removido, pois está em uso";

	private static final String MSG_FABRICANTE_NAO_ENCONTRADO = "Não existe um cadastro de fabricante com código %d";

	@Autowired
	FabricanteRepository fabricanteRepository;

	@Autowired
	private ModelMapper modelMapper;

	public List<FabricanteDTO> getTodosFabricantes() {
		return fabricanteRepository.findAll()
				.stream()
				.map(fabricante -> modelMapper.map(fabricante, FabricanteDTO.class))
				.collect(Collectors.toList());
	}

	public FabricanteDTO buscarPorId(Long id) {
		
			Fabricante fabricante =  fabricanteRepository.findById(id)
					.orElseThrow(() -> new EntidadeNaoEncontradaException(
							String.format(MSG_FABRICANTE_NAO_ENCONTRADO, id)));
        return modelMapper.map(fabricante, FabricanteDTO.class);
	}

	public FabricanteDTO salvar (FabricanteDTO fabricanteDTO) {
		Fabricante fabricante = modelMapper.map(fabricanteDTO, Fabricante.class);
		fabricanteRepository.save(fabricante);
		return modelMapper.map(fabricante, FabricanteDTO.class);
		 
	}

	public void excluir(Long id) {
		try {
		
			if (!fabricanteRepository.existsById(id)) {
				throw new EntidadeNaoEncontradaException(
						String.format(MSG_FABRICANTE_NAO_ENCONTRADO, id));
	        }
			fabricanteRepository.deleteById(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_FABRICANTE_EM_USO, id));
		}
	}
}
