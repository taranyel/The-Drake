package thedrake.actions;

import thedrake.board.BoardMove;
import thedrake.board.BoardPos;
import thedrake.game.GameState;

public class StepOnly extends BoardMove {

    public StepOnly(BoardPos origin, BoardPos target) {
        super(origin, target);
    }

    @Override
    public GameState execute(GameState originState) {
        return originState.stepOnly(origin(), target());
    }

}
