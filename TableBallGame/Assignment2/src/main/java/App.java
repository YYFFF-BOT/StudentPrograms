import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String path = "config.json";
        /*
         // Create balls
        FactoryBall factoryBall = new FactoryBall(path);
        List<GameBall> balls = factoryBall.create();

        // Create ballPit
        FactoryTable factoryTable = new FactoryTable(path);
        Table table = factoryTable.create();
        for(GameBall ball:balls){
            System.out.println("added");
            table.addItem(ball);
        }
        Long width = factoryTable.getWidth();
        Long height = factoryTable.getHeight();
         */
        

        
        ConfigReader configReader = new ConfigReader("config.json");
        Long width =configReader.getWidth();
        Long height = configReader.getHeight();
        Table table = new Table(width, height, 1.0/60,configReader.getTableFriction(),configReader.getTableColour());
        // Set up balls
        for(GameBall ball:configReader.getBalls()){
            table.addItem(ball);
        }
         

        // Set up pockets
        Paint pocktColour = Paint.valueOf("BLACK");
        FactoryPocket factoryPocket = new FactoryPocket();
        table.addItem(factoryPocket.create(0, 0, 20,pocktColour , 0));
        table.addItem(factoryPocket.create(width/2, 0, 20,pocktColour , 0));
        table.addItem(factoryPocket.create(width, 0, 20,pocktColour , 0));
        table.addItem(factoryPocket.create(width/2, height, 20,pocktColour , 0));
        table.addItem(factoryPocket.create(0, height, 20,pocktColour , 0));
        table.addItem(factoryPocket.create(width, height, 20,pocktColour , 0));
        

        // Set up game window
        GameWindow window = new GameWindow(table);
        window.run();
        primaryStage.setTitle("Pool Table Game");
        primaryStage.setScene(window.getScene());
        primaryStage.show();
        // Set up mouse event handler
        EventHandler<MouseEvent> pressHandler = new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent e) {
                table.startShot(e.getX(),e.getY());
            }
        };
        EventHandler<MouseEvent> releaseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                table.endShot(e.getX(),e.getY());
            }

        };

        primaryStage.addEventFilter(MouseEvent.MOUSE_PRESSED, pressHandler);
        primaryStage.addEventFilter(MouseEvent.MOUSE_RELEASED, releaseHandler);
        window.run();
    }
}
