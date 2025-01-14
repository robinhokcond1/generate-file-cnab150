package com.example.picpay.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos de registros CNAB", example = "E, F, H, D, C, J")
public enum TipoRegistro {
    E, F, H, D, C, J;
}
