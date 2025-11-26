public class Compiler {
    private Lexer lexer;
    private Parser parser;
    private Generator gen;
    private String languageName;

    public Compiler(String languageName, Lexer lexer, Parser parser, Generator gen) {
        this.languageName = languageName;
        //récupérer la bonne factory via une méthode statique
        AbstractCompilerFactory factory = AbstractCompilerFactory.getFactory(languageName);
        this.lexer = lexer;
        this.parser = parser;
        this.gen = gen;
    }

    public String compile() {
        return "Compiling in "+languageName+" ...\n" +
                "Using Lexer : "+lexer.toString()+" ...\n"+
                "Using Parser : "+parser.toString()+" ...\n"+
                "Using Generator : "+gen.toString()+" ...\n";
    }
}
