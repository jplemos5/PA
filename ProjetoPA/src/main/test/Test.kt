import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.reflect.full.*


fun createDoc(rootName: String, version: String, encoding: String): Document {
    // Criar um novo documento
    val doc = Document(rootName, version, encoding)

    // Criar as entidades e adicionar ao documento


    val curso = Entity("curso", linkedMapOf())
    curso.addText("Mestrado em Engenharia Informática")
    doc.getRootEntity().addChildEntity(curso)

    val fuc1 = Entity("fuc", linkedMapOf("codigo" to "M4310"))
    doc.getRootEntity().addChildEntity(fuc1)

    val nomeFuc1 = Entity("nome", linkedMapOf(), fuc1)
    nomeFuc1.addText("Programação Avançada")
    fuc1.addChildEntity(nomeFuc1)

    val ectsFuc1 = Entity("ects", linkedMapOf(), fuc1)
    ectsFuc1.addText("6.0")
    fuc1.addChildEntity(ectsFuc1)

    val avaliacaoFuc1 = Entity("avaliacao", linkedMapOf(), fuc1)
    fuc1.addChildEntity(avaliacaoFuc1)

    val componente1Fuc1 = Entity("componente", linkedMapOf("nome" to "Quizzes", "peso" to "20%"), avaliacaoFuc1)
    avaliacaoFuc1.addChildEntity(componente1Fuc1)

    val componente2Fuc1 = Entity("componente", linkedMapOf("nome" to "Projeto", "peso" to "80%"), avaliacaoFuc1)
    avaliacaoFuc1.addChildEntity(componente2Fuc1)

    val fuc2 = Entity("fuc", linkedMapOf("codigo" to "03782"))
    doc.getRootEntity().addChildEntity(fuc2)

    val nomeFuc2 = Entity("nome", linkedMapOf(), fuc2)
    nomeFuc2.addAttribute("Dissertação", "")
    fuc2.addChildEntity(nomeFuc2)

    val ectsFuc2 = Entity("ects", linkedMapOf(), fuc2)
    ectsFuc2.addText("42.0")
    fuc2.addChildEntity(ectsFuc2)


    val avaliacaoFuc2 = Entity("avaliacao", linkedMapOf(), fuc2)
    fuc2.addChildEntity(avaliacaoFuc2)

    val componente1Fuc2 = Entity("componente", linkedMapOf("nome" to "Dissertação", "peso" to "60%"), avaliacaoFuc2)
    avaliacaoFuc2.addChildEntity(componente1Fuc2)


    val componente2Fuc2 = Entity("componente", linkedMapOf("nome" to "Apresentação", "peso" to "20%"), avaliacaoFuc2)
    avaliacaoFuc2.addChildEntity(componente2Fuc2)

    val componente3Fuc2 = Entity("componente", linkedMapOf("nome" to "Discussão", "peso" to "20%"), avaliacaoFuc2)
    avaliacaoFuc2.addChildEntity(componente3Fuc2)


    return doc
}



class Test {

    //Document.kt
    @Test
    fun testSetAndGetVersion(){
        val documentTest  = Document("artists","1.0", "UTF-8")
        documentTest.setVersion("24.0")
        assertEquals(documentTest.getVersion(), "24.0")
    }

    @Test
    fun testSetAndGetEncoding(){
        val documentTest  = Document("artists","1.0", "UTF-8")
        documentTest.setEncoding("PA")
        assertEquals(documentTest.getEncoding(), "PA")
    }

    //Entity.kt

    @Test
    fun testEntityCreationException() {
        assertThrows<IllegalArgumentException> { Entity("") }
    }

    @Test
    fun testAttributeAdditionException() {
        val root = Entity("root")
        assertThrows<IllegalArgumentException> { root.addAttribute("", "") }
    }


    @Test
    fun testAddEntity() {
        val doc = createDoc("plano", "1.0", "UTF-8")
        val novaEntidade = Entity("novaEntidade", linkedMapOf())
        doc.getRootEntity().addChildEntity(novaEntidade)
        assertTrue(doc.getRootEntity().getChildren().contains(novaEntidade))
    }

