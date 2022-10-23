import javafx.scene.paint.Paint;

public class BuilderDirctor{
    private Builder builder;
    BuilderDirctor(Builder builder){
        this.builder = builder;
    }

    public GameBall buildGameBall(double startX, double startY, double startRadius, Paint colour,double mass){
        this.builder.reSet();
        this.builder.setxPos(startX);
        this.builder.setyPos(startY);
        this.builder.setRadius(startRadius);
        this.builder.setColour(colour);
        this.builder.setMass(mass);
        return (GameBall) this.builder.getResult();
    }
}