package com.jbunce.analizadorlexico.analizers.semantic;
import com.jbunce.analizadorlexico.analizers.semantic.SemanticToken;

%%
%public
%class SemanticLexer
%type SemanticToken

L=[a-zA-Z]
D=[0-9]+
space=[\t \r \n]+
type=(int|float|char|boolean)
equal=(=)
semicolon=(;)
greater=(>)
less=(<)
openP=(\()
closeP=(\))

name=({L}+|({L}{D})+)
value=({D}+|{D}+.{D}+|{name})
console=(console\.log{openP}"hello"{closeP})

declaration=({type}{space}*{greater}{space}*{name}{semicolon})
declarationAsign=({type}{space}*{greater}{space}*{name}{space}*{equal}{space}*{value}{semicolon})

forLoop=((for){space}*{openP}{space}*({name}|{value}){space}*(to){space}*({name}|{D}){space}*{closeP}{space}*{greater}{space}*\{\})

function=({type}{space}*{less}{space}*{name}{openP}{closeP}{space}*\{\})
functionCall=({name}{openP}{closeP}{semicolon})

if=(if{space}*{openP}{space}*({name}|{value}){space}*(==|!=|>|<|>=|<=){space}*({name}|{value}){space}*{closeP}{space}*\{\})
else=(else{space}*{greater}{space}*\{\})

%{
    public String lexeme;
%}

%%

{space} { /* ignore */ }
"//".* { /* ignore */ }

{declaration} {
          String lexeme = yytext();
          String name = lexeme.split(" ")[2];
          return new SemanticToken("let \t" + name, SemanticToken.Semantic.DECLARATION);
}

{declarationAsign} {
            String lexeme = yytext();
            String name = lexeme.split(" ")[2];
            String value = lexeme.split(" ")[4];
            return new SemanticToken("let \t" + name + " = " + value, SemanticToken.Semantic.DECLARATION_ASIGN);
}

{forLoop} {
          String lexeme = yytext();
          String first = lexeme.split(" ")[1].replace("(", "");
          String second = lexeme.split(" ")[3].replace(")", "");
          String token = "for(let \t" + first + "=0; " + first + " <" + second + ";" + first + "++){console.log(\"hello\")}";
          return new SemanticToken(token, SemanticToken.Semantic.FOR);
}

{function} {
          String lexeme = yytext();
          String name = lexeme.split(" ")[2].replace("{", "").replace("}", "");
          String token = "function \t" + name + "{}";
          return new SemanticToken(token, SemanticToken.Semantic.FUNCTION);
}

 . { return null; }