class Entity(private var name: String, private var attributes: MutableMap<String, String?> = mutableMapOf() , private var parent: Entity? = null ) {

    private var children: MutableList<Entity> = mutableListOf()

    fun getName(): String = this.name

    fun getAttributes(): MutableMap<String, String?> = this.attributes

    fun getChildren(): MutableList<Entity> = this.children

    fun getParent(): Entity? = this.parent

    fun getText() : String {
        if(attributes.size == 1 && attributes.values.first() === "") return attributes.keys.first()
        return "Doesn't have text!"
    }
    private fun setName(name: String){
        this.name = name
    }

    private fun setParent(parent: Entity?){
        this.parent = parent
    }

    fun addAttribute(attributeName: String, attributeValue: String?) = attributes.put(attributeName, attributeValue)

    fun removeAttribute(attribute : String) = attributes.remove(attribute)

    fun changeAttribute(attribute : String, value : String) = attributes.replace(attribute, value)

    private fun changeAttributeName(oldName : String, newName : String){
        val value = attributes.remove(oldName)// TODO saber se vale a pena tentar manter a ordem
        addAttribute(newName, value)
    }

    fun addChildEntity(entity: Entity) {
        children.add(entity)
        entity.setParent(this)
    }

    fun removeChildEntity(entity: Entity) {
        entity.setParent(null)
        children.remove(entity)
    }

    private fun attributesAreBlank() =  attributes.all { it.value.isNullOrBlank() }

    override fun toString(): String {
        return "Name: $name, Attributes: $attributes, Parent: ${parent?.name ?: "null"}, Children: $children"
    }

    interface Visitor {
        fun visit(entity: Entity)
    }

    private fun visitor(visitor: (Entity) -> Unit) = object : Visitor {
        override fun visit(entity: Entity) {
            visitor(entity)
        }
    }

    private fun accept(visitor: Visitor){
        visitor.visit(this)
        children.forEach{ it.accept(visitor)}
    }

    fun prettyPrint(indentation: Int = 0): String {
        val indent = "    ".repeat(indentation)
        val stringBuilder = StringBuilder("$indent<$name").apply {
            if (attributesAreBlank()) append(">")
            goThroughChildren(placeAttributes(this), indentation, indent)
            append(if (attributesAreBlank() || children.isNotEmpty()) "</$name>" else "/>")
        }
        return stringBuilder.toString()
    }

    private fun placeAttributes(stringBuilder: StringBuilder) : StringBuilder {
        if (attributes.isNotEmpty()) {
            attributes.forEach { (key, value) -> stringBuilder.append(if (value.isNullOrBlank()) key else " $key=\"$value\"") }
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

    fun attributePrinter() =
        accept(visitor { println("Entity: $name, Attributes: $attributes") })

    fun namePrinter() =
        accept(visitor { println("Entity Name: $name") })

    fun entityList(): MutableList<String> {
        val resultList = mutableListOf<String>()
        val v = visitor {
                resultList.add(it.getName())
            }
        accept(v)
        return resultList
    }

    fun globalAddAttributeToEntity(entityName: String,  attributeName: String,  attributeValue: String){
        val v = visitor { entity ->
            if (entity.name == entityName && entity.attributes.none { it.value.isNullOrBlank() })
                entity.addAttribute(attributeName, attributeValue)
        }
        accept(v)
    }

    fun globalRenameEntity(entityOldName: String,  entityNewName: String){
        if(this.parent != null && this.name != entityNewName) { //TODO Perguntar ao stor se vale a pena ou n. E no adicionar Entidade?
            val v = visitor { entity ->
                if (entity.name == entityOldName)
                    entity.setName(entityNewName)
            }
            accept(v)
        }
    }

    fun globalRenameAttribute(entityName: String,  attributeOldName: String,  attributeNewName: String){
        val v = visitor { entity ->
            if (entity.name == entityName && entity.attributes.contains(attributeOldName))
                entity.changeAttributeName(attributeOldName, attributeNewName)
        }
        accept(v)
    }

    fun globalRemoveEntity(entityName: String) {
        if (this.name != entityName) { //Nao pode remover a root
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

    fun globalRemoveAttribute(entityName: String, attributeName : String){
        val v = visitor { entity ->
            if(entity.name == entityName && entity.attributes.contains(attributeName) && !entity.attributesAreBlank())
                entity.removeAttribute(attributeName)
        }
        accept(v)
    }

    fun globalXPath(path: String) : String {
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
        return str
    }
}