package thedrake.actions;

import thedrake.board.BoardMove;
import thedrake.board.BoardPos;
import thedrake.game.GameState;

public class CaptureOnly extends BoardMove {

    public CaptureOnly(BoardPos origin, BoardPos target) {
        super(origin, target);
    }

    @Override
    public GameState execute(GameState originState) {
        return originState.captureOnly(origin(), target());
    }

}
