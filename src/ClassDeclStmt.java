import java.util.List;

class ClassDeclStmt implements StmtNode {
    final Token name;
    final List<FunDeclStmt> methods;

    ClassDeclStmt(Token name, List<FunDeclStmt> methods) {
        this.name = name;
        this.methods = methods;
    }

    public void evaluate() {
        if (Main.scope.namesMap.containsKey(name.lexeme)) {
            String msg = String.format("'%s' has already been defined", name.lexeme);
            throw new EvaluationException(name, msg); 
        }

        MyClass myClass = new MyClass(name.lexeme);
        Main.scope.namesMap.put(name.lexeme, myClass);
        Main.scope.isConstMap.put(name.lexeme, false);
    }
}
