package com.example.app.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "SIMPG_COVENANTS_HISTORICO", schema = "temp")
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
    @CreationTimestamp
    @Column(name = "DT_IMPORTA")
    private LocalDateTime DT_IMPORTA;
    @Column(name = "NO_ARQUIVO")
    private String NO_ARQUIVO;

}
