package com.leaflogistics.service.serialization;

public class MatrixAttrs {
    private final int rows;
    private final int cols;
    private final int[] matrix;
    private final boolean columnVector;


    public MatrixAttrs(int rows, int cols, int[] matrix, boolean colVector) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = matrix;
        this.columnVector = colVector;
    }


    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public int[] getMatrix() {
        return this.matrix;
    }

    public boolean isColumnVector() {
        return this.columnVector;
    }
}
