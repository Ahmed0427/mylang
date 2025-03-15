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
        MyFunction initializer = methods.get("init");
        if (initializer != null) return initializer.parametersCount();
        return 0;
    }

    public Object call(List<Object> args) {
        MyInstance instance = new MyInstance(this);
        MyFunction initializer = methods.get("init");
        if (initializer != null) {
            initializer.bind(instance).call(args);
        }

        return instance;
    }

    public String toString() {
        return "<Class: " + name + ">";
    }
}

