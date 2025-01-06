class AssignNode implements ExprNode {
    final Token name;
    final ExprNode value;

    AssignNode(Token name, ExprNode value) {
        this.name = name;
        this.value = value;
    }

    public Object evaluate() {
        Main.scope.update(name, value.evaluate());
        return Main.scope.varsMap.get(name.lexeme);
    }

}

