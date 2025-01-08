import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

class Tokenizer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Tokenizer(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // the start of the next lexeme.
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        if (c == '(') {
            addToken(TokenType.LEFT_PAREN);
        }
        else if (c == ')') {
            addToken(TokenType.RIGHT_PAREN);
        }
        else if (c == '{') {
            addToken(TokenType.LEFT_BRACE);
        }
        else if (c == '}') {
            addToken(TokenType.RIGHT_BRACE);
        }
        else if (c == ',') {
            addToken(TokenType.COMMA);
        }
        else if (c == '.') {
            addToken(TokenType.DOT);
        }
        else if (c == '-') {
            addToken(TokenType.MINUS);
        }
        else if (c == '+') {
            addToken(TokenType.PLUS);
        }
        else if (c == ';') {
            addToken(TokenType.SEMICOLON);
        }
        else if (c == ':') {
            addToken(TokenType.COLON);
        }
        else if (c == '?') {
            addToken(TokenType.QUESTION);
        }
        else if (c == '*') {
            addToken(TokenType.STAR);
        }
        else if (c == '%') {
            addToken(TokenType.MOD);
        }
        else if (c == '!') {
            addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
        }
        else if (c == '=') {
            addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
        }
        else if (c == '<') {
            addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
        }
        else if (c == '>') {
            addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
        }
        else if (c == '/') {
            if (match('/')) {
                while (peek() != '\n' && !isAtEnd()) advance();
            }
            else if(match('*')) {
                Stack<Integer> stack = new Stack<>();

                stack.push(0);
                while (!isAtEnd() && !stack.empty()) {
                    if (peek() == '*' && peekNext() == '/') {
                        stack.pop();
                        advance();
                    }
                    else if (peek() == '/' && peekNext() == '*') {
                        stack.push(0);
                        advance();
                    }

                    advance();
                } 

            }
            else {
                addToken(TokenType.SLASH);
            }
        }
        else if (c == '\r' || c == '\t' || c == '\n' || c == ' ') {
            if (c == '\n') line++;
        }
        else if (c == '"') {
            getStringToken();
        }
        else if(Character.isDigit(c)) {
            getNumberToken();
        }
        else if(Character.isLetter(c) || c == '_') {
            getIdentifierToken();
        }
        else {
            Main.reportError(line, "Unexpected character.");
        }
    }

    private void getIdentifierToken() {
        while (Character.isLetterOrDigit(peek()) || peek() == '_') advance();
        String word = source.substring(start, current);
        TokenType type = getIdentifierType(word);
        addToken(type);
    }

    private TokenType getIdentifierType(String word) {
        if (word.equals("true")) {
            return TokenType.TRUE;
        } 
        else if (word.equals("false")) {
            return TokenType.FALSE;
        }
        else if (word.equals("if")) {
            return TokenType.IF;
        }
        else if (word.equals("else")) {
            return TokenType.ELSE;
        }
        else if (word.equals("for")) {
            return TokenType.FOR;
        }
        else if (word.equals("while")) {
            return TokenType.WHILE;
        }
        else if (word.equals("or")) {
            return TokenType.OR;
        }
        else if (word.equals("and")) {
            return TokenType.AND;
        }
        else if (word.equals("class")) {
            return TokenType.CLASS;
        }
        else if (word.equals("super")) {
            return TokenType.SUPER;
        }
        else if (word.equals("this")) {
            return TokenType.THIS;
        }
        else if (word.equals("fun")) {
            return TokenType.FUN;
        }
        else if (word.equals("none")) {
            return TokenType.NONE;
        }
        else if (word.equals("print")) {
            return TokenType.PRINT;
        }
        else if (word.equals("return")) {
            return TokenType.RETURN;
        }
        else if (word.equals("let")) {
            return TokenType.LET;
        }
        else if (word.equals("const")) {
            return TokenType.CONST;
        }
        else {
            return TokenType.IDENTIFIER;
        }
    }

    private void getNumberToken() {
        while (Character.isDigit(peek())) advance();

        if (peek() == '.' && Character.isDigit(peekNext())) {
            advance(); // pass the "."

            while (Character.isDigit(peek())) advance();
        }

        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void getStringToken() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++; 
            advance();
        }         

        if (isAtEnd()) {
            Main.reportError(line, "Unterminated string literal.");
            return;
        }

        advance();

        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char advance() {
        return source.charAt(current++);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}

