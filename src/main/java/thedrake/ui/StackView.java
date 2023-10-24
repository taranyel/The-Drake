package thedrake.ui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import thedrake.board.BoardPos;
import thedrake.board.Move;
import thedrake.board.PositionFactory;
import thedrake.game.GameState;
import thedrake.tile.PlayingSide;
import thedrake.troop.TroopFace;
import thedrake.troop.TroopTile;

import java.util.List;

public class StackView extends HBox implements TileViewContext {

    private final GameState gameState;

    public ValidMoves validMoves;

    public TileView selectedTop, selectedBottom;

    public final HBox top, bottom;

    private final GridPane mainPane;

    public GameState mainState;

    private BoardView boardView;

    public Label blue = new Label("Modrý hrač");

    public Label orange = new Label("Oranževý hrač");
    public final Border borderBlue = new Border(
            new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));


    public final Border borderOrange = new Border(
            new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));


    public StackView(GameState gameState, HBox bottom, HBox top, GameState main, GridPane mainPane, BoardView boardView) {
        this.gameState = gameState;
        this.validMoves = new ValidMoves(main);
        this.mainPane = mainPane;
        this.mainState = main;
        this.boardView = boardView;

        PositionFactory positionFactory = gameState.board().positionFactory();
        for (int x = 0; x < 7; x++) {
            BoardPos boardPos = positionFactory.pos(x, 0);

            bottom.getChildren().add(new TileView(boardPos, new TroopTile(gameState.armyOnTurn().stack().get(x), PlayingSide.BLUE, TroopFace.AVERS), this));
            top.getChildren().add(new TileView(boardPos, new TroopTile(gameState.armyNotOnTurn().stack().get(x), PlayingSide.ORANGE, TroopFace.AVERS), this));
        }

        blue.setAlignment(Pos.CENTER);
        blue.setTextAlignment(TextAlignment.CENTER);
        blue.setPrefHeight(100);
        blue.setPrefWidth(100);
        blue.setFont(new Font("Imprint MT Shadow", 16));
        blue.setBorder(borderBlue);

        bottom.getChildren().add(blue);

        bottom.setSpacing(5);
        bottom.setAlignment(Pos.CENTER);

        orange.setAlignment(Pos.CENTER);
        orange.setTextAlignment(TextAlignment.CENTER);
        orange.setPrefHeight(100);
        orange.setPrefWidth(100);
        orange.setFont(new Font("Imprint MT Shadow", 14));

        top.getChildren().add(orange);

        top.setSpacing(5);
        top.setAlignment(Pos.CENTER);

        this.top = top;
        this.bottom = bottom;
    }

    public void setValidMoves(ValidMoves validMoves) {
        this.validMoves = validMoves;
    }

    public void setMainState(GameState mainState) {
        this.mainState = mainState;
    }

    public void setBoardView(BoardView boardView) {
        this.boardView = boardView;
    }

    @Override
    public void tileViewSelected(TileView tileView) {
        if (boardView.selected != null) {
            boardView.selected.unSelect();
            boardView.clearMoves();
            boardView.selected = null;
        }

        for (Node child : top.getChildren()) {
            if (tileView == child) {
                if (selectedTop != null && selectedTop != tileView) {
                    selectedTop.unSelect();
                    if (selectedBottom != null) {
                        selectedBottom.unSelect();
                    }
                }

                selectedTop = tileView;

                if ((mainState.armyOnTurn().side() == PlayingSide.ORANGE) && (tileView.position().i() == 7 - mainState.armyOnTurn().stack().size())) {
                    showMoves(validMoves.movesFromStack());

                } else {
                    clearMoves();
                }
            }
        }

        for (Node child : bottom.getChildren()) {
            if (tileView == child) {
                if (selectedBottom != null && selectedBottom != tileView) {
                    selectedBottom.unSelect();
                    if (selectedTop != null) {
                        selectedTop.unSelect();
                    }
                }

                selectedBottom = tileView;

                if ((mainState.armyOnTurn().side() == PlayingSide.BLUE) && (tileView.position().i() == 7 - mainState.armyOnTurn().stack().size())) {
                    showMoves(validMoves.movesFromStack());
                } else {
                    clearMoves();
                }
            }
        }

        boardView.setStackView(this);
    }

    @Override
    public void executeMove(Move move) {
        if (boardView.selected != null) {
            boardView.selected.unSelect();
            boardView.clearMoves();
            boardView.selected = null;
        }

        if (selectedTop != null && mainState.armyOnTurn().side() == PlayingSide.ORANGE) {
            selectedTop.unSelect();
            top.getChildren().set(selectedTop.position().i(), new TileView(new BoardPos(7, 1, 2), gameState.tileAt(new BoardPos(7, 1, 2)), this));

            selectedTop = null;

        } else if (selectedBottom != null && mainState.armyOnTurn().side() == PlayingSide.BLUE) {
            selectedBottom.unSelect();
            bottom.getChildren().set(selectedBottom.position().i(), new TileView(new BoardPos(7, 1, 2), gameState.tileAt(new BoardPos(7, 1, 2)), this));

            selectedBottom = null;
        }

        clearMoves();
        mainState = move.execute(mainState);
        validMoves = new ValidMoves(mainState);

        boardView.setStackView(this);
    }

    private void clearMoves() {
        for (Node node : mainPane.getChildren()) {
            TileView tileView = (TileView) node;
            tileView.clearMove();
        }
    }

    private void showMoves(List<Move> moveList) {
        for (Move move : moveList) {
            tileViewAt(move.target()).setMove(move);
        }
    }

    private TileView tileViewAt(BoardPos target) {
        int index = (3 - target.j()) * 4 + target.i();
        return (TileView) mainPane.getChildren().get(index);
    }
}
