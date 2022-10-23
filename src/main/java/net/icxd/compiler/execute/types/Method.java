package net.icxd.compiler.execute.types;

import net.icxd.compiler.ast.exp.Exp;

import java.util.ArrayList;

public class Method {
    private String id;
    private String returnType;
    private ArrayList<String> parameters;
    private ArrayList<Exp> body;


    public Method(String name, String returnType, ArrayList<String> parameters, ArrayList<Exp> body) {
        this.id = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public String getReturnType() {
        return returnType;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public ArrayList<Exp> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Method{" +
                "id='" + id + '\'' +
                '}';
    }
}
