package net.icxd.compiler.lexer;

import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {

    public ArrayList<Token> lex(String input) {
        ArrayList<Token> tokens = new ArrayList<>();
        HashMap<String, Token.Type> keywords = new HashMap<>();
        int line = 1;
        int pos = 0;
        int start = 0;
        int end = 0;

        while (pos < input.length()) {
            char c = input.charAt(pos);

            switch (c) {
                case ' ':
                case '\t':
                case '\r':
                    pos++;
                    start++;
                    break;
                case '\n':
                    pos++;
                    start = 0;
                    end = 0;
                    line++;
                    break;
                case '=':
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                        tokens.add(new Token(Token.Type.EQ, "==", line, start, end));
                        pos += 2;
                        start += 2;
                    } else {
                        tokens.add(new Token(Token.Type.ASSIGN, "=", line, start, end));
                        pos++;
                        start++;
                    }
                    break;
                case '!':
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                        tokens.add(new Token(Token.Type.NE, "!=", line, start, end));
                        pos += 2;
                        start += 2;
                    } else {
                        tokens.add(new Token(Token.Type.NOT, "!", line, start, end));
                        pos++;
                        start++;
                    }
                    break;
                case '<':
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                        tokens.add(new Token(Token.Type.LE, "<=", line, start, end));
                        pos += 2;
                        start += 2;
                    } else {
                        tokens.add(new Token(Token.Type.LT, "<", line, start, end));
                        pos++;
                        start++;
                    }
                    break;
                case '>':
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
                        tokens.add(new Token(Token.Type.GE, ">=", line, start, end));
                        pos += 2;
                        start += 2;
                    } else {
                        tokens.add(new Token(Token.Type.GT, ">", line, start, end));
                        pos++;
                        start++;
                    }
                    break;
                case '+':
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '+') {
                        tokens.add(new Token(Token.Type.INCREMENT, "++", line, start, end));
                        pos += 2;
                        start += 2;
                    } else {
                        tokens.add(new Token(Token.Type.ADD, "+", line, start, end));
                        pos++;
                        start++;
                    }
                    break;
                case '-':
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '-') {
                        tokens.add(new Token(Token.Type.DECREMENT, "--", line, start, end));
                        pos += 2;
                        start += 2;
                    } else {
                        tokens.add(new Token(Token.Type.SUB, "-", line, start, end));
                        pos++;
                        start++;
                    }
                    break;
                case '*':
                    tokens.add(new Token(Token.Type.MUL, "*", line, start, end));
                    pos++;
                    start++;
                    break;
                case '/':
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '/') {
                        while (pos < input.length() && input.charAt(pos) != '\n') {
                            pos++;
                        }

                        start = 0;
                        end = 0;
                        line++;
                    } else {
                        tokens.add(new Token(Token.Type.DIV, "/", line, start, end));
                        pos++;
                        start++;
                    }
                    break;
                case '%':
                    tokens.add(new Token(Token.Type.MOD, "%", line, start, end));
                    pos++;
                    start++;
                    break;
                case '(':
                    tokens.add(new Token(Token.Type.LPAREN, "(", line, start, end));
                    pos++;
                    start++;
                    break;
                case ')':
                    tokens.add(new Token(Token.Type.RPAREN, ")", line, start, end));
                    pos++;
                    start++;
                    break;
                case '{':
                    tokens.add(new Token(Token.Type.LBRACE, "{", line, start, end));
                    pos++;
                    start++;
                    break;
                case '}':
                    tokens.add(new Token(Token.Type.RBRACE, "}", line, start, end));
                    pos++;
                    start++;
                    break;
                case '&':
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '&') {
                        tokens.add(new Token(Token.Type.AND, "&&", line, start, end));
                        pos += 2;
                        start += 2;
                    } else {
                        throw new RuntimeException("Invalid character: " + c);
                    }
                    break;
                case '|':
                    if (pos + 1 < input.length() && input.charAt(pos + 1) == '|') {
                        tokens.add(new Token(Token.Type.OR, "||", line, start, end));
                        pos += 2;
                        start += 2;
                    } else {
                        throw new RuntimeException("Invalid character: " + c);
                    }
                    break;
                case ';':
                    tokens.add(new Token(Token.Type.SEMICOLON, ";", line, start, end));
                    pos++;
                    start++;
                    break;
                case ':':
                    tokens.add(new Token(Token.Type.COLON, ":", line, start, end));
                    pos++;
                    start++;
                    break;
                case ',':
                    tokens.add(new Token(Token.Type.COMMA, ",", line, start, end));
                    pos++;
                    start++;
                    break;
                case '.':
                    tokens.add(new Token(Token.Type.DOT, ".", line, start, end));
                    pos++;
                    start++;
                    break;
                case '"':
                    pos++;
                    start++;
                    end = start;
                    while (pos < input.length() && input.charAt(pos) != '"') {
                        pos++;
                        end++;
                    }
                    if (pos == input.length()) {
                        throw new RuntimeException("Unterminated string literal");
                    }
                    tokens.add(new Token(Token.Type.STRING_LITERAL, input.substring(start, end), line, start, end));
                    pos++;
                    start = pos;
                    break;
                case '\'':
                    pos++;
                    start++;
                    end = start;
                    while (pos < input.length() && input.charAt(pos) != '\'') {
                        pos++;
                        end++;
                    }
                    if (pos == input.length()) {
                        throw new RuntimeException("Unterminated character literal");
                    }
                    String s = input.substring(start, end);
                    if (s.length() != 1) {
                        throw new RuntimeException("Invalid character literal");
                    }
                    tokens.add(new Token(Token.Type.CHAR_LITERAL, s, line, start, end));
                    pos++;
                    start = pos;
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    end = start;
                    while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
                        pos++;
                        end++;
                    }

                    String number = input.substring(start, end);
                    tokens.add(new Token(Token.Type.INTEGER_LITERAL, number, line, start, end));
                    /*if (number.contains(".")) {
                        tokens.add(new Token(Token.Type.FLOAT_LITERAL, number, line, start, end));
                    } else {
                    }*/
                    start = pos;
                    break;
                default:
                    if (Character.isLetter(c)) {
                        end = start;
                        while (pos < input.length() && Character.isLetterOrDigit(input.charAt(pos))) {
                            pos++;
                            end++;
                        }
                        String text = input.substring(start, end);
                        for (Token.Type type : Token.Type.values()) {
                            if (type.name().equals(text.toUpperCase())) {
                                keywords.put(text, type);
                            }
                        }
                        Token.Type type = keywords.get(text);
                        if (type == Token.Type.TRUE || type == Token.Type.FALSE) {
                            type = Token.Type.BOOLEAN_LITERAL;
                        } else if (type == null) {
                            type = Token.Type.IDENTIFIER;
                        }
                        tokens.add(new Token(type, text, line, start, end));
                        start = pos;
                    } else {
                        throw new RuntimeException("Invalid character: " + c);
                    }
            }
        }

        return tokens;
    }

}
