package com.example.app.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class RelatorioTecnico {
    String id;
    List<ItemRelatorio> items;
}
