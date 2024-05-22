import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class XmlEntity(val name:String)

@Target(AnnotationTarget.PROPERTY)
annotation class XmlAttributeName(val name: String)

@Target(AnnotationTarget.PROPERTY)
annotation class InlineAttribute

@Target(AnnotationTarget.PROPERTY)
annotation class Exclude

@Target(AnnotationTarget.PROPERTY)
annotation class XmlValueTransformer(val transformer: KClass<out Transformer>)

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class XmlAdapter(val adapter: KClass<out Adapter>)


interface Transformer {
    fun transform(input: String): String
}

interface Adapter {
    fun adapt(input: Entity): Entity
}