package thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import thedrake.board.BoardPos;
import thedrake.board.Move;
import thedrake.tile.Tile;

public class TileView extends Pane {

    private final BoardPos boardPos;

    public Tile tile;

    private final TileBackgrounds backgrounds = new TileBackgrounds();

    private final Border selectBorder = new Border(
            new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

    private final TileViewContext tileViewContext;

    private Move move;

    private final ImageView moveImage;

    public boolean selected;

    public TileView(BoardPos boardPos, Tile tile, TileViewContext tileViewContext) {
        this.boardPos = boardPos;
        this.tile = tile;
        this.tileViewContext = tileViewContext;

        setPrefSize(100, 100);
        update();

        setOnMouseClicked(e -> onClick());

        moveImage = new ImageView(getClass().getResource("/assets/move.png").toString());
        moveImage.setVisible(false);
        getChildren().add(moveImage);
        selected = false;
    }

    private void onClick(){
        if (move != null) {
            tileViewContext.executeMove(move);
        }
        else if (tile.hasTroop())
            select();
    }

    public void select() {
        setBorder(selectBorder);
        tileViewContext.tileViewSelected(this);
        selected = true;
    }

    public void unSelect() {
        setBorder(null);
        selected = false;
    }

    public void update() {
        setBackground(backgrounds.get(tile));
    }

    public void setMove(Move move) {
        this.move = move;
        moveImage.setVisible(true);
    }

    public void clearMove() {
        this.move = null;
        moveImage.setVisible(false);
    }

    public BoardPos position() {
        return boardPos;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

}
