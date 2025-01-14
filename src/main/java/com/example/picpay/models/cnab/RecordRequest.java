package com.example.picpay.models.cnab;

import com.example.picpay.enums.TipoRegistro;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class RecordRequest {
    @NotNull(message = "O tipo de registro é obrigatório.")
    private TipoRegistro tipo; // Usa o enum TipoRegistro

    @NotNull(message = "A quantidade é obrigatória.")
    private Integer quantidade;

    @NotNull(message = "Os dados são obrigatórios.")
    private String dados;
}
