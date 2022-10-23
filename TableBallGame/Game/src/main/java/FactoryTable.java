import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class FactoryTable implements FactoryReader{
    private String tableColour;
	private Long tableX;
	private Long tableY;
	private double tableFriction;
    
    public FactoryTable(String path){
        JSONParser parser = new JSONParser();
        try{
            Object object= parser.parse(new FileReader(path));

            // convert Object to JSONObject
            JSONObject jsonObject = (JSONObject) object;
            // reading the Table section:
			JSONObject jsonTable = (JSONObject) jsonObject.get("Table");
            // reading a value from the table section
			this.tableColour = (String) jsonTable.get("colour");

			// reading a coordinate from the nested section within the table
			// note that the table x and y are of type Long (i.e. they are integers)
			this.tableX = (Long) ((JSONObject) jsonTable.get("size")).get("x");
			this.tableY = (Long) ((JSONObject) jsonTable.get("size")).get("y");

			// getting the friction level.
			// This is a double which should affect the rate at which the balls slow down
			this.tableFriction = (Double) jsonTable.get("friction");
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        } 
    }
    public Long getWidth(){
        return this.tableX;
    }

    public Long getHeight(){
        return this.tableY;
    }

    public Table create(){
        return new Table(tableX, tableY, 1/60, tableFriction, tableColour);
    }
}