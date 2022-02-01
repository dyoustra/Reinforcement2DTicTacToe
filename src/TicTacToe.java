// Danny Youstra
// AI
// Puzzle Checkpoint #3
// 11/18/21

import java.util.*;

public class TicTacToe {

    public static enum Player {
        ME('X') {
            @Override
            public Player other() {
                return Player.YOU;
            }
        },

        YOU('O') {
            @Override
            public Player other() {
                return Player.ME;
            }
        };

        private final char mark;

        private Player(char mark) {
            this.mark = mark;
        }

        public char mark() {
            return this.mark;
        }

        public abstract Player other();

    }


    public static class Action {
        private final int row;
        private final int col;
        private final Player player;
        public Action(int row, int col, Player player) {
            this.row = row;
            this.col = col;
            this.player = player;
        }
    }


    public static class State implements Game.State<Action> {

        public static final int N = 3;

        private final Player[] board;
        private final Player player;

        public State(Player player) {
             this.board = new Player[N*N];
             this.player = player.other();
        }

        public State(State state, Action move) {
            Player[] newBoard = state.board.clone();
            newBoard[move.row * N + move.col] = move.player;
            this.board = newBoard;
            this.player = move.player;
        }

        public int emptySquares() {
            int empty = 0;
            for (Player p : this.board) {
                if (p == null) empty++;
            }
            return empty;
        }

        public char getChar(int row, int col) {
            if (this.get(row, col) == null) return ' ';
            return this.get(row, col).mark;
        }

        public boolean isEmpty(int row, int col) {
            return this.get(row, col) == null;
        }

        public boolean wins(Player player) {
            if (player == Player.ME) return this.evaluate() == 1;
            return this.evaluate() == -1; // if player == Player.YOU
        }

        private Player get(int row, int col) {
            return this.board[row * N + col];
        }

        public int column(int index) {
            return index % N;
        }

        public int row(int index) {
            return index / N;
        }

        @Override
        public boolean isTerminal() {
            return this.evaluate() != 0 || this.emptySquares() == 0;
        }

        @Override
        public int evaluate() {
            for (int i = 0; i < this.board.length; i++) {
                if (this.board[i] != null) {
                    int numInARow = 1;
                    if (this.column(i) == 0) { // if at left of board (horizontal)
                        for (int j = 1; j < N; j++) {
                            if (this.board[i + j] == this.board[i]) numInARow++;
                            else break;
                        }
                        if(numInARow == N) return this.board[i] == Player.ME ? 1 : -1;
                        numInARow = 1;
                    }
                    if (this.row(i) == 0) { // if at top of board (vertical)
                        for (int j = N + i; j < this.board.length; j += N) {
                            if (this.board[j] == this.board[i]) numInARow++;
                            else break;
                        }
                        if(numInARow == N) return this.board[i] == Player.ME ? 1 : -1;
                        numInARow = 1;
                    }
                    if (i == 0) { // if at top left corner (diagonal)
                        for (int j = N+1; j < this.board.length; j += N+1) {
                            if (this.board[j] == this.board[i]) numInARow++;
                            else break;
                        }
                        if(numInARow == N) return this.board[i] == Player.ME ? 1 : -1;
                        numInARow = 1;
                    }
                    if (i == N-1) { // if at top right corner (diagonal)
                        for (int j = 2*N-2; j < this.board.length - N + 1; j += N-1) {
                            if (this.board[j] == this.board[i]) numInARow++;
                            else break;
                        }
                        if(numInARow == N) return this.board[i] == Player.ME ? 1 : -1;
                    }
                }
            }
            return 0; // tie / no 3 in a row
        }

        @Override
        public int hashCode() {
//            int hash = 0;
//            for (int i = 0; i < this.board.length; i++) {
//                if (this.board[i] == null) continue;
//                hash += Math.pow(9, i) * this.board[i].hashCode();
//            }
//            System.out.println(hash);
//            return hash;
            int banger = Arrays.deepHashCode(this.board);
            return banger;
        }

        @Override
        public State next(Action action) {
            return new State(this, action);
        }

        @Override
        public Iterable<Action> moves() {
            State state = this;
            return new Iterable<Action> () {
                @Override
                public Iterator<Action> iterator() {
                    return State.this.new MoveIterator(state);
                }
            };
        }

