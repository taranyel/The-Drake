package thedrake.board;

import thedrake.*;
import thedrake.tile.PlayingSide;
import thedrake.tile.TilePos;
import thedrake.troop.Troop;
import thedrake.troop.TroopFace;
import thedrake.troop.TroopTile;

import java.io.PrintWriter;
import java.util.*;

public class BoardTroops implements JSONSerializable {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;
    private static int guardsAmount;
    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        troopMap = Collections.emptyMap();
        leaderPosition = TilePos.OFF_BOARD;
        guards = 0;
        guardsAmount = 0;
    }

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    public Optional<TroopTile> at(TilePos pos) {

        if (troopMap.get((BoardPos) pos) != null) {
            Optional<TroopTile> returnMap = Optional.ofNullable(troopMap.get(pos));
            return returnMap;
        }

        return Optional.empty();
    }

    public PlayingSide playingSide() {
        return playingSide;
    }

    public TilePos leaderPosition() {
        return leaderPosition;
    }

    public int guards() {
        return guards;
    }

    public boolean isLeaderPlaced() {
        if (leaderPosition() == TilePos.OFF_BOARD) {
            return false;
        }
        return true;
    }

    public boolean isPlacingGuards() {
        if (isLeaderPlaced() && (guards < 2)) {
            return true;
        }
        return false;
    }

    public Set<BoardPos> troopPositions() {
        Set<BoardPos> newSet = new HashSet<>();

        for (BoardPos key : troopMap.keySet()) {
            if (at(key).isPresent()) {
                if (troopMap.get(key).hasTroop()) {
                    newSet.add(key);
                }
            }
        }

        return newSet;
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {

        if (at(target).isEmpty()) {

            TroopTile newTile = new TroopTile(troop, playingSide, TroopFace.AVERS);

            Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);

            newTroops.put(target, newTile);

            if (!isLeaderPlaced() && guards == 0) {
                BoardTroops newBoardTroops = new BoardTroops(playingSide, newTroops, target, guards);
                return newBoardTroops;
            }

            guardsAmount = guards;

            if (guards < 2){
                guardsAmount++;

                BoardTroops newBoardTroops = new BoardTroops(playingSide, newTroops, leaderPosition, guardsAmount);
                return newBoardTroops;
            }

            BoardTroops newBoardTroops = new BoardTroops(playingSide, newTroops, leaderPosition, guards);
            return newBoardTroops;

        } else {
            throw new IllegalArgumentException();
        }
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {

        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (at(origin).isEmpty() || at(target).isPresent()) {
            throw new IllegalArgumentException();
        }

        TroopTile tile = troopMap.get(origin);

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);

        newTroops.remove(origin);
        newTroops.put(target, tile.flipped());

        if (origin.i() == leaderPosition.i() && origin.j() == leaderPosition.j()){
            BoardTroops newBoardTroop = new BoardTroops(playingSide, newTroops, target, guards);
            return newBoardTroop;

        } else {
            BoardTroops newBoardTroop = new BoardTroops(playingSide, newTroops, leaderPosition, guards);
            return newBoardTroop;
        }
    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (at(target).isEmpty()){
            throw new IllegalArgumentException();
        }

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);

        if (target.i() == leaderPosition.i() && target.j() == leaderPosition.j()){
            newTroops.remove(target);
            BoardTroops newBoardTroop = new BoardTroops(playingSide, newTroops, TilePos.OFF_BOARD, guards);

            return newBoardTroop;
        }

        newTroops.remove(target);
        BoardTroops newBoardTroop = new BoardTroops(playingSide, newTroops, leaderPosition, guards);

        return newBoardTroop;
    }

    public class Helper implements Comparator<BoardPos> {

        @Override
        public int compare(BoardPos o1, BoardPos o2) {
            if (o1.i() == o2.i() && o1.j() == o2.j()){
                return 0;
            } else if (o1.i() > o2.i()){
                return 1;
            } else if (o1.i() == o2.i() && o1.j() > o2.j()){
                return 1;
            }

            return -1;
        }
    }

    @Override
    public void toJSON(PrintWriter writer){

        Set<BoardPos> newSet = troopPositions();

        TreeSet<BoardPos> newSortedSet = new TreeSet<>(new Helper());
        newSortedSet.addAll(newSet);

        writer.printf("{\"side\":" + "\"" + playingSide + "\"," + "\"leaderPosition\":");
        leaderPosition.toJSON(writer);
        writer.printf("," + "\"guards\":" + guards + "," + "\"troopMap\":{");

        boolean notFirst = false;

        for (BoardPos key : newSortedSet){

            if (notFirst){
                writer.printf(",");
            }
            key.toJSON(writer);
            writer.printf(":");
            troopMap.get(key).toJSON(writer);

            notFirst = true;
        }

        writer.printf("}}");
    }
}
