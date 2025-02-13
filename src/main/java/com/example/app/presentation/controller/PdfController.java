package com.example.app.presentation.controller;

import com.example.app.domain.model.ItemRelatorio;
import com.example.app.domain.model.RelatorioTecnico;
import com.example.app.domain.repository.ItemRelatorioRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pdf")
public class PdfController {

	@Autowired
	private ItemRelatorioRepository itemRelatorioRepository;

	@GetMapping
	public ResponseEntity<?> getFabricante() {
		fase1();
		return ResponseEntity.ok("Gerado com sucesso");
	}

	private void fase1() {
		String jsonSaida = "c:/sistemas/teste/json_final/";
		String folderPath = "C:/sistemas/teste/json_intermediario"; // Caminho da pasta com os PDFs
		String folderProcessados = "C:/sistemas/teste/arquivos_processados/"; // Caminho da pasta com os PDFs

//		String jsonSaida = "/Users/junio/Downloads/sistemas/teste/json_final/";
//		String folderPath = "/Users/junio/Downloads/sistemas/teste/json_intermediario"; // Caminho da pasta com os PDFs

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
		//String nomeAnt = "";
		for (File jsonFile : jsonFiles) {
			System.out.println("Lendo: " + jsonFile.getName());

			try {
				String[] idRt = jsonFile.getName().split("-");
				//nomeAnt = jsonFile.getName();
				int tam = idRt.length;
				int tamUnidade = idRt[tam - 3].length();
				if (tamUnidade > 90) {
					tamUnidade = 90;
				}
				String[] ano = idRt[tam - 1].split("\\.");
				System.out.println(ano);
				String nuRT = "RT " + idRt[tam - 3].substring(0, tamUnidade) + " " + idRt[tam - 2] + "/" + ano[0];
				String nomeArquivo = jsonFile.getName();
				carregaDados(nomeArquivo, nuRT, jsonFile.getAbsolutePath(), jsonSaida + jsonFile.getName());
				moverArquivo(jsonFile.getAbsolutePath(), folderProcessados + nomeArquivo );

			} catch (IOException e) {
				System.err.println("Erro ao ler o arquivo " + jsonFile.getName() + ": " + e.getMessage());
			}
		}
	}

	private void carregaDados(String nomeJson, String nuRT, String jsonFilePath, String jsonSaida) throws IOException {
		try {
			Gson gson = new Gson();
			Type listType = new TypeToken<List<Map<String, Object>>>() {
			}.getType();

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
				//System.out.println("Página: " + pageNumber);
				boolean captura = false;
				List<List<String>> tables = (List<List<String>>) pageData.get("tables");
				for (List<String> row : tables) {
					if (captura) {
						Object[] items = row.toArray();
						ItemRelatorio itemRelatorio = new ItemRelatorio();
						itemRelatorio.setNU_RT(nuRT);
						itemRelatorio.setNO_ARQUIVO(nomeJson);
						itemRelatorio.setNO_CLAUSULA(nomeJson.replace("\\r", " "));
						itemRelatorio.setNO_CLAUSULA(String.valueOf(items[0]).replace("\\r", " "));
						itemRelatorio.setDE_DESCRICAO(String.valueOf(items[1]).replace("\\r", " "));
						String[] status = items[2].toString().split("\\r");
						StringBuilder situacao = new StringBuilder();
						for (int i = 1; i < status.length; i++) {
							situacao.append(status[i]).append("\r");
						}
						itemRelatorio.setDE_STATUS(String.valueOf(status[0]).replace("\\r", " "));

						itemRelatorio.setDE_SITUACAO(situacao.toString().replace("\\r", " "));
						//listItemRelatorio.add((itemRelatorio));
						itemRelatorioRepository.save(itemRelatorio);

					}
					if (row.get(0).equals("Item de Acompanhamento")) {
						captura = true;
					}
				}
				//System.out.println("------------");
			}
//			ObjectMapper objectMapper = new ObjectMapper();
//			File file = new File(jsonSaida);
//			relatorioTecnico.setItems(listItemRelatorio);
//			// Escrevendo JSON no arquivo
//			objectMapper.writeValue(file, relatorioTecnico);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void moverArquivo(String origem, String destino) {
		// Caminho do arquivo original
		File arquivoOrigem = new File(origem);
		// Caminho do destino
		File arquivoDestino = new File(destino);

		// Verifica se o arquivo de origem existe
		if (!arquivoOrigem.exists()) {
			System.out.println("O arquivo de origem não existe!");
			return;
		}

		// Verifica se o diretório de destino existe, senão cria
		File diretorioDestino = new File(arquivoDestino.getParent());
		if (!diretorioDestino.exists()) {
			diretorioDestino.mkdirs(); // Cria os diretórios necessários
		}

		// Tenta mover o arquivo
		try {
			arquivoOrigem.renameTo(arquivoDestino);
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}
