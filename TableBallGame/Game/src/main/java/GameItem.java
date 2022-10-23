import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public interface GameItem {
    boolean isAvailable();
    void tick();
    double getRadius();
    double getxPos();
    double getyPos();
    double getxVel();
    double getyVel();
    double getMass();
    Paint getColour();
    boolean isMoving();
    void setxPos(double xPos);
    void setyPos(double yPos);
    void setxVel(double xVel);
    void setyVel(double yVel);
    void draw(GraphicsContext gc);

    GameItem copy();

}
