package com.example.app.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.app.application.dto.ProdutoDTO;
import com.example.app.application.exception.EntidadeEmUsoException;
import com.example.app.application.exception.EntidadeNaoEncontradaException;
import com.example.app.domain.model.Produto;
import com.example.app.domain.repository.ProdutoRepository;

@Service
public class ProdutoService {
	
	private static final String MSG_PRODUTO_EM_USO = "Produto de código %d não pode ser removido, pois está em uso";

	private static final String MSG_PRODUTO_NAO_ENCONTRADO = "Não existe um cadastro de produto com código %d";

	@Autowired
	ProdutoRepository produtoRepository;

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
					.orElseThrow(() -> new EntidadeNaoEncontradaException(
							String.format(MSG_PRODUTO_NAO_ENCONTRADO, id)));	
			ProdutoDTO produtoDTO = modelMapper.map(produto, ProdutoDTO.class);
			return produtoDTO;
	}

	public ProdutoDTO salvar (ProdutoDTO produtoDTO) {
		Produto produto = modelMapper.map(produtoDTO, Produto.class);
		produtoRepository.save(produto);
		return modelMapper.map(produto, ProdutoDTO.class);
		 
	}

	public void excluir(Long id) {
		try {
		
			if (!produtoRepository.existsById(id)) {
				throw new EntidadeNaoEncontradaException(
						String.format(MSG_PRODUTO_NAO_ENCONTRADO, id));
	        }
			produtoRepository.deleteById(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_PRODUTO_EM_USO, id));
		}
	}
}
