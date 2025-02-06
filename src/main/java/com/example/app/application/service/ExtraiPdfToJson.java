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
        File pdfFile = new File("C:\\sistemas\\teste\\AEROGALEAO-CEVIG-15-2025.pdf");

        PDFParser parser = new PDFParser(new RandomAccessBufferedFileInputStream(pdfFile));
        parser.parse();
        COSDocument cosDoc = parser.getDocument();
        PDFTextStripper pdfStripper = new PDFTextStripper();
        PDDocument pdDoc = new PDDocument(cosDoc);
        String jsonOutputPath = "C:\\sistemas\\teste\\tabelas_extraidas.json";
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
