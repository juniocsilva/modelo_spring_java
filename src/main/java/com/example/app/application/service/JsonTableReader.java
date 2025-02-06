package com.example.app.application.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.app.domain.model.ItemRelatorio;
import com.example.app.domain.model.RelatorioTecnico;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


public class JsonTableReader {
    public static void main(String[] args) {
//        String jsonFilePath = "/Users/junio/Downloads/tabelas_extraidas.json";
//        String jsonSaida = "/Users/junio/Downloads/relatorio.json";

        String jsonFilePath = "c:\\sistemas\\teste\\tabelas_extraidas.json";
        String jsonSaida = "c:\\sistemas\\teste\\relatorio.json";

        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();

            // Lendo o JSON
            FileReader reader = new FileReader(jsonFilePath);
            List<Map<String, Object>> tablesData = gson.fromJson(reader, listType);
            reader.close();
            RelatorioTecnico relatorioTecnico = new RelatorioTecnico();
            relatorioTecnico.setId("RT CEVIG 15/25");
            List<ItemRelatorio> listItemRelatorio = new ArrayList<ItemRelatorio>();
            // Iterando pelos elementos do JSON
            for (Map<String, Object> pageData : tablesData) {
                int pageNumber = ((Double) pageData.get("page")).intValue(); // Convertendo para inteiro
                System.out.println("Página: " + pageNumber);
                boolean captura = false;
                List<List<String>> tables = (List<List<String>>) pageData.get("tables");
                for (List<String> row : tables) {
                    if (captura) {
                        Object []items = row.toArray();
                        ItemRelatorio itemRelatorio = new ItemRelatorio();
                        itemRelatorio.setItem(String.valueOf(items[0]));
                        itemRelatorio.setDescricao(String.valueOf(items[1]));
                        itemRelatorio.setSituacao(String.valueOf(items[2]));
                        listItemRelatorio.add((itemRelatorio));
//                        for (Object item : items) {
//                            System.out.println(String.join(" | ", String.valueOf(item), " | ")); // Exibir como tabela formatada
//                        }


                    } if (row.get(0).equals("Item de Acompanhamento")) {
                        captura = true;
                    }
                }
                //System.out.println("------------");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(jsonSaida);
            relatorioTecnico.setItems(listItemRelatorio);
            // Escrevendo JSON no arquivo
            objectMapper.writeValue(file, relatorioTecnico);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
