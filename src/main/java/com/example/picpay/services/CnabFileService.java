package com.example.picpay.services;

import com.example.picpay.models.cnab.CnabRecord;
import com.example.picpay.models.cnab.RegistroE;
import com.example.picpay.models.cnab.RegistroF;
import com.example.picpay.models.cnab.RegistroZ;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class CnabFileService {

    /**
     * Gera o arquivo CNAB com os registros fornecidos e adiciona o trailer no final.
     *
     * @param filePath Caminho do arquivo a ser gerado
     * @param records  Lista de registros CNAB
     * @throws IOException Caso ocorra algum erro ao gravar o arquivo
     */
    public void generateCnabFile(String filePath, List<CnabRecord> records) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (CnabRecord record : records) {
                String line = record.toCnabLine();
                System.out.println("Escrevendo linha CNAB: " + line);
                writer.write(line + "\n");
            }

            // Adiciona o registro "Z" (Trailer)
            RegistroZ trailer = generateTrailer(records);
            System.out.println("Escrevendo linha CNAB (Trailer): " + trailer.toCnabLine());
            writer.write(trailer.toCnabLine());
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo CNAB: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Gera o registro trailer ("Z") com base nos registros fornecidos.
     *
     * @param records Lista de registros CNAB
     * @return Instância do registro "Z" (trailer)
     */
    private RegistroZ generateTrailer(List<CnabRecord> records) {
        int quantidadeRegistros = records.size();

        long valorTotal = records.stream()
                .filter(record -> record instanceof RegistroE || record instanceof RegistroF)
                .mapToLong(record -> {
                    try {
                        if (record instanceof RegistroE registroE) {
                            System.out.println("Processando valor do RegistroE: " + registroE.getValorPagamento());
                            return parseValorSemSeparador(registroE.getValorPagamento());
                        } else if (record instanceof RegistroF registroF) {
                            System.out.println("Processando valor do RegistroF: " + registroF.getValorAgendadoCobrado());
                            return parseValorSemSeparador(registroF.getValorAgendadoCobrado());
                        }
                    } catch (IllegalArgumentException e) {
                        System.err.println("Erro ao converter valor no registro: " + record.toCnabLine());
                    }
                    return 0L;
                }).sum();

        String valorTotalFormatado = String.format("%015d", valorTotal);
        System.out.println("Trailer - Quantidade de Registros: " + quantidadeRegistros);
        System.out.println("Trailer - Valor Total: " + valorTotalFormatado);
        return new RegistroZ(quantidadeRegistros, valorTotalFormatado);
    }

    /**
     * Converte um valor em formato String para um long sem separadores (removendo pontos e vírgulas).
     * Exemplo: "1500.75" → 150075
     *
     * @param valor String representando o valor
     * @return Valor convertido para long
     */
    private long parseValorSemSeparador(String valor) {
        if (valor == null || valor.isEmpty()) {
            return 0L;
        }
        try {
            // Remove todos os espaços, pontos ou vírgulas
            String valorLimpo = valor.replaceAll("\\s+", "").replace(".", "").replace(",", "");
            System.out.println("Valor limpo para conversão: '" + valorLimpo + "'");
            return Long.parseLong(valorLimpo);
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter valor para long: '" + valor + "'");
            throw new IllegalArgumentException("Erro ao converter valor: " + valor, e);
        }
    }

}
