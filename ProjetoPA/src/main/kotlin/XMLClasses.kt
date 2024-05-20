import kotlin.reflect.*
import kotlin.reflect.full.*

class XMLClasses {

    fun translate (obj: Any): Entity{
        val e = getEntityName(obj)
            obj::class.dataClassFields.forEach { property ->
                if(!property.hasAnnotation<Exclude>())
                    handleProperties(obj,property, e)
            }
        if(obj::class.hasAnnotation<XmlAdapter>()){
            val annotation = obj::class.findAnnotation<XmlAdapter>()
            val adapter : Adapter = (annotation?.adapter?.createInstance() as Adapter)
            return  adapter.adapt(e)
        }
        return e
    }


    private fun getXmlAttributeName(obj:KProperty<*>): String{
        if(obj.hasAnnotation<XmlAttributeName>())
            return obj.findAnnotation<XmlAttributeName>()!!.name
        return obj.name
    }

    private fun getEntityName(obj:Any): Entity{
        if(obj::class.hasAnnotation<XmlEntity>())
            return Entity(obj::class.findAnnotation<XmlEntity>()!!.name)
        return Entity(obj::class.simpleName!!)
    }

    private fun handleProperties(obj:Any, property:KProperty<*>, father: Entity){
        val currentEntity = Entity(getXmlAttributeName(property))
        val attributeValue: String = getAttributeValue(obj, property) // Obter o valor final do atributo
        if(property.call(obj) is List<*>) { // Onde se vão adicionar os filhos
            for (att in property.call(obj) as List<*>)
                currentEntity.addChildEntity(translate(att!!))
            father.addChildEntity(currentEntity)
        }
        else if(property.hasAnnotation<InlineAttribute>()) // Onde se adicionam os atributos na mesma linha
            father.addAttribute(getXmlAttributeName(property), attributeValue)
        else { //Onde se adiciona os atributos com text
            currentEntity.addText(attributeValue)
            father.addChildEntity(currentEntity)
        }
    }

    private fun getAttributeValue(obj:Any, property: KProperty<*>): String {
        if (property.hasAnnotation<XmlValueTransformer>()) {
            val annotation = property.findAnnotation<XmlValueTransformer>()
            val transformer : Transformer = (annotation?.transformer?.createInstance() as Transformer)
            return transformer.transform(property.call(obj).toString())
        } else
            return  property.call(obj).toString()
    }

    private val KClass<*>.dataClassFields: List<KProperty<*>>
        get() {
            require(isData) { "instance must be data class" }
            return primaryConstructor!!.parameters.map { p ->
                declaredMemberProperties.find { it.name == p.name }!!
            }
        }
}