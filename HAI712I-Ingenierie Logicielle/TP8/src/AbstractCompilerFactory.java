public interface AbstractCompilerFactory {
    public Lexer createLexer();
    public Parser createParser();
    public Generator createGenerator();
}
