class PrintStmt implements StmtNode {
    ExprNode expression;

    PrintStmt(ExprNode expression) {
        this.expression = expression;
    }

    public void evaluate() {
        System.out.println(Util.convertToString(expression.evaluate()));
    }
}
