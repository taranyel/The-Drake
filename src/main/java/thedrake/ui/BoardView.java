package thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import thedrake.board.BoardPos;
import thedrake.board.Move;
import thedrake.board.PositionFactory;
import thedrake.game.GameResult;
import thedrake.game.GameState;
import thedrake.tile.PlayingSide;

import java.util.List;

public class BoardView extends GridPane implements TileViewContext {

    public GameState gameState;

    private ValidMoves validMoves;

    public TileView selected;

    private final GridPane gridPane;

    boolean victory;

    public StackView stackView;

    public VBox mainBox;


    public BoardView(GameState gameState, GridPane gridPane, StackView stackView, VBox mainBox) {
        this.gameState = gameState;
        this.validMoves = new ValidMoves(gameState);
        this.gridPane = gridPane;
        this.stackView = stackView;
        this.victory = false;
        this.mainBox = mainBox;

        PositionFactory positionFactory = gameState.board().positionFactory();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                BoardPos boardPos = positionFactory.pos(x, 3 - y);
                gridPane.add(new TileView(boardPos, gameState.tileAt(boardPos), this), x, y);
            }
        }

        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(15));
        gridPane.setAlignment(Pos.CENTER);

        this.stackView.setBoardView(this);
    }

    public void setStackView(StackView stackView) {
        this.stackView = stackView;
    }

    @Override
    public void tileViewSelected(TileView tileView) {
        if (selected != null && selected != tileView) {
            selected.unSelect();
        }
        if (stackView.selectedBottom != null) {
            stackView.selectedBottom.unSelect();
        }
        if (stackView.selectedTop != null) {
            stackView.selectedTop.unSelect();
        }
        selected = tileView;

        clearMoves();
        showMoves(validMoves.boardMoves(tileView.position()));

        stackView.setBoardView(this);
    }

    @Override
    public void executeMove(Move move) {

        if (gameState.armyOnTurn().side() == PlayingSide.ORANGE){
            stackView.blue.setBorder(stackView.borderBlue);
            stackView.orange.setBorder(null);
        } else if (gameState.armyOnTurn().side() == PlayingSide.BLUE){
            stackView.orange.setBorder(stackView.borderOrange);
            stackView.blue.setBorder(null);
        }

        if (selected != null) {

            selected.unSelect();
            selected = null;

            gameState = move.execute(gameState);
            validMoves = new ValidMoves(gameState);

            stackView.setValidMoves(validMoves);
            stackView.setMainState(gameState);
        } else {
            stackView.executeMove(move);
            gameState = stackView.mainState;
            validMoves = stackView.validMoves;
        }
        this.updateTiles();
        clearMoves();

        stackView.setBoardView(this);

        if ((victory() && gameState.armyOnTurn().side() == PlayingSide.BLUE) || validMoves.allMoves().isEmpty()) {
            Win win = new Win(PlayingSide.ORANGE, mainBox, gridPane, stackView.top, stackView.bottom);
        } else if ((victory() && gameState.armyOnTurn().side() == PlayingSide.ORANGE) || validMoves.allMoves().isEmpty()) {
            Win win = new Win(PlayingSide.BLUE, mainBox, gridPane, stackView.top, stackView.bottom);
        }
    }

    private boolean victory() {
        if (gameState.result() == GameResult.VICTORY) {
            return true;
        }
        return false;
    }

    private void updateTiles() {
        for (Node node : gridPane.getChildren()) {
            TileView tileView = (TileView) node;
            tileView.setTile(gameState.tileAt(tileView.position()));
            tileView.update();
        }
    }

    public void clearMoves() {
        for (Node node : gridPane.getChildren()) {
            TileView tileView = (TileView) node;
            tileView.clearMove();
        }
    }

    private void showMoves(List<Move> moveList) {
        for (Move move : moveList)
            tileViewAt(move.target()).setMove(move);
    }

    private TileView tileViewAt(BoardPos target) {
        int index = (3 - target.j()) * 4 + target.i();
        return (TileView) gridPane.getChildren().get(index);
    }
}
