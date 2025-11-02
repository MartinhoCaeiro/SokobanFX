package pt.ipbeja.po2.sokoban2023.model;

import pt.ipbeja.po2.sokoban2023.images.ImageType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Sokoban™ game logic
 *
 * @author Martinho Caeiro (23917) & Paulo Abade (23919)
 * @version Sokoban™ Final Edition
 */
public class SokobanGameModel {
    private final BoardModel board;
    private final Keeper keeper;
    private final Set<Box> boxes;
    private SokobanView view;
    private Level level;
    private final List<SokobanView> views = new ArrayList<>();
    private final String playerName;

    /**
     * Creates the level
     *
     * @param level Level design
     */
    public SokobanGameModel(Level level, String playerName) {
        this.board = new BoardModel(level.boardContent());
        this.keeper = new Keeper(level.keeperPosition(), board);
        this.boxes = this.createSetOfBoxes(level.boxesPositions());
        this.level = level;
        this.view = null;
        this.playerName = playerName;
    }

    /**
     * Sets initial box position
     *
     * @param boxesPositions Initial box position
     */
    private Set<Box> createSetOfBoxes(Set<Position> boxesPositions) {
        Set<Box> set = new HashSet<>();
        for (Position pos : boxesPositions) {
            set.add(new Box(pos));
        }
        return set;
    }

    /**
     * Tells where the keeper is
     *
     * @return Current keeper position
     */
    public Keeper keeper() {
        return this.keeper;
    }

    /**
     * Updates the view
     *
     * @param view View registed
     */
    public void registerView(SokobanView view) {
        this.view = view;
    }

    /**
     * Detects if there is a wall
     *
     * @return Content of outside positions
     */
    public PositionContent getPosContent(Position pos) {
        if (this.isOutsideBoard(pos)) return PositionContent.WALL;
        else return this.board.getPosContent(pos);
    }

    /**
     * Detects if it is outside the level
     *
     * @param pos Current position
     * @return Detected position
     */
    public boolean isOutsideBoard(Position pos) {
        return pos.line() < 0 || pos.col() < 0 ||
                pos.line() >= board.nLines() ||
                pos.col() >= board.nCols();
    }

    /**
     * Gets number of lines in the level
     *
     * @return Number of lines
     */
    public int getNLines() {
        return this.board.nLines();
    }

    /**
     * Gets number of columns in the level
     *
     * @return Number of columns
     */
    public int getNCols() {
        return this.board.nCols();
    }

    /**
     * Ends the game if all boxes are stored
     *
     * @return True if all boxes are stored, False otherwise
     */
    public boolean allBoxesAreStored() {
        for (Box box : this.boxes) {
            if (this.getPosContent(box.getPosition()) != PositionContent.END) return false;
        }
        return true;
    }

    /**
     * Moves the keeper in the specified direction
     *
     * @param dir Direction of movement
     * @return True if keeper moved, False otherwise
     */
    public boolean moveKeeper(Direction dir) {
        return this.moveKeeperTo(this.keeper.getPosition().move(dir));
    }

    /**
     * Moves keeper to a new position
     *
     * @param newPosition Next position
     * @return True if keeper moved, False otherwise
     */
    private boolean moveKeeperTo(Position newPosition) {
        Position initialPos = this.keeper.getPosition();
        List<Position> positions = this.moveTo(initialPos, newPosition);

        if (positions.size() > 0) {
            String messageToGUI = "Keeper alterou a sua posição de: "
                    + positions.get(0) + " para " + positions.get(1);
            this.view.update(new MessageToUI(positions, messageToGUI));
            return true;
        }
        return false;
    }

    /**
     * Moves needed elements
     *
     * @param keeperPosition Initial keeper position
     * @param newKeeperPos   Next keeper position
     * @return New keeper position
     */
    private List<Position> moveTo(Position keeperPosition, Position newKeeperPos) {
        final Position possibleFinalBoxPos = Position.boxNextPositionAfterPush(keeperPosition, newKeeperPos);
        final boolean boxInNewKeeperPos = this.boxInPos(newKeeperPos);
        final boolean boxInPossibleFinalBoxPos = this.boxInPos(possibleFinalBoxPos);
        if (!boxInNewKeeperPos && !this.getPosContent(newKeeperPos).equals(PositionContent.WALL)) {
            this.keeper.moveTo(newKeeperPos);
            return List.of(newKeeperPos, keeperPosition);
        } else if (boxInNewKeeperPos && !this.getPosContent(possibleFinalBoxPos).equals(PositionContent.WALL)
                && !boxInPossibleFinalBoxPos) {
            this.keeper.moveTo(newKeeperPos);
            this.moveBoxAt(newKeeperPos, possibleFinalBoxPos);
            return List.of(newKeeperPos, keeperPosition, possibleFinalBoxPos);
        }
        return List.of();
    }

    /**
     * Moves the box
     *
     * @param start Initial box position
     * @param end   Next box position
     */
    private void moveBoxAt(Position start, Position end) {
        this.boxes.remove(new Box(start));
        this.boxes.add(new Box(end));
    }

    /**
     * Tests a position has a box
     *
     * @param pos Test position
     * @return true if there is a box in pos
     */
    public boolean boxInPos(Position pos) {
        return this.boxes.contains(new Box(pos));
    }

    /**
     * Converts a position into an image
     *
     * @param pos Current position
     * @return Image in position
     */
    public ImageType imageForPosition(Position pos) {
        if (board.getPosContent(pos).equals(PositionContent.WALL)) return ImageType.WALL;
        else if (board.getPosContent(pos).equals(PositionContent.FREE)) {
            if (this.keeper.getPosition().equals(pos)) return ImageType.KEEPER;
            else if (this.boxInPos(pos)) return ImageType.BOX;
            else return ImageType.FREE;
        } else if (board.getPosContent(pos).equals(PositionContent.END)) {
            if (this.keeper.getPosition().equals(pos)) return ImageType.KEEPER;
            else if (this.boxInPos(pos)) return ImageType.BOXEND;
            else return ImageType.END;
        }
        return ImageType.END;
    }

    /**
     * Resets the current level
     */
    public void resetLevel() {
        try {
            if (Level.getPath().equals("")) {
                this.level = new Level();
            } else {
                this.level = Level.loadLevel(Level.getPath());
            }

            Position initialKeeperPosition = this.level.keeperPosition();
            this.keeper.setPosition(initialKeeperPosition);

            this.boxes.clear();
            Set<Position> initialBoxPositions = this.level.boxesPositions();

            for (Position boxPos : initialBoxPositions) {
                Box box = new Box(boxPos);
                this.boxes.add(box);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        informView();
    }

    /**
     * Resets the level views
     */
    private void informView() {
        for (SokobanView view : views) {
            view.onLevelReset();
        }
    }

    public String getPlayerName() {
        return playerName;
    }
}