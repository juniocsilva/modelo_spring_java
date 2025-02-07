package com.example.app.application.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class ProdutoDTO {
	
	private Long id;
	//@NotNull
	//@NotEmpty
	@NotBlank(groups = Groups.CadastroProduto.class)
	private String nome;
	//@DecimalMin("0")
	@PositiveOrZero(groups = Groups.CadastroProduto.class)
	private float preco;
	@Valid
	@NotNull(groups = Groups.CadastroProduto.class)
	private FabricanteDTO fabricante;

	
}
