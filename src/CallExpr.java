import java.util.List;
import java.util.ArrayList;

class CallExpr implements ExprNode {
    final ExprNode callee;
    final Token closingParen;
    final List<ExprNode> args;

    CallExpr(ExprNode callee, Token closingParen, List<ExprNode> args) {
        this.callee = callee;
        this.closingParen = closingParen;
        this.args = args;
    }

    public Object evaluate() {
        Object calleeName = callee.evaluate(); 

        List<Object> arguments = new ArrayList<>();

        for (ExprNode argument : args) { 
            arguments.add(argument.evaluate());
        }

        if (!(calleeName instanceof MyCallable)) {
            throw new EvaluationException(closingParen,
            "Can only call functions and classes.");

        }

        MyCallable function = (MyCallable)calleeName;

        if (args.size() != function.parametersCount()) {
            throw new EvaluationException(closingParen, "Expected " +
                function.parametersCount() + " arguments but got " +
                arguments.size() + ".");
        }

        return function.call(arguments);
    }


}
