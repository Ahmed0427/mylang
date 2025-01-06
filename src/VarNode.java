class VarNode implements ExprNode {
    final Token name;

    VarNode(Token name) {
        this.name = name;
    }
    
    public Object evaluate() {
        return Main.scope.get(name);
    }
}

