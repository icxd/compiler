package net.icxd.compiler.execute.types;

public class Variable {
    private String id;
    private Object value;

    public Variable(String id, Object value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }
}
