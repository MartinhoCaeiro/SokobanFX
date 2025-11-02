package pt.ipbeja.po2.sokoban2023.model;

import java.util.Objects;

/**
 * Element that can be moved
 *
 * @author Martinho Caeiro (23917) & Paulo Abade (23919)
 * @version Sokoban™ Final Edition
 */
public abstract class MobileElement {
    Position pos;

    /**
     * Element that can be moved
     *
     * @param pos Initial element position
     */
    public MobileElement(Position pos) {
        this.pos = pos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MobileElement that = (MobileElement) o;
        return Objects.equals(pos, that.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }
}