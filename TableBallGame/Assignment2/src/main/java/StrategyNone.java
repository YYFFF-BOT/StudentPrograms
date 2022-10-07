public class StrategyNone implements Strategy{
    @Override
    public Strategy copy() {
        return new StrategyNone();
    }

    @Override
    public void doStrategy(GameBall ball, Table ballPit) {

    }
}
