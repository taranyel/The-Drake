package thedrake.board;

import thedrake.JSONSerializable;
import thedrake.tile.TilePos;

import java.io.PrintWriter;

public class Board implements JSONSerializable {

    private final BoardTile[][] board;

    // Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru, kde všechny dlaždice jsou prázdné, tedy BoardTile.EMPTY
    public Board(int dimension) {
        board = new BoardTile[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[i][j] = BoardTile.EMPTY;
            }
        }
    }
    // Rozměr hrací desky
    public int dimension() {
        return board.length;
    }

    // Vrací dlaždici na zvolené pozici.
    public BoardTile at(TilePos pos) {
        return board[pos.i()][pos.j()];
    }

    // Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
    public Board withTiles(TileAt... ats) {
        Board newBoard = new Board(dimension());

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                newBoard.board[i][j] = board[i][j];
            }
        }

        for (TileAt tileAt : ats) {
            BoardPos pos = tileAt.pos;
            newBoard.board[pos.i()][pos.j()] = tileAt.tile;
        }

        return newBoard;
    }

    // Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
    public PositionFactory positionFactory() {
        return new PositionFactory(dimension());
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }

    @Override
    public void toJSON(PrintWriter writer){
        writer.printf("{\"dimension\":" + dimension() + ",\"tiles\":[");

        boolean notFirst = false;

        for (int j = 0; j < dimension(); j++){
            for (int i = 0; i < dimension(); i++){

                if (notFirst){
                    writer.printf(",");
                }
                board[i][j].toJSON(writer);
                notFirst = true;
            }
        }

        writer.printf("]}");
    }
}

