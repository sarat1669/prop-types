package com.factor18.oss;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PFloat implements PropType, Serializable {

    @Override
    public Object parse(Object value) throws InvalidPropTypeException {
        if(value == null) value = required ? defaultValue : null;
        if(!isValid(value)) throw new InvalidPropTypeException("Invalid float value");
        return value instanceof Integer ? ((Integer) value).doubleValue() : (Double) value;
    }

    @Override
    public boolean isValid(Object value) {
        if(value == null) return !required;
        try {
            Double v = value instanceof Integer ? ((Integer) value).doubleValue() : (Double) value;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean isRequired() { return required; }

    private final String type = "float";

    private boolean required;
    private Double defaultValue;
}
