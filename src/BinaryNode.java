class BinaryNode implements ExprNode {
    final ExprNode left;
    final Token operator;
    final ExprNode right;

    BinaryNode(ExprNode left, Token operator, ExprNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Object evaluate() {
        Object objL = left.evaluate();
        Object objR = right.evaluate();

        switch (operator.type) {
            case PLUS: 

                if (objL instanceof Double && objR instanceof Double) {
                    return (double)objL + (double)objR;
                }

                if (objL instanceof String && objR instanceof String) {
                    return (String)objL + (String)objR;
                }
                
                if (objL instanceof String || objR instanceof String) {
                    if (objL instanceof Double || objR instanceof Double) {
                        return Util.convertToString(objL) + 
                        Util.convertToString(objR);
                    }
                }
                
                throw new EvaluationException(operator,
                "Tow operands must be numbers or strings."); 

            case MINUS:

                numberOperands(operator, objL, objR);

                return (double)objL - (double)objR; 

            case STAR:

                numberOperands(operator, objL, objR);

                return (double)objL * (double)objR; 

            case SLASH:

                numberOperands(operator, objL, objR);
                
                if ((double)objR == 0.0) {
                    throw new EvaluationException(operator,
                    "Zero division error."); 
                }

                return (double)objL / (double)objR; 

            case MOD:

                numberOperands(operator, objL, objR);
                
                if ((double)objR == 0.0) {
                    throw new EvaluationException(operator,
                    "Zero division error."); 
                }

                return (double)objL % (double)objR; 

            case GREATER:

                numberOperands(operator, objL, objR);

                return (double)objL > (double)objR; 

            case GREATER_EQUAL:

                numberOperands(operator, objL, objR);

                return (double)objL >= (double)objR; 

            case LESS:

                numberOperands(operator, objL, objR);

                return (double)objL < (double)objR; 

            case LESS_EQUAL:

                numberOperands(operator, objL, objR);

                return (double)objL <= (double)objR; 

            case EQUAL_EQUAL: return isEqual(objL, objR);
            case BANG_EQUAL: return !isEqual(objL, objR);  

            case OR: return Util.isTruthy(objL) || Util.isTruthy(objR);
            case AND: return Util.isTruthy(objL) && Util.isTruthy(objR);
        }

        return null;
    }

    private boolean isEqual(Object objL, Object objR) {
        if (objL == null && objR == null) return true;
        if (objL == null || objR == null) return false;
        return objL.equals(objR);
    }

    private void numberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;

        throw new EvaluationException(operator,
        "Tow operands must be numbers."); 
    } 
}
