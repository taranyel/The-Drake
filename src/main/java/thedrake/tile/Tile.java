package thedrake.tile;

import thedrake.game.GameState;
import thedrake.board.Move;
import thedrake.board.BoardPos;

import java.io.PrintWriter;
import java.util.List;

public interface Tile {

    // Vrací True, pokud je tato dlaždice volná a lze na ni vstoupit.
    public boolean canStepOn();

    // Vrací True, pokud tato dlaždice obsahuje jednotku
    public boolean hasTroop();

    public List<Move> movesFrom(BoardPos pos, GameState state);

    public void toJSON(PrintWriter writer);
}
