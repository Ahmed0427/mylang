import java.util.List;

class MyFunction implements MyCallable {
    private final FunDeclStmt funDecl;
    MyFunction(FunDeclStmt funDecl) {
        this.funDecl = funDecl;
    }

    public int parametersCount() {
        return funDecl.parameters.size();
    }

    public Object call(List<Object> args) {
        Scope scope = new Scope(Main.global);
        for (int i = 0; i < funDecl.parameters.size(); i++) {
            scope.namesMap.put(funDecl.parameters.get(i).lexeme, args.get(i));
        }

        Main.scope = scope;
        funDecl.body.evaluate();
        Main.scope = Main.global;

        return null;
    }

    public String toString() {
        return "<fn " + funDecl.name.lexeme + ">";
    }
}