    @Test
    fun testRemocaoDeEntidade() {
        val doc = createDoc("plano", "1.0", "UTF-8")
        // Adicionar a entidade fuc2
//        val fuc2 = Entity("fuc", mutableMapOf("codigo" to "03782"))
//        doc.getRootEntity().addChildEntity(fuc2)
        // Remover a entidade fuc2
        val fuc = doc.getRootEntity().getChildren().find { it.getName() == "fuc" }
        doc.getRootEntity().removeChildEntity(fuc!!)
        assertFalse(doc.getRootEntity().getChildren().contains(fuc))
    }

    @Test
    fun testAdicionarRemoverAlterarAtributosEmEntidades() {
        val doc = createDoc("plano", "1.0", "UTF-8")

        // Obter a entidade fuc
        val fuc = doc.getRootEntity().getChildren().find { it.getName() == "fuc" }


        // Adicionar atributo
        fuc!!.addAttribute("novoAtributo", "valor")
        assertEquals("valor", fuc.getAttributes()["novoAtributo"])

        // Remover atributo
        fuc.removeAttribute("codigo")
        assertNull(fuc.getAttributes()["codigo"])

        // Alterar atributo
        fuc.changeAttribute("novoAtributo", "novoValor")
        assertEquals("novoValor", fuc.getAttributes()["novoAtributo"])
    }

    @Test
    fun testAcederEntidadeMaeEEntidadesFilhasDeUmaEntidade() {
        val doc = createDoc("plano", "1.0", "UTF-8")
        val fuc = doc.getRootEntity().getChildren().find { it.getName() == "fuc" }

        assertEquals(doc.getRootEntity(), fuc!!.getParent())

        // Adicionar entidades filhas a fuc1
        val nomeFuc1 = Entity("nome", linkedMapOf(), fuc)
        val ectsFuc1 = Entity("ects", linkedMapOf(), fuc)
        fuc.addChildEntity(nomeFuc1)
        fuc.addChildEntity(ectsFuc1)

        assertTrue(fuc.getChildren().contains(nomeFuc1))
        assertTrue(fuc.getChildren().contains(ectsFuc1))
    }

    @Test
    fun testPrettyPrintFormatoStringEscritaArquivo() {
        val doc = createDoc("plano", "1.0", "UTF-8")

        val prettyPrintString = doc.prettyPrint()
        assertTrue(prettyPrintString.isNotBlank())

        val fileName = "TesteDocumento.xml"
        doc.writeToFile(fileName)
        val fileContent = File(fileName).readText()
        assertEquals(prettyPrintString, fileContent)
    }

    @Test
    fun testVarrimentoDocumentoComObjetosVisitantes() {
        val doc = createDoc("plano", "1.0", "UTF-8")
        val entityListResult = doc.getRootEntity().entityList()

        val expectedList = listOf(
            "plano", "curso", "fuc", "nome", "ects", "avaliacao",
            "componente", "componente", "fuc", "nome", "ects",
            "avaliacao", "componente", "componente", "componente"
        )

        assertEquals(expectedList, entityListResult)
    }

    @Test
    fun testAdicionarAtributosGlobalmenteAoDocumento() {
        val doc = createDoc("plano", "1.0", "UTF-8")
        val attributeName = "Ditado"
        val attributeValue = "12%"
        val entityName = "componente"
        val alteredDoc = createDoc("plano", "1.0", "UTF-8")
        alteredDoc.getRootEntity().globalAddAttributeToEntity(entityName, attributeName, attributeValue)
        val foundEntities = mutableListOf<Entity>()
        val visitor = object : Entity.Visitor {
            override fun visit(entity: Entity) {
                if (entity.getName() == entityName) {
                    foundEntities.add(entity)
                }
            }
        }
        alteredDoc.getRootEntity().accept(visitor)
        foundEntities.forEach {
            val attribute = it.getAttributes()[attributeName]
            assertEquals(attributeValue, attribute)
        }
    }

