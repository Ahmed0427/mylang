import java.util.List;

class BlockStmt implements StmtNode {
    List<StmtNode> statements;

    BlockStmt(List<StmtNode> statements) {
        this.statements = statements;
    }

    public void evaluate() {
        Scope prevScope = Main.scope;

        try {
            Main.scope = new Scope(Main.scope);

            for (StmtNode stmt : statements) {
                stmt.evaluate();
            }
        }
        finally {
            Main.scope = prevScope;
        } 
    }
}

