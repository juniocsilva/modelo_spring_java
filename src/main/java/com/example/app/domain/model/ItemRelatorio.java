package com.example.app.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
//@Entity
//@Table(name = "temp.SIMPG_COVENANTS_HISTORICO")
public class ItemRelatorio {
    String NU_RT;
    String NO_CLAUSULA;
    String DE_DESCRICAO;
    String DE_STATUS;
    String DE_SITUACAO;
}
