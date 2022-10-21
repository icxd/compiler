package net.icxd.compiler.ast;

import net.icxd.compiler.ast.exp.Exp;

import java.util.ArrayList;

public class AST {
    public ArrayList<Exp> exps = new ArrayList<>();

    public void dump() {
        Exp.dump(exps);
    }

    public void add(Exp exp) {
        exps.add(exp);
    }

    public void addAll(ArrayList<Exp> exps) {
        this.exps.addAll(exps);
    }

    public ArrayList<Exp> getExps() {
        return exps;
    }

    public void setExps(ArrayList<Exp> exps) {
        this.exps = exps;
    }

    @Override
    public String toString() {
        return "AST{" +
                "exps=" + exps +
                '}';
    }
}
