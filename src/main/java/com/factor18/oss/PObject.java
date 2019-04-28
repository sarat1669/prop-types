package com.factor18.oss;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PObject implements PropType, Serializable {
    @Override
    public Object parse(Object value) throws InvalidPropTypeException {
        if(value == null) value = required ? defaultValue : null;
        if(!isValid(value)) throw new InvalidPropTypeException("Invalid object value");
        return value;
    }

    @Override
    public boolean isValid(Object value) {
        if(value == null) return !required;
        return true;
    }

    @Override
    public Boolean isRequired() { return required; }

    public final String type = "object";

    private Boolean required;
    private HashMap<String, Object> defaultValue;
}
