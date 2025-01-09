import java.util.List;

class FunDeclStmt implements StmtNode {
    final Token name;
    final List<Token> parameters;
    final StmtNode body;

    FunDeclStmt(Token name, List<Token> parameters, StmtNode body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public void evaluate() {
        MyFunction function = new MyFunction(this);
        Main.scope.namesMap.put(name.lexeme, function);
    }
}

