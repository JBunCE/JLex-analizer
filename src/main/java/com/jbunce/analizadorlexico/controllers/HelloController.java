package com.jbunce.analizadorlexico.controllers;

import ch.qos.logback.classic.Logger;
import com.jbunce.analizadorlexico.analizers.lexical.Analyzer;
import com.jbunce.analizadorlexico.analizers.lexical.Lexer;
import com.jbunce.analizadorlexico.analizers.predictive.table.LexerTable;
import com.jbunce.analizadorlexico.analizers.predictive.table.PredictiveTableAlg;
import com.jbunce.analizadorlexico.analizers.semantic.SemanticAnalyzer;
import com.jbunce.analizadorlexico.analizers.semantic.SemanticLexer;
import com.jbunce.analizadorlexico.analizers.semantic.SemanticToken;
import com.jbunce.analizadorlexico.logger.Console;
import com.jbunce.analizadorlexico.logger.TextFlowAppender;
import com.jbunce.analizadorlexico.utils.AlertFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import lombok.extern.slf4j.Slf4j;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntFunction;

@Slf4j
public class HelloController implements Initializable {

    @FXML private CodeArea codeArea;
    @FXML private VirtualizedScrollPane<CodeArea> codeScrollPane;
    @FXML private TextFlow logArea;
    @FXML private TextFlow infoArea;
    @FXML private Label codet;
    @FXML private VBox filesVbox;
    @FXML private TextField fileField;

    private String folderPath;
    private String selectedFile;

    private final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String lexerPath = "src/main/java/com/jbunce/analizadorlexico/analizers/lexical/Lexer.flex";
        String tableLexerPath = "src/main/java/com/jbunce/analizadorlexico/analizers/predictive/table/PredictiveTableLexer.flex";
        String semanticLexerPath = "src/main/java/com/jbunce/analizadorlexico/analizers/semantic/SemanticLexer.flex";

        try {
            generateAnalyzer(lexerPath, tableLexerPath, semanticLexerPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAndStopAllAppenders();

        TextFlowAppender appender = new TextFlowAppender(logArea);
        appender.start();
        logger.addAppender(appender);

        codeArea.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        codeScrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Platform.runLater(() -> {
            IntFunction<Node> lineNumberFactory = LineNumberFactory.get(codeArea);
            codeArea.setParagraphGraphicFactory(lineNumberFactory);
        });

        folderPath = System.getProperty("user.home") + "/Desktop";

        System.setOut(createLoggingProxy(System.out));
        System.setErr(createLoggingProxy(System.err));

        System.out.println("This will be logged!");
    }

    @FXML
    protected void build() throws FileNotFoundException {
        Reader semanticLector = new BufferedReader(new FileReader(folderPath + "/" + selectedFile));
        SemanticLexer semanticLexer = new SemanticLexer(semanticLector);
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

        // Crear un flujo de salida para capturar la salida de la consola
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        // Guardar el flujo de salida original
        PrintStream old = System.out;

        // Redirigir la salida estándar al flujo de salida personalizado
        System.setOut(ps);

        EXECUTOR.execute(() -> {
            System.setOut(createLoggingProxy(System.out));
            System.setErr(createLoggingProxy(System.err));
            while (true) {
                try {
                    SemanticToken token = semanticLexer.yylex();

                    if (token != null) {
                        semanticAnalyzer.parse(token);
                    } else {
                        break;
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try (Context context = Context.create()) {
                Value value = context.eval(Source.newBuilder("js", new File("src/main/java/com/jbunce/analizadorlexico/analizers/semantic/main.js")).build());
                // Restaurar la salida estándar original
                System.out.flush();
                System.setOut(old);

                // Convertir la salida de la consola capturada a una cadena
                String output = baos.toString();

                // Imprimir la salida de la consola capturada
                log.info("> " + output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    protected void onCodeAreaChange() throws FileNotFoundException {
        Platform.runLater(() -> logArea.getChildren().clear());
        String content = codeArea.getText();
        Path path = Paths.get(folderPath + "/" + selectedFile);
        try {
            Files.writeString(path, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Reader lector = new BufferedReader(new FileReader(folderPath + "/" + selectedFile));
        Lexer lexer = new Lexer(lector);

        Reader tableLector = new BufferedReader(new FileReader(folderPath + "/" + selectedFile));
        LexerTable lexer2 = new LexerTable(tableLector);
        AtomicReference<String> result = new AtomicReference<>("");
        PredictiveTableAlg predictiveTableAlg = new PredictiveTableAlg();

        EXECUTOR.execute(() -> {
            Analyzer.analize(infoArea, lexer);
            predictiveTableAlg.parse(lexer2, result, logArea);
        });
    }

    @FXML
    protected void openFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File directory = directoryChooser.showDialog(null);

        if (directory != null) {
            folderPath = directory.getAbsolutePath();
            System.out.println(folderPath);
        } else {
            AlertFactory.makeAlert("Error", "No file selected", Alert.AlertType.ERROR);
            return;
        }

        File dir = new File(folderPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                Label label = getLabel(child);
                Platform.runLater(() -> filesVbox.getChildren().add(label));
            }
        }
    }

    @FXML
    protected void newFile(KeyEvent event) {

        if (!event.getCode().name().equals("ENTER")) {
            return;
        }

        String filename = fileField.getText();

        if (filename.isBlank()) {
            AlertFactory.makeAlert("Error", "The file name is empty", Alert.AlertType.ERROR);
            return;
        }

        File file = new File(folderPath + "/" + filename);
        try {
            file.createNewFile();
            Label label = getLabel(file);
            filesVbox.getChildren().add(label);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fileField.setText("");
        fileField.setVisible(false);
    }

    @FXML
    protected void setInput() {
        fileField.setVisible(true);
    }

    private Label getLabel (File child) {
        Label label = new Label(child.getName());
        label.setTextFill(Paint.valueOf("#ffffff"));
        label.setStyle("-fx-max-width: infinity");
        label.setOnMouseClicked(event -> {
            try {
                String content = Files.readString(child.toPath());
                Platform.runLater(() -> {
                    codet.setText(child.getName());
                    codeArea.replaceText(content);
                    filesVbox.getChildren().forEach(labelChild -> {
                        Label labelElement = (Label) labelChild;
                        selectedFile = labelElement.getText();
                        labelElement.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                    });
                    label.setBackground(new Background(new BackgroundFill(Color.valueOf("#1f1f1f"), CornerRadii.EMPTY, Insets.EMPTY)));
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return label;
    }

    private void generateAnalyzer(String lexerPath, String tableLexerPath, String semantic)throws Exception {
        jflex.Main.generate(new String[]{lexerPath});
        jflex.Main.generate(new String[]{tableLexerPath});
        jflex.Main.generate(new String[]{semantic});
    }

    private static PrintStream createLoggingProxy(final PrintStream realPrintStream) {
        return new PrintStream(realPrintStream) {
            public void print(final String string) {
                realPrintStream.print(string);
                log.info(string);
            }
        };
    }
}