    @Test
    fun testRenomearEntidadesGlobalmente(){
        val doc = createDoc("plano", "1.0", "UTF-8")
        val oldName = "fuc"
        val newName = "test"
        val alteredDoc =  createDoc("plano", "1.0", "UTF-8")
        alteredDoc.getRootEntity().globalRenameEntity(oldName, newName)
        val foundOldEntity = mutableListOf<Entity>()
        val visitorOldName = object : Entity.Visitor {
            override fun visit(entity: Entity) {
                if (entity.getName() == oldName) {
                    foundOldEntity.add(entity)
                }
            }
        }
        alteredDoc.getRootEntity().accept(visitorOldName)
        val foundNewEntity = mutableListOf<Entity>()
        val visitorNewName = object : Entity.Visitor {
            override fun visit(entity: Entity) {
                if (entity.getName() == newName) {
                    foundNewEntity.add(entity)
                }
            }
        }
        alteredDoc.getRootEntity().accept(visitorNewName)
        assertEquals(foundOldEntity.size, 0)
        assertTrue(foundNewEntity.size > 0)

    }

    @Test
    fun testRenomearAtributosGlobalmente(){
        val doc = createDoc("plano", "1.0", "UTF-8")
        val entityName = "componente"
        val oldName = "nome"
        val newName = "name"
        val alteredDoc = createDoc("plano", "1.0", "UTF-8")
        alteredDoc.getRootEntity().globalRenameAttribute(entityName, oldName, newName)
        val foundEntities = mutableListOf<Entity>()
        val visitor = object : Entity.Visitor {
            override fun visit(entity: Entity) {
                if (entity.getName() == entityName) {
                    foundEntities.add(entity)
                }
            }
        }
        alteredDoc.getRootEntity().accept(visitor)
        foundEntities.forEach {
            assertTrue(it.getAttributes().contains(newName))
        }
    }

    @Test
    fun testRemoverEntidadesGlobalmente(){
        val doc = createDoc("plano", "1.0", "UTF-8")
        val entityName = "componente"
        val alteredDoc = createDoc("plano", "1.0", "UTF-8")
        alteredDoc.getRootEntity().globalRemoveEntity(entityName)
        val foundEntities = mutableListOf<Entity>()
        val visitor = object : Entity.Visitor {
            override fun visit(entity: Entity) {
                if (entity.getName() == entityName) {
                    foundEntities.add(entity)
                }
            }
        }
        alteredDoc.getRootEntity().accept(visitor)
        assertTrue(foundEntities.size == 0)
    }

    @Test
    fun testRemoverAtributosGlobalmente(){
        val doc = createDoc("plano", "1.0", "UTF-8")
        val entityName = "componente"
        val attributeName = "peso"
        val alteredDoc = createDoc("plano", "1.0", "UTF-8")
        alteredDoc.getRootEntity().globalRemoveAttribute(entityName, attributeName)
        val foundEntities = mutableListOf<Entity>()
        val visitor = object : Entity.Visitor {
            override fun visit(entity: Entity) {
                if (entity.getName() == entityName) {
                    foundEntities.add(entity)
                }
            }
        }
        alteredDoc.getRootEntity().accept(visitor)
        foundEntities.forEach {
            assertFalse(it.getAttributes().contains(attributeName))
        }
    }

    @Test
    fun testPrintXPathGlobalmente(){
        val original = "<componente nome=\"Quizzes\" peso=\"20%\"/>\n" +
                "<componente nome=\"Projeto\" peso=\"80%\"/>\n" +
                "<componente nome=\"Dissertação\" peso=\"60%\"/>\n" +
                "<componente nome=\"Apresentação\" peso=\"20%\"/>\n" +
                "<componente nome=\"Discussão\" peso=\"20%\"/>"
        val path = "fuc/avaliacao/componente"
        val xPath = createDoc("plano", "1.0", "UTF-8").getRootEntity().globalPrintXPath(path)
        assertEquals(original,xPath)
    }

