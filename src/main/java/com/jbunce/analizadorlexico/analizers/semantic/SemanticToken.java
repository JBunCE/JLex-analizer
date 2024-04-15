package com.jbunce.analizadorlexico.analizers.semantic;

import com.jbunce.analizadorlexico.analizers.Tokens;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SemanticToken {

    public enum Semantic {
        DECLARATION,
        DECLARATION_ASIGN,
        FOR,
        FUNCTION
    }

    private String token;
    private Semantic type;

    public SemanticToken(String token, Semantic type) {
        this.token = token;
        this.type = type;
    }

}
