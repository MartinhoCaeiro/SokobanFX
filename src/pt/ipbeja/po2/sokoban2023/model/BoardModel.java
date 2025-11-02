package pt.ipbeja.po2.sokoban2023.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Game board design template
 *
 * @author Martinho Caeiro (23917) & Paulo Abade (23919)
 * @version Sokoban™ Final Edition
 */
public class BoardModel {
    private final List<List<PositionContent>> board;

    /**
     * Game board design
     *
     * @param boardContent Level layout
     */
    public BoardModel(String boardContent) {
        Map<Character, PositionContent> pc = Map.of('F',
                PositionContent.FREE, 'W',
                PositionContent.WALL, 'E',
                PositionContent.END);
        List<List<PositionContent>> boardPos = new ArrayList<>();
        boardPos.add(new ArrayList<>());
        for (int i = 0; i < boardContent.length(); i++) {
            char c = boardContent.charAt(i);
            if (c == '\n') boardPos.add(new ArrayList<>());
            else boardPos.get(boardPos.size() - 1).add(pc.get(c));
        }
        this.board = Collections.unmodifiableList(boardPos);
    }

    /**
     * Number of lines in the level
     *
     * @return Number of lines
     */
    public int nLines() {
        return this.board.size();
    }

    /**
     * Number of columns in the level
     *
     * @return Number of columns
     */
    public int nCols() {
        return this.board.get(0).size();
    }

    /**
     * Content of a certain position
     *
     * @param pos Current position
     * @return Content in that position
     */
    public PositionContent getPosContent(Position pos) {
        return this.board.get(pos.line()).get(pos.col());
    }
}