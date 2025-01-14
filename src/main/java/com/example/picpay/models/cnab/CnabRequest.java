package com.example.picpay.models.cnab;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CnabRequest {
    private RegistroA header; // Header do arquivo
    private List<RecordRequest> records; // Lista de registros (E, F, H, D, C)
}

