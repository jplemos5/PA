# XML Manipulation API

This project provides a set of Kotlin classes and functions to manipulate XML documents. Each class is designed to help the users acheiving a specific XML function, from document creation and manipulation to validation and file I/O operations.

## Table of Contents

- [Introduction](#introduction)
- [Files Overview](#files-overview)
- [Class Documentation](#class-documentation)
  - [Document](#document)
  - [Entity](#entity)
  - [XMLClasses](#xmlclasses)
  - [Annotations](#annotations)
  - [DSL](#dsl)
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
   - A file that contains some functions that can help with creating xml classes, it is related to `Annotations`.
   - Functions: Function that translates a class to an entity and some additional helping functions.
   
4. **Annotations.kt**
   - Provides annotations that auxiliate the creations of `XMLClasses`.
   - Has annotations and interfaces.
   
5. **DSL.kt**
   - Provides a Domain Specific Language to help manipulating the XML .
   
## Class Documentation

### Document

The `Document` class represents an XML document with a root entity, version, and encoding.

**Document Attributes:**
- `name: String`: The name to be given to the root `Entity`.
- `version: String`: The version of the Xml `Document`.
- `encoding: String`: The encoding of the Xml `Document`.
- `entity: Entity`: The root `Entity` of the `Document`.

**Functions:**
- `getVersion()`: Gets the version of the document.
- `getEncoding()`: Gets the encoding of the document.
- `setVersion(version: String)`: Sets the version of the document.
- `setEncoding(encoding: String)`: Sets the encoding of the document.
- `getRootEntity()`: Gets the root entity of the document.
- `prettyPrint()`: Generates a pretty-printed string representation of the document.
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
