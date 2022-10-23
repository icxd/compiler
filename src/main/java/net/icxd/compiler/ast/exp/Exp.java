package net.icxd.compiler.ast.exp;

import net.icxd.compiler.utils.ConsoleColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public abstract class Exp {
    public static void dump(ArrayList<Exp> exps) {
        dump(exps, new StringBuilder(" "), new StringBuilder());
    }
    static void dump(ArrayList<Exp> e, StringBuilder indent, StringBuilder sb) {
        if (e.size() == 0)
            return;

        for (int i = 0; i < e.size(); i++) {
            dump(e.get(i), new StringBuilder(indent), i == e.size() - 1, sb);
        }
    }
    static void dump(Exp e, StringBuilder indent, boolean last, StringBuilder prefix) {
        System.out.println(indent.toString() + prefix.toString() + (last ? "└── " : "├── ") + e.toString());

        if (e instanceof BinaryExp) {
            BinaryExp be = (BinaryExp) e;
            dump(be.left, new StringBuilder(indent).append(last ? "    " : "│   "), false, new StringBuilder());
            dump(be.right, new StringBuilder(indent).append(last ? "    " : "│   "), true, new StringBuilder());
        } else if (e instanceof AssignExp) {
            AssignExp ae = (AssignExp) e;
            dump(ae.left, new StringBuilder(indent).append(last ? "    " : "│   "), false, new StringBuilder());
            dump(ae.right, new StringBuilder(indent).append(last ? "    " : "│   "), true, new StringBuilder());
        } else if (e instanceof ReassignExp) {
            ReassignExp ae = (ReassignExp) e;
            dump(ae.left, new StringBuilder(indent).append(last ? "    " : "│   "), false, new StringBuilder());
            dump(ae.right, new StringBuilder(indent).append(last ? "    " : "│   "), true, new StringBuilder());
        } else if (e instanceof MethodDefExp) {
            MethodDefExp mde = (MethodDefExp) e;
            dump((ArrayList<Exp>) mde.body, new StringBuilder(indent).append(last ? "    " : "│   "), new StringBuilder());
        } else if (e instanceof MethodCallExp) {
            MethodCallExp mce = (MethodCallExp) e;
            dump((ArrayList<Exp>) mce.args, new StringBuilder(indent).append(last ? "    " : "│   "), new StringBuilder());
        } else if (e instanceof IfExp) {
            IfExp ie = (IfExp) e;
            dump((ArrayList<Exp>) ie.body, new StringBuilder(indent).append(last ? "    " : "│   "), new StringBuilder());
        } else if (e instanceof ElseExp) {
            ElseExp ee = (ElseExp) e;
            dump((ArrayList<Exp>) ee.body, new StringBuilder(indent).append(last ? "    " : "│   "), new StringBuilder());
        } else if (e instanceof WhileExp) {
            WhileExp we = (WhileExp) e;
            dump((ArrayList<Exp>) we.body, new StringBuilder(indent).append(last ? "    " : "│   "), new StringBuilder());
        } else if (e instanceof ForExp) {
            ForExp fe = (ForExp) e;
            dump(fe.init, new StringBuilder(indent).append(last ? "    " : "│   "), false, new StringBuilder());
            dump((ArrayList<Exp>) fe.body, new StringBuilder(indent).append(last ? "    " : "│   "), new StringBuilder());
        } else if (e instanceof ClassExp) {
            ClassExp ce = (ClassExp) e;
            dump((ArrayList<Exp>) ce.body, new StringBuilder(indent).append(last ? "    " : "│   "), new StringBuilder());
        } else if (e instanceof ConstructorExp) {
            ConstructorExp ce = (ConstructorExp) e;
            dump((ArrayList<Exp>) ce.body, new StringBuilder(indent).append(last ? "    " : "│   "), new StringBuilder());
        } else if (e instanceof ImportExp) {
            ImportExp ie = (ImportExp) e;
            dump((ArrayList<Exp>) ie.methods, new StringBuilder(indent).append(last ? "    " : "│   "), new StringBuilder());
        } else if (e instanceof PrintExp) {
            PrintExp pe = (PrintExp) e;
            dump(pe.value, new StringBuilder(indent).append(last ? "    " : "│   "), new StringBuilder());
        }
    }
    public static class IntegerExp extends Exp {
        public int value;

        public IntegerExp(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "IntegerExp" + ConsoleColors.RESET + "(" + ConsoleColors.YELLOW + value + ConsoleColors.RESET + ')';
        }
    }
    public static class FloatExp extends Exp {
        public float value;

        public FloatExp(float value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "FloatExp" + ConsoleColors.RESET + "(" + ConsoleColors.YELLOW + value + ConsoleColors.RESET + ')';
        }
    }
    public static class StringExp extends Exp {
        public String value;

        public StringExp(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "StringExp" + ConsoleColors.RESET + "(" + ConsoleColors.GREEN + '"' + value + '"' + ConsoleColors.RESET + ')';
        }
    }
    public static class CharExp extends Exp {
        public char value;

        public CharExp(char value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "CharExp" + ConsoleColors.RESET + "(" + ConsoleColors.GREEN + '\'' + value + '\'' + ConsoleColors.RESET + ')';
        }
    }
    public static class BooleanExp extends Exp {
        public boolean value;

        public BooleanExp(boolean value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "BooleanExp" + ConsoleColors.RESET + "(" + ConsoleColors.PURPLE + value + ConsoleColors.RESET + ')';
        }
    }
    public static class ObjectExp extends Exp {
        public Object value;

        public ObjectExp(Object value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "ObjectExp" + ConsoleColors.RESET + "(" + ConsoleColors.PURPLE + value + ConsoleColors.RESET + ')';
        }
    }
    public static class AssignExp extends Exp {
        public Exp left;
        public Exp right;

        public AssignExp(Exp left, Exp right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "AssignExp" + ConsoleColors.RESET;
        }
    }
    public static class ReassignExp extends Exp {
        public Exp left;
        public Exp right;

        public ReassignExp(Exp left, Exp right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "ReassignExp" + ConsoleColors.RESET;
        }
    }
    public static class VariableExp extends Exp {
        public String value;

        public VariableExp(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "VariableExp" + ConsoleColors.RESET + "(" + ConsoleColors.BLUE + value + ConsoleColors.RESET + ')';
        }
    }
    public static class MethodCallExp extends Exp {
        public String name;
        public List<Exp> args;

        public MethodCallExp(String name, List<Exp> args) {
            this.name = name;
            this.args = args;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "MethodCallExp" + ConsoleColors.RESET + "(" + ConsoleColors.GREEN + '\'' + name + '\'' + ConsoleColors.RESET + ')';
        }
    }
    public static class MethodCallFromExp extends Exp {
        public String className;
        public String name;
        public List<Exp> args;

        public MethodCallFromExp(String className, String name, List<Exp> args) {
            this.className = className;
            this.name = name;
            this.args = args;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "MethodCallFromExp" + ConsoleColors.RESET + "(" + ConsoleColors.GREEN + '\'' + className + '\'' + ConsoleColors.RESET + ", " + ConsoleColors.GREEN + '\'' + name + '\'' + ConsoleColors.RESET + ')';
        }
    }
    public static class MethodDefExp extends Exp {
        public String name;
        public List<Exp> args;
        public List<Exp> body;

        public MethodDefExp(String name, List<Exp> args, List<Exp> body) {
            this.name = name;
            this.args = args;
            this.body = body;
        }

        @Override
        public String toString() {
            StringJoiner sj = new StringJoiner(", ");
            for (Exp e : args)
                sj.add(e.toString());
            return ConsoleColors.WHITE_BOLD + "MethodDefExp" + ConsoleColors.RESET + "(" + ConsoleColors.GREEN + '\'' + name + '\'' + ConsoleColors.RESET + (args.size() > 1 ? ", " + sj : "") + ")";
        }
    }
    public static class BinaryExp extends Exp {
        public BinOpExp.Op operator;
        public Exp left;
        public Exp right;

        public BinaryExp(BinOpExp.Op operator, Exp left, Exp right) {
            this.operator = operator;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "BinaryExp" + ConsoleColors.RESET + "(" + ConsoleColors.GREEN + '\'' + operator.value + '\'' + ConsoleColors.RESET + ')';
        }
    }
    public static class IfExp extends Exp {
        public Exp condition;
        public List<Exp> body;
        public ElseExp elseBody;

        public IfExp(Exp condition, List<Exp> body, ElseExp elseBody) {
            this.condition = condition;
            this.body = body;
            this.elseBody = elseBody;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "IfExp" + ConsoleColors.RESET + "(" + condition + ConsoleColors.RESET + ')';
        }
    }
    public static class ElseExp extends Exp {
        public List<Exp> body;

        public ElseExp(List<Exp> body) {
            this.body = body;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "ElseExp" + ConsoleColors.RESET;
        }
    }
    public static class WhileExp extends Exp {
        public Exp condition;
        public List<Exp> body;

        public WhileExp(Exp condition, List<Exp> body) {
            this.condition = condition;
            this.body = body;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "WhileExp" + ConsoleColors.RESET + "(" + condition + ConsoleColors.RESET + ')';
        }
    }
    public static class ForExp extends Exp {
        public Exp init;
        public Exp condition;
        public Exp increment;
        public List<Exp> body;

        public ForExp(Exp init, Exp condition, Exp increment, List<Exp> body) {
            this.init = init;
            this.condition = condition;
            this.increment = increment;
            this.body = body;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "ForExp" + ConsoleColors.RESET + "(" + condition + ConsoleColors.RESET + ')';
        }
    }
    public static class ClassExp extends Exp {
        public String name;
        public List<Exp> body;

        public ClassExp(String name, List<Exp> body) {
            this.name = name;
            this.body = body;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "ClassExp" + ConsoleColors.RESET + "(" + ConsoleColors.GREEN + '\'' + name + '\'' + ConsoleColors.RESET + ')';
        }
    }
    public static class ConstructorExp extends Exp {
        public List<Exp> args;
        public List<Exp> body;

        public ConstructorExp(List<Exp> args, List<Exp> body) {
            this.args = args;
            this.body = body;
        }

        @Override
        public String toString() {
            StringJoiner sj = new StringJoiner(", ");
            for (Exp e : args)
                sj.add(e.toString());
            return ConsoleColors.WHITE_BOLD + "ConstructorExp" + ConsoleColors.RESET + "(" + sj + ")";
        }
    }
    public static class InstanceExp extends Exp {
        public String name;
        public List<Exp> args;

        public InstanceExp(String name, List<Exp> args) {
            this.name = name;
            this.args = args;
        }

        @Override
        public String toString() {
            StringJoiner sj = new StringJoiner(", ");
            for (Exp e : args)
                sj.add(e.toString());
            return ConsoleColors.WHITE_BOLD + "InstanceExp" + ConsoleColors.RESET + "(" + ConsoleColors.GREEN + '\'' + name + '\'' + ConsoleColors.RESET + (args.size() > 1 ? ", " + sj : "") + ")";
        }
    }
    public static class ImportExp extends Exp {
        public String name;
        public String alias;
        public List<Exp> methods;

        public ImportExp(String name) {
            this.name = name;
            this.alias = null;
            this.methods = new ArrayList<>();
        }

        public ImportExp(String name, String alias) {
            this.name = name;
            this.alias = alias;
            this.methods = new ArrayList<>();
        }

        public ImportExp(String name, List<Exp> methods) {
            this.name = name;
            this.alias = null;
            this.methods = methods;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "ImportExp" + ConsoleColors.RESET + "(" + ConsoleColors.GREEN + '\'' + name + '\'' + ConsoleColors.RESET + (alias == null ? "" : ", " + ConsoleColors.GREEN + '\'' + alias + '\'') + ConsoleColors.RESET + ')';
        }
    }
    public static class ImportedMethodExp extends Exp {
        public String name;

        public ImportedMethodExp(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "ImportedMethodExp" + ConsoleColors.RESET + "(" + ConsoleColors.GREEN + '\'' + name + '\'' + ConsoleColors.RESET + ')';
        }
    }
    public static class ReturnExp extends Exp {
        public Exp value;

        public ReturnExp(Exp value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "ReturnExp" + ConsoleColors.RESET + "(" + value + ConsoleColors.RESET + ')';
        }
    }
    public static class PrintExp extends Exp {
        public ArrayList<Exp> value;

        public PrintExp(ArrayList<Exp> value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "PrintExp" + ConsoleColors.RESET;
        }
    }
    public static class IncrementExp extends Exp {
        public String name;

        public IncrementExp(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "IncrementExp" + ConsoleColors.RESET + "(" + ConsoleColors.GREEN + '\'' + name + '\'' + ConsoleColors.RESET + ')';
        }
    }
    public static class DecrementExp extends Exp {
        public String name;

        public DecrementExp(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "DecrementExp" + ConsoleColors.RESET + "(" + ConsoleColors.GREEN + '\'' + name + '\'' + ConsoleColors.RESET + ')';
        }
    }
    public static class UnaryOpExp extends Exp {
        public Op op;
        public Exp value;

        public UnaryOpExp(Op op, Exp value) {
            this.op = op;
            this.value = value;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "UnaryOpExp" + ConsoleColors.RESET + "(" + ConsoleColors.GREEN + '\'' + op.value + '\'' + ConsoleColors.RESET + ", " + value + ConsoleColors.RESET + ')';
        }

        public enum Op {
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
            INCREMENT("++"),
            DECREMENT("--"),
            LPAREN("("),
            RPAREN(")");;

            public final String value;

            Op(String value) {
                this.value = value;
            }

            public static BinOpExp.Op fromString(String value) {
                for (BinOpExp.Op op : BinOpExp.Op.values()) {
                    if (op.value.equals(value)) {
                        return op;
                    }
                }
                return null;
            }
        }
    }
    public static class BinOpExp extends Exp {
        public Op op;
        public Exp left;
        public Exp right;

        public BinOpExp(Op op) {
            this(op, null, null);
        }

        public BinOpExp(Op op, Exp left, Exp right) {
            this.op = op;
            this.left = left;
            this.right = right;
        }

        public void setLeft(Exp left) {
            this.left = left;
        }

        public void setRight(Exp right) {
            this.right = right;
        }

        @Override
        public String toString() {
            return ConsoleColors.WHITE_BOLD + "BinOpExp" + ConsoleColors.RESET + "(" + ConsoleColors.GREEN + '\'' + op.value + '\'' + ConsoleColors.RESET + ", " + left + ", " + right + ")";
        }

        public enum Op {
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
            LPAREN("("),
            RPAREN(")");;

            public final String value;

            Op(String value) {
                this.value = value;
            }

            public static Op fromString(String value) {
                for (Op op : Op.values()) {
                    if (op.value.equals(value)) {
                        return op;
                    }
                }
                return null;
            }
        }
    }
}
