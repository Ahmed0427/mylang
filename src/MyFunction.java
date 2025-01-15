import java.util.List;

class MyFunction implements MyCallable {
    private final Token name;
    private final List<Token> parameters;
    private final StmtNode body;
    private final Scope parentScope;

    MyFunction(Token name, List<Token> parameters,
        StmtNode body, Scope parentScope) {

        this.name = name;
        this.parameters = parameters;
        this.body = body;
        this.parentScope = parentScope;

    }

    public int parametersCount() {
        return parameters.size();
    }


    public Object call(List<Object> args) {
        Scope scope = new Scope(parentScope);

        for (int i = 0; i < parameters.size(); i++) {
            scope.namesMap.put(parameters.get(i).lexeme, args.get(i));
            scope.isConstMap.put(parameters.get(i).lexeme, false);
        }

        Scope prevMainScope = Main.scope;
        
        try {
            Main.scope = scope;
            body.evaluate();
        }
        catch (ReturnVal returnVal) {
            return returnVal.value;
        }
        finally {
            Main.scope = prevMainScope;
        }

        return null;
    }

    public String toString() {
        return "<Function: " + name.lexeme + ">";
    }
}
