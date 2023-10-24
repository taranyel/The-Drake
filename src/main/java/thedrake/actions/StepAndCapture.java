package thedrake.actions;

import thedrake.board.BoardMove;
import thedrake.board.BoardPos;
import thedrake.game.GameState;

public class StepAndCapture extends BoardMove {

    public StepAndCapture(BoardPos origin, BoardPos target) {
        super(origin, target);
    }

    @Override
    public GameState execute(GameState originState) {
        return originState.stepAndCapture(origin(), target());
    }

}
