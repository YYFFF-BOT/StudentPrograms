public class StrategyInPocket implements Strategy{
    @Override
    public Strategy copy() {
        return new StrategyInPocket();
    }

    @Override
    public void doStrategy(GameBall ball, Table ballPit) {
        ball.setxVel(0);
        ball.setyVel(0);
        ball.isAvailable = false;
        
    }
}
