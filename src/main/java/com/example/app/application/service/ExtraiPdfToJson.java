package com.example.app.application.service;

import com.google.gson.Gson;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtraiPdfToJson {
    public static void main(String[] args) throws IOException {
        String folderPath = "/Users/junio/Downloads/sistemas/teste";
        //String folderPath = "C:/sistemas/teste"; // Caminho da pasta com os PDFs

        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("A pasta especificada não existe ou não é um diretório.");
            return;
        }

        // Listando arquivos PDF na pasta
        File[] pdfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

        if (pdfFiles == null || pdfFiles.length == 0) {
            System.out.println("Nenhum arquivo PDF encontrado na pasta.");
            return;
        }

        // Processando cada arquivo PDF
        for (File pdfFile : pdfFiles) {
            System.out.println("Lendo: " + pdfFile.getName());
            try {
                fazExtracao(pdfFile);
                System.out.println("Conteúdo extraído:\n");

            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo " + pdfFile.getName() + ": " + e.getMessage());
            }
        }

    }

    private static void fazExtracao(File pdfFile) throws IOException{
        //PDFParser parser = new PDFParser(new RandomAccessBufferedFileInputStream(pdfFile));
        //parser.parse();
        //COSDocument cosDoc = parser.getDocument();
        //PDFTextStripper pdfStripper = new PDFTextStripper();
        //pdfStripper.setSuppressDuplicateOverlappingText(true);

        PDDocument pdDoc = PDDocument.load(pdfFile);

        //String jsonOutputPath = "C:/sistemas/teste/json_intermediario/" + pdfFile.getName() + ".json";
        String jsonOutputPath = "/Users/junio/Downloads/sistemas/teste/json_intermediario/" + pdfFile.getName() + ".json";
        List<Map<String, Object>> extractedTables = new ArrayList<>();

        try {

            ObjectExtractor extractor = new ObjectExtractor(pdDoc);
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();

            for (PageIterator it = extractor.extract(); it.hasNext(); ) {
                Page page = it.next();
                List<Table> tables = sea.extract(page);

                Map<String, Object> pageData = new HashMap<>();
                pageData.put("page", page.getPageNumber());
                List<List<String>> tableData = new ArrayList<>();

                for (Table table : tables) {
                    for (List<RectangularTextContainer> row : table.getRows()) {
                        List<String> rowData = new ArrayList<>();
                        for (RectangularTextContainer cell : row) {
                            rowData.add(cell.getText());
                        }
                        tableData.add(rowData);
                    }
                }
                pageData.put("tables", tableData);
                extractedTables.add(pageData);
            }

            Gson gson = new Gson();
            try (FileWriter writer = new FileWriter(jsonOutputPath)) {
                gson.toJson(extractedTables, writer);
            }

            System.out.println("Tabelas extraídas e salvas em " + jsonOutputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
