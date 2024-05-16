import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class XMLClasses {

    fun translate (obj: Any): Entity{
        val e = getEntityName(obj)
        obj::class.declaredMemberProperties.forEach { property ->
            if(!property.hasAnnotation<Exclude>())
                handleProperties(obj,property, e)
        }
        return e
    }


    private fun getXmlAttributeName(obj:KProperty<*>): String{
        if(obj.hasAnnotation<XmlAttribute>())
            return obj.findAnnotation<XmlAttribute>()!!.name
        return obj.name
    }

    private fun getEntityName(obj:Any): Entity{
        if(obj::class.hasAnnotation<XmlEntity>())
            return Entity(obj::class.findAnnotation<XmlEntity>()!!.name)
        return Entity(obj::class.simpleName!!)
    }

    private fun handleProperties(obj:Any, property:KProperty<*>, father: Entity){
        val ent = Entity(getXmlAttributeName(property))
        if(property.call(obj) is List<*>) { //Onde se vão adicionar os filhos
            for (att in property.call(obj) as List<*>)
                ent.addChildEntity(translate(att!!))
            father.addChildEntity(ent)
        }
        else if(property.hasAnnotation<InlineAttribute>()) //Onde se adicionam os atributos na mesma linha
            father.addAttribute(getXmlAttributeName(property), property.call(obj).toString())
        else { //Onde se adiciona os atributos com text
            ent.addText(property.call(obj).toString())
            father.addChildEntity(ent)
        }
    }
}