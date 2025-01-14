package com.example.picpay.models.cnab;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroD extends CnabRecord {
    private String codigoRegistro = "D"; // Sempre "D" para identificar o tipo
    private String agenciaCredito;      // Agência do crédito
    private String contaCredito;        // Conta de crédito
    private String digitoCredito;       // Dígito verificador da conta de crédito
    private long valorCredito;          // Valor do crédito em centavos (ex.: 150075 para R$ 1500,75)
    private String dataCredito;         // Data do crédito no formato YYYYMMDD
    private String codigoFinalidade;    // Código da finalidade do crédito

    /**
     * Construtor customizado para inicializar os campos com base em uma String delimitada.
     * Exemplo de entrada: "1234|123456789012|5|150075|20250114|001"
     *
     * @param dados String delimitada contendo os valores dos campos
     */
    public RegistroD(String dados) {
        if (dados == null || dados.isEmpty()) {
            throw new IllegalArgumentException("Os dados para o registro não podem ser nulos ou vazios.");
        }

        // Divide os dados usando o delimitador "|"
        String[] partes = dados.split("\\|");

        if (partes.length < 6) {
            throw new IllegalArgumentException("Dados insuficientes para inicializar o RegistroD. Esperado: 6 campos.");
        }

        this.agenciaCredito = partes[0].trim();          // Agência do crédito
        this.contaCredito = partes[1].trim();           // Conta de crédito
        this.digitoCredito = partes[2].trim();          // Dígito verificador
        this.valorCredito = parseValor(partes[3]);      // Valor do crédito
        this.dataCredito = partes[4].trim();            // Data do crédito
        this.codigoFinalidade = partes[5].trim();       // Código da finalidade
    }

    /**
     * Construtor alternativo para inicializar com valores individuais.
     *
     * @param agenciaCredito  Agência do crédito
     * @param contaCredito    Conta de crédito
     * @param digitoCredito   Dígito verificador da conta de crédito
     * @param valorCredito    Valor do crédito em centavos
     * @param dataCredito     Data do crédito no formato YYYYMMDD
     * @param codigoFinalidade Código da finalidade do crédito
     */
    public RegistroD(String agenciaCredito, String contaCredito, String digitoCredito, long valorCredito, String dataCredito, String codigoFinalidade) {
        this.agenciaCredito = agenciaCredito;
        this.contaCredito = contaCredito;
        this.digitoCredito = digitoCredito;
        this.valorCredito = valorCredito;
        this.dataCredito = dataCredito;
        this.codigoFinalidade = codigoFinalidade;
    }

    /**
     * Converte uma String numérica para long (centavos).
     *
     * @param valor String numérica sem separadores
     * @return Valor convertido para long
     */
    private long parseValor(String valor) {
        if (valor == null || valor.isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(valor);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido para conversão: " + valor);
        }
    }

    @Override
    public String toCnabLine() {
        return String.format(
                "%-1s%-4s%-12s%-1s%-15d%-8s%-3s%-102s",
                codigoRegistro,
                agenciaCredito,
                contaCredito,
                digitoCredito,
                valorCredito,
                dataCredito,
                codigoFinalidade,
                "" // Preenche com espaços até completar 150 caracteres
        );
    }
}
