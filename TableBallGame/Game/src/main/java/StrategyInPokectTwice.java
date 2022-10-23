public class StrategyInPokectTwice implements Strategy{
    private int chance = 1;
    @Override
    public Strategy copy() {
        return new StrategyInPokectTwice();
    }
    @Override
    public void doStrategy(GameBall ball, Table ballPit) {
        if(this.chance>0) {
            ball.setxVel(0);
            ball.setyVel(0);
            
            this.chance--;
            ball.setxPos(ball.getInitStartX());
            ball.setyPos(ball.getInitStartY());
            for(GameBall otherball:ballPit.getGameBalls()){
                if(ballPit.checkCollision(ball, otherball)){
                    ball.setAvailable(false);
                }
            }
        }else{
            ball.setxVel(0);
            ball.setyVel(0);
            ball.setAvailable(false);
            
        }
    }
}
