import java.util.List;
import java.util.ArrayList;
import java.util.AbstractMap;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token>tokens) {
        this.tokens = tokens;
    }
    
    List<StmtNode> parse() {
        List<StmtNode> program = new ArrayList<>();

        while (!isAtEnd()) {
            program.add(declaration());
        }

        return program;
    }

    StmtNode declaration() {
        try {
            if (match(TokenType.LET, TokenType.CONST)) return variable();
            return statement();
        } 
        catch (RuntimeException  ex) {
            findNextStatement();
            return null;
        }
    }

    StmtNode variable() {
        TokenType declType = advance().type;

        if (!match(TokenType.IDENTIFIER)) {
            throw error(peek(), "Expect variable name.");
        }

        Token name = advance();

        ExprNode expr = null; 

        if (match(TokenType.EQUAL)) {
            advance(); expr = expression();
        }
        else if (declType == TokenType.CONST) {
            throw error(peek(), "Const declaration must be initialized.");
        }

        if (!match(TokenType.SEMICOLON)) {
            throw error(peek(), "Expect ; after expression.");
        }
        advance();

        return new VarDeclStmt(declType, name, expr);
            
    }

    StmtNode statement() {
        if (match(TokenType.PRINT)) return printStmt(); 
        if (match(TokenType.LEFT_BRACE)) return block(); 
        if (match(TokenType.IF)) return ifStmt(); 
        return exprStmt();
    }

    StmtNode ifStmt() {
        advance();

        if (!match(TokenType.LEFT_PAREN)) {
            throw error(peek(), "Expect '(' after if.");
        }
        advance();

        ExprNode ifCondition = expression();
        if (!match(TokenType.RIGHT_PAREN)) {
            throw error(peek(), "Expect ')' after condition.");
        }
        advance();

        StmtNode ifBranch = statement();
        var elseIfs = new ArrayList<AbstractMap.SimpleEntry<ExprNode, StmtNode>>();
        StmtNode elseBranch = null;
        while (match(TokenType.ELSE)) {
            advance();
            if (match(TokenType.IF)) {
                advance();

                if (!match(TokenType.LEFT_PAREN)) {
                    throw error(peek(), "Expect '(' after else if.");
                }
                advance();

                ExprNode elseIfCondition = expression();
                if (!match(TokenType.RIGHT_PAREN)) {
                    throw error(peek(), "Expect ')' after condition.");
                }
                advance();

                StmtNode elseIfBranch = statement();
                elseIfs.add(new AbstractMap.SimpleEntry<>(elseIfCondition, elseIfBranch));
            }
            else {
                elseBranch = statement();
                break;
            }
        }

        return new IfStmt(ifCondition, ifBranch, elseIfs, elseBranch);
    }

    StmtNode block() {
        advance();
        List<StmtNode> statements = new ArrayList<>();

        while (!match(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }

        if (!match(TokenType.RIGHT_BRACE)) {
            throw error(peek(), "Expect '}' after block");
        }
        advance();

        return new BlockStmt(statements);

    }

    StmtNode exprStmt() {
        ExprNode expr = expression();

        if (!match(TokenType.SEMICOLON)) {
            throw error(peek(), "Expect ; after expression.");
        }

        advance();

        return new ExprStmt(expr);
    }

    StmtNode printStmt() {
        advance();
        ExprNode expr = expression();

        if (!match(TokenType.SEMICOLON)) {
            throw error(peek(), "Expect ; after expression.");
        }

        advance();

        return new PrintStmt(expr);
    }

    ExprNode expression() {
        return assignment();
    }

    ExprNode assignment() {
        ExprNode expr = ternary();

        if (match(TokenType.EQUAL)) {
            Token equals = advance();
            ExprNode value = assignment();

            if (expr instanceof VarNode) {
                Token name = ((VarNode)expr).name;
                return new AssignNode(name, value);
            }

            error(equals, "Invalid assignment target."); 
        }

        return expr;
    }

    ExprNode ternary() {
        ExprNode condition = logicOr();
        if (match(TokenType.QUESTION)) {
            advance();
            ExprNode truePart = ternary();
            if (match(TokenType.COLON)) {
                advance();
                ExprNode falsePart = ternary();
                return new TernaryNode(condition, truePart, falsePart); 
            }
            else {
                throw error(peek(), "Expect ':' after expression.");
            }
        }
        else {
            return condition;
        }
    }

    ExprNode logicOr() {
        ExprNode expr = logicAnd();
        while(match(TokenType.OR)) {
            Token operator = advance();  
            ExprNode right = logicAnd();

            expr = new BinaryNode(expr, operator, right);
        }

        return expr;
    }

    ExprNode logicAnd() {
        ExprNode expr = equality();
        while(match(TokenType.AND)) {
            Token operator = advance();  
            ExprNode right = equality();

            expr = new BinaryNode(expr, operator, right);
        }

        return expr;
    }

    ExprNode equality() {
        ExprNode expr = comparison();
        while(match(TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL)) {
            Token operator = advance();  
            ExprNode right = comparison();

            expr = new BinaryNode(expr, operator, right);
        }

        return expr;
    }

    ExprNode comparison() {
        ExprNode expr = term();
        while(match(TokenType.GREATER, TokenType.GREATER_EQUAL,
            TokenType.LESS, TokenType.LESS_EQUAL)) {

            Token operator = advance();  
            ExprNode right = term();

            expr = new BinaryNode(expr, operator, right);
        }

        return expr;
    }

    ExprNode term() {
        ExprNode expr = factor();
        while(match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = advance();  
            ExprNode right = factor();

            expr = new BinaryNode(expr, operator, right);
        }

        return expr;
    }

    ExprNode factor() {
        ExprNode expr = unary();
        while(match(TokenType.SLASH, TokenType.STAR)) {
            Token operator = advance();  
            ExprNode right = unary();

            expr = new BinaryNode(expr, operator, right);
        }

        return expr;
    }

    ExprNode unary() {
        if (match(TokenType.MINUS, TokenType.BANG)) {
            Token operator = advance();
            ExprNode right = unary();

            return new UnaryNode(operator, right);
        }
        else {
            return primary();
        }
    }
    
    ExprNode primary() {
        if (match(TokenType.TRUE)) {
            advance(); return new LiteralNode(true);
        }

        if (match(TokenType.FALSE)) {
            advance(); return new LiteralNode(false);
        } 

        if (match(TokenType.NONE)) {
            advance(); return new LiteralNode(null);
        } 

        if (match(TokenType.STRING, TokenType.NUMBER)) {
            return new LiteralNode(advance().literal);
        }

        if (match(TokenType.IDENTIFIER)) {
            return new VarNode(advance());
        }

        if (match(TokenType.LEFT_PAREN)) {
            advance();
            ExprNode expr = expression();
            if (!match(TokenType.RIGHT_PAREN)) {
                throw error(peek(), "Expect ')' after expression.");
            }
            advance();
            return new GroupingNode(expr);
        }
        
        throw error(peek(), "Expect expression.");
    }

    private boolean match(TokenType... types) {
        for(TokenType type : types) {
            if(type == peek().type) {
                return true;
            }
        }
        return false;
    }

    private Token advance() {
        if (!isAtEnd()) current++; 
        return peekPrevious();
    }

    private Token peekPrevious() {
        return tokens.get(current - 1);
    }
    
    private Token peek() {
        return tokens.get(current);
    }

    private boolean isAtEnd() {
        return tokens.get(current).type == TokenType.EOF;
    }

    private void findNextStatement() {
        advance();
        while (!isAtEnd()) {
            if (peekPrevious().type == TokenType.SEMICOLON) return;

            switch (peek().type) {
                case CLASS:
                case FUN:
                case LET:
                case CONST:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                return;
            }

            advance();
        }
    }

    private RuntimeException error(Token token, String message) {
        Main.reportError(token, message);
        return new RuntimeException();
    }
}
