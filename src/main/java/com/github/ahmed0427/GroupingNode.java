package com.github.ahmed0427;

class GroupingNode implements ExprNode {
    final ExprNode expression;

    GroupingNode(ExprNode expression) {
        this.expression = expression;
    }

    public Object evaluate() {
        return expression.evaluate();
    }
}
