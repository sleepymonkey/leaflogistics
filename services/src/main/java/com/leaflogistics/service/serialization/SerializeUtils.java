package com.leaflogistics.service.serialization;

import static java.lang.Long.parseLong;

import java.util.Stack;

public class SerializeUtils {

    /**
     * convert json array or two-dimensional array into an integer array with row and column counts.
     * parsing fails on unbalanced open/close array brackets, inconsistent column counts and if either
     * of the following criteria are not met:
     *    row count x column count must be less than or equal to 100000
     *    no individual array element can be smaller than -10^9 or larger than 10^9
     */
    public static MatrixAttrs deserialize(String json) {
        int rows = 0;
        int cols = 0;
        boolean isColumnVector = false;

        // ensure open/close brackets match
        Stack<String> stack = new Stack<>();

        int firstOpenNdx = json.indexOf('[');
        if (firstOpenNdx < 0) {
            throw new RuntimeException("malformed input json. no opening bracket");
        }
        stack.push("[");

        // count the number of open brackets to determine row count
        for (int ii = firstOpenNdx+1; ii < json.length(); ii++) {
            if (json.charAt(ii) == '[') {
                rows++;
                stack.push("[");
            }
            else if (json.charAt(ii) == ']') {
                stack.pop();
            }
        }
        if (!stack.isEmpty()) {
            throw new RuntimeException("malformed input json. no closing bracket");
        }

        // cover the case of column vector [1,2,3,4]
        if (rows == 0) {
            rows = 1;
            isColumnVector = true;
        }

        // determine column count by looking at the first row
        int firstClosedNdx = json.indexOf(']', firstOpenNdx);
        String firstRow = json.substring(firstOpenNdx+1, firstClosedNdx);
        String[] rowValues = firstRow.split(",");
        if (rowValues.length < 1) {
            throw new RuntimeException("malformed input json. zero columns in row");
        }
        cols = rowValues.length;  // all rows must have this many columns

        if (rows * cols > 100000) {
            throw new RuntimeException("input matrix is too large");
        }


        // now that we know row/col count, build the matrix array
        int[] matrix = new int[rows * cols];
        int curNdx = 0;
        for (int ii=firstOpenNdx+1; ii > 0 && ii < json.length(); ) {
            int nextClosedBracket = json.indexOf(']', ii);
            String row = json.substring(ii, nextClosedBracket);

            // if column count does not match the number of columns in the first row, bail
            String[] vals = row.split(",");
            if (vals.length != cols) {
                throw new RuntimeException("malformed input json. column count mismatch");
            }

            // account for potential leading '[' as well as whitespace, etc
            for (String val : vals) {
                String digits = val.replaceAll("[^\\d-]", "");
                long longVal = parseLong(digits);
                if (longVal < -1000000000 || longVal > 1000000000) {
                    throw new RuntimeException("integer outside matrix constraints: " + digits);
                }
                matrix[curNdx++] = (int) longVal;
            }

            ii = json.indexOf("[", nextClosedBracket + 1);  // ii will be -1 after processing all rows
        }

        return new MatrixAttrs(rows, cols, matrix, isColumnVector);
    }


    /**
     * convert an integer array into a two-dimensional json array representation.
     */
    public static String serialize(int[] matrix, int rows, int cols) {
        StringBuilder sb = new StringBuilder("[");

        for (int r = 0; r < rows; r++) {
            sb.append("[");
            for (int c = 0; c < cols; c++) {
                sb.append(matrix[r * cols + c]);
                if (c < cols - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
            if (r < rows - 1) {
                sb.append(",");
            }
        }
        sb.append("]");

        return sb.toString();
    }


    /**
     * convert an integer array into a json array representation.
     */
    public static String serialize(int[] vector) {
        StringBuilder sb = new StringBuilder("[");

        for (int ii=0; ii < vector.length; ii++) {
            sb.append(vector[ii]);
            if (ii < vector.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");

        return sb.toString();
    }



}
