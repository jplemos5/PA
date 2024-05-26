/**
 * Represents an entity with a name, attributes, parent, and children.
 *
 * @constructor Creates an Entity
 * @property name The name of the entity.
 * @property attributes The attributes of the entity as a mutable map.
 * @property parent The parent entity of the current entity, or null if it has no parent.
 * @property children The list of child entities of the current entity.
 * @throws IllegalArgumentException if the entity name or any attribute name is not valid.
 */
class Entity(private var name: String, private var attributes: LinkedHashMap<String?, String> = linkedMapOf() , private var parent: Entity? = null ) {

    init {
        require(isValidEntityName(name)) { "Invalid entity name: $name" }
        attributes.keys.forEach { attributeName ->
            require(isValidAttributeName(attributeName ?: "")) { "Invalid attribute name: $attributeName" }
        }
    }

    private var children: MutableList<Entity> = mutableListOf()

    /**
     * Gets the name of the entity.
     * @return The name of the entity.
     */
    fun getName(): String = this.name

    /**
     * Gets the attributes of the entity.
     * @return The attributes of the entity as a mutable map.
     */
    fun getAttributes(): MutableMap<String?, String> = this.attributes

    /**
     * Gets the children of the entity.
     * @return The children of the entity as a mutable list.
     */
    fun getChildren(): MutableList<Entity> = this.children

    /**
     * Gets the parent of the entity.
     * @return The parent of the entity, or null if the entity has no parent.
     */
    fun getParent(): Entity? = this.parent

    /**
     * Gets the text content of the entity.
     * If the entity has only one attribute, and its value is an empty string, return the attribute name.
     * Example: <nome>Programação Avançada</nome>
     * Otherwise, returns "Doesn't have text!".
     * @return The text content of the entity.
     */
    fun getText() : String {
        if(attributes.size == 1 && attributes.keys.first() === "") return attributes.values.first()
        return "Doesn't have text!"
    }

    /**
     * Sets the name of the entity.
     * @param name The new name for the entity.
     */
    private fun setName(name: String){
        if(isValidEntityName(name))
            this.name = name
        else
            throw IllegalArgumentException("Entity name is not valid")
    }

    /**
     * Sets the parent of the entity.
     * @param parent The parent entity to set.
     */
    private fun setParent(parent: Entity?){
        this.parent = parent
    }

    /**
     * Adds a child entity to the current entity.
     * @param entity The entity to add as a child.
     */
    fun addChildEntity(entity: Entity) {
        children.add(entity)
        entity.setParent(this)
    }

    /**
     * Removes a child entity from the current entity.
     * @param entity The entity to remove from the children.
     */
    fun removeChildEntity(entity: Entity) {
        entity.setParent(null)
        children.remove(entity)
    }

    /**
     * Adds an attribute to the entity.
     * @param attributeName The name of the attribute to add.
     * @param attributeValue The value of the attribute to add.
     */
    fun addAttribute(attributeName: String, attributeValue: String) {
        if(isValidAttributeName(attributeName) && attributeValue != "")
            attributes[attributeName] = attributeValue
    }

    /**
     * Adds a text to the entity.
     * @param attributeValue The value of the attribute to add.
     */
    fun addText(attributeValue: String){
        if(attributesAreBlank())
            attributes[null] = attributeValue
    }



    /**
     * Removes the attribute with the specified name from the entity.
     * @param attribute The name of the attribute to remove.
     */
    fun removeAttribute(attribute : String) = attributes.remove(attribute)

    /**
     * Changes the value of an existing attribute in the entity.
     * If the attribute doesn't exist, it does nothing.
     * @param attributeName The name of the attribute to change.
     * @param value The new value of the attribute.
     */
    fun changeAttribute(attributeName: String, value : String) = attributes.replace(attributeName, value)

