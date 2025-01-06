public class TernaryNode implements ExprNode {
    final ExprNode condition;
    final ExprNode truePart;
    final ExprNode falsePart;
    
    TernaryNode(ExprNode condition, ExprNode truePart, ExprNode falsePart) {
        this.condition = condition;
        this.truePart = truePart;
        this.falsePart = falsePart;
    }

    public Object evaluate() {
        Object result = condition.evaluate();

        if (Util.isTruthy(result)) {
            return truePart.evaluate();
        }
        else {
            return falsePart.evaluate();
        }
    }
}
