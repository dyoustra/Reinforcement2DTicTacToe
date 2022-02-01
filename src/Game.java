// Danny Youstra
// AI
// TicTacToe Project #1
// 11/18/21

public class Game {

    public static interface State<Action> {
        public int evaluate();
        public boolean isTerminal();
        public State<Action> next(Action action);
        public Iterable<Action> moves();
    }
}
