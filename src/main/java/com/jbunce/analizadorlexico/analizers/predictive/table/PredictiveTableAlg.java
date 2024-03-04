package com.jbunce.analizadorlexico.analizers.predictive.table;

import com.jbunce.analizadorlexico.analizers.Tokens;
import com.jbunce.analizadorlexico.analizers.lexical.Lexer;
import javafx.application.Platform;
import javafx.scene.control.TableColumnBase;
import javafx.scene.text.TextFlow;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class PredictiveTableAlg {

    public static Production INITIAL_PRODUCTION = new Production("S");

    public HashMap<String, HashMap<Tokens, Production[]>> getTable() {

        // TERMINAL PRODUCTIONS

        Production P1 = new Production(Tokens.OPEN_PARENTHESIS);
        P1.setName("P1");
        P1.setTerminal(Boolean.TRUE);

        Production P2 = new Production(Tokens.CLOSE_PARENTHESIS);
        P2.setName("P2");
        P2.setTerminal(Boolean.TRUE);

        Production L = new Production(Tokens.NON_SPECIFIC);
        L.setName("L");
        L.setTerminal(Boolean.TRUE);

        Production Quote = new Production(Tokens.QUOTE);
        Quote.setName("Quote");
        Quote.setTerminal(Boolean.TRUE);

        Production B = new Production(Tokens.BOOLEAN);
        B.setName("B");
        B.setTerminal(Boolean.TRUE);

        Production OpenBrace = new Production(Tokens.OPEN_BRACE);
        OpenBrace.setName("OpenBrace");
        OpenBrace.setTerminal(Boolean.TRUE);

        Production CloseBrace = new Production(Tokens.CLOSE_BRACE);
        CloseBrace.setName("closeBrace");
        CloseBrace.setTerminal(Boolean.TRUE);

        Production Equal = new Production(Tokens.EQUALS);
        Equal.setName("Equal");
        Equal.setTerminal(Boolean.TRUE);

        Production SC = new Production(Tokens.SEMICOLON);
        SC.setName("SC");
        SC.setTerminal(Boolean.TRUE);

        Production Comma = new Production(Tokens.COMMA);
        Comma.setName("Comma");
        Comma.setTerminal(Boolean.TRUE);

        Production D = new Production(Tokens.NUMBER);
        D.setName("Number");
        D.setTerminal(Boolean.TRUE);

        Production Type = new Production(Tokens.TYPE);
        Type.setName("Type");
        Type.setTerminal(Boolean.TRUE);

        Production T = new Production(Tokens.TO);
        T.setName("TO");
        T.setTerminal(Boolean.TRUE);

        Production F = new Production(Tokens.FOR);
        F.setName("FOR");
        F.setTerminal(Boolean.TRUE);

        Production E = new Production(Tokens.ELSE);
        E.setName("ELSE");
        E.setTerminal(Boolean.TRUE);

        Production I = new Production(Tokens.IF);
        I.setName("IF");
        I.setTerminal(Boolean.TRUE);

        Production Les = new Production(Tokens.LESS_THAN);
        Les.setName("Les");
        Les.setTerminal(Boolean.TRUE);

        Production May = new Production(Tokens.GREATER_THAN);
        May.setName("May");
        May.setTerminal(Boolean.TRUE);

        Production O = new Production(Tokens.OPERATORS);
        O.setName("O");
        O.setTerminal(Boolean.TRUE);

        Production OL = new Production(Tokens.LOGIC_OP);
        OL.setName("OL");
        OL.setTerminal(Boolean.TRUE);

        Production V = new Production(Tokens.VOID_VALUE);
        V.setName("V");
        V.setTerminal(Boolean.TRUE);

        //NON TERMINAL PRODUCTIONS

        Production Braces = new Production();
        Braces.setName("Braces");
        Braces.setTerminal(Boolean.FALSE);

        Production P = new Production();
        P.setName("P");
        P.setTerminal(Boolean.FALSE);

        Production TypeV = new Production();
        TypeV.setName("TypeV");
        TypeV.setTerminal(Boolean.FALSE);

        Production OP = new Production();
        OP.setName("OP");
        OP.setTerminal(Boolean.FALSE);

        Production N = new Production();
        N.setName("N");
        N.setTerminal(Boolean.FALSE);

        Production Value = new Production();
        Value.setName("Value");
        Value.setTerminal(Boolean.FALSE);

        Production TLM = new Production();
        TLM.setName("TLM");
        TLM.setTerminal(Boolean.FALSE);

        Production NSC = new Production();
        NSC.setName("NSC");
        NSC.setTerminal(Boolean.FALSE);

        Production TFN = new Production();
        TFN.setName("TFN");
        TFN.setTerminal(Boolean.FALSE);

        Production EV = new Production();
        EV.setName("EV");
        EV.setTerminal(Boolean.FALSE);

        Production MAYK = new Production();
        MAYK.setName("MAYK");
        MAYK.setTerminal(Boolean.FALSE);

        Production PK = new Production();
        PK.setName("PK");
        PK.setTerminal(Boolean.FALSE);

        Production ForParams = new Production();
        ForParams.setName("ForParams");
        ForParams.setTerminal(Boolean.FALSE);

        Production PR = new Production();
        PR.setName("PR");
        PR.setTerminal(Boolean.FALSE);

        Production FR = new Production();
        FR.setName("FR");
        FR.setTerminal(Boolean.FALSE);

        Production PFSC = new Production();
        PFSC.setName("PFSC");
        PFSC.setTerminal(Boolean.FALSE);

        Production AF = new Production();
        AF.setName("AF");
        AF.setTerminal(Boolean.FALSE);

        Production PAF = new Production();
        PAF.setName("PAF");
        PAF.setTerminal(Boolean.FALSE);

        Production PSC = new Production();
        PSC.setName("PSC");
        PSC.setTerminal(Boolean.FALSE);

        Production FUNCALL = new Production();
        FUNCALL.setName("FUNCALL");
        FUNCALL.setTerminal(Boolean.FALSE);

        Production FUNTYPE = new Production();
        FUNTYPE.setName("FUNTYPE");
        FUNTYPE.setTerminal(Boolean.FALSE);

        Production FUNPARAMS = new Production();
        FUNPARAMS.setName("FUNPARAMS");
        FUNPARAMS.setTerminal(Boolean.FALSE);

        Production FP = new Production();
        FP.setName("FP");
        FP.setTerminal(Boolean.FALSE);

        Production FPR = new Production();
        FPR.setName("FPR");
        FPR.setTerminal(Boolean.FALSE);

        Production FPI = new Production();
        FPI.setName("FPI");
        FPI.setTerminal(Boolean.FALSE);

        Production ELSE = new Production();
        ELSE.setName("ELSE");
        ELSE.setTerminal(Boolean.FALSE);

        Production PT = new Production();
        PT.setName("PT");
        PT.setTerminal(Boolean.FALSE);

        Production IFP = new Production();
        IFP.setName("IFP");
        IFP.setTerminal(Boolean.FALSE);

        Production IFPI = new Production();
        IFPI.setName("IFPI");
        IFPI.setTerminal(Boolean.FALSE);

        Production IFR = new Production();
        IFR.setName("IFR");
        IFR.setTerminal(Boolean.FALSE);

        Production FRST = new Production();
        FRST.setName("FRST");
        FRST.setTerminal(Boolean.FALSE);

        Production ISVAR = new Production();
        ISVAR.setName("ISVAR");
        ISVAR.setTerminal(Boolean.FALSE);

        // PREDICTIVE TABLE

        HashMap<String, HashMap<Tokens, Production[]>> table = new HashMap<>();

        table.put("FRST", new HashMap<>());
        table.get("FRST").put(Tokens.NON_SPECIFIC, new Production[]{FUNCALL});
        table.get("FRST").put(Tokens.OPEN_PARENTHESIS, new Production[]{PR, PK});

        table.put("ISVAR", new HashMap<>());
        table.get("ISVAR").put(Tokens.GREATER_THAN, new Production[]{TFN, SC});
        table.get("ISVAR").put(Tokens.LESS_THAN, new Production[]{FPI, FPR});

        table.put("Braces", new HashMap<>());
        table.get("Braces").put(Tokens.OPEN_BRACE, new Production[]{OpenBrace, CloseBrace});

        table.put("P", new HashMap<>());
        table.get("P").put(Tokens.OPEN_PARENTHESIS, new Production[]{P1, P2});

        table.put("N", new HashMap<>());
        table.get("N").put(Tokens.NON_SPECIFIC, new Production[]{L, N});
        table.get("N").put(Tokens.NUMBER, new Production[]{D, N});
        table.get("N").put(Tokens.QUOTE, new Production[]{});
        table.get("N").put(Tokens.OPEN_PARENTHESIS, new Production[]{});
        table.get("N").put(Tokens.CLOSE_PARENTHESIS, new Production[]{});
        table.get("N").put(Tokens.SEMICOLON, new Production[]{});
        table.get("N").put(Tokens.EQUALS, new Production[]{EV});
        table.get("N").put(Tokens.TO, new Production[]{});
        table.get("N").put(Tokens.COMMA, new Production[]{});
        table.get("N").put(Tokens.OPERATORS, new Production[]{});

        table.put("OP", new HashMap<>());
        table.get("OP").put(Tokens.OPERATORS, new Production[]{O});
        table.get("OP").put(Tokens.GREATER_THAN, new Production[]{May});
        table.get("OP").put(Tokens.LESS_THAN, new Production[]{Les});

        table.put("Value", new HashMap<>());
        table.get("Value").put(Tokens.QUOTE, new Production[]{Quote, N, Quote});
        table.get("Value").put(Tokens.NUMBER, new Production[]{D});
        table.get("Value").put(Tokens.BOOLEAN, new Production[]{B});
        table.get("Value").put(Tokens.NON_SPECIFIC, new Production[]{N});

        table.put("TLM", new HashMap<>());
        table.get("TLM").put(Tokens.GREATER_THAN, new Production[]{May});

        table.put("NSC", new HashMap<>());
        table.get("NSC").put(Tokens.NON_SPECIFIC, new Production[]{N, SC});

        table.put("TFN", new HashMap<>());
        table.get("TFN").put(Tokens.GREATER_THAN, new Production[]{TLM, N});

        table.put("EV", new HashMap<>());
        table.get("EV").put(Tokens.EQUALS, new Production[]{Equal, Value});

        table.put("MAYK", new HashMap<>());
        table.get("MAYK").put(Tokens.GREATER_THAN, new Production[]{May, Braces});

        table.put("PK", new HashMap<>());
        table.get("PK").put(Tokens.CLOSE_PARENTHESIS, new Production[]{P2, MAYK});

        table.put("ForParams", new HashMap<>());
        table.get("ForParams").put(Tokens.NON_SPECIFIC, new Production[]{N, T, N});
        table.get("ForParams").put(Tokens.NUMBER, new Production[]{N, T, N});
        table.get("ForParams").put(Tokens.QUOTE, new Production[]{N, T, N});

        table.put("PR", new HashMap<>());
        table.get("PR").put(Tokens.OPEN_PARENTHESIS, new Production[]{P1, ForParams});

        table.put("FR", new HashMap<>());
        table.get("FR").put(Tokens.FOR, new Production[]{F, FRST});

        table.put("PFSC", new HashMap<>());
        table.get("PFSC").put(Tokens.CLOSE_PARENTHESIS, new Production[]{P2, SC});

        table.put("AF", new HashMap<>());
        table.get("AF").put(Tokens.NUMBER, new Production[]{Value, Comma, AF});
        table.get("AF").put(Tokens.BOOLEAN, new Production[]{Value, Comma, AF});
        table.get("AF").put(Tokens.QUOTE, new Production[]{Value, Comma, AF});
        table.get("AF").put(Tokens.CLOSE_PARENTHESIS, new Production[]{});

        table.put("PAF", new HashMap<>());
        table.get("PAF").put(Tokens.OPEN_PARENTHESIS, new Production[]{P1, AF, PFSC});

        table.put("PSC", new HashMap<>());
        table.get("PSC").put(Tokens.OPEN_PARENTHESIS, new Production[]{P, SC});

        table.put("FUNCALL", new HashMap<>());
        table.get("FUNCALL").put(Tokens.NON_SPECIFIC, new Production[]{N, PAF});

        table.put("FUNTYPE", new HashMap<>());
        table.get("FUNTYPE").put(Tokens.LESS_THAN, new Production[]{Les});
        table.get("FUNTYPE").put(Tokens.VOID_VALUE, new Production[]{V, Les});

        table.put("FUNPARAMS", new HashMap<>());
        table.get("FUNPARAMS").put(Tokens.TYPE, new Production[]{Type, TFN, FUNPARAMS});
        table.get("FUNPARAMS").put(Tokens.COMMA, new Production[]{Type, TFN, FUNPARAMS});
        table.get("FUNPARAMS").put(Tokens.CLOSE_PARENTHESIS, new Production[]{});

        table.put("FP", new HashMap<>());
        table.get("FP").put(Tokens.OPEN_PARENTHESIS, new Production[]{P1, FUNPARAMS});

        table.put("FPR", new HashMap<>());
        table.get("FPR").put(Tokens.OPEN_PARENTHESIS, new Production[]{FP, P2, Braces});

        table.put("FPI", new HashMap<>());
        table.get("FPI").put(Tokens.LESS_THAN, new Production[]{FUNTYPE, N});
        table.get("FPI").put(Tokens.VOID_VALUE, new Production[]{FUNTYPE, N});

        table.put("ELSE", new HashMap<>());
        table.get("ELSE").put(Tokens.ELSE, new Production[]{E, MAYK});

        table.put("PT", new HashMap<>());
        table.get("PT").put(Tokens.NON_SPECIFIC, new Production[]{Value, OP, Value});
        table.get("PT").put(Tokens.NUMBER, new Production[]{Value, OP, Value});
        table.get("PT").put(Tokens.QUOTE, new Production[]{Value, OP, Value});
        table.get("PT").put(Tokens.BOOLEAN, new Production[]{Value, OP, Value});

        table.put("IFP", new HashMap<>());
        table.get("IFP").put(Tokens.NON_SPECIFIC, new Production[]{PT});
        table.get("IFP").put(Tokens.NUMBER, new Production[]{PT});
        table.get("IFP").put(Tokens.BOOLEAN, new Production[]{PT});
        table.get("IFP").put(Tokens.QUOTE, new Production[]{PT});

        table.put("IFPI", new HashMap<>());
        table.get("IFPI").put(Tokens.OPEN_PARENTHESIS, new Production[]{P1, IFP, P2});

        table.put("IFR", new HashMap<>());
        table.get("IFR").put(Tokens.IF, new Production[]{I, IFPI, MAYK, ELSE});

        table.put("S", new HashMap<>());
        table.get("S").put(Tokens.IF, new Production[]{IFR});
        table.get("S").put(Tokens.FOR, new Production[]{FR});
        table.get("S").put(Tokens.TYPE, new Production[]{Type, ISVAR});
        table.get("S").put(Tokens.VOID_VALUE, new Production[]{FPI, FPR});
        table.get("S").put(Tokens.NON_SPECIFIC, new Production[]{N, SC});

        return table;
    }

    public void parse(LexerTable lexer, AtomicReference<String> result, TextFlow logArea) {
        HashMap<String, HashMap<Tokens, Production[]>> table = getTable();

        Production currentProduction;
        Stack<Production> stack = new Stack<>();
        stack.push(PredictiveTableAlg.INITIAL_PRODUCTION);

        boolean stop = true;

        while (stop) {
            try {
                Tokens token = lexer.yylex();
                if (token == null) {
                    String res = result.get() + "EOF";
                    result.set(res);
                    break;
                }

                if (token == Tokens.ERROR) {
                    Platform.runLater(() -> {
                        logArea.getChildren().add(new javafx.scene.text.Text("Unexpected token: " + token + "\n"));
                    });
                    stop = false;
                    break;
                }

                if (stack.isEmpty()) {
                    Platform.runLater(() -> {
                        logArea.getChildren().add(new javafx.scene.text.Text("Unexpected token: " + token + "\n"));
                    });
                    stop = false;
                    break;
                }

                currentProduction = stack.pop();

                while (true) {
                    if (currentProduction.isTerminal()) {
                        if (currentProduction.getSymbol() == token) {
                            result.set(result.get() + "Token: " + token + "\n");
                            break;
                        } else {
                            Platform.runLater(() -> {
                                logArea.getChildren().add(new javafx.scene.text.Text("Unexpected token: " + token + "\n"));
                            });
                            stop = false;
                            break;
                        }
                    } else {
                        Production[] productions = table.get(currentProduction.getName()).get(token);

                        if (productions == null) {
                            Production finalCurrentProduction = currentProduction;

                            Platform.runLater(() -> {
                                Tokens expectedToken = table
                                        .get(finalCurrentProduction.getName())
                                        .keySet().stream().findFirst().orElse(null);

                                logArea.getChildren()
                                        .add(new javafx.scene.text.Text("Expected token: " + expectedToken + "\n"));

                                logArea.getChildren()
                                        .add(new javafx.scene.text.Text("Unexpected token: " + token + "\n"));
                            });
                            stop = false;
                            break;
                        }

                        for (int i = productions.length - 1; i >= 0; i--) {
                            stack.push(productions[i]);
                        }

                        Production prod = stack.pop();

                        if (prod.isTerminal()) {
                            if (prod.getSymbol() == token) {
                                result.set(result.get() + "Token: " + token + "\n");
                                break;
                            } else {
                                Platform.runLater(() -> {
                                    logArea.getChildren().add(new javafx.scene.text.Text("Unexpected token: " + token));
                                });
                                stop = false;
                                break;
                            }
                        } else {
                            currentProduction = prod;
                        }
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
