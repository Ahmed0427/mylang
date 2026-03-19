package com.github.ahmed0427;

class ExprStmt implements StmtNode {
    ExprNode expression;

    ExprStmt(ExprNode expression) {
        this.expression = expression;
    }

    public void evaluate() {
        expression.evaluate();
    }
}
