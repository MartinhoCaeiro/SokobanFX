package pt.ipbeja.po2.sokoban2023.model;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Level that is played on
 *
 * @param keeperPosition Initial keeper position
 * @param boxesPositions Initial box position
 * @param boardContent   Level layout
 * @author Martinho Caeiro (23917) & Paulo Abade (23919)
 * @version Sokoban™ Final Edition
 */
public record Level(Position keeperPosition, Set<Position> boxesPositions, String boardContent) {
    private static String path = "";

    /**
     * Level design template
     */
    public Level() {
        this(new Position(3, 5), Set.of(new Position(3, 2),
                new Position(3, 3)), """
                FFWWWWFF
                FFWFFWFF
                WWWFFWWW
                WFFEEFFW
                WFFFFFFW
                WWWWWWWW""");
    }

    /**
     * Gets the tutorial level
     *
     * @return Tutorial level
     */
    public static Level getTutorial() {
        return new Level();
    }

    /**
     * Gets the file path
     *
     * @return File path
     */
    public static String getPath() {
        return Level.path;
    }

    public static Level loadLevel(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        List<String> readLevel = Files.readAllLines(path);
        String boardContent = """
                """;

        String[] keeperS = readLevel.get(1).split(" ");
        int keeperLine = Integer.parseInt(keeperS[0]);
        int keeperCol = Integer.parseInt(keeperS[1]);
        int nBox = Integer.parseInt(readLevel.get(2));

        Set<Position> boxPosition = new HashSet<>();
        for (int i = 0; i < nBox; i++) {
            String[] boxS = readLevel.get(i + 3).split(" ");
            int boxLine = Integer.parseInt(boxS[0]);
            int boxCol = Integer.parseInt(boxS[1]);
            boxPosition.add(new Position(boxLine, boxCol));
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 3 + nBox; i < readLevel.size(); i++) {
            if (i != readLevel.size() - 1) {
                stringBuilder.append(readLevel.get(i)).append("\n");
            } else {
                stringBuilder.append(readLevel.get(i));
            }
        }
        boardContent = stringBuilder.toString();

        return new Level(new Position(keeperLine, keeperCol), boxPosition, boardContent);
    }

    /**
     * Receives a level file
     *
     * @return Tutorial level
     */
    public static Level receivePath(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("levelFiles"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Level.path = file.getAbsolutePath();
                return Level.loadLevel(Level.path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return getTutorial();
    }
}