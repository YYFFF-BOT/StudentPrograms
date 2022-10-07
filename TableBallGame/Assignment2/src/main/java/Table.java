import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Table {
    public static Table currentBallPit;
    private final double height;
    private final double width;
    private List<GameItem> gameItems = new ArrayList<>();
    private List<GameBall> gameBalls = new ArrayList<>();
    private List<GameItem> gameItemsCopy = new ArrayList<>();
    private boolean reSet = false;
    private double friction;
    private String colour;

    public String getColour(){
        return colour;
    }

    public boolean isGameOver(){
        if(getGameBalls().size() == 1 && getGameBalls().get(0).getColour().equals(Paint.valueOf("WHITE"))){
            return true;
        }
        return false;
    }

    public boolean readyForNextHit(){
        for(GameBall ball:getGameBalls()){
            if(ball.isMoving()){
                return false;
            }
        }
        return true;
    }

    public void addItem(GameItem item){
        this.gameItems.add(item);
        this.gameItemsCopy.add(item.copy());
    }

    public List<GameBall> getGameBalls(){
        for(GameItem item:gameItems){
            if(item instanceof GameBall ){
                if(!gameBalls.contains(item)){
                    this.gameBalls.add((GameBall) item);
                }        
            }
        } 
        // Use iterator to avoid "ConcurrentModificationException" 
        for(Iterator<GameBall> it = gameBalls.iterator();it.hasNext();){
            GameBall ball = it.next();
            if(!ball.isAvailable()){
                it.remove();
            }
        }
        return this.gameBalls;
    }

    Table(double width, double height, double frameDuration,double friction,String colour) {
        this.height = height;
        this.width = width;
        this.colour=colour;
        currentBallPit=this;
        this.friction = friction;
    }

    double getHeight() {
        return height;
    }

    double getWidth() {
        return width;
    }

    void tick() {
        if(this.isGameOver()){
            return;
        }
        if(reSet){
            reSet = false;
            this.gameItems.clear();
            this.gameBalls.clear();
            for(GameItem item:this.gameItemsCopy){
                item.setxVel(0);
                item.setyVel(0);
                this.gameItems.add(item.copy());
            }
        }
        
        List<GameItem> removeItems = new ArrayList<>();
        for(GameItem item: gameItems) {
            item.tick();
            
            // Handle the edges (balls don't get a choice here)
            if (item.getxPos() + item.getRadius() > width) {
                item.setxPos(width - item.getRadius());
                item.setxVel(item.getxVel() * -1);
            }
            if (item.getxPos() - item.getRadius() < 0) {
                item.setxPos(0 + item.getRadius());
                item.setxVel(item.getxVel() * -1);
            }
            if (item.getyPos() + item.getRadius() > height) {
                item.setyPos(height - item.getRadius());
                item.setyVel(item.getyVel() * -1);
            }
            if (item.getyPos() - item.getRadius() < 0) {
                item.setyPos(0 + item.getRadius());
                item.setyVel(item.getyVel() * -1);
            }

            StrategyContext strategyExcuter = new StrategyContext();
            
            for(GameItem ball: gameItems) {
                if(ball instanceof GameBall){
                    if(checkCollision(item, ball)) {
                        ((GameBall) ball).handleCollision(item);
                    }
                    if(ball.getColour().equals(Paint.valueOf("WHITE"))){
                        strategyExcuter.setStrategy(new StrategyReset());
                    }
                    if(ball.getColour().equals(Paint.valueOf("RED"))){
                        strategyExcuter.setStrategy(new StrategyInPocket());
                    }
                    if(ball.getColour().equals(Paint.valueOf("BLUE"))){
                        strategyExcuter.setStrategy(new StrategyInPokectTwice());
                    }
                    strategyExcuter.executeStrategy((GameBall)ball, this);
                }
                
            }

            if(!item.isAvailable()){
                removeItems.add(item);
            }
            // Handle friction
            if(item instanceof GameBall) {
                item.setyVel(item.getyVel() *Math.pow(0.99,this.friction * ((GameBall) item).getMass()));
                item.setxVel(item.getxVel() *Math.pow(0.99,this.friction * ((GameBall) item).getMass()));
                // If ball is slow enough, make it stop
                if(Math.abs(item.getxVel())<this.friction/20) {
                    item.setxVel(0);
                }
                if(Math.abs(item.getyVel())<this.friction/20) {
                    item.setyVel(0);
                }
            }
        }

        // Remove the unavailable
        for(GameItem removeItem:removeItems){
            gameItems.remove(removeItem);
        }
        
    }

    List<GameItem> getEntities() {
        return gameItems;
    }
    private double startMouseXPos,startMouseYPos;

    public void startShot(double xPos,double yPos){
        System.out.println("Start");
        startMouseXPos = xPos;
        startMouseYPos = yPos;
    }

    public void endShot(double xPos,double yPos){
        double yDis = yPos - startMouseYPos;
        double xDis = xPos - startMouseXPos;
        
        for(GameBall ball:this.gameBalls){
            if(ball.getColour().equals(Paint.valueOf("WHITE"))){
                if(!ball.isMoving()){
                    ball.setxVel(-xDis/10);
                    ball.setyVel(-yDis/10);
                }
                    
            }
        }
        System.out.println("Start");
    }

    public boolean checkCollision(GameItem itemA, GameItem itemB) {
        if (itemA.equals(itemB)) {
            return false;
        }
        return Math.abs(itemA.getxPos() - itemB.getxPos()) < itemA.getRadius() + itemB.getRadius() &&
                Math.abs(itemA.getyPos() - itemB.getyPos()) < itemA.getRadius() + itemB.getRadius();
    }

    public void reSetGame() {
        reSet = true;
    }
}
