package com.rest.json.parser;

import java.util.regex.Pattern;

public class TableCell {

    final Pattern decimalPattern = Pattern.compile("\\d+\\.\\d+");
    final Pattern integerPattern = Pattern.compile("\\d+");

    private String key;
    private String value;

    TableCell(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        if (isForcedString()) {
            return value.replace("\"", "");
        } else if (isBoolean()) {
            return Boolean.valueOf(value);
        } else if (isDecimal()) {
            return Double.valueOf(value);
        } else if (isInteger()){
            return Integer.valueOf(value);
        } else {
            return value;
        }
    }

    private boolean isForcedString() {
        return value.startsWith("\"");
    }

    private boolean isBoolean() {
        return value.equalsIgnoreCase("TRUE") || value.equalsIgnoreCase("FALSE");
    }

    private boolean isDecimal() {
        return decimalPattern.matcher(value).matches();
    }

    private boolean isInteger() {
        return integerPattern.matcher(value).matches();
    }
}
