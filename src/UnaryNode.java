class UnaryNode implements ExprNode {
    final Token operator;
    final ExprNode right;

    UnaryNode(Token operator, ExprNode right) {
        this.operator = operator;
        this.right = right;
    }

    public Object evaluate() {
        Object value = right.evaluate();

        if (operator.type == TokenType.MINUS) {
            numberOperand(operator, value);
            return -(double)value;
        }

        if (operator.type == TokenType.BANG) {
            return !Util.isTruthy(value);
        }

        return null;
    }

    
    private void numberOperand(Token operator, Object obj) {
        if (obj instanceof Double) return;

        throw new EvaluationException(operator,
        "operand must be number."); 
    } 
}
