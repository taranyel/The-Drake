package thedrake.game;

import thedrake.JSONSerializable;
import thedrake.tile.PlayingSide;
import thedrake.board.Board;
import thedrake.board.BoardPos;
import thedrake.tile.Tile;
import thedrake.tile.TilePos;
import thedrake.troop.Troop;

import java.io.PrintWriter;
import java.util.Set;

public class GameState implements JSONSerializable {
    private Board board;
    private final PlayingSide sideOnTurn;
    private final Army blueArmy;
    private final Army orangeArmy;
    private final GameResult result;

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy) {
        this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
    }

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy,
            PlayingSide sideOnTurn,
            GameResult result) {
        this.board = board;
        this.sideOnTurn = sideOnTurn;
        this.blueArmy = blueArmy;
        this.orangeArmy = orangeArmy;
        this.result = result;
    }

    public Board board() {
        return board;
    }

    public PlayingSide sideOnTurn() {
        return sideOnTurn;
    }

    public GameResult result() {
        return result;
    }

    public Army army(PlayingSide side) {
        if (side == PlayingSide.BLUE) {
            return blueArmy;
        }

        return orangeArmy;
    }

    public Army armyOnTurn() {
        return army(sideOnTurn);
    }

    public Army armyNotOnTurn() {
        if (sideOnTurn == PlayingSide.BLUE)
            return orangeArmy;

        return blueArmy;
    }

    public Tile tileAt(TilePos pos) {

        if (blueArmy.boardTroops().at(pos).isPresent()){

            if (blueArmy.boardTroops().at(pos).get().hasTroop()){
                return blueArmy.boardTroops().at(pos).get();
            }
        }

        if (orangeArmy.boardTroops().at(pos).isPresent()){

            if (orangeArmy.boardTroops().at(pos).get().hasTroop()){
                return orangeArmy.boardTroops().at(pos).get();
            }
        }

        return board.at(pos);
    }

    private boolean canStepFrom(TilePos origin) {

        if (origin == TilePos.OFF_BOARD){
            return false;
        }

        if (result != GameResult.IN_PLAY || !tileAt(origin).hasTroop()) {
            return false;
        }

        if (armyNotOnTurn().boardTroops().at(origin).isPresent()){

            if (armyNotOnTurn().boardTroops().at(origin).get().hasTroop()){
                return false;
            }
        }

        if (armyOnTurn().boardTroops().isPlacingGuards() ||
                armyNotOnTurn().boardTroops().isPlacingGuards()) {
            return false;
        }

        return true;
    }

    private boolean canStepTo(TilePos target) {

        if (target == TilePos.OFF_BOARD){
            return false;
        }

        if (result != GameResult.IN_PLAY || !tileAt(target).canStepOn()){
            return false;
        }
        return true;
    }

    private boolean canCaptureOn(TilePos target) {

        if (target == TilePos.OFF_BOARD){
            return false;
        }
        if (result != GameResult.IN_PLAY){
            return false;
        }

        if (armyNotOnTurn().boardTroops().at(target).isPresent()){
            if (!armyNotOnTurn().boardTroops().at(target).get().hasTroop()){
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public boolean canStep(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canStepTo(target);
    }

    public boolean canCapture(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canCaptureOn(target);
    }

    public boolean canPlaceFromStack(TilePos target) {

        if (armyOnTurn().stack().isEmpty() || !canStepTo(target)){
            return false;
        }

        if (!armyOnTurn().boardTroops().isLeaderPlaced()){

            if (sideOnTurn == PlayingSide.BLUE){
                if (target.j() != 0){
                    return false;
                }
            } else {

                if (target.j() != board.dimension() - 1){
                    return false;
                }
            }
        }

        if (armyOnTurn().boardTroops().isPlacingGuards()){

            if (!target.isNextTo(armyOnTurn().boardTroops().leaderPosition())){
                return false;
            }
        }

        Set<BoardPos> newSet = armyOnTurn().boardTroops().troopPositions();

        for(BoardPos i : newSet){
            if (target.isNextTo(i)){
                return true;
            }
        }

        if (newSet.size() == 0){
            return true;
        }

        return false;
    }

    public GameState stepOnly(BoardPos origin, BoardPos target) {
        if (canStep(origin, target))
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);

        throw new IllegalArgumentException();
    }

    public GameState stepAndCapture(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopStep(origin, target).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState captureOnly(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopFlip(origin).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState placeFromStack(BoardPos target) {
        if (canPlaceFromStack(target)) {
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().placeFromStack(target),
                    GameResult.IN_PLAY);
        }

        throw new IllegalArgumentException();
    }

    public GameState resign() {
        return createNewGameState(
                armyNotOnTurn(),
                armyOnTurn(),
                GameResult.VICTORY);
    }

    public GameState draw() {
        return createNewGameState(
                armyOnTurn(),
                armyNotOnTurn(),
                GameResult.DRAW);
    }

    private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
        if (armyOnTurn.side() == PlayingSide.BLUE) {
            return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
        }

        return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
    }

    @Override
    public void toJSON(PrintWriter writer){
        writer.printf("{\"result\":" + "\"" + result + "\"," + "\"board\":");
        board.toJSON(writer);
        writer.printf(",\"blueArmy\":");
        blueArmy.toJSON(writer);
        writer.printf(",\"orangeArmy\":");
        orangeArmy.toJSON(writer);
        writer.printf("}");
    }
}

