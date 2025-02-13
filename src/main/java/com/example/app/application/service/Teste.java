package com.example.app.application.service;

import java.io.File;
import java.io.IOException;

public class Teste {
    public static void main(String[] args) {

        String folderProcessados = "C:/sistemas/teste/arquivos_processados/"; // Caminho da pasta com os PDFs

//		String jsonSaida = "/Users/junio/Downloads/sistemas/teste/json_final/";
//		String folderPath = "/Users/junio/Downloads/sistemas/teste/json_intermediario"; // Caminho da pasta com os PDFs

        File folder = new File(folderProcessados);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("A pasta especificada não existe ou não é um diretório.");
            return;
        }

        // Listando arquivos PDF na pasta
        File[] jsonFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

        if (jsonFiles == null || jsonFiles.length == 0) {
            System.out.println("Nenhum arquivo PDF encontrado na pasta.");
            return;
        }

        // Processando cada arquivo PDF
        //String nomeAnt = "";
        for (File jsonFile : jsonFiles) {
            //System.out.println("Lendo: " + jsonFile.getName());

            try {
                String[] idRt = jsonFile.getName().split("-");

                int tam = idRt.length;
                int tamUnidade = idRt[tam - 3].length();
                if (tamUnidade > 90) {
                    tamUnidade = 90;
                }
                String[] ano = idRt[tam - 1].split("\\.");

                String nuRT = "RT " + idRt[tam - 3].substring(0, tamUnidade) + " " + idRt[tam - 2] + "/" + ano[0];
                String nomeArquivo = jsonFile.getName();
                System.out.println("('" + nuRT + "'),");
//                if (nuRT.equals("RT GIGOV_RE 4/2024") || nuRT.equals("RT GIGOV_RE 5/2024") || nuRT.equals("RT GIGOVSP 29/2024")) {
//                    System.out.println( nuRT + " -> "  + nomeArquivo);
//                }


            } catch (Exception e) {
                System.err.println("Erro ao ler o arquivo " + jsonFile.getName() + ": " + e.getMessage());
            }
        }
    }


}
