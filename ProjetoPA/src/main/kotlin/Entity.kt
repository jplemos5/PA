import java.io.File
import kotlin.coroutines.coroutineContext

class Entity {

    private val name: String
    private val attributes: MutableMap<String, String>
    private val children : MutableList<Entity>
    private var parent : Entity?

    interface Visitor {
        fun visit(entity: Entity)
    }

    fun accept(visitor: Visitor) {
        visitor.visit(this)
        children.forEach { it.accept(visitor) }
    }


//    fun accept(visitor: (Entity)-> Boolean) {
//        if(visitor(this))
//            children.forEach {
//                it.accept(visitor)
//            }
//    }

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

//    fun prettyPrint(): String { //TODO Correct the way we present the names
//        var str = ""
//        accept { entity ->
//            if(entity is Entity)
//                str += entity.name
//            true
//        }
//        return str
//    }

    override fun toString(): String {
        return "Name: $name, Attributes: $attributes, Parent: ${parent?.name ?: "null"}, Childs: $children"
    }

//    fun prettyPrint(indentation: Int = 0): String {
//        val indent = " ".repeat(indentation)
//        val stringBuilder = StringBuilder()
//        stringBuilder.append("$indent<$name>")
//        if (attributes.isNotEmpty()) {
//            attributes.forEach { (key, value) ->
//                if(value.isNotBlank()) {
//                    stringBuilder.append(" $key=\"$value\">")
//                } else {
//                    stringBuilder.append("$key")
//                }
//            }
//        }
//        if (children.isNotEmpty()) {
//            children.forEach { child ->
//                stringBuilder.append("\n")
//                stringBuilder.append(child.prettyPrint(indentation + 2))
//            }
//            stringBuilder.append("\n$indent")
//        }
//        stringBuilder.append("</$name>")
//        return stringBuilder.toString()
//    }

    fun prettyPrint(indentation: Int = 0): String {
        val indent = " ".repeat(indentation)
        val stringBuilder = StringBuilder()
        if(entityHasValues(attributes)){
            stringBuilder.append("$indent<$name")
        }else {
            stringBuilder.append("$indent<$name>")
        }
        if (attributes.isNotEmpty()) {
            attributes.forEach { (key, value) ->
                if(value.isNotBlank()) {
                    stringBuilder.append(" $key=\"$value\"")
                } else {
                    stringBuilder.append("$key")
                }
            }
            if(children.isNotEmpty()){
                stringBuilder.append(">")
            }
        }
        if (children.isNotEmpty()) {
            children.forEach { child ->
                stringBuilder.append("\n")
                stringBuilder.append(child.prettyPrint(indentation + 4))
            }
            stringBuilder.append("\n$indent")
        }
        if(!entityHasValues(attributes)) {
            stringBuilder.append("</$name>")
        }else{
            if(children.isEmpty()) {
                stringBuilder.append("/>")
            }else{
                stringBuilder.append("</$name>")
            }

        }
        return stringBuilder.toString()
    }

    fun entityHasValues(attributes: MutableMap<String, String>): Boolean{
        var hasValues = false
        attributes.forEach{ (key, value) ->
            if(value.isNotBlank())
                hasValues = true
        }
        return hasValues
    }

    class AttributePrinterVisitor : Visitor {
        override fun visit(entity: Entity) {
            println("Entity: ${entity.name}, Attributes: ${entity.attributes}")
        }
    }

    class NamePrinterVisitor : Visitor {
        override fun visit(entity: Entity) {
            println("Entity Name: ${entity.name}")
        }
    }


}