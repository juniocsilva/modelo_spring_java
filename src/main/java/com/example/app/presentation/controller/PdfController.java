package com.example.app.presentation.controller;

import com.example.app.application.dto.FabricanteDTO;
import com.example.app.application.service.FabricanteService;
import com.example.app.domain.model.ItemRelatorio;
import com.example.app.domain.model.RelatorioTecnico;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pdf")
public class PdfController {
	@GetMapping
	public ResponseEntity<?> getFabricante() {
		fase1();
		return ResponseEntity.ok("Gerado com sucesso");
	}

	private void fase1() {
		String jsonSaida = "c:/sistemas/teste/json_final/";
		String folderPath = "C:/sistemas/teste/json_intermediario"; // Caminho da pasta com os PDFs

		File folder = new File(folderPath);

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
		for (File jsonFile : jsonFiles) {
			System.out.println("Lendo: " + jsonFile.getName());
			try {
				String [] idRt = jsonFile.getName().split("-");
				System.out.println(idRt[3]);
				String [] ano = idRt[3].split("\\.");
				System.out.println(ano);
				String nomeArquivo = "RT " + idRt[1] + " " + idRt[2] + "/" + ano[0];
				carregaDados(nomeArquivo, jsonFile.getAbsolutePath(), jsonSaida + jsonFile.getName());
				System.out.println("Conteúdo extraído:\n");

			} catch (IOException e) {
				System.err.println("Erro ao ler o arquivo " + jsonFile.getName() + ": " + e.getMessage());
			}
		}
	}

	private static void  carregaDados(String nomeJson, String jsonFilePath, String jsonSaida) throws IOException {
		try {
			Gson gson = new Gson();
			Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();

			// Lendo o JSON
			FileReader reader = new FileReader(jsonFilePath);
			List<Map<String, Object>> tablesData = gson.fromJson(reader, listType);
			reader.close();
			RelatorioTecnico relatorioTecnico = new RelatorioTecnico();
			relatorioTecnico.setId(nomeJson);
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
						itemRelatorio.setNU_RT(nomeJson);
						itemRelatorio.setNO_CLAUSULA(nomeJson);
						itemRelatorio.setNO_CLAUSULA(String.valueOf(items[0]));
						itemRelatorio.setDE_DESCRICAO(String.valueOf(items[1]));
						String [] status = items[2].toString().split("\\r");
						StringBuilder situacao = new StringBuilder();
						for (int i = 1; i < status.length; i++) {
							situacao.append(status[i]).append("\r");
						}
						itemRelatorio.setDE_STATUS(String.valueOf(status[0]));

						itemRelatorio.setDE_SITUACAO(situacao.toString());
						listItemRelatorio.add((itemRelatorio));

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