        @Override
        public String toString() {
            String result = "";
            String separator = " ";
            for (int row = 0; row < N; row++) {
                if (row > 0) {
                    result += " \n";
                    result += "---+---+---\n";
                    separator = " ";
                }
                for (int col = 0; col < N; col++) {
                    result += separator;
                    result += getChar(row, col);
                    separator = " | ";
                }
            }
            result += " \n";
            return result;
        }

        private class MoveIterator implements Iterator<Action> {

            private final State state;
            private int index;

            public MoveIterator(State state) {
                this.state = state;
                this.index = 0;
            }

            @Override
            public boolean hasNext() {
                while (index < this.state.board.length) {
                    if (this.state.isEmpty(this.state.row(index), this.state.column(index))) return true;
                    index++;
                }
                return false;
            }

            @Override
            public Action next() {
                return new Action(this.state.row(this.index), this.state.column(this.index++), this.state.player.other());
            }
        }
    }

    public class MatchBox {
        private State state;
        private ArrayList<Action> moves;

        public MatchBox(State state) {
            this.state = state;
            this.moves =
        }
    }

    public static void main(String[] args) {
        HashMap<State, ArrayList<Action>> boxes = new HashMap<>();

        for (int trainingRounds = 0; trainingRounds < 20000; trainingRounds++) {
            Random random = new Random();
            if (trainingRounds % 10000 == 0) System.out.println("check");
            State prevMeState = null;
            State prevYouState = null;
            Action prevMeMove = null;
            Action prevYouMove = null;
            State state = new State(Player.ME); // YOU move first
            while (!state.isTerminal()) {
                ArrayList<Action> moves = new ArrayList<>();
                if (!boxes.containsKey(state)) {
                    state.moves().forEach(moves::add);
                    boxes.put(state, moves);
                } else {
                    moves = boxes.get(state);
                }
                Action move = moves.get(random.nextInt(moves.size()));
//                if (move == null || moves.size() < 2) {
//                    System.out.println("bruh"); // TODO remove
//                }
                if (state.player == Player.YOU) {
                    prevMeMove = move;
                    prevMeState = state;
                }
                else if (state.player == Player.ME) {
                    prevYouMove = move;
                    prevYouState = state;
                }
                state = state.next(move);
            }
            if (state.wins(Player.ME)) {
                boxes.get(prevMeState).add(prevMeMove);
                boxes.get(prevYouState).remove(prevYouMove);
//                ArrayList<Action> debugMoves = boxes.get(prevYouState);
//                System.out.println(debugMoves.size());
            }
            else if (state.wins(Player.YOU)) {
                boxes.get(prevMeState).remove(prevMeMove);
                boxes.get(prevYouState).add(prevYouMove);
//                ArrayList<Action> debugMoves = boxes.get(prevMeState);
//                System.out.println(debugMoves.size());
            }
            // tie does nothing
        }

        State state = new State(Player.ME); // YOU move first
        Scanner scanner = new Scanner(System.in);
        for(String arg : args) {
            if (arg.equals("-ME")) state = new State(Player.YOU); // ME move first
        }

        while (!state.isTerminal()) {
            if (state.player == Player.ME) { // YOU move
                int row = 0;
                int col = 0;
                System.out.print("Row (1-3): ");
                row = scanner.nextInt() - 1;
                System.out.print("Col (1-3): ");
                col = scanner.nextInt() - 1;
                if (state.isEmpty(row, col)) {
                    state = state.next(new Action(row, col, Player.YOU));
                }
                else {
                    System.out.println("Square isn't empty");
                }
            }
            else {
                ArrayList<Action> moves = new ArrayList<>();
                if (!boxes.containsKey(state)) {
                    state.moves().forEach(moves::add);
                    boxes.put(state, moves);
                } else {
                    moves = boxes.get(state);
                }
                Random random = new Random();
                state = state.next(moves.get(random.nextInt(moves.size())));
            }
            System.out.println(state);
        }
        if (state.wins(Player.YOU)) {
            System.out.println("You won!!");
        }
        else if (state.wins(Player.ME)) {
            System.out.println("I won!!");
        }
        else {
            System.out.println("Tie!!");
        }
    }
}

