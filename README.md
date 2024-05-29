# XML Manipulation API

This project provides a set of Kotlin classes and functions to manipulate XML documents. Each class is designed to help the users achieving a specific XML task, from document creation and manipulation to validation and I/O file operations.

## Table of Contents

- [Introduction](#introduction)
- [Files Overview](#files-overview)
- [Class Documentation](#class-documentation)

## Introduction

This documentation will provide an overview of each file, describe the functions and methods contained within, and provide examples and explanations of how to use them. Additionally, we will discuss any exceptions that might be thrown by the functions and how to handle them.

## Files Overview

1. **Document.kt**
   - Represents an XML document with a root entity, version, and encoding.
   - Functions: Creating a document, getting and setting properties, pretty-printing, writing to a file.
   
2. **Entity.kt**
   - Represents an XML entity (element) with attributes and children.
   - Functions: Adding and removing attributes, adding and removing child entities, pretty-printing and some global functions.
   
3. **XMLClasses.kt**
   - A file that contains some functions that can help creating Xml classes, it is related to `Annotations`.
   - Functions: Function that translates a class to an entity and some additional helping functions.
   
4. **Annotations.kt**
   - Provides annotations and interfaces that auxiliate the creations of `XMLClasses`.
   - Has annotations and interfaces.
   
5. **DSL.kt**
   - Provides a Domain Specific Language to help manipulating the XML .
   
## Class Documentation

<details>

<summary>Document.kt</summary>

### Document.kt

The `Document` class represents a XML document with a root entity, version, and encoding.

#### Constructor
```kotlin
Document(name: String, var version: String, var encoding: String)
```

**Parameters:**

| Parameter    | Type      | Description                                | Default Value |
| :---         |  :---     | :---                                       | :---          |
| `name`       | String    | The name to be given to the root entity.   | N/A           |
| `version`    | String    | The version of the Xml document.           | N/A           |
| `encoding`   | String    | The encoding of the Xml document.          | N/A           |
| `entity`     | Entity    | The root entity of the document.           | Entity(name)  |

**Functions:**

| Function                       | Description                                                     | Usage                                   |
| :--- | :--- | :--- |
| `getVersion(): String`         | Gets the version of the document.                               | `document.getVersion()`                 |
| `getEncoding(): String`        | Gets the encoding of the document.                              | `document.getEncoding()`                |
| `setVersion(version: String)`  | Sets the version of the document.                               | `document.setVersion("1.0")`            |
| `setEncoding(encoding: String)`| Sets the encoding of the document.                              | `document.setEncoding("UTF-8")`         |
| `getRootEntity(): Entity`      | Gets the root entity of the document.                           | `document.getRootEntity()`              |
| `prettyPrint(): String`        | Generates a pretty-printed string representation of the document. | `document.prettyPrint()`              |
| `writeToFile(fileName: String)`| Writes the document content to a file.                          | `document.writeToFile("output.xml")`    |


**Exceptions:**

| Exception                    | Description                                               |
| :--- | :--- |
| `IllegalArgumentException`   | Thrown if the version or encoding is invalid.             |
| `IOException`                | Thrown if an I/O error occurs while writing to the file.  |


- `IllegalArgumentException`: Thrown if the version or encoding is invalid.
- `IOException`: Thrown if an I/O error occurs while writing to the file.

**Example:**
```kotlin
val doc = Document("rootEntityName", "1.0", "UTF-8")
doc.setVersion("1.1")
doc.setEncoding("ISO-8859-1")
println(doc.prettyPrint())

try {
    doc.writeToFile("document.xml")
} catch (e: IOException) {
    e.printStackTrace()
}
```

</details>
<details>
<summary>Entity.kt</summary>

### Entity.kt
The `Entity` class represents a XML entity with a name, attributes, a parent, and children. It provides various functions to manipulate the entity and its hierarchy. Since we only have a class that handles all the entities and attributes we have some different types of entities and attributes that will be explained later.

#### Constructor
```kotlin
Entity(name: String, attributes: LinkedHashMap<String?, String> = linkedMapOf(), parent: Entity? = null)
```

**Parameters:**

| Parameter | Type | Description | Default Value |
| :--- | :--- | :--- | :--- |
| `name` | String | The name of the entity. | N/A |
| `attributes` | LinkedHashMap<String?, String> | The attributes of the entity as a mutable map that connects the attribute name (key) and the attribute value (value). | Empty map |
| `parent` | Entity? | The parent entity of the current entity, or null if it has no parent. | null |
| `children` | MutableList<Entity> | The list of child entities. | Empty list |

**Functions:**

| Function | Description | Usage |
| :--- | :--- | :--- |
| `getName(): String` | Returns the name of the entity. | `entity.getName()` |
| `getAttributes(): MutableMap<String?, String>` | Returns the attributes of the entity as a mutable map. | `entity.getAttributes()` |
| `getChildren(): MutableList<Entity>` | Returns the children of the entity as a mutable list. | `entity.getChildren()` |
| `getParent(): Entity?` | Returns the parent of the entity, or null if the entity has no parent. | `entity.getParent()` |
| `getText(): String` | Returns the text content of the entity. If the entity has only one attribute, and its value is an empty string, it returns the attribute name. Otherwise, returns "Doesn't have text!". | `entity.getText()` |
| `addChildEntity(entity: Entity)` | Adds a child entity to the current entity. | `entity.addChildEntity(childEntity)` |
| `removeChildEntity(entity: Entity)` | Removes a child entity from the current entity. | `entity.removeChildEntity(childEntity)` |
| `addAttribute(attributeName: String, attributeValue: String)` | Adds an attribute to the entity. | `entity.addAttribute("age", "30")` |
| `addText(attributeValue: String)` | Adds a text to the entity. | `entity.addText("Sample text")` |
| `removeAttribute(attribute: String)` | Removes the attribute with the specified name from the entity. | `entity.removeAttribute("age")` |
| `changeAttribute(attributeName: String, value: String)` | Changes the value of an existing attribute in the entity. | `entity.changeAttribute("age", "31")` |
| `renameAttribute(oldName: String?, newName: String)` | Changes the name of an existing attribute in the entity. | `entity.renameAttribute("oldName", "newName")` |
| `toString(): String` | Returns a string representation of the entity, including its name, attributes, parent entity (if any), and children entities. | `entity.toString()` |
| `prettyPrint(indentation: Int = 0): String` | Generates a pretty-printed XML representation of the entity and its children. | `entity.prettyPrint(2)` |
| `entityList(): MutableList<String>` | Retrieves a list of names of all entities in the hierarchy. | `entity.entityList()` |
  
**Global Functions:**

| Function | Description | Usage |
| :--- | :--- | :--- |
| `globalAddAttributeToEntity(entityName: String, attributeName: String, attributeValue: String)` | Adds an attribute to all entities with the specified name, if they don't have any non-blank attributes. | `entity.globalAddAttributeToEntity("Person", "age", "30")` |
| `globalRenameEntity(entityOldName: String, entityNewName: String)` | Renames an entity with the specified old name to the new name globally throughout the hierarchy. | `entity.globalRenameEntity("OldName", "NewName")` |
| `globalRenameAttribute(entityName: String, attributeOldName: String, attributeNewName: String)` | Renames a specified attribute of entities with the given name throughout the hierarchy. | `entity.globalRenameAttribute("Person", "oldAttribute", "newAttribute")` |
| `globalRemoveEntity(entityName: String)` | Removes an entity with the specified name from the hierarchy except for the root entity. | `entity.globalRemoveEntity("Person")` |
| `globalRemoveAttribute(entityName: String, attributeName: String)` | Removes a specified attribute from entities with the given name throughout the hierarchy. | `entity.globalRemoveAttribute("Person", "age")` |
| `globalPrintXPath(path: String): String` | Performs a global XPath search on the hierarchy and returns a string containing the XML representation of matching entities. | `entity.globalPrintXPath("//Person")` |
| `globalXPath(path: String): MutableList<Entity>` | Performs a global XPath search on the hierarchy and returns a list with the matching entities. | `entity.globalXPath("//Person")` |

#### **Types of entities:**

Entity without child - This is the entity that is created when we don't add any child to the entity.
```xml
<componente nome="Dissertação" peso="60%"/>
```

Entity with child - This is the entity that is created when we create the entity with children or when we use the function `addChildEntity` to an entity that doesn't have children.
```xml
<avaliacao>
    <componente nome="Quizzes" peso="20%"/>
    <componente nome="Projeto" peso="80%"/>
</avaliacao>
```

#### **Types of attributes:**

The names below will help when manipulating the Xml with the DSL functions.

Inline Attribute - This is the normal attribute that is created when we add an attribute to an entity. It can be added with the function `addAttribute` .
```xml
<fuc codigo="03782">
```

Inside Attribute - This is the type of attribute that is inside of the entity. To create this we need to add a child entity that only has one attribute. In the linked hash map the name of the attribute should be null and the value should be the text we want to display inside. This can be done  by using the function `addChildEntity` and then `addText`.
```xml
<fuc>
    <ects>10</ects>
</fuc>
```

</details>
<details>
  <summary>XMLClasses.kt</summary>
  
### XMLClasses.kt

The `XMLClasses` file has a group of functions that help to create objects that can be automatically transformed into Entities.

**Functions:**
| Function | Description | Usage |
| :--- | :--- | :--- |
| `translate(obj: Any): Entity` | Receives an object that will be translated into an entity. This function is used to handle the Annotations. | `translate(myObject)` |
| `isValidEntityName(name: String): Boolean` | A helper function that validates an entity name. The name shouldn't be empty, start with special characters, or contain specific invalid characters. | `isValidEntityName("EntityName")` |
| `isValidAttributeName(name: String): Boolean` | A helper function that validates an attribute name. The name shouldn't be empty or contain spaces. | `isValidAttributeName("attributeName")` |

**Exceptions:**
- `IllegalArgumentException`: Thrown if the entity name or any attribute name is not valid or provided during the handling of properties.

</details>
<details>
  <summary>Annotations.kt</summary>
  
### Annotations.kt

This document provides an overview of the annotations and interfaces used for XML serialization in Kotlin.

#### Annotations

| Annotation | Description | Targets |
| :--- | :--- | :--- |
| `@XmlEntity(val name: String)` | Marks a class as an XML entity with a specific name. | `@Target(AnnotationTarget.CLASS)` |
| `@XmlAttributeName(val name: String)` | Specifies the XML attribute name for a property. | `@Target(AnnotationTarget.PROPERTY)` |
| `@InlineAttribute` | Marks a property as a description attribute in the XML. The property will not be nested but placed next to the name of the XML entity. | `@Target(AnnotationTarget.PROPERTY)` |
| `@Exclude` | Excludes a property from being serialized to XML. | `@Target(AnnotationTarget.PROPERTY) Exclude` |
| `@XmlValueTransformer(val transformer: KClass<out Transformer>)` | Applies a transformer to a property value during serialization. | `@Target(AnnotationTarget.PROPERTY)` |
| `@XmlAdapter(val adapter: KClass<out Adapter>)` | Applies an adapter to a property or class during serialization. | `@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)` |


#### Interfaces

| Interface | Description | Functions | Exceptions |
| :--- | :--- | :--- | :--- |
| `Transformer` | Interface for transforming a string value. Implement this interface to define custom transformation logic. | `transform(input: String): String` - Transforms the input string value. | `IllegalArgumentException` if the input is invalid or transformation fails. |
| `Adapter` | Interface for adapting an entity after mapping it. Implement this interface to define custom adaptation logic. | `adapt(input: Entity): Entity` - Adapts the input entity. | `IllegalArgumentException` if the input is invalid or adaptation fails. |

</details>

<details>
  <summary>DSL.kt</summary>

### DSL.kt

This document provides a dsl to help the XML manipulation.

**Operators:**

| Operator | Function | Usage |
| :---          | :---          | :---          |
| `/` (Division Operator) | Finds a child entity by name. | `entity / "childName"` |
| `[...]` (Get Operator) | Retrieves the value of an attribute by its name. | `entity["attributeName"]` |


**Infixes:**
| Infix | Function | Usage |
| :---          | :---          | :---          |
| `isChildOf`       |  Checks if a string is a name of a children entity of a given entity.           | `"name" isChildOf entity `|
| `isAttributeOf`   | Checks if a string is a name of an attribute of a given entity.                 | `"attribute" isChildOf entity ` |
| `fatherOf`        |  Given a list of entities or an entity sets all of them as children of another given entity. | `entityFather fatherOf listOf(entitySon1, entitySon2) ` |
| `inlineAttributesOf`   |  Given a linked hashmap sets them as inline attributes of an entity.       | `linkedMapOf("attribute" to "value") insideAttributesOf entity ` |
| `insideAttributesOf`   | Given a linked hashmap sets them as inside attributes of an entity.        | `linkedMapOf("attribute" to "value") inlineAttributesOf entity ` |


**Implicit Lambda Instances:**

| Function | Description | Usage |
| :--- | :--- | :--- |
| `document(name: String, version: String, encoding: String, build: Entity.() -> Unit)` | Creates a document with the given name, version, and encoding, and applies the provided builder function to configure the root entity of the document. | `document("docName", "1.0", "UTF-8") { ... }` |
| `Entity.childEntity(name: String, attributes: LinkedHashMap<String?, String>, build: Entity.() -> Unit)` | Adds a child entity with attributes and applies the provided builder function. | `entity.childEntity("childName", linkedMapOf("attr" to "value")) { ... }` |
| `Entity.attributeName(name: String, value: String)` | Adds an attribute to the entity. | `entity.attributeName("attrName", "attrValue")` |
| `Entity.text(textContent: String)` | Adds text content to the entity. | `entity.text("Some text content")` |

**Example:**
```kotlin
    val documentDSL  = document("plano","1.0", "UTF-8") {
        childEntity("fuc", linkedMapOf("nome" to "Dissertação", "peso" to "60%")) {
            childEntity("help", linkedMapOf("nome" to "Apresentação", "peso" to "20%", "testte" to "20%", "testeeee" to "20%")) {
                attributeName("name", "Gonçalo")
                childEntity("texto", linkedMapOf()){
                    text("Texto bonito")
                }
            }
        }
    }
```




</details>


