import java.util.HashMap;
import java.util.Map;

class Scope {
    Map<String, Object> varsMap;
    Map<String, Boolean> isConstMap;

    final Scope parentScope;

    Scope() {
        this.parentScope = null;
        this.varsMap = new HashMap<>();
        this.isConstMap = new HashMap<>();
    }

    Scope(Scope scope) {
        this.parentScope = scope;
        this.varsMap = new HashMap<>();
        this.isConstMap = new HashMap<>();
    }

    void update(Token name, Object value) {
        if (!varsMap.containsKey(name.lexeme)) {

            if (parentScope != null) {
                parentScope.update(name, value);
            }
            
            throw new EvaluationException(name,
                "Undefined variable '" + name.lexeme + "'.");
        }

        if (isConstMap.get(name.lexeme)) {
            throw new EvaluationException(name, "Assignment to constant variable."); 
        }

        varsMap.put(name.lexeme, value);
    }

    Object get(Token name) {
        if (!varsMap.containsKey(name.lexeme)) {

            if (parentScope != null) {
                return parentScope.get(name);
            }
            
            throw new EvaluationException(name,
                "Undefined variable '" + name.lexeme + "'.");
        }

        if (varsMap.get(name.lexeme) == null) {
            throw new EvaluationException(name, "accessing to uninitialized variable."); 
        }

        return varsMap.get(name.lexeme);
    }
}
