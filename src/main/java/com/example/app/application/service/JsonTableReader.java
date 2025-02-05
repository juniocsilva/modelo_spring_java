package br.gov.caixa.cedip.extrainfopdf.servico;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


public class JsonTableReader {
    public static void main(String[] args) {
        String jsonFilePath = "tabelas_extraidas.json";

        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();

            // Lendo o JSON
            FileReader reader = new FileReader(jsonFilePath);
            List<Map<String, Object>> tablesData = gson.fromJson(reader, listType);
            reader.close();

            // Iterando pelos elementos do JSON
            for (Map<String, Object> pageData : tablesData) {
                int pageNumber = ((Double) pageData.get("page")).intValue(); // Convertendo para inteiro
                System.out.println("Página: " + pageNumber);
                boolean captura = false;
                List<List<String>> tables = (List<List<String>>) pageData.get("tables");
                for (List<String> row : tables) {
                    if (captura) {
                        Object []items = row.toArray();
                        for (Object item : items) {
                            System.out.println(String.join(" | ", String.valueOf(item), " | ")); // Exibir como tabela formatada
                        }


                    } if (row.get(0).equals("Item de Acompanhamento")) {
                        captura = true;
                    }
                }
                System.out.println("------------");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
