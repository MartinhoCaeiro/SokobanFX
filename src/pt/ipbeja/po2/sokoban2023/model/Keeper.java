package pt.ipbeja.po2.sokoban2023.model;

/**
 * Keeper that is moved by the player
 *
 * @author Martinho Caeiro (23917) & Paulo Abade (23919)
 * @version Sokoban™ Final Edition
 */
public final class Keeper extends MobileElement {
    private Position pos;
    private final BoardModel boardModel;

    /**
     * Creates the Keeper at pos
     *
     * @param pos        Initial keeper position
     * @param boardModel Game board design template
     */
    public Keeper(Position pos, BoardModel boardModel) {
        super(pos);
        this.pos = pos;
        this.boardModel = boardModel;
    }

    /**
     * Gets current keeper position
     *
     * @return pos Current keeper position
     */
    public Position getPosition() {
        return this.pos;
    }

    /**
     * Sets keeper position
     *
     * @param position Current keeper position
     */
    public void setPosition(Position position) {
        this.pos = position;
    }

    /**
     * Moves keeper to the new position
     *
     * @param newKeeperPos New keeper position
     */
    public void moveTo(Position newKeeperPos) {
        this.nextMove(newKeeperPos);
        if (isValid(newKeeperPos)) this.pos = newKeeperPos;
    }

    /**
     * Checks if new keeper position is valid
     *
     * @param newKeeperPos New keeper position
     */
    private boolean isValid(Position newKeeperPos) {
        return newKeeperPos.line() >= 0 && newKeeperPos.line() < this.boardModel.nLines()
                && newKeeperPos.col() >= 0 && newKeeperPos.col() < this.boardModel.nCols();
    }

    /**
     * Dictates new keeper movement direction
     *
     * @param newKeeperPos New keeper position
     */
    private void nextMove(Position newKeeperPos) {
        if (isGoingUp(newKeeperPos)) {
            newKeeperPos.move(Direction.UP);
        } else if (isGoingDown(newKeeperPos)) {
            newKeeperPos.move(Direction.DOWN);
        } else if (isGoingRight(newKeeperPos)) {
            newKeeperPos.move(Direction.RIGHT);
        } else if (isGoingLeft(newKeeperPos)) {
            newKeeperPos.move(Direction.LEFT);
        }
    }

    /**
     * Moves keeper one place up
     *
     * @param newKeeperPos New keeper position
     * @return Keeper position after being moved
     */
    private boolean isGoingUp(Position newKeeperPos) {
        return newKeeperPos.line() - this.pos.line() < 0;
    }

    /**
     * Moves keeper one place down
     *
     * @param newKeeperPos New keeper position
     * @return Keeper position after being moved
     */
    private boolean isGoingDown(Position newKeeperPos) {
        return newKeeperPos.line() - this.pos.line() > 0;
    }

    /**
     * Moves keeper one place to the right
     *
     * @param newKeeperPos New keeper position
     * @return Keeper position after being moved
     */
    private boolean isGoingRight(Position newKeeperPos) {
        return newKeeperPos.col() - this.pos.col() < 0;
    }

    /**
     * Moves keeper one place to the left
     *
     * @param newKeeperPos New keeper position
     * @return Keeper position after being moved
     */
    private boolean isGoingLeft(Position newKeeperPos) {
        return newKeeperPos.col() - this.pos.col() > 0;
    }
}