
fun main(args: Array<String>) {
//    val doc = Document()
//    val local = Entity("local", mutableMapOf())
//    val foreign = Entity("externo", mutableMapOf())
//    doc.addEntity(local)
//    doc.addEntity(foreign)
//    println(doc.toString())
//    doc.removeEntity(foreign)
//    println(doc.toString())
//    local.addAttribute("atributo", "1")
//    local.addAttribute("atributos", "2")
//    local.addAttribute("atributoss", "3")
//    println(local.toString())
//    local.removeAttribute("atributo")
//    println(local.toString())
//    local.changeAttribute("atributoss", "4")
//    println(local.toString())
//    foreign.addChildEntity(local)
//
//    val test1 = Entity("test1", mutableMapOf())
//    val test2 = Entity("test2", mutableMapOf())
//    foreign.addChildEntity(test1)
//    test1.addChildEntity(test2)
//    println(foreign.toString())
//    println(foreign.prettyPrint())
    // Criar um novo documento
    val doc = Document("plano", "1.0", "UTF-8")

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
    nomeFuc2.addText("Dissertação")
    fuc2.addChildEntity(nomeFuc2)

    val ectsFuc2 = Entity("ects", linkedMapOf(), fuc2)
    ectsFuc2.addText("42.0")
    fuc2.addChildEntity(ectsFuc2)


    val avaliacaoFuc2 = Entity("avaliacao", linkedMapOf(), fuc2)
    fuc2.addChildEntity(avaliacaoFuc2)

    val componente1Fuc2 = Entity("componente", linkedMapOf("nome" to "Dissertação", "peso" to "60%"), avaliacaoFuc2)
    avaliacaoFuc2.addChildEntity(componente1Fuc2)

    val componente2Fuc2 = Entity("componente", linkedMapOf("nome" to "Apresentação", "peso" to "20%", "testte" to "20%", "testeeee" to "20%"), avaliacaoFuc2)
    avaliacaoFuc2.addChildEntity(componente2Fuc2)

    val componente3Fuc2 = Entity("componente", linkedMapOf("nome" to "Discussão", "peso" to "20%"), avaliacaoFuc2)
    avaliacaoFuc2.addChildEntity(componente3Fuc2)






//    println("---------Teste Visitor-----------")
//    // Imprimir os atributos de todas as entidades
//    val attributePrinterVisitor = Entity.AttributePrinterVisitor()
//    doc.getEntities().forEach { it.accept(attributePrinterVisitor) }
//
//    println("------------------")
//
//    // Imprimir apenas os nomes de todas as entidades
//    doc.getEntities().forEach { it.namePrinter() }


// Criar um novo Visitor para adicionar atributos à entidade desejada
    //val addAttributeVisitor = Entity.AddAttributeToEntityVisitor("componente", "goncalo", "ganda programador meu")


//    val addAttributeVisitor = Entity.AddAttributeToEntityVisitor("nome", "joao", "lindo")

    // Aplicar o Visitor à entidade desejada no documento
    //doc.getEntities().forEach { it.accept(addAttributeVisitor) }

//    doc.getRootEntity().globalAddAttributeToEntity("componente", "teste", "2")
//    doc.getRootEntity().globalRenameEntity("nome", "TESSSSSSSSSSSSSSSSSSS")
//    doc.getRootEntity().globalRenameAttribute("componente", "nome", "Testttttt" )
//    doc.getRootEntity().globalRenameAttribute("componente", "testte", "Testt" )
//    doc.getRootEntity().globalRemoveEntity("ects")
//    doc.getRootEntity().globalRemoveAttribute("componente", "peso")
//    // Imprimir o documento
//    println(doc.prettyPrint())

//    println(doc.getRootEntity().globalXPath("plano/curso"))
    // Escrever o documento em um arquivo
//    doc.writeToFile("Teste1.xml")

    class AddPercentage : Transformer {
        override fun transform(input: String): String = "$input%"
    }

    class AddParenthesis : Transformer {
        override fun transform(input: String): String = "($input)"
    }

    class ChangeAttribute : Adapter {
        override fun adapt(input: Entity): Entity {
            input.changeAttribute("code", "M!@!@#!#")
            return input
        }
    }

    class ChangeEntityName : Adapter {
        override fun adapt(input: Entity): Entity {
            input.globalRenameEntity("avaliacao", "TESTTTTTTTTT")
            return input
        }
    }


    @XmlEntity ("component")
    data class ComponenteAvaliacao(
        @XmlAttributeName("name") @InlineAttribute @XmlValueTransformer(AddParenthesis::class)
        val nome: String,
        @XmlAttributeName("weight") @InlineAttribute @XmlValueTransformer(AddPercentage::class)
        val peso: Int)

    @XmlAdapter(ChangeEntityName::class)
    @XmlEntity ("fuc")
    data class FUC(
        @XmlAttributeName("code") @InlineAttribute
        val codigo: String,
        @XmlAttributeName("name")
        val nome: String,
        val ects: Double,
        @Exclude
        val observacoes: String,
        val avaliacao: List<ComponenteAvaliacao>)



    val componente = ComponenteAvaliacao( "nome", 5)
    val componente1 = ComponenteAvaliacao( "teste", 100000)
    val fuc = FUC( "M2332", "Programação Avançada",  6.0, "lalalala", mutableListOf(componente,componente1) )
    val c = XMLClasses()
    println(c.translate(componente).prettyPrint())
    println(c.translate(fuc).prettyPrint())
    val en = c.translate(fuc)
    en.globalRemoveEntity("ects")
    println(en.prettyPrint())
}

