package com.algs.queues;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {
    public static void main(String[] args) {
        if (args.length > 0) {
            int k = Integer.parseInt(args[0]);
            RandomizedQueue<String> rq = new RandomizedQueue<>();
            
            while (!StdIn.isEmpty()) {
                rq.enqueue(StdIn.readString());
            }

            for (int i = 0; i < k; ++i) {
                StdOut.println(rq.dequeue());
            }
        }
    }
}
