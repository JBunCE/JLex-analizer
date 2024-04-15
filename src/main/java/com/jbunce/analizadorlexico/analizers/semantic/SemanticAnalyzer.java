package com.jbunce.analizadorlexico.analizers.semantic;

import com.jbunce.analizadorlexico.analizers.Tokens;
import com.jbunce.analizadorlexico.analizers.lexical.Lexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SemanticAnalyzer {

    private final Path path = Paths.get("src/main/java/com/jbunce/analizadorlexico/analizers/semantic/main.js");

    public SemanticAnalyzer() {
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parse(SemanticToken token) {
        write(token.getToken());
    }

    public void write(String message) {
        try {
            Files.writeString(path, message, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
