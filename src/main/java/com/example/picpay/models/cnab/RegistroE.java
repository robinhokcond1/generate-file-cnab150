package com.example.picpay.models.cnab;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroE extends CnabRecord {
    private String codigoRegistro = "E"; // Sempre "E" para identificar este tipo de registro
    private String identificacaoCliente; // Nome ou ID do cliente (30 caracteres)
    private String cpfCnpj;              // CPF ou CNPJ do cliente (14 caracteres)
    private String valorPagamento;       // Valor do pagamento sem separadores (15 caracteres)
    private String dataPagamento;        // Data do pagamento no formato YYYYMMDD (8 caracteres)
    private String descricaoPagamento;   // Descrição do pagamento (50 caracteres)

    /**
     * Construtor customizado para inicializar os campos com base em uma String de entrada.
     * Exemplo de entrada: "Cliente01|12345678901|150075|20250114|Pagamento de Serviço"
     *
     * @param dados String delimitada contendo os valores dos campos
     */
    public RegistroE(String dados) {
        if (dados == null || dados.isEmpty()) {
            throw new IllegalArgumentException("Os dados para o registro não podem ser nulos ou vazios.");
        }

        String[] partes = dados.split("\\|");
        if (partes.length < 5) {
            throw new IllegalArgumentException("Dados insuficientes para inicializar o RegistroE. Esperado: 5 campos.");
        }

        this.identificacaoCliente = partes[0].trim();
        this.cpfCnpj = partes[1].trim();
        this.valorPagamento = partes[2].trim(); // Remove espaços extras
        this.dataPagamento = partes[3].trim();
        this.descricaoPagamento = partes[4].trim();
    }

    /**
     * Ajusta o tamanho do campo para o formato CNAB, preenchendo com espaços ou cortando o valor.
     *
     * @param valor   Valor a ser ajustado
     * @param tamanho Tamanho esperado do campo
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
        String line = String.format(
                "%-1s%-30s%-14s%-15s%-8s%-50s%-32s",
                codigoRegistro,
                identificacaoCliente,
                cpfCnpj,
                valorPagamento,
                dataPagamento,
                descricaoPagamento,
                "" // Completa até 150 caracteres
        );
        System.out.println("Linha CNAB gerada para RegistroE: " + line);
        return line;
    }

}
