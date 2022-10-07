import javafx.scene.paint.Paint;

public class FactoryPocket implements FactoryReader {
    public GameItem create(double xPos, double yPos, double radius, Paint colour,double mass){
        return new GamePocket(xPos, yPos, radius, colour,mass);
    }
}
