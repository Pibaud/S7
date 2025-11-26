public class JavaCompilerFactory implements AbstractCompilerFactory {
    private String languageName;

    private JavaCompilerFactory() {
        this.languageName = "Java";
    }

    @Override
    public Lexer createLexer() {
        return null;
    }

    @Override
    public Parser createParser() {
        return null;
    }

    @Override
    public Generator createGenerator() {
        return null;
    }
}