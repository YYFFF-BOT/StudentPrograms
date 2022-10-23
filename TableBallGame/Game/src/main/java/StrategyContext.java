public class StrategyContext {
    private Strategy strategy;
    public void setStrategy(Strategy strategy){
        this.strategy = strategy;
    }

    public void executeStrategy(GameBall ball, Table ballPit){
        strategy.doStrategy(ball, ballPit);
    }
}
