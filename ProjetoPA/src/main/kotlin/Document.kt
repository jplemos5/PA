import java.io.File
import java.lang.StringBuilder

class Document {

    private val entities: MutableList<Entity> = mutableListOf<Entity>()

    fun addEntity(entity: Entity) =
        entities.add(entity)


    fun removeEntity(entity: Entity) =
        entities.remove(entity)

//    override fun toString(): String {
//        return "Entities: $entities "
//    }

    fun prettyPrint(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
        entities.forEach { entity ->
            stringBuilder.append(entity.prettyPrint())
        }
        return stringBuilder.toString()
    }

    fun writeToFile(fileName: String) {
        val prettyString = prettyPrint()
        File(fileName).writeText(prettyString)
    }


}