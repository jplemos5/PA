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

    val componente1Fuc2 = Entity("componente", mutableMapOf("nome" to "Dissertação", "peso" to "60%"), avaliacaoFuc2)
    avaliacaoFuc2.addChildEntity(componente1Fuc2)
    println(avaliacaoFuc2.getText())

    val componente2Fuc2 = Entity("componente", mutableMapOf("nome" to "Apresentação", "peso" to "20%"), avaliacaoFuc2)
    avaliacaoFuc2.addChildEntity(componente2Fuc2)

    val componente3Fuc2 = Entity("componente", mutableMapOf("nome" to "Discussão", "peso" to "20%"), avaliacaoFuc2)
    avaliacaoFuc2.addChildEntity(componente3Fuc2)






    println("---------Teste Visitor-----------")
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

//    doc.getRootEntity().addAttributeToEntity("componente", "teste", "2")
//    doc.getRootEntity().renameEntity("plano", "teste")
//    doc.getRootEntity().renameAttribute("componente", "nome", "Testttttt" )
    doc.getRootEntity().globalRemoveEntity("plano")
//    doc.getRootEntity().globalRemoveAttribute("componente", "peso")
    // Imprimir o documento
    println(doc.prettyPrint())

    // Escrever o documento em um arquivo
    doc.writeToFile("Teste1.xml")

}

