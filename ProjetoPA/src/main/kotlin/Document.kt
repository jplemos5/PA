import java.io.File
import java.lang.StringBuilder

/**
 * Represents an XML document with a root entity, version, and encoding.
 * @param name The name of the document.
 * @property version The version of the document.
 * @property encoding The encoding of the document.
 * @property entity The Root Entity of the document.
 */
class Document(name: String, private var version: String, private var encoding: String) {
    // Root entity of the document
    private val entity: Entity = Entity(name)

    /**
     * Gets the version of the document.
     * @return The version of the document.
     */
    fun getVersion() : String = version

    /**
     * Gets the encoding of the document.
     * @return The encoding of the document.
     */
    fun getEncoding() : String = encoding

    /**
     * Sets the version of the document.
     * @param version The new version for the document.
     */
    fun setVersion(version: String) { this.version = version }

    /**
     * Sets the encoding of the document.
     * @param encoding The new encoding for the document.
     */
    fun setEncoding(encoding: String) { this.encoding = encoding }

    /**
     * Gets the root entity of the document.
     * @return The root entity of the document.
     */
    fun getRootEntity(): Entity = entity

    /**
     * Generates a pretty-printed string representation of the document.
     * @return A pretty-printed string representation of the document.
     */
    fun prettyPrint(): String = StringBuilder("<?xml version=\"$version\" encoding=\"$encoding\"?>\n" + entity.prettyPrint()).toString()

    /**
     * Writes the document content to a file with the specified filename.
     * @param fileName The name of the file to write to.
     */
    fun writeToFile(fileName: String) = File(fileName).writeText(prettyPrint())

}