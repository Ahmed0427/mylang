import java.util.List;

class MyFunction implements MyCallable {
    private final FunDeclStmt funDecl;
    private final Scope parentScope;

    MyFunction(FunDeclStmt funDecl, Scope parentScope) {
        this.funDecl = funDecl;
        this.parentScope = parentScope;
    }

    public int parametersCount() {
        return funDecl.parameters.size();
    }

    public Object call(List<Object> args) {
        Scope scope = new Scope(parentScope);

        for (int i = 0; i < funDecl.parameters.size(); i++) {
            scope.namesMap.put(funDecl.parameters.get(i).lexeme, args.get(i));
            scope.isConstMap.put(funDecl.parameters.get(i).lexeme, false);
        }
        
        try {
            Main.scope = scope;
            funDecl.body.evaluate();
        }
        catch (ReturnVal val) {
            return val.value;
        }
        finally {
            Main.scope = parentScope;
        }

        return null;
    }

    public String toString() {
        return "<Function: " + funDecl.name.lexeme + ">";
    }
}
