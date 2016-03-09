package com.algs.puzzle;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

public class Board {

    private int N;
    private int[][] board;

    public Board(int[][] blocks) {

        N = blocks[0].length;

        board = new int[N][N];

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                board[i][j] = blocks[i][j];
            }
        }
    }

    public int dimension() {
        return N;
    }

    public int hamming() {
        int error = 0;

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                int val = board[i][j];
                if (val != (i * N + j) + 1 && val != 0) {
                    ++error;
                }
            }
        }

        return error;
    }

    public int manhattan() {
        int error = 0;

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                int val = board[i][j];

                if (val != 0) {
                    int row = (val - 1) / N;
                    int col = (val - 1) % N;
                    error += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }

        return error;
    }

    public boolean isGoal() {

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (board[i][j] != (i * N + j) + 1 && (i != N - 1 || j != N - 1)) {
                    return false;
                }
            }
        }

        return true;
    }

    public Board twin() {
        Board newB = new Board(board);
        for (int i = N - 1; i >= 0; --i) {
            for (int j = N - 2; j >= 0; --j) {
                if (newB.board[i][j] != 0 && newB.board[i][j + 1] != 0) {
                    newB.exch(i, j, i, j + 1);
                    return newB;
                }
            }
        }
        return null;
    }

    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }

        if (y == null) {
            return false;
        }

        if (this.getClass() != y.getClass()) {
            return false;
        }

        Board that = (Board) y;

        if (that.board.length != N || that.board[0].length != N) {
            return false;
        }

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (board[i][j] != that.board[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        if (N < 2) {
            return null;
        }

        ArrayList<Board> neighbors = new ArrayList<>();

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (board[i][j] == 0) {
                    // Top block from empty frame
                    if (i > 0) {
                        neighbors.add(getNeighbor(i, j, i - 1, j));
                    }

                    // Bottom block from empty frame
                    if (i < N - 1) {
                        neighbors.add(getNeighbor(i, j, i + 1, j));
                    }

                    // Left block from empty frame
                    if (j > 0) {
                        neighbors.add(getNeighbor(i, j, i, j - 1));
                    }

                    // Right block from empty frame
                    if (j < N - 1) {
                        neighbors.add(getNeighbor(i, j, i, j + 1));
                    }

                    return neighbors;
                }
            }
        }

        return null;
    }

    public String toString() {
//        String s = "" + N + "\n";
//
//        for (int i = 0; i < N; ++i) {
//            s += " ";
//            for (int j = 0; j < N; ++j) {
//                s += board[i][j] + " ";
//            }
//
//            s += "\n";
//        }
//        return s;

        StringBuffer buf =  new StringBuffer();
        buf.append("" + N + "\n");

        for (int i = 0; i < N; ++i) {
            buf.append(" ");
            for (int j = 0; j < N; ++j) {
                buf.append(board[i][j] + " ");
            }

            buf.append("\n");
        }

        return buf.toString();
    }


    private void exch(int i0, int j0, int i1, int j1) {
        int tmp = board[i0][j0];
        board[i0][j0] = board[i1][j1];
        board[i1][j1] = tmp;
    }

    private Board getNeighbor(int i0, int j0, int i1, int j1) {
        Board neighbor = new Board(board);
        neighbor.exch(i0, j0, i1, j1);
        return neighbor;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                blocks[i][j] = in.readInt();
            }
        }

        Board board = new Board(blocks);
        System.out.println("dimension: " + board.dimension());
        System.out.println("hamming: " + board.hamming());
        System.out.println("manhattan: " + board.manhattan());
        System.out.println("goal: " + board.isGoal());
        System.out.println(board);
        System.out.println("neighbors: ");
        Iterable<Board> it = board.neighbors();
        for (Board b : it) {
            System.out.println(b);
        }
        System.out.println("twin: ");
        System.out.println(board.twin());
    }
}
