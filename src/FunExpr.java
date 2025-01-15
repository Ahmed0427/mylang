import java.util.List;

class FunExpr implements ExprNode {
    final Token name;
    final List<Token> parameters;
    final StmtNode body;

    FunExpr(Token name, List<Token> parameters, StmtNode body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public Object evaluate() {
        if (Main.scope.namesMap.containsKey(name.lexeme)) {
            String msg = String.format("'%s' has already been defined", name.lexeme);
            throw new EvaluationException(name, msg); 
        }

        MyFunction function = new MyFunction(name, parameters, body, Main.scope);

        return function;
    }
}

