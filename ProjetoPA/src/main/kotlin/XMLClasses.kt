import kotlin.reflect.*
import kotlin.reflect.full.*

/**
 * Translates an object into an [Entity] representation.
 *
 * @param obj The object to translate.
 * @return The translated [Entity].
 */
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

/**
 * Retrieves the XML attribute name from a property, considering the [XmlAttributeName] annotation.
 *
 * @param obj The property to get the attribute name from.
 * @return The XML attribute name.
 */
private fun getXmlAttributeName(obj:KProperty<*>): String{
    if(obj.hasAnnotation<XmlAttributeName>() && isValidAttributeName(obj.findAnnotation<XmlAttributeName>()!!.name))
        return obj.findAnnotation<XmlAttributeName>()!!.name
    else if(isValidAttributeName(obj.name))
        return obj.name
    else
        throw  IllegalArgumentException("Attribute name is not valid")
}

/**
 * Retrieves the XML entity name from an object, considering the [XmlEntity] annotation.
 *
 * @param obj The object to get the entity name from.
 * @return The XML entity as an [Entity] object.
 * @throws IllegalArgumentException if the entity name is invalid or does not conform to XML naming standards.
 */
private fun getEntityName(obj:Any): Entity{
    if(obj::class.hasAnnotation<XmlEntity>() && isValidEntityName(obj::class.findAnnotation<XmlEntity>()!!.name))
        return Entity(obj::class.findAnnotation<XmlEntity>()!!.name)
    else if (isValidEntityName(obj::class.simpleName!!))
        return Entity(obj::class.simpleName!!)
    else
        throw IllegalArgumentException("Invalid XML entity name")
}

/**
 * Checks if the provided string is a valid XML entity name according to XML naming standards.
 *
 * XML entity names must adhere to the following rules:
 * - Must not be empty.
 * - Must start with a letter or underscore.
 * - Subsequent characters can be letters, digits, hyphens, underscores, or periods.
 *
 * @param name The string to validate as an XML entity name.
 * @return true if the name is a valid XML entity name, false otherwise.
 */
fun isValidEntityName(name: String): Boolean {
    if (name.isEmpty()) return false
    if (!name[0].isLetter() && name[0] != '_') return false
    for (char in name)
        if (!(char.isLetterOrDigit() || char == '-' || char == '_' || char == '.')) return false
    return true
}


/**
 * Checks if an attribute name is valid.
 * XML attribute names must adhere to the following rules:
 *  - Must not contain whitespace characters.
 *  - Must not be empty.
 *
 * @param attributeName The name of the attribute to add.
 * @return A boolean that means if the specified name is valid.
 */
fun isValidAttributeName(attributeName: String) = attributeName.split(" ").size == 1 && attributeName != ""


/**
 * Handles the properties of an object and adds them to the parent entity.
 *
 * @param obj The object containing the properties.
 * @param property The property to handle.
 * @param father The parent entity to add the handled property to.
 */
private fun handleProperties(obj:Any, property:KProperty<*>, father: Entity){
    val currentEntity = Entity(getXmlAttributeName(property))
    val attributeValue: String = getAttributeValue(obj, property)
    if(property.call(obj) is List<*>) {
        for (att in property.call(obj) as List<*>)
            currentEntity.addChildEntity(translate(att!!))
        father.addChildEntity(currentEntity)
    }
    else if(property.hasAnnotation<InlineAttribute>())
        father.addAttribute(getXmlAttributeName(property), attributeValue)
    else { //Onde se adiciona os atributos com text
        currentEntity.addText(attributeValue)
        father.addChildEntity(currentEntity)
    }
}

/**
 * Retrieves the value of a property, applying a transformation if [XmlValueTransformer] annotation is present.
 *
 * @param obj The object containing the property.
 * @param property The property to get the value from.
 * @return The transformed or raw value of the property as a String.
 */
private fun getAttributeValue(obj:Any, property: KProperty<*>): String {
    if (property.hasAnnotation<XmlValueTransformer>()) {
        val annotation = property.findAnnotation<XmlValueTransformer>()
        val transformer : Transformer = (annotation?.transformer?.createInstance() as Transformer)
        return transformer.transform(property.call(obj).toString())
    } else
        return  property.call(obj).toString()
}

/**
 * Extension property to get all the fields of a data class.
 *
 * @receiver The Kotlin class to get the fields from.
 * @return A list of properties representing the fields of the data class.
 * @throws IllegalArgumentException If the class is not a data class.
 */
private val KClass<*>.dataClassFields: List<KProperty<*>>
    get() {
        require(isData) { "instance must be data class" }
        return primaryConstructor!!.parameters.map { p ->
            declaredMemberProperties.find { it.name == p.name }!!
        }
    }