    /**
     * Changes the name of an existing attribute in the entity.
     * If the attribute doesn't exist, it does nothing.
     * @param oldName The current name of the attribute.
     * @param newName The new name to set for the attribute.
     */
    private fun renameAttribute(oldName: String?, newName: String) {
        if (isValidAttributeName(newName) && oldName != null && attributes.containsKey(oldName)) {
            val newAttributes = LinkedHashMap<String?, String>()
            for ((key, value) in attributes) {
                if (key == oldName) {
                    newAttributes[newName] = value
                } else {
                    newAttributes[key] = value
                }
            }
            attributes.clear()
            attributes.putAll(newAttributes)
        }else{
            throw IllegalArgumentException("Attribute name is not valid or attribute with oldName doesn't exist")
        }
    }

    /**
     * Checks if all attributes in the entity are blank or null.
     * @return true if all attributes are blank or null, false otherwise.
     */
    private fun attributesAreBlank() : Boolean =  attributes.all { it.key.isNullOrBlank() }

    /**
     * Returns a string representation of the entity.
     * The string includes the name, attributes, parent entity (if any), and children entities.
     * @return A string representation of the entity.
     */
    override fun toString(): String {
        return "Name: $name, Attributes: $attributes, Parent: ${parent?.name ?: "null"}, Children: $children"
    }

    /**
     * Represents a visitor interface for entities.
     */
    interface Visitor {
        /**
         * Visits an entity.
         * @param entity The entity to visit.
         */
        fun visit(entity: Entity)
    }

    /**
     * Creates a visitor from the provided function.
     * @param visitor The function that defines the visit behavior.
     * @return A visitor based on the provided function.
     */
    private fun visitor(visitor: (Entity) -> Unit) = object : Visitor {
        override fun visit(entity: Entity) {
            visitor(entity)
        }
    }

    /**
     * Accepts a visitor and visits the current entity and its children.
     * @param visitor The visitor to accept.
     */
    fun accept(visitor: Visitor){
        visitor.visit(this)
        children.forEach{ it.accept(visitor)}
    }

    /**
     * Generates a pretty-printed XML representation of the entity and its children.
     * @param indentation The indentation level for formatting (default is 0).
     * @return A pretty-printed XML representation of the entity and its children.
     */
    fun prettyPrint(indentation: Int = 0): String {
        val indent = "    ".repeat(indentation)
        val stringBuilder = StringBuilder("$indent<$name").apply {
            if (attributesAreBlank()) append(">")
            goThroughChildren(placeAttributes(this), indentation, indent)
            append(if (attributesAreBlank() || children.isNotEmpty()) "</$name>" else "/>")
        }
        return stringBuilder.toString()
    }

    /**
     * Appends attributes to the given StringBuilder.
     * @param stringBuilder The StringBuilder to append attributes to.
     * @return The StringBuilder with attributes appended.
     */
    private fun placeAttributes(stringBuilder: StringBuilder) : StringBuilder {
        if (attributes.isNotEmpty()) {
            attributes.forEach { (key, value) -> stringBuilder.append(if (key.isNullOrBlank()) value else " $key=\"$value\"") }
            stringBuilder.append( if(children.isNotEmpty()) ">" else "")
        }
        return stringBuilder
    }

    /**
     * Recursively traverses through children and appends their pretty-printed representations to the StringBuilder.
     * @param stringBuilder The StringBuilder to append child representations to.
     * @param indentation The current indentation level.
     * @param indent The string representation of indentation.
     * @return The StringBuilder with child representations appended.
     */
    private fun goThroughChildren(stringBuilder: StringBuilder, indentation: Int, indent:String) : StringBuilder{
        if (children.isNotEmpty()) {
            children.forEach { child -> stringBuilder.append("\n" + child.prettyPrint(indentation + 1))}
            stringBuilder.append("\n$indent")
        }
        return stringBuilder
    }

    /**
     * Retrieves a list of names of all entities in the hierarchy.
     * @return A list containing the names of all entities.
     */
    fun entityList(): MutableList<String> {
        val resultList = mutableListOf<String>()
        val v = visitor { resultList.add(it.getName()) }
        accept(v)
        return resultList
    }

