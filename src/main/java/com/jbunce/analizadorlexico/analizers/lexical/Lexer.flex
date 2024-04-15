package com.jbunce.analizadorlexico.analizers.lexical;
import com.jbunce.analizadorlexico.analizers.Tokens;

%%
%public
%class Lexer
%type Tokens

L=[a-zA-Z]
D=[0-9]+
space=[\t \r \n]+
console=(console\.log{openP}"hello"{closeP})

%{
    public String lexeme;
%}

%%

for  { lexeme = yytext(); return Tokens.FOR; }
if  { lexeme = yytext(); return Tokens.IF; }
else  { lexeme = yytext(); return Tokens.ELSE; }
to { lexeme = yytext(); return Tokens.TO; }

{space} { /* ignore */ }
"//".* { /* ignore */ }

"true" | "false" { lexeme = yytext(); return Tokens.BOOLEAN; }
"int" | "float" | "string" | "bool" { lexeme = yytext(); return Tokens.TYPE; }
"==" | "<=" | ">=" | "!=" { lexeme = yytext(); return Tokens.OPERATORS; }
"||" | "&&" { lexeme = yytext(); return Tokens.LOGIC_OP; }
"void" { lexeme = yytext(); return Tokens.VOID_VALUE; }
"=" { lexeme = yytext(); return Tokens.EQUALS; }
"<" { lexeme = yytext(); return Tokens.LESS_THAN; }
">" { lexeme = yytext(); return Tokens.GREATER_THAN; }
"{" { lexeme = yytext(); return Tokens.OPEN_BRACE; }
"}" { lexeme = yytext(); return Tokens.CLOSE_BRACE; }
"(" { lexeme = yytext(); return Tokens.OPEN_PARENTHESIS; }
")" { lexeme = yytext(); return Tokens.CLOSE_PARENTHESIS; }
";" { lexeme = yytext(); return Tokens.SEMICOLON; }
"," { lexeme = yytext(); return Tokens.COMMA; }
"\"" | "'" { lexeme = yytext(); return Tokens.QUOTE; }
{D} { lexeme = yytext(); return Tokens.NUMBER; }
{L} { lexeme = yytext(); return Tokens.NON_SPECIFIC; }
 . { return Tokens.ERROR; }
