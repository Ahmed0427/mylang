import java.util.List;
import java.util.Map;
 
class MyClass implements MyCallable {
    final String name;
    Map<String, MyFunction> methods;

    MyClass(String name, Map<String, MyFunction> methods) {
        this.name = name;
        this.methods = methods;
    }

    public int parametersCount() {
        return 0;
    }

    public Object call(List<Object> args) {
        MyInstance instance = new MyInstance(this);
        return instance;
    }

    public String toString() {
        return "<Class: " + name + ">";
    }
}