    //Annotations.kt

    @XmlEntity("fuc")
    data class Course(
        @XmlAttributeName("codigo") val code: String,
        @InlineAttribute val name: String,
        val ects: Int
    )

    @Test
    fun testXmlEntityAnnotation() {
        val xmlEntityAnnotation = Course::class.findAnnotation<XmlEntity>()
        assertNotNull(xmlEntityAnnotation)
        assertEquals("fuc", xmlEntityAnnotation?.name)
    }

    @Test
    fun testXmlAttributeNameAnnotation() {
        val codeProperty = Course::class.members.find { it.name == "code" }
        val xmlAttributeNameAnnotation = codeProperty?.findAnnotation<XmlAttributeName>()
        assertNotNull(xmlAttributeNameAnnotation)
        assertEquals("codigo", xmlAttributeNameAnnotation?.name)
    }

    @Test
    fun testInlineAttributeAnnotation() {
        val nameProperty = Course::class.members.find { it.name == "name" }
        val inlineAttributeAnnotation = nameProperty?.findAnnotation<InlineAttribute>()
        assertNotNull(inlineAttributeAnnotation)
    }


    class Exclude(private val entityName: String) : Adapter {
        override fun adapt(input: Entity): Entity {
            input.globalRemoveEntity(entityName)
            return input
        }
    }

    class AddPercentage : Transformer {
        override fun transform(input: String): String = "$input%"
    }

    class AddParenthesis : Transformer {
        override fun transform(input: String): String = "($input)"
    }


    @Test
    fun testAddPercentageTransformer() {
        val transformer = AddPercentage()
        val input = "50"
        val output = transformer.transform(input)
        assertEquals("50%", output)
    }

    @Test
    fun testAddParenthesisTransformer() {
        val transformer = AddParenthesis()
        val input = "test"
        val output = transformer.transform(input)
        assertEquals("(test)", output)
    }

    class XmlValueTransformer(private val transformFunction: (String) -> String) : Transformer {
        override fun transform(input: String): String = transformFunction(input)
    }

    class XmlAdapter(private val adaptFunction: (Entity) -> Entity) : Adapter {
        override fun adapt(input: Entity): Entity = adaptFunction(input)
    }


    class ChangeAttribute : Adapter {
        override fun adapt(input: Entity): Entity {
            input.changeAttribute("code", "teste")
            return input
        }
    }

    class ChangeEntityName : Adapter {
        override fun adapt(input: Entity): Entity {
            input.globalRenameEntity("curso", "test")
            return input
        }
    }

    @Test
    fun testChangeAttributeAdapter() {
        val adapter = ChangeAttribute()
        val entity = Entity("exampleEntity", linkedMapOf("code" to "M1234"))
        val adaptedEntity = adapter.adapt(entity)
        assertEquals("teste", adaptedEntity.getAttributes()["code"])
    }

    @Test
    fun testChangeEntityNameAdapter() {
        val adapter = ChangeEntityName()
        val avaliacaoEntity = Entity(name = "avaliacao")
        val cursoEntity = Entity("curso")
        avaliacaoEntity.addChildEntity(cursoEntity)
        val adaptedEntity = adapter.adapt(cursoEntity)
        assertEquals("test", adaptedEntity.getName())
    }

    @Test
    fun testExcludeAdapter() {
        val rootEntity = Entity("plano")
        val cursoEntity = Entity("curso")
        rootEntity.addChildEntity(cursoEntity)
        val avaliacaoEntity = Entity("avaliacao")
        cursoEntity.addChildEntity(avaliacaoEntity)
        val adapter = Exclude("avaliacao")
        adapter.adapt(rootEntity)
        val entityNames = rootEntity.entityList()
        assertFalse("avaliacao" in entityNames)
    }

