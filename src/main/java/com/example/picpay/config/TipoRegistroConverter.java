package com.example.picpay.config;

import com.example.picpay.enums.TipoRegistro;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TipoRegistroConverter implements Converter<String, TipoRegistro> {

    @Override
    public TipoRegistro convert(String source) {
        System.out.println("Convertendo valor do tipo: " + source);
        try {
            TipoRegistro registro = TipoRegistro.valueOf(source.toUpperCase());
            System.out.println("Valor convertido para: " + registro);
            return registro;
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao converter tipo: " + source);
            throw new IllegalArgumentException("Tipo de registro inválido: " + source, e);
        }
    }
}
