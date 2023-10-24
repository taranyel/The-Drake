package thedrake.board;

public class Offset2D {
    public final int x;
    public final int y;

    // Konstruktor
    public Offset2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    // Zjištuje, zda se tento offset rovná jinému offsetu
    public boolean equalsTo(int x, int y){
        if (this.x == x && this.y == y){
            return true;
        }
        return false;
    }

    // Vrací nový offset, kde y souřadnice má obrácené znaménko
    public Offset2D yFlipped(){

        Offset2D flipped = new Offset2D(this.x, -(this.y));
        return flipped;
    }
}
