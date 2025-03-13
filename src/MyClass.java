import java.util.List;

class MyClass implements MyCallable {
    final String name;

    MyClass(String name) {
        this.name = name;
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

