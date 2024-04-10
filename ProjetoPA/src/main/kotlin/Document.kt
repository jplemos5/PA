import java.io.File
import java.lang.StringBuilder

class Document(name: String, private var version: String, private var encoding: String) {
    private val entity: Entity = Entity(name)

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

}