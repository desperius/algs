package com.algs.collinear;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Comparator;

public class FastCollinearPoints {

    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {

        if (points == null) {
            throw new NullPointerException();
        }

        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null) {
                throw new NullPointerException();
            }
        }
        
        if (points.length < 2) {
            segments = new LineSegment[0];
            return;
        }

        Point[] dots = new Point[points.length];
        System.arraycopy(points, 0, dots, 0, points.length);

        Arrays.sort(dots, 0, dots.length);
        Point[] sorted = new Point[dots.length];
        System.arraycopy(dots, 0, sorted, 0, dots.length);

        for (int i = 0; i < dots.length; ++i) {
            System.arraycopy(sorted, 0, dots, 0, sorted.length);
            sort(dots, dots[i].slopeOrder());

            double slope = dots[0].slopeTo(dots[1]);

            // Equal points
            if (Double.compare(slope, Double.NEGATIVE_INFINITY) == 0) {
                throw new IllegalArgumentException();
            }
        }

        System.arraycopy(sorted, 0, dots, 0, sorted.length);

        ArrayList<Point> line = new ArrayList<>();
        ArrayList<LineSegment> segs = new ArrayList<>();
        TreeMap<Point, ArrayList<Double>> slps = new TreeMap<>();

        int index = 0;

        while (dots.length > 3) {

            int i = 0;

            System.arraycopy(sorted, index, dots, 0, sorted.length - index);
            sort(dots, dots[i].slopeOrder());

            int k;

            for (int j = i + 1; j < dots.length - 1; j = k) {
                line.add(dots[i]);
                line.add(dots[j]);

                double slope = dots[i].slopeTo(dots[j]);

                k = j + 1;

                while (Double.compare(slope, dots[i].slopeTo(dots[k])) == 0) {
                    line.add(dots[k++]);

                    if (k > dots.length - 1) {
                        break;
                    }
                }

                if (line.size() > 3) {
                    Point min = Collections.min(line);
                    Point max = Collections.max(line);

                    // Existed
                    boolean existed = false;
                    if (slps.containsKey(max)) {
                        for (double val : slps.get(max)) {
                            if (Double.compare(val, slope) == 0) {
                                existed = true;
                            }
                        }
                    }

                    // Skip if point was already added like a end of segment
                    if (!existed) {
                        segs.add(new LineSegment(min, max));

                        if (!slps.containsKey(max)) {
                            slps.put(max, new ArrayList<>());
                        }
                        slps.get(max).add(slope);
                    }
                }

                line.clear();
            }

            Point[] arr = new Point[dots.length - 1];
            dots = arr;
            ++index;
        }

        segments = new LineSegment[segs.size()];
        segs.toArray(segments);

        line.clear();
        segs.clear();
        slps.clear();
    }

    // bottom-up mergesort
    private static void sort(Point[] a, Comparator<Point> comparator) {
        int N = a.length;
        Point[] aux = new Point[N];
        for (int n = 1; n < N; n = n+n) {
            for (int i = 0; i < N-n; i += n+n) {
                int lo = i;
                int m  = i+n-1;
                int hi = Math.min(i+n+n-1, N-1);
                merge(a, aux, lo, m, hi, comparator);
            }
        }
    }

    // Stably merge a[lo..m] with a[m+1..hi] using aux[lo..hi]
    private static void merge(Point[] a, Point[] aux, int lo, int m, int hi, Comparator<Point> comparator) {
        // copy to aux[]
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        // merge back to a[]
        int i = lo, j = m+1;
        for (int k = lo; k <= hi; k++) {
            if      (i > m)                a[k] = aux[j++];
            else if (j > hi)               a[k] = aux[i++];
            else if (less(comparator, aux[j], aux[i])) a[k] = aux[j++];
            else                           a[k] = aux[i++];
        }
    }

    // Is v < w ?
    private static boolean less(Comparator<Point> comparator, Point v, Point w) {
        return comparator.compare(v, w) < 0;
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        LineSegment[] arr = new LineSegment[segments.length];
        System.arraycopy(segments, 0, arr, 0, segments.length);
        return arr;
    }

    public static void main(String[] args) {

        // Read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // Draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // Print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }

    }
}
