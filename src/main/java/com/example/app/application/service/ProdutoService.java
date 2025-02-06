package com.example.app.application.service;

import com.example.app.application.dto.FabricanteDTO;
import com.example.app.application.dto.ProdutoDTO;
import com.example.app.application.exception.*;
import com.example.app.domain.model.Fabricante;
import com.example.app.domain.model.Produto;
import com.example.app.domain.repository.ProdutoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {
	
	private static final String MSG_PRODUTO_EM_USO = "Produto de código %d não pode ser removido, pois está em uso";

	private static final String MSG_PRODUTO_NAO_ENCONTRADO = "Não existe um cadastro de produto com código %d";

	@Autowired
	ProdutoRepository produtoRepository;

	@Autowired
	FabricanteService fabricanteService;

	@Autowired
	private ModelMapper modelMapper;

	public List<ProdutoDTO> getTodosProdutos() {
		return produtoRepository.findAll()
				.stream()
				.map(produto -> modelMapper.map(produto, ProdutoDTO.class))
				.collect(Collectors.toList());
	}

	public ProdutoDTO buscarPorId(Long id) {
		
			Produto produto =  produtoRepository.findById(id)
					.orElseThrow(() -> new ProdutoNaoEncontradoException(
							String.format(MSG_PRODUTO_NAO_ENCONTRADO, id)));	
			ProdutoDTO produtoDTO = modelMapper.map(produto, ProdutoDTO.class);
			return produtoDTO;
	}

	public ProdutoDTO salvar (ProdutoDTO produtoDTO) {

		try {
			Long fabricanteId = produtoDTO.getFabricante().getId();

			FabricanteDTO fabricante = fabricanteService.buscarPorId(fabricanteId);

			produtoDTO.setFabricante(fabricante);
			Produto produto = modelMapper.map(produtoDTO, Produto.class);
			produtoRepository.save(produto);
			return modelMapper.map(produto, ProdutoDTO.class);
		} catch (Exception e) {
			throw (new NegocioException(String.format("Não existe um cadastro de fabricante com código %d",produtoDTO.getFabricante().getId()),e));
		}

		 
	}

	public void excluir(Long id) {
		try {
		
			if (!produtoRepository.existsById(id)) {
				throw new ProdutoNaoEncontradoException(
						String.format(MSG_PRODUTO_NAO_ENCONTRADO, id));
	        }
			produtoRepository.deleteById(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_PRODUTO_EM_USO, id));
		}
	}
}
