class ReturnStmt implements StmtNode {
    final Token keyword;
    final ExprNode expr;

    ReturnStmt(Token keyword, ExprNode expr) {
        this.keyword = keyword;
        this.expr = expr;
    }

    public void evaluate() {
        Object value = null;
        if (expr != null) value = expr.evaluate();
        throw new ReturnVal(value, keyword);
    }
}
