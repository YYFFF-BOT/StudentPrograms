public class StrategyReset implements Strategy{
    @Override
    public void doStrategy(GameBall ball, Table ballPit) {
        ballPit.reSetGame();
    }
    @Override
    public Strategy copy() {
        return new StrategyReset();
    }
}
