package com.algs.kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {

    private SET<Point2D> points;

    public PointSET() {
        points = new SET<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        check(p);

        points.add(p);
    }

    public boolean contains(Point2D p) {
        check(p);

        return points.contains(p);
    }

    public void draw() {
        for (Point2D pnt : points) {
            pnt.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> r = new ArrayList<>();

        for (Point2D pnt : points) {
            if (rect.contains(pnt)) {
                r.add(pnt);
            }
        }

        return r;
    }

    public Point2D nearest(Point2D p) {
        check(p);

        if (!points.isEmpty()) {
            double min_dist = Double.MAX_VALUE;
            Point2D neighbor = null;

            for (Point2D pnt : points) {
                double dist = pnt.distanceTo(p);

                if (dist < min_dist) {
                    min_dist = dist;
                    neighbor = pnt;
                }
            }

            return neighbor;
        } else {
            return null;
        }
    }

    private void check(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        }
    }

    public static void main(String[] args) {
    }
}
