package com.factor18.oss;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PFloat implements PropType, Serializable {

    @Override
    public Object parse(Object value) throws InvalidPropTypeException {
        if(value == null) return required ? defaultValue : null;
        if(!isValid(value)) throw new InvalidPropTypeException("Invalid float value");
        return value instanceof Integer ? ((Integer) value).doubleValue() : (Double) value;
    }

    @Override
    public boolean isValid(Object value) {
        if(value == null) return !required;
        Double v = value instanceof Integer ? ((Integer) value).doubleValue() : (Double) value;
        return true;
    }

    @Override
    public boolean isRequired() { return required; }

    private final String type = "float";

    private boolean required;
    private Double defaultValue;
}
