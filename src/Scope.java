import java.util.HashMap;
import java.util.Map;

class Scope {
    final Map<String, Object> varsMap;
    final Map<String, Boolean> isConstMap;

    final Scope parentScope;

    Scope() {
        this(null);
    }

    Scope(Scope parentScope) {
        this.varsMap = new HashMap<>();
        this.isConstMap = new HashMap<>();
        this.parentScope = parentScope;
    }

    void update(Token name, Object value) {
        if (!varsMap.containsKey(name.lexeme)) {
            if (parentScope != null) {
                parentScope.update(name, value);
                return;
            }
            
            throw new EvaluationException(name,
                "Undefined variable '" + name.lexeme + "'.");
        }

        if (isConstMap.get(name.lexeme)) {
            throw new EvaluationException(name, "Reassignment to constant variable."); 
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
            throw new EvaluationException(name, "accessing to a variable with no value."); 
        }

        return varsMap.get(name.lexeme);
    }
}
