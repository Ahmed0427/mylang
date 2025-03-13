class DotExpr implements ExprNode {
    final Token name;
    final ExprNode expr;

    public DotExpr(ExprNode expr, Token name) {
        this.expr = expr;
        this.name = name;
    }

    public Object evaluate() {
        Object obj = expr.evaluate();
        if (obj instanceof MyInstance) {
            return ((MyInstance) obj).get(name);
        }
        else {
            throw new EvaluationException(name,
                "Dot syntax Only with instances of classes.");
        }
    } 
}
