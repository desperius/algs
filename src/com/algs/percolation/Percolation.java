package com.algs.percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdRandom;

public class Percolation {
    private Site[] grid;
    private int n = 0;
    private WeightedQuickUnionUF wuf;

    private enum Site {
        BLOCK, OPEN, FULL
    }

    public Percolation(int N) {
        // Throw exception if N <= 0
        if (N <= 0) {
            throw new IllegalArgumentException();
        }

        n = N;
        int size = n * n + 2;
        grid = new Site[size];

        grid[0] = Site.FULL;
        grid[size - 1] = Site.OPEN;

        for (int i = 1; i < size - 1; ++i) {
            grid[i] = Site.BLOCK;
        }

        wuf = new WeightedQuickUnionUF(size);

        // Connect ot the first cell (top)
        for (int i = 1; i <= n; ++i) {
            wuf.union(i, 0);
        }

        // Connect ot the last cell (bottom)
        for (int i = size - n - 1; i <= size - 2; ++i) {
            wuf.union(i, size - 1);
        }
    }

    public void open(int i, int j) {
        // Throw exception if indexes out of range
        if (i <= 0 || i > n) {
            throw new IndexOutOfBoundsException();
        }

        if (j <= 0 || j > n) {
            throw new IndexOutOfBoundsException();
        }

        --i;
        --j;
        int p = (i * n + j) + 1;
        int size = n * n + 2;

        // Open blocked site
        if (grid[p] == Site.BLOCK) {
            grid[p] = Site.OPEN;

            // Check neighbor sites

            // Up cell
            int q = ((i - 1) * n + j) + 1;
            if ((i - 1) >= 0 && q > 0 && q < size - 1 && grid[q] == Site.OPEN) {
                wuf.union(p, q);
            }

            // Bottom cell
            q = ((i + 1) * n + j) + 1;
            if ((i + 1) < n && q > 0 && q < size - 1 && grid[q] == Site.OPEN) {
                wuf.union(p, q);
            }

            // Left cell
            q = i * n + (j - 1) + 1;
            if ((j - 1) >= 0 && q > 0 && q < size - 1 && grid[q] == Site.OPEN) {
                wuf.union(p, q);
            }

            // Right cell
            q = i * n + (j + 1) + 1;
            if ((j + 1) < n && q > 0 && q < size - 1 && grid[q] == Site.OPEN) {
                wuf.union(p, q);
            }
        }
    }

    public boolean isOpen(int i, int j) {
        // Throw exception if indexes out of range
        if (i <= 0 || i > n) {
            throw new IndexOutOfBoundsException();
        }

        if (j <= 0 || j > n) {
            throw new IndexOutOfBoundsException();
        }

        --i;
        --j;
        int index = (i * n + j) + 1;

        return grid[index] == Site.OPEN;
    }

    public boolean isFull(int i, int j) {
        // Throw exception if indexes out of range
        if (i <= 0 || i > n) {
            throw new IndexOutOfBoundsException();
        }

        if (j <= 0 || j > n) {
            throw new IndexOutOfBoundsException();
        }

        --i;
        --j;
        int index = (i * n + j) + 1;

        if (grid[index] == Site.OPEN) {
            return wuf.connected(index, 0);
        }
        else {
            return false;
        }
    }

    public boolean percolates() {
        if (n == 1) {
            return isOpen(1, 1);
        }
        else {
            return wuf.connected((n * n + 2) - 1, 0);
        }
    }

//    public void show() {
//        System.out.println("*");
//
//        for (int i = 1; i <= n; ++i) {
//            for (int j = 1; j <= n; ++j) {
//
//                if (isFull(i, j)) {
//                    System.out.print("* ");
//                }
//                else if (isOpen(i, j)) {
//                    System.out.print("0 ");
//                }
//                else {
//                    System.out.print("X ");
//                }
//            }
//
//            System.out.print("\n");
//        }
//
//        System.out.println("0");
//    }

//    public static void main(String[] args) {
//        int N = 3;
//        Percolation p = new Percolation(N);
//        p.show();
//
//        while (!p.percolates()) {
//            Random rand = new Random();
//            int i = rand.nextInt(N) + 1;
//            int j = rand.nextInt(N) + 1;
//            p.open(i, j);
//            p.show();
//        }
//    }
}
