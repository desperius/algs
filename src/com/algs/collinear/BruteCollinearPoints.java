package com.algs.collinear;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {

        if (points == null) {
            throw new NullPointerException();
        }

        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null) {
                throw new NullPointerException();
            }
        }

        // Input array must not mutate. Use a copy of it
        Point[] dots = new Point[points.length];
        System.arraycopy(points, 0, dots, 0, points.length);

        ArrayList<LineSegment> segs = new ArrayList<>();
        ArrayList<Point> line = new ArrayList<>();

        Arrays.sort(dots, 0, dots.length);

        double slope_q;
        double slope_r;
        double slope_s;

        for (int p = 0; p < dots.length; ++p) {

            for (int q = p + 1; q < dots.length; ++q) {
                slope_q = dots[p].slopeTo(dots[q]);

                // Equal points
                if (Double.compare(slope_q, Double.NEGATIVE_INFINITY) == 0) {
                    throw new IllegalArgumentException();
                }

                for (int r = q + 1; r < dots.length; ++r) {
                    slope_r = dots[p].slopeTo(dots[r]);

                    // Equal points
                    if (Double.compare(slope_r, Double.NEGATIVE_INFINITY) == 0) {
                        throw new IllegalArgumentException();
                    }

                    if (Double.compare(slope_q, slope_r) != 0) {
                        continue;
                    }

                    for (int s = r + 1; s < dots.length; ++s) {
                        slope_s = dots[p].slopeTo(dots[s]);

                        // Equal points
                        if (Double.compare(slope_s, Double.NEGATIVE_INFINITY) == 0) {
                            throw new IllegalArgumentException();
                        }

                        if (Double.compare(slope_q, slope_s) != 0) {
                            continue;
                        }

                        line.add(dots[p]);
                        line.add(dots[q]);
                        line.add(dots[r]);
                        line.add(dots[s]);

                        Point min = Collections.min(line);
                        Point max = Collections.max(line);

                        segs.add(new LineSegment(min, max));
                        line.clear();
                    }
                }
            }
        }

        segments = new LineSegment[segs.size()];
        segs.toArray(segments);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
