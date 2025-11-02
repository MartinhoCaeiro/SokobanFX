package pt.ipbeja.po2.sokoban2023.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pt.ipbeja.po2.sokoban2023.model.Direction.*;

/**
 * Sokoban™ game testing
 *
 * @author Martinho Caeiro (23917) & Paulo Abade (23919)
 * @version Sokoban™ Final Edition
 */
class SokobanGameModelTest {

    /**
     * Tests if the keeper can move to the right
     */
    @Test
    void movementToTheRightTest() {
        Level level = new Level();
        SokobanGameModel sokoban = new SokobanGameModel(level, "debug");
        sokoban.registerView(messageToUI -> {
        });
        sokoban.moveKeeper(RIGHT);
        assertEquals(new Position(3, 6), sokoban.keeper().getPosition());
    }

    /**
     * Tests if the keeper can move to an empty position
     */
    @Test
    void goOutsideWallsTest() {
        Level level = new Level();
        SokobanGameModel sokoban = new SokobanGameModel(level, "debug");
        sokoban.registerView(messageToUI -> {
        });
        sokoban.moveKeeper(UP);
        sokoban.moveKeeper(UP);
        assertEquals(new Position(3, 5), sokoban.keeper().getPosition());
    }

    /**
     * Tests if the game is winnable
     */
    @Test
    void checkWinTest() {
        Level level = new Level(new Position(3, 6), Set.of(new Position(3, 5),
                new Position(3, 3)), """
                FFWWWWFF
                FFWFFWFF
                WWWFFWWW
                WFFEEFFW
                WFFFFFFW
                WWWWWWWW""");

        SokobanGameModel sokoban = new SokobanGameModel(level, "debug");
        sokoban.registerView(messageToUI -> {
        });

        sokoban.moveKeeper(LEFT);
        assertTrue(sokoban.allBoxesAreStored());
    }
}