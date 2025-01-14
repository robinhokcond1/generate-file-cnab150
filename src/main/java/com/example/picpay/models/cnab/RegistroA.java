package com.example.picpay.models.cnab;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroA extends CnabRecord {
    private String codigoRegistro = "A"; // Posição 001
    private String codigoRemessaRetorno; // Posição 002
    private String codigoConvenio; // Posição 003-015
    private String nomeEmpresa; // Posição 023-042
    private String codigoBanco = "341"; // Posição 043-045
    private String nomeBanco = "BANCO ITAU"; // Posição 046-055
    private LocalDate dataGeracao; // Posição 066-073
    private int numeroSequencialArquivo; // Posição 074-079
    private String layoutVersao = "04"; // Posição 080-081
    private String identificacaoServico = "DEBITO AUTOMATICO"; // Posição 082-098

    @Override
    public String toCnabLine() {
        return String.format("%-1s%-1s%-13s%-7s%-20s%-3s%-10s%-10s%8s%06d%-2s%-17s%-52s",
                codigoRegistro,
                codigoRemessaRetorno,
                codigoConvenio,
                "",
                nomeEmpresa,
                codigoBanco,
                nomeBanco,
                "",
                dataGeracao.toString().replace("-", ""),
                numeroSequencialArquivo,
                layoutVersao,
                identificacaoServico,
                "");
    }
}
