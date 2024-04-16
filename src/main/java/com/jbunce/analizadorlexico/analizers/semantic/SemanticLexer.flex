package com.jbunce.analizadorlexico.analizers.semantic;
import com.jbunce.analizadorlexico.analizers.semantic.SemanticToken;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


%%
%public
%class SemanticLexer
%type SemanticToken

L=[a-zA-Z]
D=[0-9]+
space=[\t \r \n]+
type=(int|float|char|boolean|void)
equal=(=)
semicolon=(;)
greater=(>)
less=(<)
openP=(\()
closeP=(\))

name=({L}+|({L}{D})+)
value=({D}+|{D}+.{D}+|{name})
console=(console\.log\(\"{name}\"\))
consolebye=(console\.log\(\"{name}\"\))

declaration=({type}{space}*{greater}{space}*{name}{semicolon})
declarationAsign=({type}{space}*{greater}{space}*{name}{space}*{equal}{space}*{value}{semicolon})

forLoop=((for){space}*{openP}{space}*({name}|{value}){space}*(to){space}*({name}|{D}){space}*{closeP}{space}*{greater}{space}*\{{space}*{console}{space}*\})

function=({type}{space}*{less}{space}*{name}{openP}{closeP}{space}*\{{space}*{console}{space}*\})
functionCall=({name}{openP}{closeP}{semicolon})

else=(else{space}*{greater}{space}*\{{space}*{consolebye}{space}*\})
if=(if{space}*{openP}{space}*({name}|{value}){space}*(==|!=|>|<|>=|<=){space}*({name}|{value}){space}*{closeP}{space}*{greater}{space}*\{{space}*{console}{space}*\}{space}*{else})

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
          String out = "";
          Pattern pattern = Pattern.compile("\\{\\s*(.*?)\\s*\\}", Pattern.DOTALL);
          Matcher matcher = pattern.matcher(lexeme);
          while (matcher.find()) {
              out = matcher.group(1);
          }
          String first = lexeme.split(" ")[1].replace("(", "");
          String second = lexeme.split(" ")[3].replace(")", "");

          String token = "for(let \t" + first + "=0; " + first + " <" + second + ";" + first + "++){" + out + "}";
          return new SemanticToken(token, SemanticToken.Semantic.FOR);
}

{function} {
          String lexeme = yytext();

          String token = lexeme
            .replace("int", "")
            .replace("float", "")
            .replace("char", "")
            .replace("boolean", "")
            .replace("void", "")
            .replace("<", "function ");

          return new SemanticToken(token, SemanticToken.Semantic.FUNCTION);
}

{functionCall} {
          String lexeme = yytext();
          String name = lexeme.split(" ")[0];
          String token = name + "()";
          return new SemanticToken(lexeme, SemanticToken.Semantic.FUNCTION_CALL);
}

{if} {
          String lexeme = yytext();
          String token = lexeme.replace(">", "");
          return new SemanticToken(token, SemanticToken.Semantic.IF);
}

 . { return null; }