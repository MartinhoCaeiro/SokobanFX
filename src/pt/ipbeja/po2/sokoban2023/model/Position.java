package pt.ipbeja.po2.sokoban2023.model;

/**
 * Element that can be moved
 *
 * @param line Initial line
 * @param col  Initial column
 * @author Martinho Caeiro (23917) & Paulo Abade (23919)
 * @version Sokoban™ Final Edition
 */
public record Position(int line, int col) {
    @Override
    public String toString() {
        return line + ", " + col;
    }

    /**
     * Next box position after being pushed by the keeper
     *
     * @param p1 Initial box position
     * @param p2 Final box position
     * @return Box position after being pushed
     */
    public static Position boxNextPositionAfterPush(Position p1, Position p2) {
        final int dLine = p2.line() - p1.line();
        final int dCol = p2.col() - p1.col();
        assert (Math.abs(dLine) == 1) && (dCol == 0) || (dLine == 0) && (Math.abs(dCol) == 1);

        return new Position(p2.line() + dLine, p2.col() + dCol);
    }

    /**
     * Moves element in a certain direction
     *
     * @param dir Movement direction
     * @return Position after movement
     */
    public Position move(Direction dir) {
        return switch (dir) {
            case UP -> new Position(this.line() - 1, this.col());
            case DOWN -> new Position(this.line() + 1, this.col());
            case LEFT -> new Position(this.line(), this.col() - 1);
            case RIGHT -> new Position(this.line(), this.col() + 1);
        };
    }
}