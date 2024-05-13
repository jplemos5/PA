import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class XMLClasses {

    fun translate (obj: Any): Entity{
        val e = Entity(obj::class.simpleName!!)
        obj::class.declaredMemberProperties.forEach {
            val ent = Entity(it.name)
            if(it.call(obj) is List<*>) { //TODO onde se vão adicionar os filhos
                for (att in it.call(obj) as List<*>)
                    ent.addChildEntity(translate(att!!))
                e.addChildEntity(ent)
            }
            else if(obj is ComponenteAvaliacao || (obj is FUC && it.name == "codigo")) //TODO onde se vão adicionar os atributos na mesma linha
                e.addAttribute(it.name, it.call(obj).toString())
            else  { //TODO onde se adiciona os atributos com text
                ent.addText(it.call(obj).toString())
                e.addChildEntity(ent)
            }
        }
        return e
    }

    //if(obj::class.hasAnnotation<EntityName>())
    //return Entity(obj::class.findAnnotation<EntityName>()?.name!!, lMap)
}