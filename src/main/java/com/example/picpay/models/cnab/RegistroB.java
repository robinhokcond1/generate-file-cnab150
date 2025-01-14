package com.example.picpay.models.cnab;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegistroB extends CnabRecord {
    private String codigoRegistro = "B"; // Posição 001
    private String identificacaoCliente; // Posição 002-026
    private String agencia; // Posição 027-030
    private String conta; // Posição 039-043
    private String dac; // Posição 044
    private LocalDate dataOpcao; // Posição 045-052
    private int codigoMovimento; // Posição 150

    @Override
    public String toCnabLine() {
        return String.format("%-1s%-25s%-4s%-8s%-5s%-1s%8s%-97s%-1d",
                codigoRegistro,
                identificacaoCliente,
                agencia,
                "",
                conta,
                dac,
                dataOpcao.toString().replace("-", ""),
                "",
                codigoMovimento);
    }
}

