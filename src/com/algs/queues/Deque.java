package com.algs.queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            /* No more items to return */
            if (current == null) {
                throw new NoSuchElementException();
            }

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return (first == null) || (last == null);
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        Node old = first;
        first = new Node();
        first.item = item;

        if (isEmpty()) {
            last = first;
        } else {
            first.next = old;
            first.prev = null;
            old.prev = first;
        }

        ++size;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        Node old = last;
        last = new Node();
        last.item = item;

        if (isEmpty()) {
            first = last;
        } else {
            last.prev = old;
            last.next = null;
            old.next = last;
        }

        ++size;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = first.item;

        if (first.next == null) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
        }

        --size;
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Item item = last.item;

        if (last.prev == null) {
            last = null;
            first = null;
        } else {
            last = last.prev;
            last.next = null;
        }

        --size;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    public static void main(String[] args) {
    }
}