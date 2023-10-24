package thedrake.troop;

import thedrake.game.GameState;
import thedrake.board.Move;
import thedrake.tile.PlayingSide;
import thedrake.board.BoardPos;
import thedrake.tile.Tile;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public final class TroopTile implements Tile {

    Troop troop;
    PlayingSide side;
    TroopFace face;

    // Konstruktor
    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    // Vrací barvu, za kterou hraje jednotka na této dlaždici
    public PlayingSide side() {
        if (side == PlayingSide.ORANGE) {
            return PlayingSide.ORANGE;
        } else {
            return PlayingSide.BLUE;
        }
    }

    // Vrací stranu, na kterou je jednotka otočena
    public TroopFace face() {
        if (face == TroopFace.REVERS) {
            return TroopFace.REVERS;
        } else {
            return TroopFace.AVERS;
        }
    }

    // Jednotka, která stojí na této dlaždici
    public Troop troop() {
        return troop;
    }

    // Vrací False, protože na dlaždici s jednotkou se nedá vstoupit
    @Override
    public boolean canStepOn() {
        return false;
    }

    // Vrací True
    @Override
    public boolean hasTroop() {
        return true;
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {

        List<TroopAction> newListActions = troop.actions(face);

        List<Move> newList = new ArrayList<>();

        int index = 0;

        for (int i = 0; i < newListActions.size(); i++) {

            List<Move> newActionList = newListActions.get(i).movesFrom(pos, side, state);

            if (!newActionList.isEmpty()) {

                for (Move j : newActionList) {
                    newList.add(index, j);
                    index++;
                }
            }
        }
        return newList;
    }

    // Vytvoří novou dlaždici, s jednotkou otočenou na opačnou stranu
// (z rubu na líc nebo z líce na rub)
    public TroopTile flipped() {
        if (face == TroopFace.REVERS) {
            TroopTile tile = new TroopTile(troop, side, TroopFace.AVERS);
            return tile;
        } else {
            TroopTile tile = new TroopTile(troop, side, TroopFace.REVERS);
            return tile;
        }
    }

    public void toJSON(PrintWriter writer) {
        writer.printf("{\"troop\":" + "\"" + troop.name() + "\"," + "\"side\":" +
                "\"" + side + "\"," + "\"face\":" + "\"" + face + "\"}");
    }
}
