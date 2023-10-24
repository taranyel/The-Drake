package thedrake.game;

import thedrake.JSONSerializable;

import java.io.PrintWriter;

public enum GameResult implements JSONSerializable {
    VICTORY, DRAW, IN_PLAY;

    @Override
    public void toJSON(PrintWriter writer){
        writer.printf("\"" + "IN_PLAY" + "\"");
    }
}
