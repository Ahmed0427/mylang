class WhileStmt implements StmtNode {
    final ExprNode condition;
    final StmtNode body;

    WhileStmt(ExprNode condition, StmtNode body) {
        this.condition = condition;
        this.body = body;
    }

    public void evaluate() {
        while (Util.isTruthy(condition.evaluate())) {
            body.evaluate();
        } 
    }
}

