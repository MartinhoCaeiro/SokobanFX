package pt.ipbeja.po2.sokoban2023.model;

import java.util.List;

/**
 * Message to be sent from the model so that the interface updates the positions in the list
 *
 * @author Martinho Caeiro (23917) & Paulo Abade (23919)
 * @version Sokoban™ Final Edition
 */
public record MessageToUI(List<Position> positions, String message) {}