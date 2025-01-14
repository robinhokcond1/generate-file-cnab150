package com.example.picpay.models.cnab;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CnabRecord {
    public abstract String toCnabLine();
}

