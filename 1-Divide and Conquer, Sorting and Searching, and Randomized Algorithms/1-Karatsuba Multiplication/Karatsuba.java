import java.math.BigInteger;

public class Karatsuba {

    /**
     * Calculates the multiplication of two integers using Karatsuba
     * Multiplication.
     * @param x first operand
     * @param y second operand
     * @return product
     */
    private static BigInteger karatsuba(BigInteger x, BigInteger y) {
        String xStr = x.toString(), yStr = y.toString();

        // Pad leading zeros to make x and y of the same length
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
        String aStr = xStr.substring(0, n / 2), bStr = xStr.substring(n / 2), cStr = yStr.substring(0, n / 2),
                dStr = yStr.substring(n / 2);
        BigInteger a = new BigInteger(aStr), b = new BigInteger(bStr), c = new BigInteger(cStr),
                d = new BigInteger(dStr);
        BigInteger ac = karatsuba(new BigInteger(aStr), c);
        BigInteger bd = karatsuba(b, d);
        BigInteger adBC = karatsuba(a.add(b), c.add(d)).subtract(ac).subtract(bd);
        // Combine the results
        String part1Str = padZeros(ac.toString(), 2 * (n - n / 2), false);
        String part2Str = padZeros(adBC.toString(), n - n / 2, false);
        return new BigInteger(part1Str).add(new BigInteger(part2Str)).add(bd);
    }

    /**
     * Private helper method to pad the given number of zeros to the given string.
     * @param s string to be padded
     * @param nZeros number of zeros
     * @param atFront boolean whether to pad zeros at front
     * @return string with padded zeros
     */
    private static String padZeros(String s, int nZeros, boolean atFront) {
        String newStr = "";
        if (atFront) {
            for (int i = 0; i < nZeros; ++i) {
                newStr += "0";
            }
            newStr += s;
        } else {
            newStr += s;
            for (int i = 0; i < nZeros; ++i) {
                newStr += "0";
            }
        }
        return newStr;
    }

    /**
     * Main driver.
     * @param args arguments from command line
     */
    public static void main(String[] args) {
        BigInteger x = new BigInteger("1234"), y = new BigInteger("5678");
        assert karatsuba(x, y).equals(new BigInteger("7006652"));
        System.out.println("Passed!");
    }

}
