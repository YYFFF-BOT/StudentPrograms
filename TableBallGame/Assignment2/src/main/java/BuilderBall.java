import javafx.scene.paint.Paint;

public class BuilderBall implements Builder {
    private GameBall ball;

    @Override
    public void reSet() {
        this.ball = new GameBall();
        
    }

    @Override
    public void setxPos(double x) {
        this.ball.setxPos(x);
        
    }

    @Override
    public void setyPos(double y) {
        this.ball.setyPos(y);
        
    }

    @Override
    public void setColour(Paint colour) {
        this.ball.setColour(colour);
    }

    @Override
    public void setRadius(double radius) {
        this.ball.setRadius(radius);
        
    }

    @Override
    public void setMass(double mass) {
        this.ball.setMass(mass);
        
    }


    @Override
    public GameBall getResult() {
        return this.ball;
    } 
}
