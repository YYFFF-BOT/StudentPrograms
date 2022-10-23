import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.scene.paint.Paint;

public class FactoryBall implements FactoryReader{
    private List<GameBall> balls = new ArrayList<>();
    private JSONArray jsonBallsBall;
    private JSONObject jsonObject;
    private JSONObject jsonBalls;
    public FactoryBall(String path){
        JSONParser parser = new JSONParser();
        try {
			Object object = parser.parse(new FileReader(path));

			// convert Object to JSONObject
			jsonObject = (JSONObject) object;

			// reading the "Balls" section:
			jsonBalls = (JSONObject) jsonObject.get("Balls");

			// reading the "Balls: ball" array:
            jsonBallsBall = (JSONArray) jsonBalls.get("ball");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public List<GameBall> create() {
		for (Object obj : this.jsonBallsBall) {
			JSONObject jsonBall = (JSONObject) obj;
			// the ball colour is a String
			String colour = (String) jsonBall.get("colour");
			// the ball position, velocity, mass are all doubles
			Double positionX = (Double) ((JSONObject) jsonBall.get("position")).get("x");
			Double positionY =(Double) ((JSONObject) jsonBall.get("position")).get("y");
			Double mass = (Double) jsonBall.get("mass");
			System.out.println("Ball x: " + positionX +" Ball y: " + positionY + ", mass: " + mass);
            BuilderBall builderBall = new BuilderBall();
            BuilderDirctor builderDirctor = new BuilderDirctor(builderBall);
			
            System.out.println(colour);
            

            balls.add(builderDirctor.buildGameBall(positionX, positionY, 10, Paint.valueOf(colour.toUpperCase()), mass));
				
		}
        return balls;
    }

    
}