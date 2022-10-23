public interface Strategy {
    Strategy copy();
    void doStrategy(GameBall ball,Table ballPit);
}
