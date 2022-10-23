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
        else if (exp instanceof Exp.PrintExp) {
            Exp.PrintExp pe = (Exp.PrintExp) exp;
            ArrayList<Exp> values = pe.value;
            StringBuilder sb = new StringBuilder();
            for (Exp e : values) {
                if (e instanceof Exp.IntegerExp) {
                    int value = ((Exp.IntegerExp) e).value;
                    sb.append(value);
                } else if (e instanceof Exp.StringExp) {
                    String value = ((Exp.StringExp) e).value;
                    sb.append(value);
                } else if (e instanceof Exp.CharExp) {
                    char value = ((Exp.CharExp) e).value;
                    sb.append(value);
                } else if (e instanceof Exp.FloatExp) {
                    float value = ((Exp.FloatExp) e).value;
                    sb.append(value);
                } else if (e instanceof Exp.BooleanExp) {
                    boolean value = ((Exp.BooleanExp) e).value;
                    sb.append(value);
                } else if (e instanceof Exp.VariableExp) {
                    String value = ((Exp.VariableExp) e).value;
                    if (!variables.containsKey(value))
                        throw new RuntimeException("Variable '" + value + "' does not exist");
                    sb.append(variables.get(value).getValue());
                } else if (e instanceof Exp.ObjectExp) {
                    Object value = ((Exp.ObjectExp) e).value;
                    sb.append(value);
                } else {
                    throw new RuntimeException("Invalid variable declaration: " + e);
                }
            }
            System.out.println(sb);
        }
        else if (exp instanceof Exp.IncrementExp) {
            Exp.IncrementExp ie = (Exp.IncrementExp) exp;
            String id = ie.name;
            if (!variables.containsKey(id))
                throw new RuntimeException("Variable '" + id + "' does not exist");
            Variable variable = variables.get(id);
            if (variable.getValue() instanceof Integer) {
                int value = (int) variable.getValue();
                value++;
                variables.put(id, new Variable(id, value));
            } else {
                throw new RuntimeException("Variable '" + id + "' is not an integer");
            }
        }
        else if (exp instanceof Exp.DecrementExp) {
            Exp.DecrementExp de = (Exp.DecrementExp) exp;
            String id = de.name;
            if (!variables.containsKey(id))
                throw new RuntimeException("Variable '" + id + "' does not exist");
            Variable variable = variables.get(id);
            if (variable.getValue() instanceof Integer) {
                int value = (int) variable.getValue();
                value--;
                variables.put(id, new Variable(id, value));
            } else {
                throw new RuntimeException("Variable '" + id + "' is not an integer");
            }
        }
        else if (exp instanceof Exp.ForExp) {
            Exp.ForExp fe = (Exp.ForExp) exp;
            Exp.AssignExp init = (Exp.AssignExp) fe.init;
            String id = ((Exp.VariableExp) init.left).value;
            int v = ((Exp.IntegerExp) init.right).value;
            variables.put(id, new Variable(id, v));
            Variable variable = variables.get(id);
            if (variable.getValue() instanceof Integer) {
                Exp.BinOpExp condition = (Exp.BinOpExp) fe.condition;
                int end = ((Exp.IntegerExp) condition.right).value;
                Exp.VariableExp ve = (Exp.VariableExp) condition.left;

                if (!variables.containsKey(ve.value))
                    throw new RuntimeException("Variable '" + ve.value + "' does not exist");

                for (int i = (int) variable.getValue(); i < end; i++) {
                    ArrayList<Exp> body = (ArrayList<Exp>) fe.body;
                    for (Exp e : body)
                        execute(e);
                    Exp.IncrementExp ie = (Exp.IncrementExp) fe.increment;
                    String id2 = ie.name;
                    if (!variables.containsKey(id2))
                        throw new RuntimeException("Variable '" + id2 + "' does not exist");
                    Variable variable2 = variables.get(id2);
                    if (variable2.getValue() instanceof Integer) {
                        int value = (int) variable2.getValue();
                        value++;
                        variables.put(id2, new Variable(id2, value));
                    } else {
                        throw new RuntimeException("Variable '" + id2 + "' is not an integer");
                    }
                }
                variables.remove(id);
            } else {
                throw new RuntimeException("Variable '" + id + "' is not an integer");
            }
        }
        else if (exp instanceof Exp.IfExp) {
            Exp.IfExp ie = (Exp.IfExp) exp;
            Exp condition = ie.condition;
            if (condition instanceof Exp.BinOpExp) {
                Exp.BinOpExp boe = (Exp.BinOpExp) condition;
                if (boe.left instanceof Exp.VariableExp) {
                    if (boe.right instanceof Exp.BooleanExp) {
                        String id = ((Exp.VariableExp) boe.left).value;
                        if (!variables.containsKey(id))
                            throw new RuntimeException("Variable '" + id + "' does not exist");
                        Variable variable = variables.get(id);
                        if (variable.getValue() instanceof Boolean) {
                            boolean value = (boolean) variable.getValue();
                            boolean value2 = ((Exp.BooleanExp) boe.right).value;
                            if (boe.op == Exp.BinOpExp.Op.EQ) {
                                if (value == value2) {
                                    ArrayList<Exp> body = (ArrayList<Exp>) ie.body;
                                    for (Exp e : body)
                                        execute(e);
                                } else {
                                    Exp.ElseExp body = ie.elseBody;
                                    for (Exp e : body.body)
                                        execute(e);
                                }
                            } else if (boe.op == Exp.BinOpExp.Op.NE) {
                                if (value != value2) {
                                    ArrayList<Exp> body = (ArrayList<Exp>) ie.body;
                                    for (Exp e : body)
                                        execute(e);
                                } else {
                                    Exp.ElseExp body = ie.elseBody;
                                    for (Exp e : body.body)
                                        execute(e);
                                }
                            } else {
                                throw new RuntimeException("Invalid operator: " + boe.op);
                            }
                        } else {
                            throw new RuntimeException("Variable '" + id + "' is not a boolean");
                        }
                    } else {
                        throw new RuntimeException("Invalid condition: " + boe.right);
                    }
                }
            }
        }
    }
    private void registerDefaultMethods() {
        
    }
    public void dump() {
        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD + "Variables:" + ConsoleColors.RESET);
        for (Variable v : variables.values())
            System.out.println("  " + ConsoleColors.WHITE_BOLD + v.getId() + ConsoleColors.RESET + " = " + getColor(v.getValue()) + v.getValue() + ConsoleColors.RESET);
        System.out.println();
        System.out.println(ConsoleColors.WHITE_BOLD + "Methods:" + ConsoleColors.RESET);
        for (Method m : methods.values())
            System.out.println("  " + ConsoleColors.WHITE_BOLD + m.getId() + ConsoleColors.RESET + " = " + getColor(m) + m + ConsoleColors.RESET);
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
