public class AdaCompilerFactory implements AbstractCompilerFactory {
    private String languageName;

    private AdaCompilerFactory() {
        languageName = "Ada";
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