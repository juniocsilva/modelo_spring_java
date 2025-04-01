package com.example.app.application.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FabricanteDTO {
	@NotNull(groups = Groups.FabricanteId.class)
	private Long id;
	@NotBlank
	private String nome;
	@NotBlank
	private String industria;
}
