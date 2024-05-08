import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class XMLClasses {

    fun translate (obj: Any): Entity{
        val e = Entity(obj::class.simpleName!!)
        obj::class.declaredMemberProperties.forEach {
            if(it.call(obj) is MutableList<*>)
                for ( entity in it.call(obj) as MutableList<*>)
                    e.addChildEntity(translate(entity!!))
            else
                e.addAttribute(it.name, it.call(obj).toString())
        }
        return e
    }

    //if(obj::class.hasAnnotation<EntityName>())
    //return Entity(obj::class.findAnnotation<EntityName>()?.name!!, lMap)
}