package net.icxd.compiler.lexer;

public class Token {

    private Type type;
    private String value;
    private int line;
    private int start;
    private int end;

    public Token(Type type, String value, int line, int start, int end) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.start = start;
        this.end = end;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", line=" + line +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    public enum Type {
        // Keywords
        IF("if"),
        ELSE("else"),
        WHILE("while"),
        FOR("for"),
        DO("do"),
        PRINT("print"),
        BREAK("break"),
        CONTINUE("continue"),
        RETURN("return"),
        CLASS("class"),
        CONSTRUCTOR("constructor"),
        NEW("new"),
        THIS("this"),
        SUPER("super"),
        NULL("null"),
        TRUE("true"),
        FALSE("false"),
        IMPORT("import"),
        FROM("from"),
        AS("as"),
        INSTANCEOF("instanceof"),
        // Operators
        ASSIGN("="),
        ADD("+"),
        SUB("-"),
        MUL("*"),
        DIV("/"),
        MOD("%"),
        EQ("=="),
        NE("!="),
        LT("<"),
        LE("<="),
        GT(">"),
        GE(">="),
        AND("&&"),
        OR("||"),
        NOT("!"),
        // Delimiters
        LPAREN("("),
        RPAREN(")"),
        LBRACE("{"),
        RBRACE("}"),
        LBRACKET("["),
        RBRACKET("]"),
        SEMICOLON(";"),
        COMMA(","),
        DOT("."),
        // Literals
        IDENTIFIER(""),
        INTEGER_LITERAL(""),
        FLOAT_LITERAL(""),
        STRING_LITERAL(""),
        BOOLEAN_LITERAL(""),
        CHAR_LITERAL(""),
        // Data types
        INT("int"),
        FLOAT("float"),
        BOOLEAN("boolean"),
        STRING("string"),
        OBJECT("object"),
        INSTANCE("instance"),
        VOID("void"),
        // Special tokens
        EOF(""),
        ERROR("");

        private final String regex;
        Type(String regex) {
            this.regex = regex;
        }
        public String getRegex() {
            return regex;
        }
    }
}
