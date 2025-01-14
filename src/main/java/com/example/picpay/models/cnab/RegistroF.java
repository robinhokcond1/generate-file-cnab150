package com.example.picpay.models.cnab;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroF extends CnabRecord {
    private String codigoRegistro = "F"; // Sempre "F" para identificar o tipo
    private String identificacaoCliente; // Nome ou ID do cliente (30 caracteres)
    private String codigoTransacao;      // Código da transação (5 caracteres)
    private String valorAgendadoCobrado; // Valor da transação sem separadores (15 caracteres)
    private String dataTransacao;        // Data no formato YYYYMMDD (8 caracteres)
    private String numeroDocumento;      // Número do documento (15 caracteres)

    /**
     * Construtor customizado para inicializar os campos com base em uma String delimitada.
     * Exemplo de entrada: "Cliente01|12345|150075|20250114|DOC123456"
     *
     * @param dados String delimitada contendo os valores dos campos.
     */
    public RegistroF(String dados) {
        if (dados == null || dados.isEmpty()) {
            throw new IllegalArgumentException("Os dados para o registro não podem ser nulos ou vazios.");
        }

        String[] partes = dados.split("\\|");
        if (partes.length < 5) {
            throw new IllegalArgumentException("Dados insuficientes para inicializar o RegistroF. Esperado: 5 campos.");
        }

        this.identificacaoCliente = partes[0].trim();
        this.codigoTransacao = partes[1].trim();
        this.valorAgendadoCobrado = partes[2].trim(); // Remove espaços extras
        this.dataTransacao = partes[3].trim();
        this.numeroDocumento = partes[4].trim();
    }


    /**
     * Ajusta o tamanho do campo para o formato CNAB, preenchendo com espaços ou cortando o valor.
     *
     * @param valor    Valor a ser ajustado
     * @param tamanho  Tamanho esperado do campo
     * @return Valor ajustado
     */
    private String ajustarTamanho(String valor, int tamanho) {
        if (valor == null) {
            return " ".repeat(tamanho);
        }
        if (valor.length() > tamanho) {
            return valor.substring(0, tamanho); // Corta o valor
        }
        return String.format("%-" + tamanho + "s", valor); // Preenche com espaços à direita
    }

    @Override
    public String toCnabLine() {
        try {
            String line = String.format(
                    "%-1s%-30s%-5s%-15s%-8s%-15s%-76s",
                    codigoRegistro,
                    identificacaoCliente,
                    codigoTransacao,
                    valorAgendadoCobrado,
                    dataTransacao,
                    numeroDocumento,
                    "" // Preenche até 150 caracteres
            );
            System.out.println("Linha CNAB gerada para RegistroF: " + line);
            return line;
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao gerar linha CNAB para RegistroF: " + e.getMessage());
        }
    }
}
