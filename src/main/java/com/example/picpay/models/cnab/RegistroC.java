package com.example.picpay.models.cnab;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroC extends CnabRecord {
    private String codigoRegistro = "C"; // Sempre "C" para identificar este tipo
    private String agenciaDebito;       // Agência de débito (4 dígitos)
    private String contaCorrente;       // Conta corrente (12 dígitos)
    private String digitoConta;         // Dígito verificador da conta corrente (1 dígito)
    private String nomeCliente;         // Nome do cliente (30 caracteres)
    private String dataMovimento;       // Data do movimento no formato YYYYMMDD
    private long valorMovimento;        // Valor do movimento em centavos

    /**
     * Construtor customizado para inicializar os campos com base em uma String delimitada.
     * Exemplo de entrada: "1234|123456789012|5|Cliente Exemplo|20250114|150075"
     *
     * @param dados String delimitada contendo os valores dos campos
     */
    public RegistroC(String dados) {
        if (dados == null || dados.isEmpty()) {
            throw new IllegalArgumentException("Os dados para o registro não podem ser nulos ou vazios.");
        }

        // Divide os dados usando o delimitador "|"
        String[] partes = dados.split("\\|");

        if (partes.length < 6) {
            throw new IllegalArgumentException("Dados insuficientes para inicializar o RegistroC. Esperado: 6 campos.");
        }

        this.agenciaDebito = validateAgencia(partes[0].trim());
        this.contaCorrente = validateContaCorrente(partes[1].trim());
        this.digitoConta = validateDigitoConta(partes[2].trim());
        this.nomeCliente = validateNomeCliente(partes[3].trim());
        this.dataMovimento = validateData(partes[4].trim());
        this.valorMovimento = parseValor(partes[5].trim());
    }

    /**
     * Construtor alternativo para inicializar com valores individuais.
     */
    public RegistroC(String agenciaDebito, String contaCorrente, String digitoConta, String nomeCliente, String dataMovimento, long valorMovimento) {
        this.agenciaDebito = validateAgencia(agenciaDebito);
        this.contaCorrente = validateContaCorrente(contaCorrente);
        this.digitoConta = validateDigitoConta(digitoConta);
        this.nomeCliente = validateNomeCliente(nomeCliente);
        this.dataMovimento = validateData(dataMovimento);
        this.valorMovimento = valorMovimento;
    }

    /**
     * Valida a agência de débito (4 dígitos).
     */
    private String validateAgencia(String agencia) {
        if (agencia == null || agencia.length() != 4) {
            throw new IllegalArgumentException("Agência deve conter exatamente 4 dígitos.");
        }
        return agencia;
    }

    /**
     * Valida a conta corrente (12 dígitos).
     */
    private String validateContaCorrente(String conta) {
        if (conta == null || conta.length() != 12) {
            throw new IllegalArgumentException("Conta corrente deve conter exatamente 12 dígitos.");
        }
        return conta;
    }

    /**
     * Valida o dígito verificador da conta corrente (1 dígito).
     */
    private String validateDigitoConta(String digito) {
        if (digito == null || digito.length() != 1) {
            throw new IllegalArgumentException("Dígito da conta deve conter exatamente 1 dígito.");
        }
        return digito;
    }

    /**
     * Valida o nome do cliente (até 30 caracteres).
     */
    private String validateNomeCliente(String nome) {
        if (nome == null || nome.length() > 30) {
            throw new IllegalArgumentException("Nome do cliente deve ter no máximo 30 caracteres.");
        }
        return nome;
    }

    /**
     * Valida a data do movimento (formato YYYYMMDD).
     */
    private String validateData(String data) {
        if (data == null || !data.matches("\\d{8}")) {
            throw new IllegalArgumentException("Data deve estar no formato YYYYMMDD.");
        }
        return data;
    }

    /**
     * Converte uma String numérica para long (centavos).
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
                "%-1s%-4s%-12s%-1s%-30s%-8s%-15d%-79s",
                codigoRegistro,
                agenciaDebito,
                contaCorrente,
                digitoConta,
                nomeCliente,
                dataMovimento,
                valorMovimento,
                "" // Preenche com espaços até completar 150 caracteres
        );
    }
}
