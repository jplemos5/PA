class Document {

    private val entities: MutableList<Entity> = mutableListOf<Entity>()

    fun addEntity(entity: Entity) =
        entities.add(entity)


    fun removeEntity(entity: Entity) =
        entities.remove(entity)

    override fun toString(): String {
        return "Entities: $entities "
    }


}