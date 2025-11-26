public class CCompilerFactory implements AbstractCompilerFactory {
    private String languageName;

    private CCompilerFactory() {
        languageName = "C";
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