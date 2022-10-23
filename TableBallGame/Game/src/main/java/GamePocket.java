import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class GamePocket implements GameItem{
    private static final Strategy StrategyNone = null;
    private double xPos,yPos,radius,mass;
    private Paint colour;
    public GamePocket(double xPos, double yPos, double radius, Paint colour,double mass){
        this.xPos=xPos;
        this.yPos=yPos;
        this.radius=radius;
        this.colour = Paint.valueOf("BLACK");
        this.mass = 0;
    }
    @Override
    public void tick() {

    }
    
    @Override
    public double getRadius() {
        return this.radius;
    }

    @Override
    public double getxPos() {
        return this.xPos;
    }

    @Override
    public double getyPos() {
        return this.yPos;
    }

    @Override
    public double getxVel() {
        return 0;
    }

    @Override
    public double getyVel() {
        return 0;
    }

    @Override
    public double getMass() {
        return 0;
    }
    @Override
    public Paint getColour() {
        return colour;
    }

    @Override
    public void setxPos(double xPos) {

    }

    @Override
    public void setyPos(double yPos) {

    }

    @Override
    public void setxVel(double xVel) {

    }

    @Override
    public void setyVel(double yVel) {

    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(colour);
        gc.fillOval(getxPos() - getRadius(),
                    getyPos() - getRadius(),
                    getRadius() * 2,
                    getRadius() * 2);
    }


    @Override
    public GameItem copy() {
        return new GamePocket(xPos,yPos,radius, colour, mass);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
    @Override
    public boolean isMoving() {
        return false;
    }
    
}
