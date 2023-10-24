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

public class SlideAction extends TroopAction {
    public SlideAction(Offset2D offset) {
        super(offset);
    }

    public SlideAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> result = new ArrayList<>();
        TilePos target = origin.stepByPlayingSide(offset(), side);

        while (target != TilePos.OFF_BOARD){

            if (!state.canStep(origin, target) && !state.canCapture(origin, target)){
                return result;
            }
            if (state.canStep(origin, target)) {
                result.add(new StepOnly(origin, (BoardPos) target));
            } else if (state.canCapture(origin, target)) {
                result.add(new StepAndCapture(origin, (BoardPos) target));
                return result;
            }

            target = target.stepByPlayingSide(offset(), side);
        }


        return result;
    }
}
