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
public class PInteger implements PropType, Serializable {

    @Override
    public Object parse(Object value) throws InvalidPropTypeException {
        if(value == null) value = required ? defaultValue : null;
        if(!isValid(value)) throw new InvalidPropTypeException("Invalid integer value");
        return value instanceof Double ? ((Double) value).intValue() : value instanceof Integer ? (Integer) value : new Integer(value.toString());
    }

    @Override
    public boolean isValid(Object value) {
        if(value == null) return !required;
        try {
            Integer v = value instanceof Double ? ((Double) value).intValue() : value instanceof Integer ? (Integer) value : new Integer(value.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean isRequired() { return required; }

    private final String type = "integer";

    private Boolean required;
    private Integer defaultValue;
}
