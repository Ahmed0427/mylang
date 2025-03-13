import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

        Map<String, MyFunction> callableMethods = new HashMap<>();
        
        for (FunDeclStmt method : methods) {
            MyFunction met = new MyFunction(method.name,
                method.parameters, method.body, Main.scope);
            callableMethods.put(method.name.lexeme, met);
        }
        

        MyClass myClass = new MyClass(name.lexeme, callableMethods);
        Main.scope.namesMap.put(name.lexeme, myClass);
        Main.scope.isConstMap.put(name.lexeme, false);
    }
}
