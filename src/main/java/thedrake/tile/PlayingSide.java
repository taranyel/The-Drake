package thedrake.tile;

import thedrake.JSONSerializable;

import java.io.PrintWriter;

public enum PlayingSide implements JSONSerializable {
    ORANGE,
    BLUE;

    @Override
    public void toJSON(PrintWriter writer){
        writer.printf("\"" + "ORANGE" + "\"");
    }
}
