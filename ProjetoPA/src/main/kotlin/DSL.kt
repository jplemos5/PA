/**
 * Finds a child entity by name and returns it.
 *
 * @param entityName The name of the child entity to find.
 * @return The found child entity.
 * @throws ClassCastException If the found entity is not of type [Entity].
 */
operator fun Entity.div(entityName: String) : Entity =
    this.getChildren().find { it.getName() == entityName } as? Entity
        ?: throw NoSuchElementException("Child entity '$entityName' not found")


/**
 * Gets the value of an attribute by its name.
 *
 * @param attributeName The name of the attribute.
 * @return The value of the attribute as a [String].
 * @throws NoSuchElementException If the attribute does not exist.
 */
operator fun Entity.get(attributeName: String): String =
    this.getAttributes()[attributeName]?.toString()
        ?: throw NoSuchElementException("Attribute '$attributeName' not found")

/**
 * Checks if an entity contains a specific entity as a child.
 *
 * @param entityName The name of the entity to check.
 * @return `true` if the entity contains the entity, `false` otherwise.
 */
operator fun Entity.contains(entityName: String) : Boolean =
    this.getAttributes().containsKey(entityName)

/**
 * Adds a child entity to the current entity.
 *
 * @param ent The child entity to add.
 */
infix fun Entity.fatherOf(ent:Entity) =
    addChildEntity(ent)


/**
 * Adds a list of child entities to the current entity.
 *
 * @param entityList The list of child entities to add.
 */
infix fun Entity.fatherOf(entityList:Collection<Entity>) =
    entityList.forEach { addChildEntity(it) }



/**
 * Adds attributes to an entity as inline attributes.
 *
 * @param ent The entity to add the attributes to.
 */
infix fun LinkedHashMap<String,String>.inlineAttributesOf(ent : Entity){
    this.forEach{(name, value) ->
        ent.addAttribute(name, value)
    }
}

/**
 * Adds attributes to an entity as inside attributes (nested).
 *
 * @param ent The entity to add the attributes to.
 */
infix fun LinkedHashMap<String,String>.insideAttributesOf(ent : Entity){
    this.forEach{(name, value) ->
        val child = Entity(name)
        child.addText(value)
        ent.addChildEntity(child)
    }
}

/**
 * Creates an entity with a given name and applies the provided builder function.
 *
 * @param name The name of the entity.
 * @param build The builder function to configure the entity.
 * @return The created and configured entity.
 */
fun entity(name: String, build: Entity.() -> Unit){
    Entity(name).apply {
        build(this)
    }
}

/**
 * Adds a child entity with attributes and applies the provided builder function.
 *
 * @param name The name of the child entity.
 * @param attributes The attributes of the child entity.
 * @param build The builder function to configure the child entity.
 */
fun Entity.childEntity(name: String, attributes : LinkedHashMap<String?, String>, build: Entity.() -> Unit) {
    val ent = Entity(name, attributes)
    this.addChildEntity(ent)
    ent.apply {
        build(this)
    }
}

/**
 * Adds an attribute to the entity.
 *
 * @param name The name of the attribute.
 * @param value The value of the attribute.
 */
fun Entity.attributeName(name: String, value: String) =
    this.addAttribute(name, value)

/**
 * Adds text content to the entity.
 *
 * @param textContent The text content to add.
 */
fun Entity.text(textContent:String ) =
    this.addText(textContent)