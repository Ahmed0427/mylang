class VarDeclStmt implements StmtNode {
    TokenType declType;
    Token name;
    ExprNode expr;

    VarDeclStmt(TokenType declType, Token name, ExprNode expr) {
        this.declType = declType;
        this.name = name;
        this.expr = expr;
    }

    public void evaluate() {
        if (Main.scope.varsMap.containsKey(name.lexeme)) {
            String msg = String.format("'%s' has already been defined", name.lexeme);
            throw new EvaluationException(name, msg); 
        }

        Object value = null;

        if (this.expr != null) {
            value = expr.evaluate();
        }

        Main.scope.varsMap.put(name.lexeme, value);

        boolean isConst = (declType == TokenType.CONST); 

        Main.scope.isConstMap.put(name.lexeme, isConst); 
    }
}
