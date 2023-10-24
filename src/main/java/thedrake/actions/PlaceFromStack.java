package thedrake.actions;

import thedrake.board.BoardPos;
import thedrake.board.Move;
import thedrake.game.GameState;

public class PlaceFromStack extends Move {

    public PlaceFromStack(BoardPos target) {
        super(target);
    }

    @Override
    public GameState execute(GameState originState) {
        return originState.placeFromStack(target());
    }

}
