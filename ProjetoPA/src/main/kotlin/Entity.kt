class Entity {

    private val name: String
    private val attributes: MutableMap<String, String>
    private val children : MutableList<Entity>
    private var parent : Entity?

    fun accept(visitor: Visitor){
        visitor.visit(this)
        children.forEach{ it.accept(visitor)}
    }

    fun getName(): String {
        return this.name
    }

    fun getAttributes(): MutableMap<String, String> {
        return this.attributes
    }

    fun getChildren(): MutableList<Entity> {
        return this.children
    }

    fun getParent(): Entity? {
        return this.parent
    }

    interface Visitor {
        fun visit(entity: Entity)
    }

    constructor(name: String, attributes: MutableMap<String, String> = mutableMapOf() , parent: Entity? = null ){
        this.name = name
        this.attributes = attributes
        this.children = mutableListOf()
        this.parent = parent
    }

    
    private fun setParent(parent: Entity?){
        this.parent = parent
    }

//    fun addAttribute(attribute: String, value: String) =
//        attributes.put(attribute, value)

    fun addAttribute(attributeName: String, attributeValue: String) {
        attributes[attributeName] = attributeValue
    }

    fun removeAttribute(attribute : String) = attributes.remove(attribute)

    fun changeAttribute(attribute : String, value : String) = attributes.replace(attribute, value)

    fun addChildEntity(entity: Entity) {
        children.add(entity)
        entity.setParent(this)
    }

    fun removeChildEntity(entity: Entity) {
        entity.setParent(null)
        children.remove(entity)
    }


    override fun toString(): String {
        return "Name: $name, Attributes: $attributes, Parent: ${parent?.name ?: "null"}, Children: $children"
    }

    fun prettyPrint(indentation: Int = 0): String {
        val indent = "    ".repeat(indentation)
        val stringBuilder = StringBuilder("$indent<$name").apply {
            if (attributes.all { it.value.isBlank() }) append(">")
            goThroughChildren(placeAttributes(this), indentation, indent)
            append(if (attributes.all { it.value.isBlank() } || children.isNotEmpty()) "</$name>" else "/>")
        }
        return stringBuilder.toString()
    }

    private fun placeAttributes(stringBuilder: StringBuilder) : StringBuilder {
        if (attributes.isNotEmpty()) {
            attributes.forEach { (key, value) -> stringBuilder.append(if (value.isNotBlank()) " $key=\"$value\"" else key) }
            stringBuilder.append( if(children.isNotEmpty()) ">" else "")
        }
        return stringBuilder
    }
    private fun goThroughChildren(stringBuilder: StringBuilder, indentation: Int, indent:String) : StringBuilder{
        if (children.isNotEmpty()) {
            children.forEach { child -> stringBuilder.append("\n" + child.prettyPrint(indentation + 1))}
            stringBuilder.append("\n$indent")
        }
        return stringBuilder
    }


    private fun visitor(visitor: (Entity) -> Unit) = object : Visitor {
        override fun visit(entity: Entity) {
            visitor(entity)
        }
    }

    fun attributePrinter() =
        accept(visitor { println("Entity: $name, Attributes: $attributes") })


    fun namePrinter() =
        accept(visitor { println("Entity Name: $name") })


    fun addAttributeToEntity(entityName: String,  attributeName: String,  attributeValue: String){
        val v = visitor { entity ->
            if (entity.name == entityName && !entity.attributes.all {it.value.isBlank()})
                entity.addAttribute(attributeName, attributeValue)
        }
        accept(v)
    }
}