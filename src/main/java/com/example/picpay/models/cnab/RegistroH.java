package com.example.picpay.models.cnab;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroH extends CnabRecord {
    private String codigoRegistro = "H"; // Sempre "H" para identificar o trailer
    private int totalRegistros;          // Total de registros no arquivo
    private long valorTotal;             // Valor total em centavos (ex.: 150075 para R$ 1500,75)

    /**
     * Construtor para inicializar com base em dados delimitados.
     * Exemplo de entrada: "10|150075"
     *
     * @param dados String delimitada contendo total de registros e valor total
     */
    public RegistroH(String dados) {
        if (dados == null || dados.isEmpty()) {
            throw new IllegalArgumentException("Os dados para o registro não podem ser nulos ou vazios.");
        }

        // Divide os dados usando o delimitador "|"
        String[] partes = dados.split("\\|");

        if (partes.length < 2) {
            throw new IllegalArgumentException("Dados insuficientes para inicializar o RegistroH. Esperado: 2 campos.");
        }

        try {
            this.totalRegistros = Integer.parseInt(partes[0].trim());   // Total de registros
            this.valorTotal = Long.parseLong(partes[1].trim());        // Valor total em centavos
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Erro ao converter valores numéricos no RegistroH: " + e.getMessage());
        }
    }

    /**
     * Construtor alternativo para inicializar com valores individuais.
     *
     * @param totalRegistros Número total de registros no arquivo
     * @param valorTotal     Valor total em centavos
     */
    public RegistroH(int totalRegistros, long valorTotal) {
        this.totalRegistros = totalRegistros;
        this.valorTotal = valorTotal;
    }

    @Override
    public String toCnabLine() {
        return String.format(
                "%-1s%06d%-15s%-129s",
                codigoRegistro,
                totalRegistros,
                String.format("%015d", valorTotal), // Formata o valor com zeros à esquerda
                "" // Preenche com espaços até completar 150 caracteres
        );
    }
}
