import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import jline.console.ConsoleReader;
import jline.console.history.FileHistory;
import jline.console.completer.StringsCompleter;
 

public class Main {

    static boolean isThereError = false;
    static boolean isThereRuntimeError = false;
    
    static Scope scope = new Scope();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            runREPL();
        }
        else if (args.length == 1) {
            run(Files.readString(Paths.get(args[0])));
            if (isThereRuntimeError) System.exit(1);
            if (isThereError) System.exit(1);
        }
        else {
            System.exit(1);
        }
    }

    private static void runREPL() throws IOException {
        ConsoleReader reader = new ConsoleReader();

        reader.setHistory(new FileHistory(new File("history.txt")));

        reader.setExpandEvents(false); 

        String[] keywords = {
            "false", "true",
            "if", "else", "while", "for",
            "or", "and",
            "class", "super", "this",
            "fun",
            "none", 
            "print",
            "return",
            "let", "const"
        };

        reader.addCompleter(new StringsCompleter(keywords));

        while (true) {
            try {
                String line = reader.readLine("> ");

                if (line == null || line.equals("exit")) break;

                run(line);

                isThereError = false;
                isThereRuntimeError = false;
            }
            catch (IOException e) {
                System.err.println("Error reading input: " + e.getMessage());
            }
        }
    }

    private static void run(String source) {
        Tokenizer tokenizer = new Tokenizer(source);
        List<Token> tokens = tokenizer.scanTokens();

        Parser parser = new Parser(tokens);
        List<StmtNode> statements = parser.parse();

        if (isThereError) return;

        try {
            for(StmtNode stmt : statements) {
                stmt.evaluate();
            }
        }
        catch(EvaluationException ex) {
            reportEvaluationError(ex);
        }
    }

    static void reportError(int line, String message) {
        printError(line, message, "");
    }

    static void reportError(Token token, String message) {
        String where = "";
        if (token.type == TokenType.EOF) where = "at end";
        else where = "at '" + token.lexeme + "'";
        printError(token.line, message, where);
    }

    private static void printError(int line, String message, String where) {
        String errMsg = "[line " + line + "] Error " + where + ": " + message;
        System.err.println(errMsg);
        isThereError = true;
    }

    static void reportEvaluationError(EvaluationException ex) {
        System.err.println("[line " + ex.token.line + "] " + ex.getMessage());
        isThereRuntimeError = true;
    } 
}
