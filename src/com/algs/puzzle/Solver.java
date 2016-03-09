package com.algs.puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private MinPQ<Node> pq = new MinPQ<>();
    private int minMoves = -1;
    private Node bestNode;
    private boolean solvable = false;

    private class Node implements Comparable<Node> {
        private Board board;
        private int moves;
        private int dist;
        private Node prev;

        public Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev  = prev;
            dist = board.manhattan();
        }

        @Override
        public int compareTo(Node that) {
            return this.moves + this.dist - that.moves - that.dist;
        }
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException();
        }

        pq.insert(new Node(initial, 0, null));
        pq.insert(new Node(initial.twin(), 0, null));

        while (!pq.isEmpty()) {
            Node curr = pq.delMin();

            if (curr.board.isGoal()) {
                Node root = root(curr);

                if (!root.board.equals(initial)) {
                    break;
                }

                solvable = true;

                if (minMoves == -1 || curr.moves + curr.dist < minMoves) {
                    minMoves = curr.moves;
                    bestNode = curr;
                }
            }

            if (minMoves == -1 || curr.moves + curr.dist < minMoves) {
                Iterable<Board> it = curr.board.neighbors();

                for (Board b : it) {
                    if (curr.prev == null || !b.equals(curr.prev.board)) {
                        pq.insert(new Node(b, curr.moves + 1, curr));
                    }
                }
            } else {
                break;
            }
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return minMoves;
    }

    public Iterable<Board> solution() {
        if (isSolvable()) {
            Stack<Board> sol = new Stack<>();

            Node curr = bestNode;

            while (curr != null) {
                sol.push(curr.board);
                curr = curr.prev;
            }

            return sol;
        }

        return null;
    }

    private Node root(Node node) {
        Node curr = node;

        while (curr.prev != null) {
            curr = curr.prev;
        }

        return curr;
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // Solve the puzzle
        Solver solver = new Solver(initial);

        // Print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }

    }
}
