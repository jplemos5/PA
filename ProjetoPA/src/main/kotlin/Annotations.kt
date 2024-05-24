import kotlin.reflect.KClass

/**
 * Annotation to mark a class as an XML entity with a specific name.
 * @property name The XML tag name for the annotated class.
 * @throws IllegalArgumentException if the provided name is empty, contains invalid characters or doesn't comply with the XML name standards
 */
@Target(AnnotationTarget.CLASS)
annotation class XmlEntity(val name: String)

/**
 * Annotation to specify the XML attribute name for a property.
 * @property name The XML attribute name for the annotated property.
 * @throws IllegalArgumentException if the provided name is empty, contains invalid characters or doesn't comply with the XML Standards.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class XmlAttributeName(val name: String)

/**
 * Annotation to mark a property as a description attribute in the XML, which means it will not be nested but next to the name of the Xml Entity.
 * No parameters needed, simply marks the property to be serialized in same line has the name of the entity.
 * Ex: <fuc codigo="03782">
 */
@Target(AnnotationTarget.PROPERTY)
annotation class InlineAttribute

/**
 * Annotation to exclude a property from being serialized to XML.
 * No parameters needed, simply marks the property to be excluded from serialization.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class Exclude

/**
 * Annotation to apply a transformer to a property value during serialization.
 * @property transformer The class of the transformer to be used for the property.
 */
@Target(AnnotationTarget.PROPERTY)
annotation class XmlValueTransformer(val transformer: KClass<out Transformer>)

/**
 * Annotation to apply an adapter to a property or class during serialization.
 * @property adapter The class of the adapter to be used for the property or class.
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class XmlAdapter(val adapter: KClass<out Adapter>)

/**
 * Interface for transforming a string value.
 * Implement this interface to define custom transformation logic.
 */
interface Transformer {
    /**
     * Transforms the input string value.
     * @param input The string value to be transformed.
     * @return The transformed string.
     * @throws IllegalArgumentException if the input is invalid or transformation fails.
     */
    fun transform(input: String): String
}

/**
 * Interface for adapting an entity after mapping it.
 * Implement this interface to define custom adaptation logic.
 */
interface Adapter {
    /**
     * Adapts the input entity.
     * @param input The entity to be adapted.
     * @return The adapted entity.
     * @throws IllegalArgumentException if the input entity is invalid or adaptation fails.
     */
    fun adapt(input: Entity): Entity
}