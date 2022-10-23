package net.icxd.compiler.execute;

import net.icxd.compiler.ast.AST;
import net.icxd.compiler.ast.exp.Exp;
import net.icxd.compiler.execute.types.Method;
import net.icxd.compiler.execute.types.Variable;
import net.icxd.compiler.utils.ConsoleColors;

import java.util.ArrayList;
import java.util.HashMap;

public class Execution {
    private final HashMap<String, Variable> variables = new HashMap<>();
    private final HashMap<String, Method> methods = new HashMap<>();
    public AST ast;
    private int index = 0;
    public Execution(AST ast) {
        this.ast = ast;
        this.registerDefaultMethods();
    }
    public void execute() {
        ArrayList<Exp> exps = ast.getExps();
        while (index < exps.size()) {
            Exp exp = exps.get(index);

            if (exp instanceof Exp.MethodDefExp) {
                Exp.MethodDefExp mde = (Exp.MethodDefExp) exp;
                String id = mde.name;
                String returnType = "void";
                ArrayList<String> parameters = new ArrayList<>();
                ArrayList<Exp> body = (ArrayList<Exp>) mde.body;

                methods.put(id, new Method(id, returnType, parameters, body));
            }
            else {
                execute(exp);
            }

            index++;
        }
    }
    private void execute(Exp exp) {
        if (exp instanceof Exp.AssignExp) {
            Exp left = ((Exp.AssignExp) exp).left;
            Exp right = ((Exp.AssignExp) exp).right;
            if (left instanceof Exp.VariableExp) {
                String id = ((Exp.VariableExp) left).value;
                if (variables.containsKey(id))
                    throw new RuntimeException("Variable '" + id + "' already exists");

                if (right instanceof Exp.IntegerExp) {
                    int value = ((Exp.IntegerExp) right).value;
                    variables.put(id, new Variable(id, value));
                } else if (right instanceof Exp.StringExp) {
                    String value = ((Exp.StringExp) right).value;
                    variables.put(id, new Variable(id, value));
                } else if (right instanceof Exp.CharExp) {
                    char value = ((Exp.CharExp) right).value;
                    variables.put(id, new Variable(id, value));
                } else if (right instanceof Exp.FloatExp) {
                    float value = ((Exp.FloatExp) right).value;
                    variables.put(id, new Variable(id, value));
                } else if (right instanceof Exp.BooleanExp) {
                    boolean value = ((Exp.BooleanExp) right).value;
                    variables.put(id, new Variable(id, value));
                } else if (right instanceof Exp.VariableExp) {
                    String value = ((Exp.VariableExp) right).value;
                    if (!variables.containsKey(value))
                        throw new RuntimeException("Variable '" + value + "' does not exist");
                    variables.put(id, new Variable(id, variables.get(value).getValue()));
                } else if (right instanceof Exp.ObjectExp) {
                    Object value = ((Exp.ObjectExp) right).value;
                    variables.put(id, new Variable(id, value));
                } else {
                    throw new RuntimeException("Invalid variable declaration: " + id);
                }
            }
        }
        else if (exp instanceof Exp.ReassignExp) {
            Exp left = ((Exp.ReassignExp) exp).left;
            Exp right = ((Exp.ReassignExp) exp).right;
            if (left instanceof Exp.VariableExp) {
                String id = ((Exp.VariableExp) left).value;
                if (!variables.containsKey(id))
                    throw new RuntimeException("Variable '" + id + "' does not exist");

                if (right instanceof Exp.IntegerExp) {
                    int value = ((Exp.IntegerExp) right).value;
                    variables.put(id, new Variable(id, value));
                } else if (right instanceof Exp.StringExp) {
                    String value = ((Exp.StringExp) right).value;
                    variables.put(id, new Variable(id, value));
                } else if (right instanceof Exp.CharExp) {
                    char value = ((Exp.CharExp) right).value;
                    variables.put(id, new Variable(id, value));
                } else if (right instanceof Exp.FloatExp) {
                    float value = ((Exp.FloatExp) right).value;
                    variables.put(id, new Variable(id, value));
                } else if (right instanceof Exp.BooleanExp) {
                    boolean value = ((Exp.BooleanExp) right).value;
                    variables.put(id, new Variable(id, value));
                } else if (right instanceof Exp.VariableExp) {
                    String value = ((Exp.VariableExp) right).value;
                    if (!variables.containsKey(value))
                        throw new RuntimeException("Variable '" + value + "' does not exist");
                    variables.put(id, new Variable(id, variables.get(value).getValue()));
                } else if (right instanceof Exp.ObjectExp) {
                    Object value = ((Exp.ObjectExp) right).value;
                    variables.put(id, new Variable(id, value));
                } else {
                    throw new RuntimeException("Invalid variable declaration: " + id);
                }
            }
        }
        else if (exp instanceof Exp.MethodCallExp) {
            Exp.MethodCallExp mce = (Exp.MethodCallExp) exp;
            String id = mce.name;
            if (!methods.containsKey(id))
                throw new RuntimeException("Method '" + id + "' does not exist");
            Method method = methods.get(id);
            ArrayList<Exp> body = method.getBody();
            for (Exp e : body)
                execute(e);
        }
    }
    private void registerDefaultMethods() {
        methods.put("print", new Method("print", "void", new ArrayList<>(), new ArrayList<Exp>() {{
            add(new Exp.MethodCallExp("println", new ArrayList<>()));
        }}));
    }
    public void dump() {
        for (Variable v : variables.values())
            System.out.println(ConsoleColors.WHITE_BOLD + v.getId() + ConsoleColors.RESET + " = " + getColor(v.getValue()) + v.getValue() + ConsoleColors.RESET);
        for (Method m : methods.values())
            System.out.println(ConsoleColors.WHITE_BOLD + m.getId() + ConsoleColors.RESET + " = " + getColor(m) + m + ConsoleColors.RESET);
    }
    private String getColor(Object value) {
        if (value instanceof Integer) {
            return ConsoleColors.YELLOW;
        } else if (value instanceof String) {
            return ConsoleColors.GREEN;
        } else if (value instanceof Character) {
            return ConsoleColors.GREEN;
        } else if (value instanceof Float) {
            return ConsoleColors.YELLOW;
        } else if (value instanceof Boolean) {
            return ConsoleColors.PURPLE;
        } else if (value instanceof Object) {
            return ConsoleColors.BLUE;
        }
        return ConsoleColors.RESET;
    }
}
