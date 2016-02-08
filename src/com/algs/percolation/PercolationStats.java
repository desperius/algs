package com.algs.percolation;

import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private Percolation percolation;
    private int t;
    private double mu;
    private double sigma;
    private double[] x;

    public PercolationStats(int N, int T) {
        // Throw exception if N <= 0 or T <= 0
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }

        t = T;
        x = new double[T];
        int size = N * N;

        for (int test = 0; test < T; ++test) {
            percolation = new Percolation(N);
            int count = 0;

            while (!percolation.percolates()) {
                int i = StdRandom.uniform(N) + 1;
                int j = StdRandom.uniform(N) + 1;

                if (!percolation.isOpen(i, j)) {
                    percolation.open(i, j);
                    ++count;
                }
            }

            x[test] = (double) count / size;
        }
    }

    public double mean() {
        mu = StdStats.mean(x);
        return mu;
    }

    public double stddev() {
        sigma = StdStats.stddev(x);
        return sigma;
    }

    public double confidenceLo() {
        return mu - ((1.96 * sigma) / Math.sqrt(t));
    }

    public double confidenceHi() {
        return mu + ((1.96 * sigma) / Math.sqrt(t));
    }

    public static void main(String[] args) {
        if (args.length >= 2) {
            int N = Integer.parseInt(args[0]);
            int T = Integer.parseInt(args[1]);
            PercolationStats ps = new PercolationStats(N, T);
            StdOut.println("mean                    = " + ps.mean());
            StdOut.println("stddev                  = " + ps.stddev());
            StdOut.println("95% confidence interval = " + ps.confidenceLo() + ", " + ps.confidenceHi());
        }
    }
}
