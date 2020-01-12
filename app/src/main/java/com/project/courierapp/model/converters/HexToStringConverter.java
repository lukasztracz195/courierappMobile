package com.project.courierapp.model.converters;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

import lombok.SneakyThrows;

public class HexToStringConverter {

    @SneakyThrows
    public static String convert(String hex){
        byte[] bytes = Hex.decodeHex(hex.toCharArray());
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
