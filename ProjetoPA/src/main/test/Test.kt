import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File


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




    @Test
    fun testAdicaoDeEntidade() {
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
        val alteredDoc =  createDoc("plano", "1.0", "UTF-8")
        alteredDoc.getRootEntity().globalAddAttributeToEntity(entityName, attributeName, attributeValue)
        assertNotEquals(doc.getRootEntity().prettyPrint(), alteredDoc.getRootEntity().prettyPrint())
    }

    @Test
    fun testRenomearEntidadesGlobalmente(){
        val doc = createDoc("plano", "1.0", "UTF-8")
        val oldName = "componente"
        val newName = "test"
        val alteredDoc =  createDoc("plano", "1.0", "UTF-8")
        alteredDoc.getRootEntity().globalRenameEntity(oldName, newName)
        assertNotEquals(doc.getRootEntity().prettyPrint(), alteredDoc.getRootEntity().prettyPrint())
    }

    @Test
    fun testRenomearAtributosGlobalmente(){
        val doc = createDoc("plano", "1.0", "UTF-8")
        val entityName = "componente"
        val oldName = "nome"
        val newName = "name"
        val alteredDoc = createDoc("plano", "1.0", "UTF-8")
        alteredDoc.getRootEntity().globalRenameAttribute(entityName, oldName, newName)
        assertNotEquals(doc.getRootEntity().prettyPrint(), alteredDoc.getRootEntity().prettyPrint())
    }

    @Test
    fun testRemoverEntidadesGlobalmente(){
        val doc = createDoc("plano", "1.0", "UTF-8")
        val entityName = "componente"
        val alteredDoc = createDoc("plano", "1.0", "UTF-8")
        alteredDoc.getRootEntity().globalRemoveEntity(entityName)
        assertNotEquals(doc.getRootEntity().prettyPrint(), alteredDoc.getRootEntity().prettyPrint())
    }

    @Test
    fun testRemoverAtributosGlobalmente(){
        val doc = createDoc("plano", "1.0", "UTF-8")
        val entityName = "componente"
        val attributeName = "peso"
        val alteredDoc = createDoc("plano", "1.0", "UTF-8")
        alteredDoc.getRootEntity().globalRemoveAttribute(entityName, attributeName)
        assertNotEquals(doc.getRootEntity().prettyPrint(), alteredDoc.getRootEntity().prettyPrint())
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
}



