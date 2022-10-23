import javafx.scene.paint.Paint;

public interface Builder {
    void reSet();

    void setxPos(double x);
    void setyPos(double y);
    void setColour(Paint colour);
    void setRadius(double radius);
    void setMass(double mass);
    GameItem getResult();
}
