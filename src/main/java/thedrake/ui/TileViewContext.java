package thedrake.ui;

import thedrake.board.Move;

import java.io.IOException;

public interface TileViewContext {

    void tileViewSelected(TileView tileView);

    void executeMove(Move move) ;

}
