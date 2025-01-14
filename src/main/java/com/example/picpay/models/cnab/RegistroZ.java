package com.example.picpay.models.cnab;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroZ extends CnabRecord {
    private String codigoRegistro = "Z"; // Posição 001
    private int quantidadeRegistros; // Posição 002-007
    private String valorTotal; // Posição 008-022

    /**
     * Construtor para inicialização direta.
     *
     * @param quantidadeRegistros Número total de registros no arquivo.
     * @param valorTotal Valor total dos registros E e F, em formato numérico sem separadores.
     */
    public RegistroZ(int quantidadeRegistros, String valorTotal) {
        this.codigoRegistro = "Z";
        this.quantidadeRegistros = quantidadeRegistros;

        if (valorTotal == null || !valorTotal.matches("\\d+")) {
            throw new IllegalArgumentException("O valor total deve ser numérico e sem separadores.");
        }
        this.valorTotal = valorTotal;
    }

    @Override
    public String toCnabLine() {
        return String.format("%-1s%06d%-15s%-129s",
                codigoRegistro,
                quantidadeRegistros,
                valorTotal,
                ""); // Preenche com espaços até completar 150 caracteres
    }
}
