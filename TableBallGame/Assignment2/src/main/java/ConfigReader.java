import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.paint.Paint;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConfigReader {
	/**
	 * You will probably not want to use a static method/class for this.
	 * 
	 * This is just an example of how to access different parts of the json
	 * 
	 * @param path The path of the json file to read
	 */
	private ArrayList<GameBall> balls = new ArrayList<>();
	private String tableColour;
	private Long tableX;
	private Long tableY;
	private double tableFriction;
	public ArrayList<GameBall> getBalls(){
		return balls;
	}
	public String getTableColour(){
		return this.tableColour;
	}
	public double getTableFriction(){
		return tableFriction;
	}
	public boolean hasBall(){
		return balls.size()>0;
	}
	public Long getHeight(){
		return tableY;
	}
	public Long getWidth(){
		return tableX;
	}
	public ConfigReader(String path) {

		JSONParser parser = new JSONParser();
		try {
			Object object = parser.parse(new FileReader(path));

			// convert Object to JSONObject
			JSONObject jsonObject = (JSONObject) object;

			// reading the Table section:
			JSONObject jsonTable = (JSONObject) jsonObject.get("Table");

			// reading a value from the table section
			tableColour = (String) jsonTable.get("colour");

			// reading a coordinate from the nested section within the table
			// note that the table x and y are of type Long (i.e. they are integers)
			tableX = (Long) ((JSONObject) jsonTable.get("size")).get("x");
			tableY = (Long) ((JSONObject) jsonTable.get("size")).get("y");

			// getting the friction level.
			// This is a double which should affect the rate at which the balls slow down
			tableFriction = (Double) jsonTable.get("friction");

			// reading the "Balls" section:
			JSONObject jsonBalls = (JSONObject) jsonObject.get("Balls");

			// reading the "Balls: ball" array:
			JSONArray jsonBallsBall = (JSONArray) jsonBalls.get("ball");

			// reading from the array:
			for (Object obj : jsonBallsBall) {
				JSONObject jsonBall = (JSONObject) obj;

				// the ball colour is a String
				String colour = (String) jsonBall.get("colour");

				// the ball position, velocity, mass are all doubles
				Double positionX = (Double) ((JSONObject) jsonBall.get("position")).get("x");
				Double positionY =(Double) ((JSONObject) jsonBall.get("position")).get("y");



				Double mass = (Double) jsonBall.get("mass");

				System.out.println("Ball x: " + positionX +" Ball y: " + positionY + ", mass: " + mass);
				Strategy strategy=new StrategyNone();
				switch (colour){
					case "red":strategy=new StrategyInPocket();break;
					case "blue":strategy=new StrategyInPokectTwice();break;
					// Make this invaild, easy to debug
					//case "white":strategy = new StrategyReset();break;
				}
				balls.add(new GameBall(positionX,positionY,20, Paint.valueOf(colour),mass));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
