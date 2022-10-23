import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class GameBall implements GameItem{
    protected boolean isAvailable = true;
    private boolean isCue = false;
    private double xPos;
    private double yPos;
    private double radius;
    private double xVel;
    private double yVel;
    private Paint colour;
    private Double mass;
    private double initStartX,initStartY;

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }
 
    public Paint getColour() {
        return colour;
    }
    public boolean isCue(){
        return isCue;
    }
    public boolean isMoving(){
        if(xVel==0 && yVel ==0){
            return false;
        }
        return true;
    }

    public void draw(GraphicsContext gc){
        gc.setFill(Paint.valueOf("BLACK"));
        gc.fillOval(getxPos() - getRadius(),
                getyPos() - getRadius(),
                getRadius() * 2,
                getRadius() * 2);
        gc.setFill(this.colour);
        gc.fillOval(getxPos() - getRadius() + 2,
                getyPos() - getRadius() + 2,
                getRadius() * 2 - 4,
                getRadius() * 2 - 4);
    }
    GameBall(double startX, double startY, double startRadius, Paint colour,double mass) {
        this.xPos = startX;
        this.yPos = startY;
        this.radius = startRadius;
        this.colour = colour;
        this.mass=mass;
        xVel = 0;
        yVel = 0;
        this.initStartX = startX;
        this.initStartY = startY;
        if(Paint.valueOf("WHITE").equals(colour)){
            this.isCue = true;
        }
    }

    public GameBall() {
    }

    public void tick() {
        xPos += xVel;
        yPos += yVel;
        
    }

    public void setxVel(double xVel) {
        this.xVel = xVel;
    }

    public void setyVel(double yVel) {
        this.yVel = yVel;
    }

    public double getRadius() {
        return radius;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public  double getxVel() {
        return xVel;
    }

    public double getyVel() {
        return yVel;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public  void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public void setColour(Paint colour){
        this.colour = colour;
    }

    public void setAvailable(boolean b){
        this.isAvailable = b;
    }

    public void setMass(double mass){
        this.mass = mass;
    }

    public void setRadius(Double radius){
        this.radius = radius;
    }

    public void handleCollision(GameItem item){
        CollisionHandler.handleCollision(this,item);
    }

    @Override
    public GameItem copy() {
        return new GameBall(this.initStartX,this.initStartY,this.radius,this.colour,this.mass);
    }

    public double getInitStartY() {
        return initStartY;
    }
    public double getMass(){
        return this.mass;
    }
    public double getInitStartX() {
        return initStartX;
    }

}
