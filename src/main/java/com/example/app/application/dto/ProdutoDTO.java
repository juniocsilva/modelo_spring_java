package com.example.app.application.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoDTO {
	
	private Long id;
	//@NotNull
	//@NotEmpty
	@NotBlank
	private String nome;
	//@DecimalMin("0")
	@PositiveOrZero
	private float preco;
	@Valid
	@ConvertGroup(from = Default.class, to = Groups.FabricanteId.class)
	@NotNull
	private FabricanteDTO fabricante;

	
}
