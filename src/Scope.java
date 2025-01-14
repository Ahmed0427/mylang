import java.util.HashMap;
import java.util.Map;

class Scope {
    final Map<String, Object> namesMap;
    final Map<String, Boolean> isConstMap;

    final Scope parentScope;

    Scope() {
        this(null);
    }

    Scope(Scope parentScope) {
        this.namesMap = new HashMap<>();
        this.isConstMap = new HashMap<>();
        this.parentScope = parentScope;
    }

    void update(Token name, Object value) {
        if (!namesMap.containsKey(name.lexeme)) {
            if (parentScope != null) {
                parentScope.update(name, value);
                return;
            }
            
            throw new EvaluationException(name,
                "'" + name.lexeme + "' is undefined.");
        }

        if (isConstMap.get(name.lexeme)) {
            throw new EvaluationException(name, "Reassignment to constant variable."); 
        }

        namesMap.put(name.lexeme, value);
    }

    Object get(Token name) {
        if (!namesMap.containsKey(name.lexeme)) {

            if (parentScope != null) {
                return parentScope.get(name);
            }
            
            throw new EvaluationException(name,
                "'" + name.lexeme + "' is undefined.");
        }

        if (namesMap.get(name.lexeme) == null) {
            throw new EvaluationException(name, "accessing to a variable with no value."); 
        }

        return namesMap.get(name.lexeme);
    }
}
