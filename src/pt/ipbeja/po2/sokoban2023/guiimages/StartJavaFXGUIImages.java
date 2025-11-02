package pt.ipbeja.po2.sokoban2023.guiimages;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.ipbeja.po2.sokoban2023.model.Level;
import pt.ipbeja.po2.sokoban2023.model.Position;
import pt.ipbeja.po2.sokoban2023.model.SokobanGameModel;

import java.util.Optional;
import java.util.Set;


/**
 * Sokoban™ game start
 * Based on: https://en.wikipedia.org/wiki/Sokoban
 * Sokoban Images: https://www.kenney.nl/assets/sokoban
 *
 * @author Martinho Caeiro (23917) & Paulo Abade (23919)
 * @version Sokoban™ Final Edition
 */
public class StartJavaFXGUIImages extends Application {
    private static final int PANE_SIZE_X = 1380;
    private static final int PANE_SIZE_Y = 750;
    private SokobanBoardImages sokobanBoardImages;
    private String playerName;

    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Starts the Sokoban™ main menu
     *
     * @param primaryStage Primary stage
     */
    @Override
    public void start(Stage primaryStage) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType buttonTypeTut = new ButtonType("Tutorial");
        ButtonType buttonTypeSLevel = new ButtonType("Selecionar Nivel");
        ButtonType buttonTypeExit = new ButtonType("Sair");
        alert.getButtonTypes().setAll(buttonTypeTut, buttonTypeSLevel, buttonTypeExit);

        alert.setTitle("Sokoban™");
        alert.setHeaderText("Bem vindo ao Sokoban™!");
        alert.setContentText("""
                Comandos:
                Cima -> W ou Seta para cima
                Baixo -> S ou Seta para baixo
                Esquerda -> D ou Seta para a esquerda
                Direita -> A ou Seta para a direita
                Reniciar -> R
                Pausa -> ESC""");

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeTut) {
                this.askName();
                this.tutorial();
            } else if (response == buttonTypeSLevel) {
                this.selectLevel(primaryStage);
            } else if (response == buttonTypeExit) {
                alert.close();
            }
        });
    }

    /**
     * Tutorial level for Sokoban™
     */
    public void tutorial() {
        final String boardContent =
                """
                        FFWWWWFF
                        FFWFFWFF
                        WWWFFWWW
                        WFFEEFFW
                        WFFFFFFW
                        WWWWWWWW""";

        Position keeperPosition = new Position(3, 5);

        Set<Position> boxesPositions =
                Set.of(new Position(3, 2),
                        new Position(3, 3));

        SokobanGameModel sokoban = new SokobanGameModel(
                new Level(keeperPosition, boxesPositions, boardContent), playerName);
        Stage stage = new Stage();
        BorderPane borderPane = new BorderPane();

        stage.setWidth(PANE_SIZE_X);
        stage.setHeight(PANE_SIZE_Y);
        stage.setMaximized(true);
        stage.setTitle("Sokoban™");

        sokobanBoardImages =
                new SokobanBoardImages(sokoban,
                        stage, "keeper.png",
                        "box.png",
                        "boxend.png",
                        "wall.png",
                        "free.png",
                        "end.png");

        borderPane.setCenter(sokobanBoardImages);
        borderPane.setLeft(sokobanBoardImages.getTimeLabel());
        borderPane.setTop(sokobanBoardImages.setMenuBar());

        borderPane.setRight(sokobanBoardImages.buildHBox());
        stage.setScene(new Scene(borderPane));

        sokoban.registerView(sokobanBoardImages);
        sokobanBoardImages.requestFocus();
        stage.show();
    }

    /**
     * Creates level from a file
     *
     * @param primaryStage Primary stage
     */
    private void selectLevel(Stage primaryStage) {
        Level level = Level.receivePath(primaryStage);
        this.askName();
        SokobanGameModel sokobanGameModel = new SokobanGameModel(level, playerName);
        sokobanBoardImages =
                new SokobanBoardImages(sokobanGameModel,
                        primaryStage,
                        "keeper.png",
                        "box.png",
                        "boxend.png",
                        "wall.png",
                        "free.png",
                        "end.png");
        BorderPane borderPane = new BorderPane();

        primaryStage.setWidth(PANE_SIZE_X);
        primaryStage.setHeight(PANE_SIZE_Y);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Sokoban™");

        borderPane.setCenter(sokobanBoardImages);
        borderPane.setLeft(sokobanBoardImages.getTimeLabel());
        borderPane.setTop(sokobanBoardImages.setMenuBar());
        borderPane.setRight(sokobanBoardImages.buildHBox());
        primaryStage.setScene(new Scene(borderPane));

        sokobanGameModel.registerView(sokobanBoardImages);
        sokobanBoardImages.requestFocus();

        primaryStage.show();
    }

    /**
     * Asks user for a 3 letter name
     */
    private void askName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Bem vindo");
        dialog.setHeaderText("Insira o seu nome(Max 3 letras)");
        dialog.setContentText("Por favor insira o seu nome:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> this.playerName = s);
    }
}