import java.util.Map;
import java.util.HashMap;

class MyInstance {
    final MyClass myClass;
    private final Map<String, Object> fields = new HashMap<>();

    MyInstance(MyClass myClass) {
        this.myClass = myClass;
    }

    public Object get(Token name) {
        if (fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }

        if (myClass.methods.containsKey(name.lexeme)) {
            return myClass.methods.get(name.lexeme);
        }

        throw new EvaluationException(name, 
            "Undefined property '" + name.lexeme + "'.");
    }

    public void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }

    public String toString() {
        return myClass.name + " instance";
    }
}

