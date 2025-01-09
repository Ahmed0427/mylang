import java.util.List;
import java.util.ArrayList;
import java.util.AbstractMap;
import java.util.Arrays;

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
            if (match(TokenType.FUN)) return funDecl();
            return statement();
        } 
        catch (RuntimeException  ex) {
            findNextStatement();
            return null;
        }
    }

    StmtNode funDecl() {
        advance();
        return function("function");
    }

    StmtNode function(String kind) {
        if (!match(TokenType.IDENTIFIER)) {
            throw error(peek(), "Expect " + kind + " name.");
        }

        Token name = advance();

        if (!match(TokenType.LEFT_PAREN)) {
            throw error(peek(), "Expect ( " + kind + " name.");
        }
        advance();

        List<Token> params = new ArrayList<>();
        if (!match(TokenType.RIGHT_PAREN)) {
            params = parameters();
        }

        if (!match(TokenType.RIGHT_PAREN)) {
            throw error(peek(), "Expect ) after parameters");
        }
        advance();

        if (!match(TokenType.LEFT_BRACE)) {
            throw error(peek(), "Expect { after " + kind + " parameters");
        }

        StmtNode body = block();

        return new FunDeclStmt(name, params, body);

    }

    List<Token> parameters() {
        List<Token> params = new ArrayList<>();

        do {
            if (params.size() >= 255) {
                error(peek(), "Can't have more than 255 parameters.");
            }

            if (match(TokenType.COMMA)) {
                advance();
            }

            if (!match(TokenType.IDENTIFIER)) {
                throw error(peek(), "Expect a parameter name");
            }

            params.add(advance());
        }
        while (match(TokenType.COMMA));

        return params;
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
        if (match(TokenType.WHILE)) return whileStmt(); 
        if (match(TokenType.FOR)) return forStmt(); 
        return exprStmt();
    }

    StmtNode forStmt() {
        advance();

        if (!match(TokenType.LEFT_PAREN)) {
            throw error(peek(), "Expect '(' after if.");
        }
        advance();

        StmtNode init; 
        if (match(TokenType.LET, TokenType.CONST)) init = variable();
        else if (match(TokenType.SEMICOLON)) {
            init = null; advance();
        } 
        else init = exprStmt();

        ExprNode condition = null;
        if (!match(TokenType.SEMICOLON)) {
            condition = expression();
            if (!match(TokenType.SEMICOLON)) {
                throw error(peek(), "Expect ';' after for condition.");
            }
        }
        advance();

        ExprNode updateExpr = null;
        if (!match(TokenType.RIGHT_PAREN)) {
            updateExpr = expression();
            if (!match(TokenType.RIGHT_PAREN)) {
                throw error(peek(), "Expect ')' after for clauses.");
            }
        }
        advance();

        StmtNode body = statement();

        if (updateExpr != null) {
            body = new BlockStmt(Arrays.asList(
                body,
                new ExprStmt(updateExpr) 
            ));
        }

        if (condition == null) condition = new LiteralNode(true);
        body = new WhileStmt(condition, body);

        if (init != null) body = new BlockStmt(Arrays.asList(init, body));
        return body; 
    }

    StmtNode whileStmt() {
        advance();

        if (!match(TokenType.LEFT_PAREN)) {
            throw error(peek(), "Expect '(' after if.");
        }
        advance();

        ExprNode condition = expression();

        if (!match(TokenType.RIGHT_PAREN)) {
            throw error(peek(), "Expect ')' after condition.");
        }
        advance();

        StmtNode body = statement();

        return new WhileStmt(condition, body);
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
        while(match(TokenType.SLASH, TokenType.STAR, TokenType.MOD)) {
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
            return call();
        }
    }

    ExprNode call() {
        ExprNode expr = primary();
        while (match(TokenType.LEFT_PAREN)) {
            advance();
            List<ExprNode> argsList = args();
            if (!match(TokenType.RIGHT_PAREN)) {
                throw error(peek(), "Expect ')' after call.");
            }
            expr = new CallExpr(expr, advance(), argsList);
        }
        return expr;
    }

    List<ExprNode> args() {
        List<ExprNode> argsList = new ArrayList<>();
        if (match(TokenType.RIGHT_PAREN)) {
            return argsList;
        }
        while (true) {
            argsList.add(expression());
            if (argsList.size() >= 255) {
                error(peek(), "Can't have more than 255 arguments.");
            }
            if (!match(TokenType.COMMA)) {
                break;
            }
            advance();
        }
        return argsList;
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