    /**
     * Adds an attribute to all entities with the specified name, if they don't have any non-blank attributes.
     * @param entityName The name of the entities to which the attribute will be added.
     * @param attributeName The name of the attribute to add.
     * @param attributeValue The value of the attribute to add.
     */
    fun globalAddAttributeToEntity(entityName: String,  attributeName: String,  attributeValue: String){
        val v = visitor { entity ->
            if (entity.name == entityName && entity.attributes.none { it.key.isNullOrBlank() })
                entity.addAttribute(attributeName, attributeValue)
        }
        accept(v)
    }

    /**
     * Renames an entity with the specified old name to the new name globally throughout the hierarchy.
     * @param entityOldName The current name of the entity to rename.
     * @param entityNewName The new name for the entity.
     */
    fun globalRenameEntity(entityOldName: String,  entityNewName: String){
       val v = visitor { entity ->
            if (entity.parent != null && entity.name == entityOldName)
                entity.setName(entityNewName)
        }
        accept(v)
    }

    /**
     * Renames a specified attribute of entities with the given name throughout the hierarchy.
     * @param entityName The name of the entities whose attributes will be renamed.
     * @param attributeOldName The current name of the attribute to rename.
     * @param attributeNewName The new name for the attribute.
     */
    fun globalRenameAttribute(entityName: String,  attributeOldName: String,  attributeNewName: String){
        val v = visitor { entity ->
            if (entity.name == entityName && entity.attributes.contains(attributeOldName))
                entity.renameAttribute(attributeOldName, attributeNewName)
        }
        accept(v)
    }

    /**
     * Can remove an entity with the specified name from the hierarchy except for the root entity.
     * @param entityName The name of the entities to remove.
     */
    fun globalRemoveEntity(entityName: String) {
        if (this.name != entityName) {
            val v = visitor { entity ->
                val childrenToRemove = mutableListOf<Entity>()
                for (child in entity.children)
                    if (child.name == entityName && this.name != child.name)
                        childrenToRemove.add(child)
                childrenToRemove.forEach { entity.removeChildEntity(it) }
            }
            accept(v)
        }
    }

    /**
     * Removes a specified attribute from entities with the given name throughout the hierarchy.
     * @param entityName The name of the entities from which the attribute will be removed.
     * @param attributeName The name of the attribute to remove.
     */
    fun globalRemoveAttribute(entityName: String, attributeName : String){
        val v = visitor { entity ->
            if(entity.name == entityName && entity.attributes.contains(attributeName) && !entity.attributesAreBlank())
                entity.removeAttribute(attributeName)
        }
        accept(v)
    }

    /**
     * Performs a global XPath search on the hierarchy and returns a string containing the XML representation of matching entities.
     * @param path The XPath expression to search for.
     * @return A string containing the XML representation of entities matching the XPath expression.
     */
    fun globalPrintXPath(path: String) : String {
        val listToSearch : MutableList<String> = path.split("/") as MutableList<String>
        var str = ""
        val v = visitor{ entity ->
            if(listToSearch.size != 0 && entity.name == listToSearch[0]) {
                if (listToSearch.size - 1 == 0)
                    str += entity.prettyPrint() + "\n"
                else
                    listToSearch.removeAt(0)
            }
        }
        accept(v)
        return str.substring(0, str.length - 1)
    }

    /**
     * Performs a global XPath search on the hierarchy and returns a list with the matching entities.
     * @param path The XPath expression to search for.
     * @return A list with the matching entities.
     */
    fun globalXPath(path: String) : MutableList<Entity> {
        val listToSearch : MutableList<String> = path.split("/") as MutableList<String>
        val list = mutableListOf<Entity>()
        val v = visitor{ entity ->
            if(listToSearch.size != 0 && entity.name == listToSearch[0]) {
                if (listToSearch.size - 1 == 0)
                    list.add(entity)
                else
                    listToSearch.removeAt(0)
            }
        }
        accept(v)
        return list
    }

}
