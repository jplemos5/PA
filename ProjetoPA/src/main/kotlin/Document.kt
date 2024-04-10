import java.io.File
import java.lang.StringBuilder

class Document {
    private val entity: Entity
    private var  version : String
    private var encoding : String

    constructor(name: String, version: String, encoding: String){
        this.entity = Entity(name)
        this.version = version
        this.encoding = encoding
    }

    fun getVersion() : String = version

    fun getEncoding() : String = encoding

    fun setVersion(version: String) {
        this.version = version
    }

    fun setEncoding(encoding: String) {
        this.encoding = encoding
    }

    fun getRootEntity(): Entity = entity

    fun prettyPrint(): String = StringBuilder("<?xml version=\"$version\" encoding=\"$encoding\"?>\n" + entity.prettyPrint()).toString()

    fun writeToFile(fileName: String) = File(fileName).writeText(prettyPrint())

//    fun addAttributeToAllEntities(entityName: String, attributeName: String, attributeValue: String) {
//        entities.forEach { it.accept(Entity.AddAttributeVisitor(entityName, attributeName, attributeValue)) }
//    }



}