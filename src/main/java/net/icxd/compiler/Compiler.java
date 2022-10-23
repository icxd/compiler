package net.icxd.compiler;

import net.icxd.compiler.ast.AST;
import net.icxd.compiler.execute.Execution;
import net.icxd.compiler.lexer.Lexer;
import net.icxd.compiler.lexer.Token;
import net.icxd.compiler.parser.Parser;
import net.icxd.compiler.utils.ConsoleColors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Compiler {

    public static void main(String[] args) throws IOException {
        long cms = System.currentTimeMillis();

        Lexer lexer = new Lexer();
        AST ast = new AST();

        ArrayList<Token> tokens = new ArrayList<>();

        InputStream is = Compiler.class.getResourceAsStream("/main.e");
        assert is != null;
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        for (String line; (line = reader.readLine()) != null;) {
            ArrayList<Token> t = lexer.lex(line);
            tokens.addAll(t);
        }

        long lexTime = System.currentTimeMillis() - cms;

        Parser parser = new Parser(tokens);
        ast.addAll(parser.parse().getExps());
        ast.dump();

        long parseTime = System.currentTimeMillis() - cms - lexTime;

        Execution execution = new Execution(ast);
        execution.execute();
        execution.dump();

        long executeTime = System.currentTimeMillis() - cms - lexTime - parseTime;

        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD + "Finished lexing in " + ConsoleColors.GREEN + lexTime + "ms");
        System.out.println(ConsoleColors.WHITE_BOLD + "Finished parsing in " + ConsoleColors.RED + parseTime + "ms");
        System.out.println(ConsoleColors.WHITE_BOLD + "Finished executing in " + ConsoleColors.BLUE + executeTime + "ms");
    }

}
