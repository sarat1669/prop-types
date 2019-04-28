# prop-types [![Maven Central](https://img.shields.io/maven-central/v/com.factor18.oss/prop-types.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.factor18.oss%22%20AND%20a:%22prop-types%22)
Extensible type checking framework for arbitrary objects

### Usage
To use prop-types, add the following dependency:
```xml
<dependency>
  <groupId>com.factor18.oss</groupId>
  <artifactId>prop-types</artifactId>
  <version>VERSION</version>
</dependency>
```

### Quick Start

PropTypes is a powerful tool for validating arbitrary objects.

PropTypes exports a range of validators that can be used to make sure the data you receive is valid. 

- [PInteger](https://github.com/sarat1669/prop-types/blob/master/src/main/java/com/factor18/oss/PInteger.java)
- [PFloat](https://github.com/sarat1669/prop-types/blob/master/src/main/java/com/factor18/oss/PFloat.java)
- [PBoolean](https://github.com/sarat1669/prop-types/blob/master/src/main/java/com/factor18/oss/PBoolean.java)
- [PString](https://github.com/sarat1669/prop-types/blob/master/src/main/java/com/factor18/oss/PString.java)
- [PObject](https://github.com/sarat1669/prop-types/blob/master/src/main/java/com/factor18/oss/PObject.java)
- [PArray](https://github.com/sarat1669/prop-types/blob/master/src/main/java/com/factor18/oss/PArray.java)
- [PShape](https://github.com/sarat1669/prop-types/blob/master/src/main/java/com/factor18/oss/PShape.java)

Here are some examples which showcase the possibilities using a simple integer validator

```java
PropType schema = PInteger.builder().defaultValue(10).required(true).build();

// 123 will be assigned to 'a'
Integer a = (Integer) schema.parse(123);

// 10  will be assigned to 'b'
Integer b = (Integer) schema.parse(null);

// will throw InvalidPropTypeException
Integer c = (Integer) schema.parse("invalid");
```

```java
PropType schemaWithoutRequired =  PInteger.builder().defaultValue(10).required(false).build();

// 123  will be assigned to 'd'
Integer d = (Integer) schemaWithoutRequired.parse(123);

// null will be assigned to 'e'
Integer e = (Integer) schemaWithoutRequired.parse(null);

// will throw InvalidPropTypeException
Integer f = (Integer) schemaWithoutRequired.parse("invalid");
```

```java
PropType schemaWithoutDefaultValue = PInteger.builder().required(true).build();

// 123  will be assigned to 'g'
Integer g = (Integer) schemaWithoutDefaultValue.parse(123);

// will throw InvalidPropTypeException
Integer h = (Integer) schemaWithoutDefaultValue.parse(null);

// will throw InvalidPropTypeException
Integer i = (Integer) schemaWithoutDefaultValue.parse("invalid");
```

Default value is only used when required is true. So schemaWithoutRequired is the same as schemaWithoutDefaultValueAndRequired. This behaviour can be overriden by creating custom prop-types, which we will look at further in this document.

```java
PropType schemaWithoutDefaultValueAndRequired = PInteger.builder().required(false).build();

// 123  will be assigned to 'j'
Integer j = (Integer) schemaWithoutDefaultValueAndRequired.parse(123);

// null will be assigned to 'k'
Integer k = (Integer) schemaWithoutDefaultValueAndRequired.parse(null);

// will throw InvalidPropTypeException
Integer l = (Integer) schemaWithoutDefaultValueAndRequired.parse("invalid");
```



`PInteger`, `PFloat`, `PBoolean`, `PString`, `PObject` have the same properties.

`required` -> to validate if the given value is required or not
`defaultValue` -> if required and the value is null, it will be replaced with this value

`PArray`, `PShape` are special. They allow us to compose complex structures.

#### PArray
As the name suggests, it is used to define arrays.

`PArray` has two additional properties `items`, `additionalItems`

Lets say you want to validate a List which has this list `[ 1, true, "sample", 2.0 ]`.
The `items` property comes in handy. It allows you to define the order of prop-types to be used to validate the List.

```java
PropType schema = PArray.builder().items(Lists.newArrayList(
    PInteger.builder().required(true).build(),
    PBoolean.builder().required(true).build(),
    PString.builder().required(true).build(),
    PFloat.builder().required(true).build()
)).required(true).build();

List<Object> a = schema.parse(Lists.newArrayList(1, true, "as", 2.0));
```

There might be a use case where you aren't aware the number items of the list. `additionalItems` will help you express such lists.

```java
PropType schema = PArray.builder().additionalItems(
    PInteger.builder().required(true).build()
).required(true).build();

List<Integer> a = (List<Integer>) schema.parse(Lists.newArrayList(1, 2, 3, 4));
```

`additionalItems` can be clubbed with `items` to express lists like `[1, true, "as", 2.0, 2, 3, 4, 5, 6, ... ]`

```java
PropType schema = PArray.builder().items(Lists.newArrayList(
    PInteger.builder().required(true).build(),
    PBoolean.builder().required(true).build(),
    PString.builder().required(true).build(),
    PFloat.builder().required(true).build()
)).additionalItems(
    PInteger.builder().required(true).build()
).required(true).build();

List<Object> a = (List<Object>) schema.parse(Lists.newArrayList(1, true, "as", 2.0, 1, 2, 3, 4, 5, 6));
```

#### PShape
It allows you to define the structure of a Map

`PShape` has a special property `schema`, which is a map of String:PropType. It is used to validate Maps like

```json
{
    "integer": 10,
    "float": 2.0,
    "boolean": true,
    "string": "sample",
    "object": "any random object",
    "array": [ 1, 2, 3, 4, 5 ]
}
```

The below mentioned schema validates the above mentioned data

```java
Map<String, PropType> shape = Maps.newHashMap();

shape.put("integer",
    PInteger.builder().required(true).build()
);

shape.put("float",
    PFloat.builder().required(true).build()
);

shape.put("boolean",
    PBoolean.builder().required(true).build()
);

shape.put("string",
    PString.builder().required(true).build()
);

shape.put("object",
    PObject.builder().required(true).build()
);

shape.put("array",
    PArray.builder().additionalItems(
        PInteger.builder().required(true).build()
    ).required(true).build()
);

PropType schema = PShape.builder().schema(shape).required(true).build();

Map<String, Object> data = Maps.newHashMap();
data.put("integer", 10);
data.put("float", 2.0);
data.put("boolean", true);
data.put("string", "sample");
data.put("object", "any random object");
data.put("array", Lists.newArrayList(1, 2, 3, 4, 5));

Map<String, Object> a = (Map<String, Object>) schema.parse(data);
```

### Custom PropType
You can create your custom prop-types by implementing the [`PropType`](https://github.com/sarat1669/prop-types/blob/master/src/main/java/com/factor18/oss/PropType.java) interface

```java
public interface PropType {
    String getType();
    Object parse(Object value) throws InvalidPropTypeException;
    Boolean isRequired();
    Object getDefaultValue();
    boolean isValid(Object value);
}
```

### License
prop-types is released under the Apache 2 License. Check [LICENSE](https://github.com/sarat1669/prop-types/blob/master/LICENSE) file for more information.
