package pt.ipbeja.po2.sokoban2023.guiimages;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import pt.ipbeja.po2.sokoban2023.images.ImageType;
import pt.ipbeja.po2.sokoban2023.model.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static javafx.application.Platform.exit;

/**
 * Sokoban™ game design template
 *
 * @author Martinho Caeiro (23917) & Paulo Abade (23919)
 * @version Sokoban™ Final Edition
 */
public class SokobanBoardImages extends GridPane implements SokobanView {
    final SokobanGameModel sokoban;
    final Map<ImageType, Image> imageTypeToImage;
    private static final int SQUARE_SIZE_MIN = 64;
    private static final int SQUARE_SIZE_MAX = 64;
    private final Stage stage;
    private String boxMoves = "";
    private static final TextArea textArea = new TextArea();
    private int nMoves = 0;
    private Timeline timeline;
    private LocalTime localTime = LocalTime.parse("00:00");
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
    private final Label timerLabel = new Label();
    private static final int PANE_SIZE_X = 1380;
    private static final int PANE_SIZE_Y = 750;

    /**
     * Creates a sokoban board with labels and handle keystrokes
     *
     * @param sokoban             Game logic
     * @param primaryStage        Initial stage
     * @param keeperImageFilename Image for the keeper
     * @param boxImageFilename    Image for the box
     * @param boxEndImageFilename Image for the box end
     * @param wallImageFilename   Image for the wall
     * @param freeImageFilename   Image for the floor
     * @param endImageFilename    Image for the end position
     */
    public SokobanBoardImages(SokobanGameModel sokoban,
                              Stage primaryStage, String keeperImageFilename,
                              String boxImageFilename,
                              String boxEndImageFilename,
                              String wallImageFilename,
                              String freeImageFilename,
                              String endImageFilename) {

        this.imageTypeToImage =
                Map.of(ImageType.KEEPER, new Image("images/" + keeperImageFilename),
                        ImageType.BOX, new Image("images/" + boxImageFilename),
                        ImageType.BOXEND, new Image("images/" + boxEndImageFilename),
                        ImageType.WALL, new Image("images/" + wallImageFilename),
                        ImageType.END, new Image("images/" + endImageFilename),
                        ImageType.FREE, new Image("images/" + freeImageFilename));

        this.sokoban = sokoban;
        this.stage = primaryStage;
        this.sokoban.registerView(this);
        this.timerLabel.setText("00:00");
        this.buildGUI();
        this.setOnKeyPressed(event ->
        {
            final Map<KeyCode, Direction> keyToDir = Map.of(
                    KeyCode.UP, Direction.UP,
                    KeyCode.DOWN, Direction.DOWN,
                    KeyCode.LEFT, Direction.LEFT,
                    KeyCode.RIGHT, Direction.RIGHT,
                    KeyCode.W, Direction.UP,
                    KeyCode.S, Direction.DOWN,
                    KeyCode.A, Direction.LEFT,
                    KeyCode.D, Direction.RIGHT);
            Direction direction = keyToDir.get(event.getCode());
            if (direction != null && !sokoban.moveKeeper(direction)) {
                SokobanBoardImages.this.couldNotMove();
            }
            if (event.getCode() == KeyCode.R) {
                this.resetGame();
                SokobanBoardImages.this.requestFocus();
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                this.menuGame();
            }
        });
    }

    /**
     * Builds the graphical interface
     */
    private void buildGUI() {
        assert (this.sokoban != null);

        this.createTopCol();
        this.createSideLine();

        for (int line = 1; line <= this.sokoban.getNLines(); line++) {
            for (int col = 1; col <= this.sokoban.getNCols(); col++) {
                Label label = new Label();
                label.setMinWidth(SQUARE_SIZE_MIN);
                label.setMinHeight(SQUARE_SIZE_MIN);
                label.setMaxWidth(SQUARE_SIZE_MAX);
                label.setMaxHeight(SQUARE_SIZE_MAX);
                ImageType imgType = this.sokoban.imageForPosition(new Position(line - 1, col - 1));
                label.setGraphic(this.createImageView(this.imageTypeToImage.get(imgType)));
                this.add(label, col, line);
            }
        }
        this.requestFocus();
    }

