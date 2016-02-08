package com.algs.queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int n = 0;

    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        if (n == queue.length) {
            Item[] copy = (Item[]) new Object[2 * queue.length];

            for (int i = 0; i < n; ++i) {
                copy[i] = queue[i];
            }

            queue = copy;
        }

        queue[n++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int index = StdRandom.uniform(n);

        Item item = queue[index];
        queue[index] = null;

        /* Resize the array and packing the elements */
        if ((n - 1) > 0 && (n - 1) == queue.length / 4) {
            Item[] copy = (Item[]) new Object[queue.length / 2];

            for (int i = 0; i < n; ++i) {
                copy[i] = queue[i];
            }

            queue = copy;
        }

        queue[index] = queue[n - 1];
        queue[--n] = null;

        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return queue[StdRandom.uniform(n)];
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i = n;
        private Item[] var;

        public RandomizedQueueIterator() {
            var = (Item[]) new Object[n];

            for (int a = 0; a < n; ++a) {
                int b = StdRandom.uniform(a + 1);
                var[a] = var[b];
                var[b] = queue[a];
            }
        }

        public boolean hasNext() {
            return i > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return var[--i];
        }
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    public static void main(String[] args) {
    }
}
