package com.factor18.oss;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PBoolean implements PropType, Serializable {
    @Override
    public Object parse(Object value) throws InvalidPropTypeException {
        if(value == null) return required ? defaultValue : null;
        if(!isValid(value)) throw new InvalidPropTypeException("Invalid boolean value");
        return (Boolean) value;
    }

    @Override
    public boolean isValid(Object value) {
        if(value == null) return !required;
        Boolean v = (Boolean) value;
        return true;
    }

    @Override
    public boolean isRequired() { return required; }

    public final String type = "boolean";

    private Boolean required;
    private Boolean defaultValue;
}