    /**
     * Updates the interface after every movement
     */
    @Override
    public void update(MessageToUI messageToUI) {
        this.buildGUI();
        this.nMoves++;
        if (this.nMoves == 1) this.timeline.play();
        this.boxMoves += letterComing(messageToUI.positions().get(1)) + " -> "
                + letterComing(messageToUI.positions().get(0)) + "\n";
        this.textArea.setText(this.boxMoves);
        this.textArea.setScrollTop(Double.MAX_VALUE);
        this.scoreMenu();
    }

    /**
     * Creates the score menu
     */
    private void scoreMenu() {
        if (this.sokoban.allBoxesAreStored()) {
            this.timeline.pause();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Nível Terminado!");
            alert.setTitle("Parabéns!");
            alert.setHeaderText("Parabéns!");
            alert.showAndWait();
            this.scoreFile();
            this.newGame();
        }
    }

    /**
     * Creates a score file at the end of a level
     */
    private void scoreFile() {
        String score = "score.txt";
        File file = new File(score);
        if (!file.exists()) {
            try {
                file.createNewFile();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            List<String> readInfo = Files.readAllLines(file.toPath());
            String scoreInfo = "Player Name: " + sokoban.getPlayerName() + "\n" + "Number of moves: " + this.nMoves +
                    "\n" + "Time to complete: " + this.localTime.format(dtf) + "\n" + "\n";
            readInfo.add(scoreInfo);
            Files.write(file.toPath(), readInfo);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a pause menu with exit
     */
    private void menuGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        this.timeline.pause();

        ButtonType buttonTypeYes = new ButtonType("Sim");
        ButtonType buttonTypeNo = new ButtonType("Não");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.setTitle("Sokoban™");
        alert.setHeaderText("Está a sair do Sokoban™!");
        alert.setContentText("Ao sair irá perder o seu progresso, deseja continuar?");

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                exit();
            } else if (response == buttonTypeNo) {
                alert.close();
                this.timeline.play();
            }
        });
    }

    /**
     * Creates a new game menu
     */
    private void newGame() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        ButtonType buttonTypeYes = new ButtonType("Sim");
        ButtonType buttonTypeNo = new ButtonType("Não");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.setTitle("Sokoban™");
        alert.setHeaderText("Acabou o Sokoban™!");
        alert.setContentText("Quer selecionar outro nivel?");

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeYes) {
                this.chooseLevel();
                this.resetGame();
            } else if (response == buttonTypeNo) {
                this.resetGame();
            }
        });
    }

    /**
     * Resets the game
     */
    private void resetGame() {
        this.timerLabel.setText("00:00");
        this.sokoban.resetLevel();
        this.buildGUI();
        this.nMoves = 0;
        this.textArea.setText("");
        this.boxMoves = "";
        this.localTime = LocalTime.parse("00:00");
        this.timeline.pause();
    }

    /**
     * Allocates a new ImageView object using the image
     *
     * @param image Image for object
     * @return Image as object
     */
    private Node createImageView(Image image) {
        return new ImageView(image);
    }

    /**
     * Makes a sound if keeper cannot move
     */
    private void couldNotMove() {
        Toolkit.getDefaultToolkit().beep();
    }

    /**
     * Creates area to display keeper moves
     */
    public HBox buildHBox() {
        this.textArea.setEditable(false);
        this.textArea.setMouseTransparent(true);
        this.textArea.setFocusTraversable(false);
        HBox hBox = new HBox();
        hBox.getChildren().add(textArea);
        return hBox;
    }

    /**
     * Creates a timer
     *
     * @return Hbox with timer on the left
     */
    public HBox getTimeLabel() {
        this.createTime();
        HBox hBox = new HBox();
        this.timerLabel.setFont(new Font("", 50));
        hBox.getChildren().add(this.timerLabel);
        return hBox;
    }

    /**
     * Starts the time after the first keeper movement
     */
    private void createTime() {
        this.timeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> incrementTime()));
        this.timeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Creates level from a file
     */
    private void chooseLevel() {
        Level level = Level.receivePath(this.stage);
        SokobanGameModel sokobanGameModel = new SokobanGameModel(level, sokoban.getPlayerName());
        SokobanBoardImages sokobanBoardImages =
                new SokobanBoardImages(sokobanGameModel,
                        stage, "keeper.png",
                        "box.png",
                        "boxend.png",
                        "wall.png",
                        "free.png",
                        "end.png");
        BorderPane borderPane = new BorderPane();

        this.stage.setWidth(PANE_SIZE_X);
        this.stage.setHeight(PANE_SIZE_Y);
        this.stage.setTitle("Sokoban™");

        borderPane.setCenter(sokobanBoardImages);
        borderPane.setLeft(sokobanBoardImages.getTimeLabel());
        borderPane.setTop(setMenuBar());
        borderPane.setRight(sokobanBoardImages.buildHBox());
        this.stage.setScene(new Scene(borderPane));

        sokobanGameModel.registerView(sokobanBoardImages);
        sokobanBoardImages.requestFocus();
        this.stage.show();
    }

    /**
     * Makes timer increment
     */
    private void incrementTime() {
        this.localTime = localTime.plusSeconds(1);
        this.timerLabel.setText(this.localTime.format(dtf));
    }

    /**
     * Converts the column numbers to letters
     *
     * @param position Current position
     */
    private String letterComing(Position position) {
        int c = position.col();
        char coming = (char) (c + 65);
        return "(" + position.line() + "," + coming + ")";
    }

    /**
     * Create the letters at the top of each column to identify them.
     */
    private void createTopCol() {
        for (int i = 0; i < this.sokoban.getNCols(); i++) {
            char coming = (char) (i + 65);
            Label label = new Label("     " + coming);
            label.setFont(new Font("", 20));
            label.setMinWidth(SQUARE_SIZE_MIN);
            label.setMinHeight(SQUARE_SIZE_MIN);
            label.setMaxWidth(SQUARE_SIZE_MAX);
            label.setMaxHeight(SQUARE_SIZE_MAX);

            this.add(label, i + 1, 0);
        }
    }

    /**
     * Create the numbers at the side of each row to identify them.
     */
    private void createSideLine() {
        for (int i = 0; i < this.sokoban.getNLines(); i++) {

            Label label = new Label(String.valueOf(i));
            label.setFont(new Font("", 20));
            label.setMinWidth(SQUARE_SIZE_MIN);
            label.setMinHeight(SQUARE_SIZE_MIN);
            label.setMaxWidth(SQUARE_SIZE_MAX);
            label.setMaxHeight(SQUARE_SIZE_MAX);
            this.add(label, 0, i + 1);
        }
    }

    /**
     * Creates a menu bar
     */
    public MenuBar setMenuBar() {
        MenuItem restartButton = new MenuItem("Reiniciar o Jogo");
        restartButton.setOnAction(event -> {
            this.resetGame();
            SokobanBoardImages.this.requestFocus();
        });
        MenuItem chooseFile = new MenuItem("Escolher Nível");
        chooseFile.setOnAction(event -> {
            this.chooseLevel();
            this.resetGame();
        });
        MenuItem pauseButton = new MenuItem("Sair");
        pauseButton.setOnAction(event -> {
            this.menuGame();
        });
        Menu menu = new Menu("Opções", null, restartButton, chooseFile, pauseButton);

        return new MenuBar(menu);
    }
}