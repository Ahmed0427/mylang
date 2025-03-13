class DotAssignExpr implements ExprNode {
    final DotExpr dotExpr;
    final ExprNode value;

    DotAssignExpr(DotExpr dotExpr, ExprNode value) {
        this.dotExpr = dotExpr;
        this.value = value;
    }

    public Object evaluate() {
        Object obj = dotExpr.expr.evaluate();

        if (!(obj instanceof MyInstance)) { 
          throw new EvaluationException(dotExpr.name,
             "Only instances have fields.");
        }
        
        Object val = value.evaluate();
        ((MyInstance) obj).set(dotExpr.name, val); 
        return val;
    }
}
