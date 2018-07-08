import java.math.BigInteger;

public class Karatsuba {

    /**
     * Calculates the multiplication of two integers using Karatsuba
     * Multiplication.
     * Naive calculation: O(n^2)
     * @param x first operand
     * @param y second operand
     * @return product
     */
    private BigInteger karatsuba(BigInteger x, BigInteger y) {
        // We assume the input x and y are both non-negative.

        String xStr = x.toString(), yStr = y.toString();

        // Pad leading zeros to make x and y of the same length   [O(n)]
        if (xStr.length() > yStr.length()) {
            yStr = padZeros(yStr, xStr.length() - yStr.length(), true);
        } else if (xStr.length() < yStr.length()) {
            xStr = padZeros(xStr, yStr.length() - xStr.length(), true);
        }

        int n = xStr.length();
        // Base case
        if (n == 1) {
            return x.multiply(y);
        }
        // Recursive case
        // [Divide]   [O(n)]
        String aStr = xStr.substring(0, n / 2), bStr = xStr.substring(n / 2), cStr = yStr.substring(0, n / 2),
                dStr = yStr.substring(n / 2);
        BigInteger a = new BigInteger(aStr), b = new BigInteger(bStr), c = new BigInteger(cStr),
                d = new BigInteger(dStr);
        // [Conquer]
        BigInteger ac = karatsuba(new BigInteger(aStr), c);
        BigInteger bd = karatsuba(b, d);
        BigInteger adBC = karatsuba(a.add(b), c.add(d)).subtract(ac).subtract(bd);
        // Combine the results   [O(n)]
        String part1Str = padZeros(ac.toString(), 2 * (n - n / 2), false);
        String part2Str = padZeros(adBC.toString(), n - n / 2, false);
        return new BigInteger(part1Str).add(new BigInteger(part2Str)).add(bd);
        // T(n) = 3T(n/2) + O(n)
        // a = 2, b = 2, d = 1
        // According to Master Method, the overall running time complexity is O(n^1.585), better than O(n^2).
    }

    /**
     * Private helper method to pad the given number of zeros to the given
     * string.
     * @param s string to be padded
     * @param nZero number of zeros
     * @param atFront boolean whether to pad zeros at front
     * @return string with padded zeros
     */
    private String padZeros(String s, int nZero, boolean atFront) {
        StringBuilder newStr = new StringBuilder();
        if (atFront) {
            for (int i = 0; i < nZero; ++i) {
                newStr.append(0);
            }
            newStr.append(s);
        } else {
            newStr.append(s);
            for (int i = 0; i < nZero; ++i) {
                newStr.append(0);
            }
        }
        return newStr.toString();
        // Running time complexity: O(n)
    }

}
