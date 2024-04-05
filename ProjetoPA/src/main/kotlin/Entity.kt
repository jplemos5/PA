class Entity {

    private val name: String
    private val attributes: MutableMap<String, String>
    private val children : MutableList<Entity>
    private var parent : Entity?

    fun accept(visitor: (Entity)-> Boolean) {
        if(visitor(this))
            children.forEach {
                it.accept(visitor)
            }
    }


    constructor(name: String, attributes: MutableMap<String, String>, parent: Entity? = null ){
        this.name = name
        this.attributes = attributes
        this.children = mutableListOf<Entity>()
        this.parent = parent
    }

    
    private fun setParent(parent: Entity?){
        this.parent = parent
    }

    fun addAttribute(attribute: String, value: String) =
        attributes.put(attribute, value)

    fun removeAttribute(attribute : String) =
        attributes.remove(attribute)

    fun changeAttribute(attribute : String, value : String) =
        attributes.replace(attribute, value)

    fun addChildEntity(entity: Entity) {
        children.add(entity)
        entity.setParent(this)
    }

    fun removeChildEntity(entity: Entity) {
        entity.setParent(null)
        children.remove(entity)
    }

    fun prettyPrint(): String { //TODO Correct the way we present the names
        var str = ""
        accept { entity ->
            if(entity is Entity)
                str += entity.name
            true
        }
        return str
    }

    override fun toString(): String {
        return "Name: $name, Attributes: $attributes, Parent: ${parent?.name ?: "null"}, Childs: $children"
    }




}