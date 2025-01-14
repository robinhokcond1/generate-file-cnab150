package com.example.picpay.controllers;

import com.example.picpay.models.cnab.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cnab")
public class CnabController {

    @PostMapping("/generate")
    public ResponseEntity<String> generateCnabFile(@RequestBody @Validated CnabRequest cnabRequest) {
        String userHome = System.getProperty("user.home");
        String filePath = userHome + "\\Downloads\\cnab.txt";

        try {
            // Gera os registros
            List<CnabRecord> records = generateRecords(cnabRequest.getRecords());

            // Escreve o arquivo CNAB
            try (FileWriter writer = new FileWriter(filePath)) {
                // Escreve o header
                writer.write(String.format("%-150s%n", cnabRequest.getHeader().toCnabLine()));

                // Escreve os registros
                for (CnabRecord record : records) {
                    String linha = record.toCnabLine();
                    System.out.println("Escrevendo linha CNAB: " + linha);
                    writer.write(String.format("%-150s%n", linha));
                }

                // Escreve o trailer
                RegistroZ trailer = generateTrailer(records);
                writer.write(String.format("%-150s%n", trailer.toCnabLine()));
            }

            return ResponseEntity.ok("Arquivo CNAB gerado com sucesso! Caminho: " + filePath);
        } catch (Exception e) {
            System.err.println("Erro ao gerar o arquivo CNAB: " + e.getMessage());
            return ResponseEntity.status(400).body("Erro ao processar os registros CNAB: " + e.getMessage());
        }
    }

    private List<CnabRecord> generateRecords(List<RecordRequest> recordsRequest) {
        List<CnabRecord> records = new ArrayList<>();

        for (RecordRequest request : recordsRequest) {
            System.out.println("Processando tipo: " + request.getTipo());
            System.out.println("Quantidade: " + request.getQuantidade());
            System.out.println("Dados: " + request.getDados());

            for (int i = 0; i < request.getQuantidade(); i++) {
                try {
                    CnabRecord record;
                    switch (request.getTipo()) {
                        case E -> {
                            record = new RegistroE(request.getDados());
                            System.out.println("RegistroE criado: " + record.toCnabLine());
                        }
                        case F -> {
                            record = new RegistroF(request.getDados());
                            System.out.println("RegistroF criado: " + record.toCnabLine());
                        }
                        default -> throw new IllegalArgumentException("Tipo de registro inválido: " + request.getTipo());
                    }
                    records.add(record); // Adiciona o registro à lista
                } catch (Exception e) {
                    System.err.println("Erro ao processar registro do tipo " + request.getTipo() + ": " + e.getMessage());
                    throw e; // Propaga o erro
                }
            }
        }

        return records;
    }

    private RegistroZ generateTrailer(List<CnabRecord> records) {
        int quantidadeRegistros = records.size();
        String valorTotal = String.valueOf(
                records.stream()
                        .filter(record -> record instanceof RegistroE || record instanceof RegistroF)
                        .mapToLong(record -> {
                            if (record instanceof RegistroE registroE) {
                                return Long.parseLong(registroE.getValorPagamento());
                            } else if (record instanceof RegistroF registroF) {
                                return Long.parseLong(registroF.getValorAgendadoCobrado());
                            }
                            return 0L;
                        }).sum()
        );

        return new RegistroZ(quantidadeRegistros, valorTotal);
    }

}
