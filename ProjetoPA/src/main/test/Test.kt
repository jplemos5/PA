import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.util.List


fun createDoc(rootName: String, version: String, encoding: String): Document {
    // Criar um novo documento
    val doc = Document(rootName, version, encoding)

    // Criar as entidades e adicionar ao documento
    val curso = Entity("curso", mutableMapOf())
    curso.addAttribute("Mestrado em Engenharia Informática", null)
    doc.getRootEntity().addChildEntity(curso)

    val fuc1 = Entity("fuc", mutableMapOf("codigo" to "M4310"))
    doc.getRootEntity().addChildEntity(fuc1)

    val nomeFuc1 = Entity("nome", mutableMapOf(), fuc1)
    nomeFuc1.addAttribute("Programação Avançada", null)
    fuc1.addChildEntity(nomeFuc1)

    val ectsFuc1 = Entity("ects", mutableMapOf(), fuc1)
    ectsFuc1.addAttribute("6.0", null)
    fuc1.addChildEntity(ectsFuc1)

    val avaliacaoFuc1 = Entity("avaliacao", mutableMapOf(), fuc1)
    fuc1.addChildEntity(avaliacaoFuc1)

    val componente1Fuc1 = Entity("componente", mutableMapOf("nome" to "Quizzes", "peso" to "20%"), avaliacaoFuc1)
    avaliacaoFuc1.addChildEntity(componente1Fuc1)

    val componente2Fuc1 = Entity("componente", mutableMapOf("nome" to "Projeto", "peso" to "80%"), avaliacaoFuc1)
    avaliacaoFuc1.addChildEntity(componente2Fuc1)

    val fuc2 = Entity("fuc", mutableMapOf("codigo" to "03782"))
    doc.getRootEntity().addChildEntity(fuc2)

    val nomeFuc2 = Entity("nome", mutableMapOf(), fuc2)
    nomeFuc2.addAttribute("Dissertação", "")
    fuc2.addChildEntity(nomeFuc2)

    val ectsFuc2 = Entity("ects", mutableMapOf(), fuc2)
    ectsFuc2.addAttribute("42.0", null)
    fuc2.addChildEntity(ectsFuc2)

    val avaliacaoFuc2 = Entity("avaliacao", mutableMapOf(), fuc2)
    fuc2.addChildEntity(avaliacaoFuc2)

    val componente1Fuc2 =
        Entity("componente", mutableMapOf("nome" to "Dissertação", "peso" to "60%"), avaliacaoFuc2)
    avaliacaoFuc2.addChildEntity(componente1Fuc2)

    val componente2Fuc2 =
        Entity("componente", mutableMapOf("nome" to "Apresentação", "peso" to "20%"), avaliacaoFuc2)
    avaliacaoFuc2.addChildEntity(componente2Fuc2)

    val componente3Fuc2 = Entity("componente", mutableMapOf("nome" to "Discussão", "peso" to "20%"), avaliacaoFuc2)
    avaliacaoFuc2.addChildEntity(componente3Fuc2)

    return doc
}

class Test {

    @Test
    fun testAdicaoDeEntidade() {
        val doc = createDoc("plano", "1.0", "UTF-8")
        val novaEntidade = Entity("novaEntidade", mutableMapOf())
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
        assertEquals("valor", fuc!!.getAttributes()["novoAtributo"])

        // Remover atributo
        fuc!!.removeAttribute("codigo")
        assertNull(fuc!!.getAttributes()["codigo"])

        // Alterar atributo
        fuc!!.changeAttribute("novoAtributo", "novoValor")
        assertEquals("novoValor", fuc!!.getAttributes()["novoAtributo"])
    }

    @Test
    fun testAcederEntidadeMaeEEntidadesFilhasDeUmaEntidade() {
        val doc = createDoc("plano", "1.0", "UTF-8")
        val fuc = doc.getRootEntity().getChildren().find { it.getName() == "fuc" }

        assertEquals(doc.getRootEntity(), fuc!!.getParent())

        // Adicionar entidades filhas a fuc1
        val nomeFuc1 = Entity("nome", mutableMapOf(), fuc)
        val ectsFuc1 = Entity("ects", mutableMapOf(), fuc)
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
//
//    @Test
//    fun testAdicionarAtributosGlobalmenteAoDocumento() {
//        val attributeName = "atributo"
//        val attributeValue = "valor"
//        val entityName = "componente"
//        val expectedResult = "{nome=Quizzes, peso=20%, $attributeName=$attributeValue}"
//
//        doc.getRootEntity().globalAddAttributeToEntity(entityName, attributeName, attributeValue)
//
//        assertEquals(expectedResult, componente1Fuc1.getAttributes())
//    }
}



