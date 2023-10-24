package thedrake.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import thedrake.board.Board;
import thedrake.board.BoardTile;
import thedrake.board.PositionFactory;
import thedrake.game.StandardDrakeSetup;

public class TwoPlayersGameController {

    @FXML
    private HBox topRow;
    @FXML
    private HBox bottomRow;
    @FXML
    private GridPane gameBoard;

    @FXML
    private VBox mainBox;

    StackView stackView;

    BoardView boardView;

    private final StandardDrakeSetup standardDrakeSetup = new StandardDrakeSetup();

    Board board = new Board(4);

    public void initialize() {
        setupTopRow();
        setupGameBoard();
    }

    private void setupTopRow() {
        Board boardStack = new Board(7);
        PositionFactory positionFactory = board.positionFactory();
        board = board.withTiles(new Board.TileAt(positionFactory.pos(1, 1), BoardTile.MOUNTAIN));
//        board = board.withTiles(new Board.TileAt(positionFactory.pos(2, 1), BoardTile.MOUNTAIN));
//        board = board.withTiles(new Board.TileAt(positionFactory.pos(3, 1), BoardTile.MOUNTAIN));
//        board = board.withTiles(new Board.TileAt(positionFactory.pos(0, 1), BoardTile.MOUNTAIN));

        stackView = new StackView(standardDrakeSetup.startState(boardStack), bottomRow, topRow, standardDrakeSetup.startState(board), gameBoard, boardView);
    }

    private void setupGameBoard() {
        boardView = new BoardView(standardDrakeSetup.startState(board), gameBoard, stackView, mainBox);
    }
}
