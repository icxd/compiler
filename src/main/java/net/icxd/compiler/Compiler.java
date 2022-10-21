package net.icxd.compiler;

import net.icxd.compiler.ast.AST;
import net.icxd.compiler.lexer.Lexer;
import net.icxd.compiler.lexer.Token;
import net.icxd.compiler.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Compiler {

    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();
        AST ast = new AST();

        ArrayList<Token> tokens = new ArrayList<>();

        InputStream is = Compiler.class.getResourceAsStream("/main.e");
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        for (String line; (line = reader.readLine()) != null;) {
            ArrayList<Token> t = lexer.lex(line);
            tokens.addAll(t);
        }

        Parser parser = new Parser(tokens);
        ast.addAll(parser.parse().getExps());
        ast.dump();
    }

}
