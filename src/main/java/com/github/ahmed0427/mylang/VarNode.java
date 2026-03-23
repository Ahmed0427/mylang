package com.github.ahmed0427.mylang;

class VarNode implements ExprNode {
    final Token name;

    VarNode(Token name) {
        this.name = name;
    }
    
    public Object evaluate() {
        return Main.scope.get(name);
    }
}

