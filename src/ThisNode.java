
class ThisNode implements ExprNode {
    final Token keyword;
    public ThisNode(Token keyword) {
        this.keyword = keyword;
    }

    public Object evaluate() {
        return Main.scope.get(keyword);
    }
}
