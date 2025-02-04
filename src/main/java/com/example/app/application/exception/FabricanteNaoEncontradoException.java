package com.example.app.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.NOT_FOUND)
public class FabricanteNaoEncontradoException extends EntidadeNaoEncontradaException {
	private static final long serialVersionUID = 1L;

    public FabricanteNaoEncontradoException(String mensagem) {

        super(mensagem);
    }

    public FabricanteNaoEncontradoException(Long id) {

        super(String.format("Não existe um cadastro de fabricante com código %d", id));
    }
}