    @Test
    fun testXmlValueTransformer() {
        val transformer = XmlValueTransformer { input -> "<tag>$input</tag>" }
        val transformedValue = transformer.transform("valor")
        assertEquals("<tag>valor</tag>", transformedValue)
    }


    @Test
    fun testXmlAdapter() {
        val rootEntity = Entity("plano")
        val cursoEntity = Entity("curso")
        rootEntity.addChildEntity(cursoEntity)
        val adapter = XmlAdapter { entity ->
            entity.globalRenameEntity("curso", "course")
            entity
        }
        val adaptedEntity = adapter.adapt(rootEntity)
        val entityNames = adaptedEntity.entityList()
        assertEquals(false, "curso" in entityNames)
    }

    //DSL.kt

    @Test
    fun testAddChildEntity() {
        val rootEntity = Entity("root")
        val childEntity = Entity("child")
        rootEntity fatherOf childEntity
        assertEquals(childEntity, rootEntity.getChildren().first())
    }

    @Test
    fun testDocumentAndChildEntity(){
        val documentDSL  = document("artists","1.0", "UTF-8") {
            childEntity("beatles", linkedMapOf("nome" to "Dissertação", "peso" to "60%")) {
            }
        }
        val actualPrettyPrint = documentDSL.prettyPrint()
        val expectedPrettyPrint = """
        <?xml version="1.0" encoding="UTF-8"?>
        <artists>
            <beatles nome="Dissertação" peso="60%"/>
        </artists>
        """.trimIndent()
        assertEquals(expectedPrettyPrint, actualPrettyPrint, "Expected and actual documents do not match")

    }

    @Test
    fun testAddAttribute() {
        val entity = Entity("entity")
        entity.attributeName("attr", "value")
        assertEquals("value", entity["attr"])
    }

    @Test
    fun testAddText() {
        val rootEntity = Entity("root")
        val childEntity = Entity("child")
        rootEntity fatherOf childEntity
        childEntity.text("text")
        assertEquals("text", childEntity.getText())
    }

    @Test
    fun testIsChildOf() {
        val parent = Entity("parent")
        val child = Entity("child")
        parent fatherOf child
        assertEquals(true, "child" isChildOf parent)
    }

    @Test
    fun testIsAttributeOf() {
        val entity = Entity("entity")
        entity.attributeName("attr", "value")
        assertEquals(true, "attr" isAttributeOf entity)
    }

    @Test
    fun testInlineAttributesOf() {
        val entity = Entity("entity")
        val attributes = linkedMapOf("attr1" to "value1", "attr2" to "value2")
        attributes.inlineAttributesOf(entity)
        val expectedPrettyPrint = "<entity attr1=\"value1\" attr2=\"value2\"/>"
        val actualPrettyPrint = entity.prettyPrint()
        assertEquals(expectedPrettyPrint, actualPrettyPrint, "Expected and actual pretty prints do not match")
    }

    @Test
    fun testInsideAttributesOf() {
        val entity = Entity("entity")
        val attributes = linkedMapOf("attr1" to "value1")
        attributes.insideAttributesOf(entity)
        val expectedPrettyPrint = "<entity>\n    <attr1>value1</attr1>\n</entity>"
        val actualPrettyPrint = entity.prettyPrint()
        assertEquals(expectedPrettyPrint, actualPrettyPrint, "Expected and actual pretty prints do not match")
    }

    @Test
    fun testDivOperator(){
        val fuc = Entity("fuc")
        val componente = Entity("componente")
        fuc fatherOf componente
        assertEquals(componente,fuc / "componente" )
    }

    @Test
    fun testGetOperator(){
        val fuc = Entity("fuc")
        val componente = Entity("componente", linkedMapOf("valor" to "20"))
        fuc fatherOf componente
        assertEquals("20",(fuc / "componente")["valor"] )
    }
    

    // XMLClasses.kt
    @Test
    fun testisValidEntityName(){
        assertFalse(isValidEntityName("1errado"))
    }

    @Test
    fun testisValidAttributeName(){
        assertFalse(isValidAttributeName(""))
    }



}



