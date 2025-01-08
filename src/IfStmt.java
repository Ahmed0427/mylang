import java.util.ArrayList;
import java.util.AbstractMap;

class IfStmt implements StmtNode {
    final ExprNode condition;
    final StmtNode ifBranch;
    final ArrayList<AbstractMap.SimpleEntry<ExprNode, StmtNode>> elseIfConditionsBranches;
    final StmtNode elseBranch;

    IfStmt(ExprNode condition, StmtNode ifBranch,
    ArrayList<AbstractMap.SimpleEntry<ExprNode, StmtNode>> elseIfConditionsBranches,
    StmtNode elseBranch) {
        
        this.condition = condition;
        this.ifBranch = ifBranch;
        this.elseIfConditionsBranches = elseIfConditionsBranches;
        this.elseBranch = elseBranch;
    }

    public void evaluate() {
        if (Util.isTruthy(condition.evaluate())) {
            ifBranch.evaluate();
            return;
        } 
        for (var entry : elseIfConditionsBranches) {
            if (Util.isTruthy(entry.getKey().evaluate())) {
                entry.getValue().evaluate();
                return;
            } 
        }
        if (elseBranch != null) {
            elseBranch.evaluate();
        }
    }
}
