package net.icxd.compiler.parser;

import net.icxd.compiler.lexer.Token;
import net.icxd.compiler.ast.AST;
import net.icxd.compiler.ast.exp.Exp;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Parser {
    private final ArrayList<Token> tokens;
    private final AST ast;
    private int pos;
    private Token currentToken;
    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
        this.currentToken = tokens.get(pos);
        this.ast = new AST();
    }
    private void error(String msg) {
        throw new Error(msg);
    }
    private boolean checkToken(Token.Type type) {
        return this.currentToken.getType() == type;
    }
    private Token match(Token.Type type) {
        if (!checkToken(type)) {
            error("excepted token type: " + type + ", but got: " + currentToken.getType());
        }

        Token tok = this.currentToken;
        try {
            this.pos++;
            this.currentToken = this.tokens.get(pos);
        } catch (IndexOutOfBoundsException e) {
            this.currentToken = this.tokens.get(pos - 1);
        }

        return tok;
    }
    public AST parseStatements() {
        AST ast1 = new AST();
        if (checkToken(Token.Type.STRING)) {
            match(Token.Type.STRING);
            String name = match(Token.Type.IDENTIFIER).getValue();
            match(Token.Type.ASSIGN);
            String value = match(Token.Type.STRING_LITERAL).getValue();
            match(Token.Type.SEMICOLON);

            Exp exp = new Exp.AssignExp(new Exp.VariableExp(name), new Exp.StringExp(value));
            ast1.add(exp);

            return ast1;
        }
        else if (checkToken(Token.Type.INT)) {
            match(Token.Type.INT);
            String name = match(Token.Type.IDENTIFIER).getValue();

            if (checkToken(Token.Type.ASSIGN)) {
                match(Token.Type.ASSIGN);
                ArrayList<Exp> exps = new ArrayList<>();
                while (this.currentToken.getType() != Token.Type.SEMICOLON || pos == tokens.size() - 1) {
                    if (checkToken(Token.Type.INTEGER_LITERAL)) {
                        int value = Integer.parseInt(match(Token.Type.INTEGER_LITERAL).getValue());
                        exps.add(new Exp.IntegerExp(value));
                    } else if (checkToken(Token.Type.IDENTIFIER)) {
                        String identifier = match(Token.Type.IDENTIFIER).getValue();
                        exps.add(new Exp.VariableExp(identifier));
                    } else if (checkToken(Token.Type.ADD)) {
                        match(Token.Type.ADD);
                        exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.ADD));
                    } else if (checkToken(Token.Type.SUB)) {
                        match(Token.Type.SUB);
                        exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.SUB));
                    } else if (checkToken(Token.Type.MUL)) {
                        match(Token.Type.MUL);
                        exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.MUL));
                    } else if (checkToken(Token.Type.DIV)) {
                        match(Token.Type.DIV);
                        exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.DIV));
                    } else if (checkToken(Token.Type.MOD)) {
                        match(Token.Type.MOD);
                        exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.MOD));
                    } else if (checkToken(Token.Type.SEMICOLON)) {
                        match(Token.Type.SEMICOLON);
                        break;
                    } else if (checkToken(Token.Type.EOF)) {
                        break;
                    } else {
                        error("unexpected token: " + this.currentToken);
                        break;
                    }
                }
                Exp r = new Exp.AssignExp(new Exp.VariableExp(name), parseExpression(exps));
                ast1.add(r);
                return ast1;
            } else if (checkToken(Token.Type.LPAREN)) {
                match(Token.Type.LPAREN);
                ArrayList<Exp> params = new ArrayList<>();
                while (this.currentToken.getType() != Token.Type.RPAREN) {
                    if (checkToken(Token.Type.IDENTIFIER)) {
                        String identifier = match(Token.Type.IDENTIFIER).getValue();
                        params.add(new Exp.VariableExp(identifier));
                    } else if (checkToken(Token.Type.COMMA)) {
                        match(Token.Type.COMMA);
                    } else {
                        error("unexpected token: " + this.currentToken);
                        break;
                    }
                }
                match(Token.Type.RPAREN);
                match(Token.Type.LBRACE);
                ArrayList<Exp> exps = new ArrayList<>();
                while (this.currentToken.getType() != Token.Type.RBRACE) {
                    AST body = parseStatements();
                    exps.addAll(body.getExps());
                }
                match(Token.Type.RBRACE);
                Exp exp = new Exp.MethodDefExp(name, params, exps);
                ast1.add(exp);
                return ast1;
            }
            else {
                error("unexpected token: " + this.currentToken);
                return null;
            }
        }
        else if (checkToken(Token.Type.FLOAT)) {
            match(Token.Type.FLOAT);
            String name = match(Token.Type.IDENTIFIER).getValue();
            match(Token.Type.ASSIGN);
            ArrayList<Exp> exps = new ArrayList<>();

            while (this.currentToken.getType() != Token.Type.SEMICOLON || pos == tokens.size() - 1) {
                if (checkToken(Token.Type.FLOAT_LITERAL)) {
                    float value = Float.parseFloat(match(Token.Type.FLOAT_LITERAL).getValue());
                    exps.add(new Exp.FloatExp(value));
                } else if (checkToken(Token.Type.IDENTIFIER)) {
                    String identifier = match(Token.Type.IDENTIFIER).getValue();
                    exps.add(new Exp.VariableExp(identifier));
                } else if (checkToken(Token.Type.ADD)) {
                    match(Token.Type.ADD);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.ADD));
                } else if (checkToken(Token.Type.SUB)) {
                    match(Token.Type.SUB);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.SUB));
                } else if (checkToken(Token.Type.MUL)) {
                    match(Token.Type.MUL);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.MUL));
                } else if (checkToken(Token.Type.DIV)) {
                    match(Token.Type.DIV);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.DIV));
                } else if (checkToken(Token.Type.SEMICOLON)) {
                    match(Token.Type.SEMICOLON);
                    break;
                } else if (checkToken(Token.Type.EOF)) {
                    break;
                } else {
                    error("unexpected token: " + this.currentToken);
                    break;
                }
            }

            Exp r = new Exp.AssignExp(new Exp.VariableExp(name), parseExpression(exps));
            ast1.add(r);

            return ast1;
        }
        else if (checkToken(Token.Type.BOOLEAN)) {
            match(Token.Type.BOOLEAN);
            String name = match(Token.Type.IDENTIFIER).getValue();
            match(Token.Type.ASSIGN);
            boolean value = Boolean.parseBoolean(match(Token.Type.BOOLEAN_LITERAL).getValue());
            match(Token.Type.SEMICOLON);

            Exp exp = new Exp.AssignExp(new Exp.VariableExp(name), new Exp.BooleanExp(value));
            ast1.add(exp);

            return ast1;
        }
        else if (checkToken(Token.Type.OBJECT)) {
            match(Token.Type.OBJECT);
            String name = match(Token.Type.IDENTIFIER).getValue();
            match(Token.Type.ASSIGN);

            if (checkToken(Token.Type.NEW)) {
                match(Token.Type.NEW);
                String identifier = match(Token.Type.IDENTIFIER).getValue();
                match(Token.Type.LPAREN);
                ArrayList<Exp> exps = new ArrayList<>();

                while (this.currentToken.getType() != Token.Type.RPAREN || pos == tokens.size() - 1) {
                    if (checkToken(Token.Type.INTEGER_LITERAL)) {
                        int value = Integer.parseInt(match(Token.Type.INTEGER_LITERAL).getValue());
                        exps.add(new Exp.IntegerExp(value));
                    } else if (checkToken(Token.Type.IDENTIFIER)) {
                        String id = match(Token.Type.IDENTIFIER).getValue();
                        exps.add(new Exp.VariableExp(id));
                    } else if (checkToken(Token.Type.STRING_LITERAL)) {
                        String value = match(Token.Type.STRING_LITERAL).getValue();
                        exps.add(new Exp.StringExp(value));
                    } else if (checkToken(Token.Type.BOOLEAN_LITERAL)) {
                        boolean value = Boolean.parseBoolean(match(Token.Type.BOOLEAN_LITERAL).getValue());
                        exps.add(new Exp.BooleanExp(value));
                    } else if (checkToken(Token.Type.ADD)) {
                        match(Token.Type.ADD);
                        exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.ADD));
                    } else if (checkToken(Token.Type.SUB)) {
                        match(Token.Type.SUB);
                        exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.SUB));
                    } else if (checkToken(Token.Type.MUL)) {
                        match(Token.Type.MUL);
                        exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.MUL));
                    } else if (checkToken(Token.Type.DIV)) {
                        match(Token.Type.DIV);
                        exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.DIV));
                    } else if (checkToken(Token.Type.RPAREN)) {
                        match(Token.Type.RPAREN);
                        break;
                    } else if (checkToken(Token.Type.EOF)) {
                        break;
                    } else {
                        error("unexpected token: " + this.currentToken);
                        break;
                    }
                }

                ArrayList<Exp> expr = new ArrayList<>();
                for (Exp value : exps) {
                    if (value instanceof Exp.IntegerExp) {
                        expr.add(value);
                    } else if (value instanceof Exp.FloatExp) {
                        expr.add(value);
                    } else if (value instanceof Exp.BinOpExp) {
                        expr.add(value);
                    } else {
                        expr.clear();
                        break;
                    }
                }

                match(Token.Type.RPAREN);
                match(Token.Type.SEMICOLON);

                if (expr.size() > 0) {
                    ArrayList<Exp> opkegsp = new ArrayList<>();
                    opkegsp.add(parseExpression(expr));
                    Exp r = new Exp.AssignExp(new Exp.VariableExp(name), new Exp.InstanceExp(identifier, opkegsp));
                    ast1.add(r);
                }
                else {
                    Exp r = new Exp.AssignExp(new Exp.VariableExp(name), new Exp.InstanceExp(identifier, exps));
                    ast1.add(r);
                }

                return ast1;
            }
            else {
                Exp r = null;
                if (checkToken(Token.Type.IDENTIFIER)) {
                    String identifier = match(Token.Type.IDENTIFIER).getValue();
                    r = new Exp.AssignExp(new Exp.VariableExp(name), new Exp.ObjectExp(identifier));
                }
                else if (checkToken(Token.Type.STRING_LITERAL)) {
                    String value = match(Token.Type.STRING_LITERAL).getValue();
                    r = new Exp.AssignExp(new Exp.VariableExp(name), new Exp.ObjectExp(value));
                }
                else if (checkToken(Token.Type.INTEGER_LITERAL)) {
                    int value = Integer.parseInt(match(Token.Type.INTEGER_LITERAL).getValue());
                    r = new Exp.AssignExp(new Exp.VariableExp(name), new Exp.ObjectExp(value));
                }
                else if (checkToken(Token.Type.FLOAT_LITERAL)) {
                    float value = Float.parseFloat(match(Token.Type.FLOAT_LITERAL).getValue());
                    r = new Exp.AssignExp(new Exp.VariableExp(name), new Exp.ObjectExp(value));
                }
                else if (checkToken(Token.Type.BOOLEAN_LITERAL)) {
                    boolean value = Boolean.parseBoolean(match(Token.Type.BOOLEAN_LITERAL).getValue());
                    r = new Exp.AssignExp(new Exp.VariableExp(name), new Exp.ObjectExp(value));
                }
                else if (checkToken(Token.Type.NULL)) {
                    match(Token.Type.NULL);
                    r = new Exp.AssignExp(new Exp.VariableExp(name), new Exp.ObjectExp(null));
                }
                match(Token.Type.SEMICOLON);
                ast1.add(r);
                return ast1;
            }
        }
        else if (checkToken(Token.Type.INSTANCE)) {
            match(Token.Type.INSTANCE);
            String name = match(Token.Type.IDENTIFIER).getValue();
            match(Token.Type.ASSIGN);

            match(Token.Type.NEW);
            String identifier = match(Token.Type.IDENTIFIER).getValue();
            match(Token.Type.LPAREN);
            ArrayList<Exp> exps = new ArrayList<>();

            while (this.currentToken.getType() != Token.Type.RPAREN || pos == tokens.size() - 1) {
                if (checkToken(Token.Type.INTEGER_LITERAL)) {
                    int value = Integer.parseInt(match(Token.Type.INTEGER_LITERAL).getValue());
                    exps.add(new Exp.IntegerExp(value));
                } else if (checkToken(Token.Type.IDENTIFIER)) {
                    String id = match(Token.Type.IDENTIFIER).getValue();
                    exps.add(new Exp.VariableExp(id));
                } else if (checkToken(Token.Type.STRING_LITERAL)) {
                    String value = match(Token.Type.STRING_LITERAL).getValue();
                    exps.add(new Exp.StringExp(value));
                } else if (checkToken(Token.Type.BOOLEAN_LITERAL)) {
                    boolean value = Boolean.parseBoolean(match(Token.Type.BOOLEAN_LITERAL).getValue());
                    exps.add(new Exp.BooleanExp(value));
                } else if (checkToken(Token.Type.ADD)) {
                    match(Token.Type.ADD);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.ADD));
                } else if (checkToken(Token.Type.SUB)) {
                    match(Token.Type.SUB);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.SUB));
                } else if (checkToken(Token.Type.MUL)) {
                    match(Token.Type.MUL);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.MUL));
                } else if (checkToken(Token.Type.DIV)) {
                    match(Token.Type.DIV);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.DIV));
                } else if (checkToken(Token.Type.RPAREN)) {
                    match(Token.Type.RPAREN);
                    break;
                } else if (checkToken(Token.Type.EOF)) {
                    break;
                } else {
                    error("unexpected token: " + this.currentToken);
                    break;
                }
            }

            ArrayList<Exp> expr = new ArrayList<>();
            for (Exp value : exps) {
                if (value instanceof Exp.IntegerExp) {
                    expr.add(value);
                } else if (value instanceof Exp.FloatExp) {
                    expr.add(value);
                } else if (value instanceof Exp.BinOpExp) {
                    expr.add(value);
                } else {
                    expr.clear();
                    break;
                }
            }

            match(Token.Type.RPAREN);
            match(Token.Type.SEMICOLON);

            if (expr.size() > 0) {
                ArrayList<Exp> opkegsp = new ArrayList<>();
                opkegsp.add(parseExpression(expr));
                Exp r = new Exp.AssignExp(new Exp.VariableExp(name), new Exp.InstanceExp(identifier, opkegsp));
                ast1.add(r);
            } else {
                Exp r = new Exp.AssignExp(new Exp.VariableExp(name), new Exp.InstanceExp(identifier, exps));
                ast1.add(r);
            }

            return ast1;
        }
        else if (checkToken(Token.Type.IDENTIFIER)) {
            String name = match(Token.Type.IDENTIFIER).getValue();

            if (checkToken(Token.Type.DOT)) {
                while (this.currentToken.getType() != Token.Type.LPAREN) {
                    match(Token.Type.DOT);
                    String identifier = match(Token.Type.IDENTIFIER).getValue();
                    name += "." + identifier;
                }
            }

            match(Token.Type.LPAREN);
            ArrayList<Exp> exps = new ArrayList<>();

            while (this.currentToken.getType() != Token.Type.RPAREN || pos == tokens.size() - 1) {
                if (checkToken(Token.Type.INTEGER_LITERAL)) {
                    int value = Integer.parseInt(match(Token.Type.INTEGER_LITERAL).getValue());
                    exps.add(new Exp.IntegerExp(value));
                } else if (checkToken(Token.Type.IDENTIFIER)) {
                    String identifier = match(Token.Type.IDENTIFIER).getValue();
                    exps.add(new Exp.VariableExp(identifier));
                } else if (checkToken(Token.Type.STRING_LITERAL)) {
                    String value = match(Token.Type.STRING_LITERAL).getValue();
                    exps.add(new Exp.StringExp(value));
                } else if (checkToken(Token.Type.BOOLEAN_LITERAL)) {
                    boolean value = Boolean.parseBoolean(match(Token.Type.BOOLEAN_LITERAL).getValue());
                    exps.add(new Exp.BooleanExp(value));
                } else if (checkToken(Token.Type.ADD)) {
                    match(Token.Type.ADD);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.ADD));
                } else if (checkToken(Token.Type.SUB)) {
                    match(Token.Type.SUB);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.SUB));
                } else if (checkToken(Token.Type.MUL)) {
                    match(Token.Type.MUL);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.MUL));
                } else if (checkToken(Token.Type.DIV)) {
                    match(Token.Type.DIV);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.DIV));
                } else if (checkToken(Token.Type.RPAREN)) {
                    match(Token.Type.RPAREN);
                    break;
                } else if (checkToken(Token.Type.EOF)) {
                    break;
                } else {
                    error("unexpected token: " + this.currentToken);
                    break;
                }
            }

            match(Token.Type.RPAREN);

            if (checkToken(Token.Type.COLON)) {
                match(Token.Type.COLON);
                String identifier = match(Token.Type.VOID).getValue();
                match(Token.Type.LBRACE);
                ArrayList<Exp> block = new ArrayList<>();
                while (this.currentToken.getType() != Token.Type.RBRACE) {
                    AST body = parseStatements();
                    block.addAll(body.getExps());
                }
                match(Token.Type.RBRACE);
                Exp r = new Exp.MethodDefExp(identifier, exps, block);
                ast1.add(r);
                return ast1;
            }

            match(Token.Type.SEMICOLON);

            ArrayList<Exp> expr = new ArrayList<>();
            for (Exp value : exps) {
                if (value instanceof Exp.IntegerExp) {
                    expr.add(value);
                } else if (value instanceof Exp.FloatExp) {
                    expr.add(value);
                } else if (value instanceof Exp.BinOpExp) {
                    expr.add(value);
                } else {
                    expr.clear();
                    break;
                }
            }

            if (expr.size() > 0) {
                ArrayList<Exp> opkegsp = new ArrayList<>();
                opkegsp.add(parseExpression(expr));
                if (name.contains(".")) {
                    String[] split = name.split("\\.");
                    Exp r = new Exp.MethodCallFromExp(split[0], split[1], opkegsp);
                    ast1.add(r);
                } else {
                    Exp r = new Exp.MethodCallExp(name, opkegsp);
                    ast1.add(r);
                }
            }
            else {
                if (name.contains(".")) {
                    String[] split = name.split("\\.");
                    Exp r = new Exp.MethodCallFromExp(split[0], split[1], exps);
                    ast1.add(r);
                } else {
                    Exp r = new Exp.MethodCallExp(name, exps);
                    ast1.add(r);
                }
            }

            return ast1;
        }
        else if (checkToken(Token.Type.PRINT)) {
            match(Token.Type.PRINT);
            match(Token.Type.LPAREN);
            ArrayList<Exp> exps = new ArrayList<>();

            while (this.currentToken.getType() != Token.Type.RPAREN || pos == tokens.size() - 1) {
                if (checkToken(Token.Type.INTEGER_LITERAL)) {
                    int value = Integer.parseInt(match(Token.Type.INTEGER_LITERAL).getValue());
                    exps.add(new Exp.IntegerExp(value));
                } else if (checkToken(Token.Type.IDENTIFIER)) {
                    String identifier = match(Token.Type.IDENTIFIER).getValue();
                    exps.add(new Exp.VariableExp(identifier));
                } else if (checkToken(Token.Type.STRING_LITERAL)) {
                    String value = match(Token.Type.STRING_LITERAL).getValue();
                    exps.add(new Exp.StringExp(value));
                } else if (checkToken(Token.Type.BOOLEAN_LITERAL)) {
                    boolean value = Boolean.parseBoolean(match(Token.Type.BOOLEAN_LITERAL).getValue());
                    exps.add(new Exp.BooleanExp(value));
                } else if (checkToken(Token.Type.ADD)) {
                    match(Token.Type.ADD);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.ADD));
                } else if (checkToken(Token.Type.SUB)) {
                    match(Token.Type.SUB);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.SUB));
                } else if (checkToken(Token.Type.MUL)) {
                    match(Token.Type.MUL);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.MUL));
                } else if (checkToken(Token.Type.DIV)) {
                    match(Token.Type.DIV);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.DIV));
                } else if (checkToken(Token.Type.RPAREN)) {
                    match(Token.Type.RPAREN);
                    break;
                } else if (checkToken(Token.Type.EOF)) {
                    break;
                } else {
                    error("unexpected token: " + this.currentToken);
                    break;
                }
            }

            match(Token.Type.RPAREN);
            match(Token.Type.SEMICOLON);

            ArrayList<Exp> expr = new ArrayList<>();
            for (Exp value : exps) {
                if (value instanceof Exp.IntegerExp) {
                    expr.add(value);
                } else if (value instanceof Exp.FloatExp) {
                    expr.add(value);
                } else if (value instanceof Exp.BinOpExp) {
                    expr.add(value);
                } else {
                    expr.clear();
                    break;
                }
            }

            if (expr.size() > 0) {
                ArrayList<Exp> opkegsp = new ArrayList<>();
                opkegsp.add(parseExpression(expr));
                Exp r = new Exp.MethodCallExp("print", opkegsp);
                ast1.add(r);
            } else {
                Exp r = new Exp.MethodCallExp("print", exps);
                ast1.add(r);
            }

            return ast1;
        }
        else if (checkToken(Token.Type.IF)) {
            match(Token.Type.IF);
            match(Token.Type.LPAREN);

            ArrayList<Exp> exps = new ArrayList<>();
            while (this.currentToken.getType() != Token.Type.RPAREN || pos == tokens.size() - 1) {
                if (checkToken(Token.Type.INTEGER_LITERAL)) {
                    int value = Integer.parseInt(match(Token.Type.INTEGER_LITERAL).getValue());
                    exps.add(new Exp.IntegerExp(value));
                } else if (checkToken(Token.Type.IDENTIFIER)) {
                    String identifier = match(Token.Type.IDENTIFIER).getValue();
                    exps.add(new Exp.VariableExp(identifier));
                } else if (checkToken(Token.Type.STRING_LITERAL)) {
                    String value = match(Token.Type.STRING_LITERAL).getValue();
                    exps.add(new Exp.StringExp(value));
                } else if (checkToken(Token.Type.BOOLEAN_LITERAL)) {
                    boolean value = Boolean.parseBoolean(match(Token.Type.BOOLEAN_LITERAL).getValue());
                    exps.add(new Exp.BooleanExp(value));
                } else if (checkToken(Token.Type.EQ)) {
                    match(Token.Type.EQ);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.EQ));
                } else if (checkToken(Token.Type.NE)) {
                    match(Token.Type.NE);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.NE));
                } else if (checkToken(Token.Type.LT)) {
                    match(Token.Type.LT);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.LT));
                } else if (checkToken(Token.Type.LE)) {
                    match(Token.Type.LE);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.LE));
                } else if (checkToken(Token.Type.GT)) {
                    match(Token.Type.GT);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.GT));
                } else if (checkToken(Token.Type.GE)) {
                    match(Token.Type.GE);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.GE));
                } else if (checkToken(Token.Type.ADD)) {
                    match(Token.Type.ADD);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.ADD));
                } else if (checkToken(Token.Type.SUB)) {
                    match(Token.Type.SUB);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.SUB));
                } else if (checkToken(Token.Type.MUL)) {
                    match(Token.Type.MUL);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.MUL));
                } else if (checkToken(Token.Type.DIV)) {
                    match(Token.Type.DIV);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.DIV));
                } else if (checkToken(Token.Type.RPAREN)) {
                    match(Token.Type.RPAREN);
                    break;
                } else if (checkToken(Token.Type.EOF)) {
                    break;
                } else {
                    error("unexpected token: " + this.currentToken);
                    break;
                }
            }

            Exp condition = parseCondition(exps);

            ArrayList<Exp> block = new ArrayList<>();

            match(Token.Type.RPAREN);
            match(Token.Type.LBRACE);

            while (this.currentToken.getType() != Token.Type.RBRACE || pos == tokens.size() - 1) {
                block.addAll(parseStatements().getExps());
            }

            match(Token.Type.RBRACE);

            ArrayList<Exp> elseBlock = new ArrayList<>();

            if (checkToken(Token.Type.ELSE)) {
                match(Token.Type.ELSE);
                match(Token.Type.LBRACE);
                while (this.currentToken.getType() != Token.Type.RBRACE) {
                    AST body = parseStatements();
                    elseBlock.addAll(body.getExps());
                }
                match(Token.Type.RBRACE);
            }

            Exp.ElseExp e = new Exp.ElseExp(elseBlock);
            Exp r = new Exp.IfExp(condition, block, e);
            ast1.add(r);
            ast1.add(e);

            return ast1;
        }
        else if (checkToken(Token.Type.WHILE)) {
            match(Token.Type.WHILE);
            match(Token.Type.LPAREN);

            ArrayList<Exp> exps = new ArrayList<>();
            while (this.currentToken.getType() != Token.Type.RPAREN || pos == tokens.size() - 1) {
                if (checkToken(Token.Type.INTEGER_LITERAL)) {
                    int value = Integer.parseInt(match(Token.Type.INTEGER_LITERAL).getValue());
                    exps.add(new Exp.IntegerExp(value));
                } else if (checkToken(Token.Type.IDENTIFIER)) {
                    String identifier = match(Token.Type.IDENTIFIER).getValue();
                    exps.add(new Exp.VariableExp(identifier));
                } else if (checkToken(Token.Type.STRING_LITERAL)) {
                    String value = match(Token.Type.STRING_LITERAL).getValue();
                    exps.add(new Exp.StringExp(value));
                } else if (checkToken(Token.Type.BOOLEAN_LITERAL)) {
                    boolean value = Boolean.parseBoolean(match(Token.Type.BOOLEAN_LITERAL).getValue());
                    exps.add(new Exp.BooleanExp(value));
                } else if (checkToken(Token.Type.EQ)) {
                    match(Token.Type.EQ);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.EQ));
                } else if (checkToken(Token.Type.NE)) {
                    match(Token.Type.NE);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.NE));
                } else if (checkToken(Token.Type.LT)) {
                    match(Token.Type.LT);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.LT));
                } else if (checkToken(Token.Type.LE)) {
                    match(Token.Type.LE);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.LE));
                } else if (checkToken(Token.Type.GT)) {
                    match(Token.Type.GT);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.GT));
                } else if (checkToken(Token.Type.GE)) {
                    match(Token.Type.GE);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.GE));
                } else if (checkToken(Token.Type.ADD)) {
                    match(Token.Type.ADD);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.ADD));
                } else if (checkToken(Token.Type.SUB)) {
                    match(Token.Type.SUB);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.SUB));
                } else if (checkToken(Token.Type.MUL)) {
                    match(Token.Type.MUL);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.MUL));
                } else if (checkToken(Token.Type.DIV)) {
                    match(Token.Type.DIV);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.DIV));
                } else if (checkToken(Token.Type.RPAREN)) {
                    match(Token.Type.RPAREN);
                    break;
                } else if (checkToken(Token.Type.EOF)) {
                    break;
                } else {
                    error("unexpected token: " + this.currentToken);
                    break;
                }
            }

            Exp condition = parseCondition(exps);

            ArrayList<Exp> block = new ArrayList<>();

            match(Token.Type.RPAREN);
            match(Token.Type.LBRACE);

            while (this.currentToken.getType() != Token.Type.RBRACE || pos == tokens.size() - 1) {
                block.addAll(parseStatements().getExps());
            }

            match(Token.Type.RBRACE);

            Exp r = new Exp.WhileExp(condition, block);
            ast1.add(r);

            return ast1;
        }
        else if (checkToken(Token.Type.CONSTRUCTOR)) {
            match(Token.Type.CONSTRUCTOR);
            match(Token.Type.LPAREN);
            ArrayList<Exp> params = new ArrayList<>();
            while (this.currentToken.getType() != Token.Type.RPAREN) {
                if (checkToken(Token.Type.IDENTIFIER)) {
                    String identifier = match(Token.Type.IDENTIFIER).getValue();
                    params.add(new Exp.VariableExp(identifier));
                } else if (checkToken(Token.Type.COMMA)) {
                    match(Token.Type.COMMA);
                } else {
                    error("unexpected token: " + this.currentToken);
                    break;
                }
            }
            match(Token.Type.RPAREN);
            match(Token.Type.LBRACE);
            ArrayList<Exp> exps = new ArrayList<>();
            while (this.currentToken.getType() != Token.Type.RBRACE) {
                AST body = parseStatements();
                exps.addAll(body.getExps());
            }

            match(Token.Type.RBRACE);

            Exp exp = new Exp.ConstructorExp(params, exps);
            ast1.add(exp);

            return ast1;
        }
        else if (checkToken(Token.Type.NEW)) {
            match(Token.Type.NEW);
            String identifier = match(Token.Type.IDENTIFIER).getValue();
            match(Token.Type.LPAREN);
            ArrayList<Exp> exps = new ArrayList<>();

            while (this.currentToken.getType() != Token.Type.RPAREN || pos == tokens.size() - 1) {
                if (checkToken(Token.Type.INTEGER_LITERAL)) {
                    int value = Integer.parseInt(match(Token.Type.INTEGER_LITERAL).getValue());
                    exps.add(new Exp.IntegerExp(value));
                } else if (checkToken(Token.Type.IDENTIFIER)) {
                    String id = match(Token.Type.IDENTIFIER).getValue();
                    exps.add(new Exp.VariableExp(id));
                } else if (checkToken(Token.Type.STRING_LITERAL)) {
                    String value = match(Token.Type.STRING_LITERAL).getValue();
                    exps.add(new Exp.StringExp(value));
                } else if (checkToken(Token.Type.BOOLEAN_LITERAL)) {
                    boolean value = Boolean.parseBoolean(match(Token.Type.BOOLEAN_LITERAL).getValue());
                    exps.add(new Exp.BooleanExp(value));
                } else if (checkToken(Token.Type.ADD)) {
                    match(Token.Type.ADD);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.ADD));
                } else if (checkToken(Token.Type.SUB)) {
                    match(Token.Type.SUB);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.SUB));
                } else if (checkToken(Token.Type.MUL)) {
                    match(Token.Type.MUL);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.MUL));
                } else if (checkToken(Token.Type.DIV)) {
                    match(Token.Type.DIV);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.DIV));
                } else if (checkToken(Token.Type.RPAREN)) {
                    match(Token.Type.RPAREN);
                    break;
                } else if (checkToken(Token.Type.EOF)) {
                    break;
                } else {
                    error("unexpected token: " + this.currentToken);
                    break;
                }
            }

            ArrayList<Exp> expr = new ArrayList<>();
            for (Exp value : exps) {
                if (value instanceof Exp.IntegerExp) {
                    expr.add(value);
                } else if (value instanceof Exp.FloatExp) {
                    expr.add(value);
                } else if (value instanceof Exp.BinOpExp) {
                    expr.add(value);
                } else {
                    expr.clear();
                    break;
                }
            }

            match(Token.Type.RPAREN);
            match(Token.Type.SEMICOLON);

            if (expr.size() > 0) {
                ArrayList<Exp> opkegsp = new ArrayList<>();
                opkegsp.add(parseExpression(expr));
                Exp r = new Exp.InstanceExp(identifier, opkegsp);
                ast1.add(r);
            }
            else {
                Exp r = new Exp.InstanceExp(identifier, exps);
                ast1.add(r);
            }

            return ast1;
        }
        else if (checkToken(Token.Type.EOF)) {
            return ast1;
        }
        else if (checkToken(Token.Type.IMPORT)) {
            match(Token.Type.IMPORT);
            String identifier = match(Token.Type.IDENTIFIER).getValue();
            if (checkToken(Token.Type.AS)) {
                match(Token.Type.AS);
                String alias = match(Token.Type.IDENTIFIER).getValue();
                Exp r = new Exp.ImportExp(identifier, alias);
                ast1.add(r);
            }
            else {
                Exp r = new Exp.ImportExp(identifier);
                ast1.add(r);
            }
            match(Token.Type.SEMICOLON);

            return ast1;
        }
        else if (checkToken(Token.Type.FROM)) {
            match(Token.Type.FROM);
            String identifier = match(Token.Type.IDENTIFIER).getValue();
            match(Token.Type.IMPORT);
            ArrayList<Exp> exps = new ArrayList<>();

            while (this.currentToken.getType() != Token.Type.SEMICOLON) {
                if (checkToken(Token.Type.IDENTIFIER)) {
                    String id = match(Token.Type.IDENTIFIER).getValue();
                    exps.add(new Exp.ImportedMethodExp(id));
                } else if (checkToken(Token.Type.COMMA)) {
                    match(Token.Type.COMMA);
                } else {
                    error("unexpected token: " + this.currentToken);
                    break;
                }
            }

            Exp r = new Exp.ImportExp(identifier, exps);
            ast1.add(r);

            return ast1;
        }
        else if (checkToken(Token.Type.RETURN)) {
            match(Token.Type.RETURN);
            ArrayList<Exp> exps = new ArrayList<>();
            while (this.currentToken.getType() != Token.Type.SEMICOLON) {
                if (checkToken(Token.Type.INTEGER_LITERAL)) {
                    int value = Integer.parseInt(match(Token.Type.INTEGER_LITERAL).getValue());
                    exps.add(new Exp.IntegerExp(value));
                } else if (checkToken(Token.Type.IDENTIFIER)) {
                    String id = match(Token.Type.IDENTIFIER).getValue();
                    exps.add(new Exp.VariableExp(id));
                } else if (checkToken(Token.Type.STRING_LITERAL)) {
                    String value = match(Token.Type.STRING_LITERAL).getValue();
                    exps.add(new Exp.StringExp(value));
                } else if (checkToken(Token.Type.BOOLEAN_LITERAL)) {
                    boolean value = Boolean.parseBoolean(match(Token.Type.BOOLEAN_LITERAL).getValue());
                    exps.add(new Exp.BooleanExp(value));
                } else if (checkToken(Token.Type.ADD)) {
                    match(Token.Type.ADD);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.ADD));
                } else if (checkToken(Token.Type.SUB)) {
                    match(Token.Type.SUB);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.SUB));
                } else if (checkToken(Token.Type.MUL)) {
                    match(Token.Type.MUL);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.MUL));
                } else if (checkToken(Token.Type.DIV)) {
                    match(Token.Type.DIV);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.DIV));
                } else if (checkToken(Token.Type.MOD)) {
                    match(Token.Type.MOD);
                    exps.add(new Exp.BinOpExp(Exp.BinOpExp.Op.MOD));
                } else if (checkToken(Token.Type.SEMICOLON)) {
                    match(Token.Type.SEMICOLON);
                    break;
                } else if (checkToken(Token.Type.EOF)) {
                    break;
                } else {
                    error("unexpected token: " + this.currentToken);
                    break;
                }
            }
            ArrayList<Exp> expr = new ArrayList<>();
            for (Exp value : exps) {
                if (value instanceof Exp.IntegerExp) {
                    expr.add(value);
                } else if (value instanceof Exp.FloatExp) {
                    expr.add(value);
                } else if (value instanceof Exp.BinOpExp) {
                    expr.add(value);
                } else {
                    expr.clear();
                    break;
                }
            }
            if (expr.size() > 0) {
                Exp r = new Exp.ReturnExp(parseExpression(expr));
                ast1.add(r);
            }
            else {
                Exp r = new Exp.ReturnExp(exps.get(0));
                ast1.add(r);
            }
            return ast1;
        }
        else {
            if (pos >= tokens.size() - 1) {
                return ast1;
            }

            match(this.currentToken.getType());
            return ast1;
        }
    }
    public AST parse() {
        if (checkToken(Token.Type.VOID)) {
            match(Token.Type.VOID);
            String name = match(Token.Type.IDENTIFIER).getValue();
            match(Token.Type.LPAREN);
            ArrayList<Exp> params = new ArrayList<>();
            while (this.currentToken.getType() != Token.Type.RPAREN) {
                if (checkToken(Token.Type.IDENTIFIER)) {
                    String identifier = match(Token.Type.IDENTIFIER).getValue();
                    params.add(new Exp.VariableExp(identifier));
                } else if (checkToken(Token.Type.COMMA)) {
                    match(Token.Type.COMMA);
                } else {
                    error("unexpected token: " + this.currentToken);
                    break;
                }
            }
            match(Token.Type.RPAREN);
            match(Token.Type.LBRACE);
            ArrayList<Exp> exps = new ArrayList<>();
            while (this.currentToken.getType() != Token.Type.RBRACE) {
                AST body = parseStatements();
                exps.addAll(body.getExps());
            }
            match(Token.Type.RBRACE);
            Exp exp = new Exp.MethodDefExp(name, params, exps);
            this.ast.add(exp);
            return parse();
        }
        else if (checkToken(Token.Type.CLASS)) {
            match(Token.Type.CLASS);
            String className = match(Token.Type.IDENTIFIER).getValue();
            match(Token.Type.LBRACE);
            ArrayList<Exp> exps = new ArrayList<>();

            while (this.currentToken.getType() != Token.Type.RBRACE || pos >= tokens.size() - 1) {
                AST body = parseStatements();
                exps.addAll(body.getExps());

            }

            match(Token.Type.RBRACE);

            Exp exp = new Exp.ClassExp(className, exps);
            this.ast.add(exp);

            return parse();
        }
        else {
            if (pos >= tokens.size() - 1)
                return this.ast;

            AST body = parseStatements();
            this.ast.addAll(body.getExps());

            return parse();
        }
    }
    public Exp parseCondition(ArrayList<Exp> exps) {
        if (exps.size() == 1) {
            return exps.get(0);
        } else if (exps.size() == 3) {
            Exp.BinOpExp.Op op = ((Exp.BinOpExp) exps.get(1)).op;
            Exp left = exps.get(0);
            Exp right = exps.get(2);
            return new Exp.BinOpExp(op, left, right);
        } else {
            Exp.BinOpExp.Op op = ((Exp.BinOpExp) exps.get(1)).op;
            Exp left = exps.get(0);
            Exp right = parseCondition(new ArrayList<>(exps.subList(2, exps.size())));
            return new Exp.BinOpExp(op, left, right);
        }
    }
    public Exp parseExpression(ArrayList<Exp> exps) {
        ArrayList<Exp> newExps = new ArrayList<>(exps);
        for (int i = 0; i < newExps.size(); i++) {
            Exp exp = newExps.get(i);
            if (exp instanceof Exp.BinOpExp) {
                Exp.BinOpExp binOpExp = (Exp.BinOpExp) exp;
                if (binOpExp.op == Exp.BinOpExp.Op.MUL || binOpExp.op == Exp.BinOpExp.Op.DIV || binOpExp.op == Exp.BinOpExp.Op.MOD) {
                    Exp left = newExps.get(i - 1);
                    Exp right = newExps.get(i + 1);
                    newExps.set(i - 1, new Exp.BinaryExp(binOpExp.op, left, right));
                    newExps.remove(i);
                    newExps.remove(i);
                    i--;
                }
            }
        }
        for (int i = 0; i < newExps.size(); i++) {
            Exp exp = newExps.get(i);
            if (exp instanceof Exp.BinOpExp) {
                Exp.BinOpExp binOpExp = (Exp.BinOpExp) exp;
                if (binOpExp.op == Exp.BinOpExp.Op.ADD || binOpExp.op == Exp.BinOpExp.Op.SUB) {
                    Exp left = newExps.get(i - 1);
                    Exp right = newExps.get(i + 1);
                    newExps.set(i - 1, new Exp.BinaryExp(binOpExp.op, left, right));
                    newExps.remove(i);
                    newExps.remove(i);
                    i--;
                }
            }
        }
        return newExps.get(newExps.size() - 1);
    }
}
