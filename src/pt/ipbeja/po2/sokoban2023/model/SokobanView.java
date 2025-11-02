package pt.ipbeja.po2.sokoban2023.model;

/**
 * Messages from the model to the interface
 * Updates the interface after every movement
 *
 * @author Martinho Caeiro (23917) & Paulo Abade (23919)
 * @version Sokoban™ Final Edition
 */
public interface SokobanView {
    void update(MessageToUI messageToUI);

    default void onLevelReset() {}
}