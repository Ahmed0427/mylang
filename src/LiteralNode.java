class LiteralNode implements ExprNode {
    final Object value;

    LiteralNode(Object value) {
        this.value = value;
    }

    public Object evaluate() {
        return value; 
    }
}
