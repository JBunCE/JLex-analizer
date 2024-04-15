package com.jbunce.analizadorlexico.analizers.lexical;

import com.jbunce.analizadorlexico.analizers.Tokens;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class Analyzer {

    public static void analize(TextFlow logArea, Lexer lexer) {
        AtomicReference<String> result = new AtomicReference<>("");

        while (true) {
            try {
                Tokens token = lexer.yylex();
                if (token == null) {
                    String res = result.get() + "EOF";
                    result.set(res);
                    break;
                }
                switch (token) {
                    case IF -> result.set(result.get() + "If: " + lexer.lexeme + "\n");
                    case ELSE -> result.set(result.get() + "Else: " + lexer.lexeme + "\n");
                    case FOR -> result.set(result.get() + "For: " + lexer.lexeme + "\n");
                    case TO -> result.set(result.get() + "To: " + lexer.lexeme + "\n");
                    case BOOLEAN -> result.set(result.get() + "Boolean: " + lexer.lexeme + "\n");
                    case NUMBER -> result.set(result.get() + "Number: " + lexer.lexeme + "\n");
                    case TYPE -> result.set(result.get() + "Type: " + lexer.lexeme + "\n");
                    case EQUALS -> result.set(result.get() + "Equals: " + lexer.lexeme + "\n");
                    case LESS_THAN -> result.set(result.get() + "Less than: " + lexer.lexeme + "\n");
                    case GREATER_THAN -> result.set(result.get() + "Greater than: " + lexer.lexeme + "\n");
                    case LOGIC_OP -> result.set(result.get() + "Logic op: " + lexer.lexeme + "\n");
                    case VOID_VALUE -> result.set(result.get() + "Void value: " + lexer.lexeme + "\n");
                    case OPERATORS -> result.set(result.get() + "Operators: " + lexer.lexeme + "\n");
                    case COMMA -> result.set(result.get() + "Comma: " + lexer.lexeme + "\n");
                    case NEW_LINE -> result.set(result.get() + "New line: " + lexer.lexeme + "\n");
                    case QUOTE -> result.set(result.get() + "Quote: " + lexer.lexeme + "\n");
                    case DISPLAY_DATA -> result.set(result.get() + "Display data: " + lexer.lexeme + "\n");
                    case OPEN_BRACE -> result.set(result.get() + "Open brace: " + lexer.lexeme + "\n");
                    case CLOSE_BRACE -> result.set(result.get() + "Close brace: " + lexer.lexeme + "\n");
                    case OPEN_PARENTHESIS -> result.set(result.get() + "Open parenthesis: " + lexer.lexeme + "\n");
                    case CLOSE_PARENTHESIS -> result.set(result.get() + "Close parenthesis: " + lexer.lexeme + "\n");
                    case SEMICOLON -> result.set(result.get() + "Semicolon: " + lexer.lexeme + "\n");
                    case NON_SPECIFIC -> result.set(result.get() + "Non specific: " + lexer.lexeme + "\n");
                    case ERROR -> result.set(result.get() + "Unexpected: " + lexer.lexeme + "\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Platform.runLater(() -> {
            logArea.getChildren().clear();
            Text text = new Text(result.get());
            logArea.getChildren().add(text);
        });
    }

}
