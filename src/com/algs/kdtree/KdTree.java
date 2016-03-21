package com.algs.kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {

    private enum Orientation {
        // Left / Right
        LR,

        // Above / Below
        AB;

        public Orientation next() {
            if (this.equals(Orientation.AB)) {
                return Orientation.LR;
            }

            return Orientation.AB;
        }
    }

    private static class Node {
        private Point2D p;
        private RectHV  rect;
        private Node lb;
        private Node rt;

        public Node(Point2D p) {
            this.p = p;
        }
    }

    private Node root;
    private int size = 0;

    public KdTree() {
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        check(p);

        if (isEmpty()) {
            root = new Node(p);
            root.rect = new RectHV(0, 0, 1, 1);
            ++size;
            return;
        }

        root = put(root, p, Orientation.LR);
    }

    private Node put(Node x, Point2D p, Orientation orientation) {
        if (x == null) {
            ++size;
            return new Node(p);
        }

        if (x.p.equals(p)) {
            return x;
        }

        int cmp = compare(p, x.p, orientation);
        Orientation nextOrientation = orientation.next();

        if (cmp < 0) {
            x.lb = put(x.lb, p, nextOrientation);

            if (x.lb.rect == null) {
                if (orientation == Orientation.LR) {
                    x.lb.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax());
                } else {
                    x.lb.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y());
                }
            }
        } else {
            x.rt = put(x.rt, p, nextOrientation);

            if (x.rt.rect == null) {
                if (orientation == Orientation.LR) {
                    x.rt.rect = new RectHV(x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
                } else {
                    x.rt.rect = new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax());
                }
            }
        }

        return x;
    }

    private int compare(Point2D p, Point2D q, Orientation orientation) {
        if (orientation == Orientation.LR) {
            return Double.compare(p.x(), q.x());
        } else {
            return Double.compare(p.y(), q.y());
        }
    }

    public boolean contains(Point2D p) {
        check(p);

        return contains(root, p, Orientation.LR);
    }

    private boolean contains(Node x, Point2D p, Orientation orientation) {
        if (x == null) {
            return false;
        }

        if (x.p.equals(p)) {
            return true;
        }

        int cmp = compare(p, x.p, orientation);
        Orientation nextOrientation = orientation.next();

        if (cmp < 0) {
            return contains(x.lb, p, nextOrientation);
        } else {
            return contains(x.rt, p, nextOrientation);
        }
    }

    public void draw() {
        draw(root, Orientation.LR);
    }

    private void draw(Node x, Orientation orientation) {
        if (x == null) {
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();

        if (orientation == Orientation.LR) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.001);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.001);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }

        Orientation next = orientation.next();
        draw(x.lb, next);
        draw(x.rt, next);
    }

    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> r = new ArrayList<>();

        if (!isEmpty()) {
            findPoints(r, rect, root);
        }

        return r;
    }

    private void findPoints(ArrayList<Point2D> r, RectHV rect, Node x) {
        if (!rect.intersects(x.rect)) {
            return;
        }

        if (rect.contains(x.p)) {
            r.add(x.p);
        }

        if (x.lb != null) {
            findPoints(r, rect, x.lb);
        }

        if (x.rt != null) {
            findPoints(r, rect, x.rt);
        }
    }

    public Point2D nearest(Point2D p) {
        check(p);

        if (isEmpty()) {
            return null;
        }

        return findNearest(root, p, root.p, Double.MAX_VALUE, Orientation.LR);
    }

    private Point2D findNearest(Node x, Point2D p, Point2D nearest, double minDist, Orientation orientation) {
        if (x == null) {
            return nearest;
        }
        Point2D closest = nearest;
        double closestDistance = minDist;
        double distance = x.p.distanceSquaredTo(p);
        if (distance < minDist) {
            closest = x.p;
            closestDistance = distance;
        }
        Node first, second;
        if (orientation == Orientation.LR) {
            if (p.x() < x.p.x()) {
                first = x.lb;
                second = x.rt;
            } else {
                first = x.rt;
                second = x.lb;
            }
        } else {
            if (p.y() < x.p.y()) {
                first = x.lb;
                second = x.rt;
            } else {
                first = x.rt;
                second = x.lb;
            }
        }
        Orientation nextOrientation = orientation.next();
        if (first != null && first.rect.distanceSquaredTo(p) < closestDistance) {
            closest = findNearest(first, p, closest, closestDistance,
                    nextOrientation);
            closestDistance = closest.distanceSquaredTo(p);
        }
        if (second != null
                && second.rect.distanceSquaredTo(p) < closestDistance) {
            closest = findNearest(second, p, closest, closestDistance,
                    nextOrientation);
        }

        return closest;
    }

    private void check(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
    }

    public static void main(String[] args) {
    }
}
