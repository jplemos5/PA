# XML Manipulation API

This project provides a set of Kotlin classes and functions to manipulate XML documents. Each class is designed to help the users achieving a specific XML task, from document creation and manipulation to validation and I/O file operations.

## Table of Contents

- [Introduction](#introduction)
- [Files Overview](#files-overview)
- [Class Documentation](#class-documentation)
  - [Document.kt](#document)
  - [Entity.kt](#entity)
  - [XMLClasses.kt](#xmlclasses)
  - [Annotations.kt](#annotations)
  - [DSL.kt](#dsl)
- [License](#license)

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

### Document

The `Document` class represents a XML document with a root entity, version, and encoding.

#### Constructor
```kotlin
Document(name: String, var version: String, var encoding: String)
```

**Parameters:**
- `name: String`: The name to be given to the root `Entity`.
- `version: String`: The version of the Xml `Document`.
- `encoding: String`: The encoding of the Xml `Document`.
- `entity: Entity`: The root `Entity` of the `Document`.

**Functions:**
- `getVersion(): String`: Gets the version of the document.
- `getEncoding(): String`: Gets the encoding of the document.
- `setVersion(version: String)`: Sets the version of the document.
- `setEncoding(encoding: String)`: Sets the encoding of the document.
- `getRootEntity(): Entity`: Gets the root entity of the document.
- `prettyPrint(): String`: Generates a pretty-printed string representation of the document.
- `writeToFile(fileName: String)`: Writes the document content to a file.

**Exceptions:**
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
### Entity

The `Entity` class represents a XML entity with a name, attributes, a parent, and children. It provides various functions to manipulate the entity and its hierarchy.

#### Constructor
```kotlin
Entity(name: String, attributes: LinkedHashMap<String?, String> = linkedMapOf(), parent: Entity? = null)
```

**Parameters:**
- `name: String`: The name of the entity.
- `attributes: LinkedHashMap<String?, String>`: The attributes of the entity as a mutable map that makes a connection between the name of the attribute (key) and the value of the atribute (value). Default is an empty map.
- `parent: Entity?`: The parent entity of the current entity, or null if it has no parent.
- `children: MutableList<Entity>`: The list of its children entities, by default is empty.

**Functions:**
- `getName(): String`:Returns the name of the entity.
- `getAttributes(): MutableMap<String?, String>`: Returns the attributes of the entity as a mutable map.
- `getChildren(): MutableList<Entity>`: Returns the children of the entity as a mutable list.
- `getParent(): Entity?`: Returns the parent of the entity, or null if the entity has no parent.
- `getText(): String`: Returns the text content of the entity. If the entity has only one attribute, and its value is an empty string, it returns the attribute name. Otherwise, returns "Doesn't have text!". 
- `addChildEntity(entity: Entity)`: Adds a child entity to the current entity.
- `removeChildEntity(entity: Entity)`: Removes a child entity from the current entity.
- `addAttribute(attributeName: String, attributeValue: String)`: Adds an attribute to the entity.
- `addText(attributeValue: String)`: Adds a text to the entity.
- `removeAttribute(attribute: String)`: Removes the attribute with the specified name from the entity.
- `changeAttribute(attributeName: String, value: String)`: Changes the value of an existing attribute in the entity.
- `renameAttribute(oldName: String?, newName: String)`: Changes the name of an existing attribute in the entity.
- `toString(): String`: Returns a string representation of the entity, including its name, attributes, parent entity (if any), and children entities.
- `prettyPrint(indentation: Int = 0): String`: Generates a pretty-printed XML representation of the entity and its children.
- `entityList(): MutableList<String>`: Retrieves a list of names of all entities in the hierarchy.
  
**Global Functions:**
- `globalAddAttributeToEntity(entityName: String, attributeName: String, attributeValue: String)`: Adds an attribute to all entities with the specified name, if they don't have any non-blank attributes.
- `globalRenameEntity(entityOldName: String, entityNewName: String)`: Renames an entity with the specified old name to the new name globally throughout the hierarchy.
- `globalRenameAttribute(entityName: String, attributeOldName: String, attributeNewName: String)`: Renames a specified attribute of entities with the given name throughout the hierarchy.
- `globalRemoveEntity(entityName: String)`: Removes an entity with the specified name from the hierarchy except for the root entity.
- `globalRemoveAttribute(entityName: String, attributeName: String)`: Removes a specified attribute from entities with the given name throughout the hierarchy.
- `globalPrintXPath(path: String): String`: Performs a global XPath search on the hierarchy and returns a string containing the XML representation of matching entities.
- `globalXPath(path: String): MutableList<Entity>`: Performs a global XPath search on the hierarchy and returns a list with the matching entities.

**Example:**
```kotlin

```

### XMLClasses

The `XMLClasses` file has a group of functions that help to create objects that can be automatically transformed into Entities.

**Functions:**
- `translate(obj: Any): Entity`: Receives an object that will be translated into an entity. This function is used to handle the Annotations. 
- `isValidEntityName(name: String): Boolean`: A helper functions that validates an entity name. In this case the name shouldn't be empty, start with special characters, and shouldn't have some specific characters. 
- `isValidAttributeName(name: String): Boolean`: A helper functions that validates an attribute name. In this case the name shouldn't be empty or have any spaces.
**Exceptions:**
- `IllegalArgumentException`: Thrown if the entity name or any attribute name is not valid or provided during the handling of properties.

**Example:**
```kotlin

```

### Annotations

This document provides an overview of the annotations and interfaces used for XML serialization in Kotlin.

#### Annotations
#### `@XmlEntity`

Marks a class as an XML entity with a specific name.

```kotlin
@Target(AnnotationTarget.CLASS)
annotation class XmlEntity(val name: String)
```

#### `@XmlAttributeName`
Specifies the XML attribute name for a property.

```kotlin
@Target(AnnotationTarget.PROPERTY)
annotation class XmlAttributeName(val name: String)
```

#### `@InlineAttribute`
Marks a property as a description attribute in the XML. The property will not be nested but placed next to the name of the XML entity.

```kotlin
@Target(AnnotationTarget.PROPERTY)
annotation class InlineAttribute
```

#### `@Exclude`
Excludes a property from being serialized to XML.

```kotlin
@Target(AnnotationTarget.PROPERTY)
annotation class Exclude
```

#### `@XmlValueTransformer`
Applies a transformer to a property value during serialization.

```kotlin
@Target(AnnotationTarget.PROPERTY)
annotation class XmlValueTransformer(val transformer: KClass<out Transformer>)
```

#### `@XmlAdapter`
Applies an adapter to a property or class during serialization.

```kotlin
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class XmlAdapter(val adapter: KClass<out Adapter>)
```
#### Interfaces

`Transformer`
Interface for transforming a string value. Implement this interface to define custom transformation logic.

**Functions:**
`transform(input: String): String`: Transforms the input string value.

**Exceptions:**
- `IllegalArgumentException` if the input is invalid or transformation fails.

- `Adapter`
Interface for adapting an entity after mapping it. Implement this interface to define custom adaptation logic.

**Functions:**
`adapt(input: Entity): Entity`: Adapts the input entity.

**Exceptions:**
- `IllegalArgumentException` if the input is invalid or adaptation fails.

### DSL

This document provides a dsl to help the XML manipulation.

**Operators:**
#### `Division Operator (/) `
This operator lets us find an Entity given an Entity and the name of the children we want to find.

#### `Get Operator ([...}) `
This operator lets us find the value of a given attribute name.

**Infixes:**
#### `isChildOf`
This infix checks if a string is a name of a children entity of a given entity.

#### `isAttributeOf`
This infix checks if a string is a name of an attribute of a given entity.

#### `fatherOf`
This infix given a list of entities sets all of them as children of another given entity.

#### `inlineAttributesOf`
This infix given a linked hashmap sets them as inline attributes of an entity.

#### `insideAttributesOf`
This infix given a linked hashmap sets them as inside attributes of an entity.

**Implicit Lambda Instances:**
- `document(name: String, version: String, encoding: String, build: Entity.() -> Unit)`: Creates a document with the given name, version, and encoding, and applies the provided builder function to configure the root entity of the document.
- `Entity.childEntity(name: String, attributes : LinkedHashMap<String?, String>, build: Entity.() -> Unit)`: Adds a child entity with attributes and applies the provided builder function.
- `Entity.attributeName(name: String, value: String)`: Adds an attribute to the entity.
- `Entity.text(textContent: String)`: Adds text content to the entity.

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
