import java.util.List;
import java.util.ArrayList;

class CallExpr implements ExprNode {
    final ExprNode calleeExpr;
    final Token closingParen;
    final List<ExprNode> args;

    CallExpr(ExprNode calleeExpr, Token closingParen, List<ExprNode> args) {
        this.calleeExpr = calleeExpr;
        this.closingParen = closingParen;
        this.args = args;
    }

    public Object evaluate() {
        Object functionObj = calleeExpr.evaluate(); 

        if (!(functionObj instanceof MyCallable)) {
            throw new EvaluationException(closingParen,
            "Can only call functions and classes.");

        }

        MyCallable function = (MyCallable)functionObj;

        if (args.size() != function.parametersCount()) {
            throw new EvaluationException(closingParen, "Expected " +
                function.parametersCount() + " arguments but got " +
                args.size() + ".");
        }

        List<Object> arguments = new ArrayList<>();

        for (ExprNode argument : args) { 
            arguments.add(argument.evaluate());
        }

        return function.call(arguments);
    }


}
