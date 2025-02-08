package com.example.app.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "SIMPG_COVENANTS_HISTORICO")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRelatorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long NU_ID;
    private String NU_RT;
    @Column(length = 4096)
    private String NO_CLAUSULA;
    @Column(length = 4096)
    private String DE_DESCRICAO;
    private String DE_STATUS;
    @Column(length = 4096)
    private String DE_SITUACAO;
}
