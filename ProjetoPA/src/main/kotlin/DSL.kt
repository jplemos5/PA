operator fun Entity.div(entityName: String) : Entity =
    this.getChildren().find { it.getName() == entityName } as Entity

operator fun Entity.get(attributeName: String) : String =
    this.getAttributes()[attributeName].toString()

operator fun Entity.contains(entityName: String) : Boolean =
    this.getAttributes().containsKey(entityName)

infix fun Entity.fatherOf(ent:Entity) =
    addChildEntity(ent)


infix fun Entity.fatherOf(entityList:Collection<Entity>) =
    entityList.forEach { addChildEntity(it) }


infix fun LinkedHashMap<String,String>.inlineAttributesOf(ent : Entity) : Unit{
    this.forEach{(name, value) ->
        ent.addAttribute(name, value)
    }
}

infix fun LinkedHashMap<String,String>.insideAttributesOf(ent : Entity) : Unit{
    this.forEach{(name, value) ->
        val child = Entity(name)
        child.addText(value)
        ent.addChildEntity(child)
    }
}

fun entity(name: String, build: Entity.() -> Unit) =
    Entity(name).apply {
        build(this)
    }

fun Entity.childEntity(name: String, attributes : LinkedHashMap<String?, String>, build: Entity.() -> Unit) {
    val ent = Entity(name, attributes)
    this.addChildEntity(ent)
    ent.apply {
        build(this)
    }
}

fun Entity.attribute(name: String, value: String) =
    this.addAttribute(name, value)

fun Entity.text(textContent:String ) =
    this.addText(textContent)