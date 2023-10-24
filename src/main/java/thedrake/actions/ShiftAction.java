package thedrake.actions;

import thedrake.board.BoardPos;
import thedrake.board.Move;
import thedrake.board.Offset2D;
import thedrake.game.GameState;
import thedrake.tile.PlayingSide;
import thedrake.tile.TilePos;
import thedrake.troop.TroopAction;

import java.util.ArrayList;
import java.util.List;

public class ShiftAction extends TroopAction {

    public ShiftAction(Offset2D offset) {
        super(offset);
    }

    public ShiftAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> result = new ArrayList<>();
        TilePos target = origin.stepByPlayingSide(offset(), side);

        if (state.canStep(origin, target)) {
            result.add(new StepOnly(origin, (BoardPos) target));
        } else if (state.canCapture(origin, target)) {
            result.add(new StepAndCapture(origin, (BoardPos) target));
        }

        return result;
    }
}
