package com.factor18.oss;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PArray implements PropType, Serializable {

    @Override
    public Object parse(Object value) throws InvalidPropTypeException {
        List v = (List) value;
        List<Object> parsed = new ArrayList<>();
        items = items == null ? new ArrayList<PropType>() : items;

        if(value == null) {
            if(!required) return null;

            for (int i = 0; i < items.size(); i++) {
                parsed.set(i, items.get(i).parse(v.get(i)));
            }
            return parsed;
        }

        if(!isValid(value)) throw new InvalidPropTypeException("Invalid array value");

        for (int i = 0; i < items.size(); i++) {
            parsed.set(i, items.get(i).parse(v.get(i)));
        }

        for (int i = items.size(); i < v.size(); i++) {
            parsed.set(i, additionalItems.parse(v.get(i)));
        }

        return parsed;
    }

    @Override
    public boolean isValid(Object value) {
        if(value == null) return !required;
        List v = (List) value;

        items = items == null ? new ArrayList<PropType>() : items;

        for (int i = 0; i < items.size(); i++) {
            if(!items.get(i).isValid(v.get(i))) return false;
        }

        if(additionalItems == null) return v.size() == items.size();

        for (int i = items.size(); i < v.size(); i++) {
            if(!additionalItems.isValid(v.get(i))) return false;
        }

        return true;
    }

    @Override
    public boolean isRequired() { return required; }

    private final String type = "array";

    private Boolean required;
    private List<Object> defaultValue;

    private List<PropType> items;
    private PropType additionalItems;
}
