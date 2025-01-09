import java.util.List;

interface MyCallable {
    int parametersCount();
    Object call(List<Object> arguments);
}
