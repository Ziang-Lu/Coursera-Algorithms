/**
 * Given an array of points, find the closest pair.
 *
 * Naive implementation: O(n^2)
 */

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ClosestPairFinder {

    /**
     * Finds the closest pair in the given 1D points.
     * @param points given 1D points
     * @return closest pair
     */
    private double[] findClosestPair1D(double[] points) {
        // Check whether the input point array is null or empty
        if ((points == null) || (points.length == 0)) {
            return new double[0];
        }

        if (points.length == 1) {
            return new double[0];
        }

        // Sort the points   [O(nlog n)]
        Arrays.sort(points);
        // The closest pair must be adjacent in the ordering.

        // Iterate over the adjacent pairs, and find closest pair   [O(n)]
        double p1 = points[0], p2 = points[1];
        double minDistance = points[1] - points[0];
        for (int i = 1; i < (points.length - 1); ++i) {
            if ((points[i + 1] - points[i]) < minDistance) {
                p1 = points[i];
                p2 = points[i + 1];
                minDistance = points[i + 1] - points[i];
            }
        }
        return new double[]{p1, p2};
        // Overall running time complexity: O(nlog n), better than O(n^2)
    }

    /**
     * Finds the closest pair in the given 2D points.
     * @param points given 2D points
     * @return closest pair
     */
    private Point2D.Double[] findClosestPair2D(Point2D.Double[] points) {
        // Check whether the input point array is null or empty
        if ((points == null) || (points.length == 0)) {
            return new Point2D.Double[0];
        }

        if (points.length == 1) {
            return new Point2D.Double[0];
        }

        // Pre-processing
        // Make a copy of the points sorted by x (Px)   [O(nlog n)]
        Point2D.Double[] Px = new Point2D.Double[points.length];
        System.arraycopy(points, 0, Px, 0, points.length);
        Arrays.sort(Px, new Comparator<Point2D.Double>() {
            @Override
            public int compare(Point2D.Double o1, Point2D.Double o2) {
                return Double.compare(o1.getX(), o2.getX());
            }
        });
        // Make a copy of the points sorted by y (Py)   [O(nlog n)]
        Point2D.Double[] Py = new Point2D.Double[points.length];
        System.arraycopy(points, 0, Py, 0, points.length);
        Arrays.sort(Py, new Comparator<Point2D.Double>() {
            @Override
            public int compare(Point2D.Double o1, Point2D.Double o2) {
                return Double.compare(o1.getY(), o2.getY());
            }
        });

        return findClosestPair2DHelper(Px, Py);
        // Overall running time complexity: O(nlog n), better than O(n^2)
    }

    /**
     * Private helper method to find the closest pair in the given 2D points
     * recursively.
     * @param Px given 2D points sorted by x
     * @param Py given 2D points sorted by y
     * @return closest pair
     */
    private Point2D.Double[] findClosestPair2DHelper(Point2D.Double[] Px, Point2D.Double[] Py) {
        int nPoint = Px.length;
        // Base case 1: 2 points
        if (nPoint == 2) {
            return Px;
        }
        // Base case 2: 3 points
        if (nPoint == 3) {
            double distance1 = Px[0].distance(Px[1]), distance2 = Px[1].distance(Px[2]),
                    distance3 = Px[2].distance(Px[0]);
            double minDistance = Math.min(Math.min(distance1, distance2), distance3);
            if (minDistance == distance1) {
                return new Point2D.Double[]{Px[0], Px[1]};
            } else if (minDistance == distance2) {
                return new Point2D.Double[]{Px[1], Px[2]};
            } else {
                return new Point2D.Double[]{Px[2], Px[0]};
            }
        }

        // Recursive case
        // [Divide]
        // Let Q be the left half of P, and R be the right half of P
        Point2D.Double[] Qx = new Point2D.Double[nPoint / 2], Qy = new Point2D.Double[nPoint / 2];
        System.arraycopy(Px, 0, Qx, 0, nPoint / 2);
        Point2D.Double[] Rx = new Point2D.Double[nPoint - nPoint / 2], Ry = new Point2D.Double[nPoint - nPoint / 2];
        System.arraycopy(Px, nPoint / 2, Rx, 0, nPoint - nPoint / 2);
        // To create Qy and Ry, iterate over Py: if x is smaller than or equal to the threshold, put the point in Qy;
        // otherwise put it in Ry
        double xThreshold = Qx[Qx.length - 1].getX();
        int QyPtr = 0, RyPtr = 0;
        for (Point2D.Double p : Py) {
            if (p.getX() <= xThreshold) {
                Qy[QyPtr] = p;
                ++QyPtr;
            } else {
                Ry[RyPtr] = p;
                ++RyPtr;
            }
        }
        // [Conquer]
        Point2D.Double[] leftClosestPair = findClosestPair2DHelper(Qx, Qy);
        Point2D.Double[] rightClosestPair = findClosestPair2DHelper(Rx, Ry);
        // Combine the results
        double leftClosestDistance = leftClosestPair[0].distance(leftClosestPair[1]),
                rightClosestDistance = rightClosestPair[0].distance(rightClosestPair[1]);
        double delta = leftClosestDistance;
        Point2D.Double[] deltaPair = leftClosestPair;
        if (leftClosestDistance > rightClosestDistance) {
            delta = rightClosestDistance;
            deltaPair = rightClosestPair;
        }
        Point2D.Double[] closerSplitPair = findCloserSplitPair(Py, xThreshold, delta);
        if (closerSplitPair != null) {
            return closerSplitPair;
        }
        return deltaPair;
        // T(n) = 2T(n/2) + O(n)
        // a = 2, b = 2, d = 1
        // According to Master Method, the running time complexity is O(nlog n).
    }

    /**
     * Helper method to find the closest closer split pair in the given 2D
     * points.
     * @param Py given 2D points sorted by y
     * @param xThreshold maximum x in the left-half
     * @param delta closest distance among non-split pairs
     * @return closer closest split pair
     */
    private Point2D.Double[] findCloserSplitPair(Point2D.Double[] Py, double xThreshold, double delta) {
        // Filtering
        double lowerBound = xThreshold - delta, upperBound = xThreshold + delta;
        // Let Sy be the points of P with x within the range
        List<Point2D.Double> Sy = new ArrayList<>();
        // To create Sy: iterate over Py: if x is within the range, put the point in Sy
        for (Point2D.Double p : Py) {
            if ((p.getX() > lowerBound) && (p.getX() < upperBound)) {
                Sy.add(p);
            }
        }
        // Iterate over Sy, and for each point, look at its at most 7 subsequent points, and find the closest closer
        // split pair
        Point2D.Double p1 = null, p2 = null;
        double closerDistance = delta;
        for (int i = 0; i < (Sy.size() - 1); ++i) {
            int numToLook = Math.min(7, Sy.size() - 1 - i);
            for (int j = 1; j <= numToLook; ++j) {
                if (Sy.get(i).distance(Sy.get(i + j)) < closerDistance) {
                    p1 = Sy.get(i);
                    p2 = Sy.get(i + j);
                    closerDistance = Sy.get(i).distance(Sy.get(i + j));
                }
            }
        }
        // Return the closest closer split pair accordingly
        if (closerDistance == delta) {
            return null;
        }
        return new Point2D.Double[]{p1, p2};
        // Running time complexity: O(n)
    }

}
