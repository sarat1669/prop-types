package com.factor18.oss;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PShape implements PropType, Serializable {
    @Override
    public Object parse(Object value) throws InvalidPropTypeException {
        HashMap v = (HashMap) value;
        HashMap parsed = new HashMap();
        if(value == null) {
            if(!required) return null;
            for (Map.Entry<String, PropType> prop : schema.entrySet()) {
                parsed.put(prop.getKey(), prop.getValue().parse(null));
            }
            return parsed;
        }
        if(!isValid(value)) throw new InvalidPropTypeException("Invalid shape value");

        for (Map.Entry<String, PropType> prop : schema.entrySet()) {
            parsed.put(prop.getKey(), prop.getValue().parse(v.get(prop.getKey())));
        }
        return parsed;
    }

    @Override
    public boolean isValid(Object value) {
        if(value == null) return !required;
        HashMap v = (HashMap) value;
        for (Map.Entry<String, PropType> prop : schema.entrySet()) {
            if(!prop.getValue().isValid(v.get(prop.getKey()))) return false;
        }
        return true;
    }

    @Override
    public boolean isRequired() { return required; }

    private final String type = "shape";

    private Boolean required;
    private Map<String, Object> defaultValue;

    private Map<String, PropType> schema;

}
