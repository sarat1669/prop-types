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
        if(value == null) {
            if(!required) return null;
            return new HashMap<>();
        }
        if(!isValid(value)) throw new InvalidPropTypeException("Invalid object value");
        return (HashMap) value;
    }

    @Override
    public boolean isValid(Object value) {
        if(value == null) return !required;
        HashMap v = (HashMap) value;
        return true;
    }

    @Override
    public boolean isRequired() { return required; }

    public final String type = "object";

    private Boolean required;
    private HashMap<String, Object> defaultValue;
}
