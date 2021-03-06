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
public class PString implements PropType, Serializable {

    @Override
    public Object parse(Object value) throws InvalidPropTypeException {
        if(value == null) value = required ? defaultValue : null;
        if(!isValid(value)) throw new InvalidPropTypeException("Invalid string value");
        return (String) value;
    }

    @Override
    public boolean isValid(Object value) {
        if(value == null) return !required;
        try {
            String v = (String) value;
            return !required || !v.equals("");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean isRequired() { return required; }

    private final String type = "string";

    private Boolean required;
    private String defaultValue;
}
