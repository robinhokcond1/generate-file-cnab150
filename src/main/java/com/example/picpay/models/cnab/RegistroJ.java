package com.example.picpay.models.cnab;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroJ extends CnabRecord {
    private String codigoRegistro = "J"; // Sempre "J" para identificar o tipo
    private String descricaoMovimento;  // Descrição do movimento
    private String numeroDocumento;     // Número do documento associado ao movimento
    private long valorMovimento;        // Valor do movimento em centavos
    private String dataMovimento;       // Data no formato YYYYMMDD

    /**
     * Construtor customizado para inicializar os campos com base em uma String delimitada.
     * Exemplo de entrada: "Pagamento|DOC123456|150075|20250114"
     *
     * @param dados String delimitada contendo os valores dos campos.
     */
    public RegistroJ(String dados) {
        if (dados == null || dados.isEmpty()) {
            throw new IllegalArgumentException("Os dados para o registro não podem ser nulos ou vazios.");
        }

        // Divide os dados usando o delimitador "|"
        String[] partes = dados.split("\\|");

        if (partes.length < 4) {
            throw new IllegalArgumentException("Dados insuficientes para inicializar o RegistroJ. Esperado: 4 campos.");
        }

        this.descricaoMovimento = partes[0].trim();         // Descrição do movimento
        this.numeroDocumento = partes[1].trim();           // Número do documento
        this.valorMovimento = parseValor(partes[2]);       // Valor, convertido para centavos
        this.dataMovimento = partes[3].trim();             // Data do movimento
    }

    /**
     * Construtor alternativo para inicializar com valores individuais.
     *
     * @param descricaoMovimento Descrição do movimento
     * @param numeroDocumento    Número do documento associado ao movimento
     * @param valorMovimento     Valor do movimento em centavos
     * @param dataMovimento      Data do movimento no formato YYYYMMDD
     */
    public RegistroJ(String descricaoMovimento, String numeroDocumento, long valorMovimento, String dataMovimento) {
        this.descricaoMovimento = descricaoMovimento;
        this.numeroDocumento = numeroDocumento;
        this.valorMovimento = valorMovimento;
        this.dataMovimento = dataMovimento;
    }

    /**
     * Converte uma String numérica para um valor em centavos (long).
     * Exemplo: "150075" será convertido para 150075.
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
                "%-1s%-30s%-15s%-15d%-8s%-81s",
                codigoRegistro,
                descricaoMovimento,
                numeroDocumento,
                valorMovimento,
                dataMovimento,
                "" // Preenche com espaços até completar 150 caracteres
        );
    }
}
