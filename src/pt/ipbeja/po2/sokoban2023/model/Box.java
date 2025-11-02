package pt.ipbeja.po2.sokoban2023.model;

/**
 * Box that is moved by the keeper
 *
 * @author Martinho Caeiro (23917) & Paulo Abade (23919)
 * @version Sokoban™ Final Edition
 */
public final class Box extends MobileElement {

    /**
     * Creates a Box at pos
     *
     * @param pos Initial box position
     */
    public Box(Position pos) {
        super(pos);
        this.pos = pos;
    }

    /**
     * Gets current box position
     *
     * @return pos Current box position
     */
    public Position getPosition() {
        return pos;
    }
}