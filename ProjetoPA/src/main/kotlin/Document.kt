import java.io.*
import java.lang.StringBuilder

/**
 * Represents an XML document with a root entity, version, and encoding.
 * @param name The name of the document.
 * @property version The version of the document.
 * @property encoding The encoding of the document.
 * @property entity The Root Entity of the document.
 * @throws IllegalArgumentException if the version or encoding is invalid.
 */
class Document(name: String, private var version: String, private var encoding: String) {

    init {
        require(version.matches(Regex("""\d+(\.\d+)*"""))) { "Invalid XML version format" }
        require(encoding.isNotEmpty()) { "Encoding cannot be empty" }
    }


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
     * @throws IllegalArgumentException if the version is invalid.
     */
    fun setVersion(version: String) {
        require(version.matches(Regex("""\d+(\.\d+)*"""))) { "Invalid XML version format" }
        this.version = version
    }

    /**
     * Sets the encoding of the document.
     * @param encoding The new encoding for the document.
     * @throws IllegalArgumentException if the encoding is empty.
     */
    fun setEncoding(encoding: String) {
        require(encoding.isNotEmpty()) { "Encoding cannot be empty" }
        this.encoding = encoding
    }

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
     * @throws IOException if an I/O error occurs while writing to the file.
     */
    @Throws(IOException::class)
    fun writeToFile(fileName: String) {
        try {
            File(fileName).writeText(prettyPrint())
        } catch (e: IOException) {
            throw IOException("Failed to write to file: $fileName", e)
        }
    }

}