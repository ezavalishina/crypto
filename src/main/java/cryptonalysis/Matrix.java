package cryptonalysis;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public class Matrix implements Cloneable {
    public double[][] values;
    public int columnsNumber;
    public int linesNumber;
    public Matrix inverce;
    public double determinant;
    public double module;

    public Matrix(double[][] values, double module) {
        this.values = values;
        this.linesNumber = values.length;
        this.columnsNumber = values[0].length;
        this.module = module;
        if (columnsNumber == linesNumber) {
            this.determinant = getDeterminant();
            if (determinant < 0) {
                determinant += module;
            }
        }
//        if (determinant != 0) {
//            inverce = getInverse();
//        }
    }

    public Matrix(int size) {
        this.values = new double[size][size];
        this.linesNumber = values.length;
        this.columnsNumber = values[0].length;
    }

    public Matrix(Polynomial[] values) {
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i].polynomial;
        }
        this.columnsNumber = values.length;
        this.linesNumber = 1;
    }

//    public Matrix(int unitMatrixSize) {
//        this.linesNumber = unitMatrixSize;
//        this.columnsNumber = unitMatrixSize;
//        this.values = new double[linesNumber][columnsNumber];
//        for (int i = 0; i < linesNumber; i++) {
//            for (int j = 0; j < columnsNumber; j++) {
//                values[i][j] = (i == j) ? 1 : 0;
//            }
//        }
//    }

    public Matrix(Matrix matrix) {
        linesNumber = matrix.linesNumber;
        columnsNumber = matrix.columnsNumber;
        values = new double[linesNumber][columnsNumber];
        module = matrix.module;
        for (int i = 0; i < linesNumber; i++) {
            for (int j = 0; j < columnsNumber; j++) {
                values[i][j] = matrix.values[i][j];
            }
        }
        if (linesNumber == columnsNumber) {
            determinant = getDeterminant();
            if (determinant < 0) {
                determinant += module;
            }
        }
    }

    public Matrix divide(int value) {
        return multOnNumber(1 / value);
    }

    public Matrix clone() throws CloneNotSupportedException {
        return new Matrix(values, module);
    }

    public Matrix(int linesNumber, int columnsNumber, double module) {
        this.linesNumber = linesNumber;
        this.columnsNumber = columnsNumber;
        this.values = new double[linesNumber][columnsNumber];
        this.module = module;
    }

    public Matrix multOnMatrix(Matrix matrix) {
        if (columnsNumber != matrix.linesNumber) {
            throw new IllegalArgumentException();
        }

        Matrix multMatrix = new Matrix(linesNumber, matrix.columnsNumber, module);
        for (int i = 0; i < linesNumber; i++) {
            for (int j = 0; j < matrix.columnsNumber; j++) {
                int elem = 0;
                for (int k = 0; k < matrix.linesNumber; k++) {
                    elem += (values[i][k] * matrix.values[k][j]) % module;
                }
                multMatrix.values[i][j] = elem % module;
            }
        }
        return multMatrix;
    }

    public Matrix multOnVector(Matrix matrix) {
        double[][] multVector = new double[columnsNumber][1];
        for (int i = 0; i < linesNumber; i++) {
            double elem = 0;
            for (int j = 0; j < matrix.linesNumber; j++) {
                elem = elem + (values[i][j] * matrix.values[j][0]);
            }
            multVector[i][0] = elem % matrix.module;
        }
        return new Matrix(multVector, matrix.module);
    }


    public Matrix multOnNumber(double number) {
        Matrix multMatrix = new Matrix(this);
        for (int i = 0; i < linesNumber; i++) {
            for (int j = 0; j < columnsNumber; j++) {
                multMatrix.values[i][j] = (values[i][j] * number) % module;
            }
        }
        return multMatrix;
    }

    public Matrix powNumber(double number) {
        Matrix powMatrix = new Matrix(this);
        for (int i = 0; i < linesNumber; i++) {
            for (int j = 0; j < columnsNumber; j++) {
                powMatrix.values[i][j] = myPow(powMatrix.values[i][j], number) % module;
            }
        }
        return powMatrix;
    }

    public double myPow(double number, double power) {
        double result = number;
        for (int i = 0; i < power - 1; i++) {
            result = (result*number) % module;
        }
        return result;
    }

    public Matrix transpose() {
        Matrix transposeMatrix = new Matrix(values, module);
        for (int i = 0; i < transposeMatrix.linesNumber; i++) {
            for (int j = i + 1; j < transposeMatrix.columnsNumber; j++) {
                double temp = transposeMatrix.values[i][j];
                transposeMatrix.values[i][j] = transposeMatrix.values[j][i];
                transposeMatrix.values[j][i] = temp;
            }
        }
        return transposeMatrix;
    }

    public double getMinor(int i, int j) {
        if (linesNumber == 2) {
            return getMinor2(i, j);
        }
        double[][] values = new double[linesNumber - 1][columnsNumber - 1];
        int kCount = 0;
        int tCount = 0;
        for (int k = 0; k < linesNumber; k++) {
            if (k != i) {
                for (int t = 0; t < columnsNumber; t++) {
                    if (t != j) {
                        values[kCount][tCount] = this.values[k][t];
                        tCount++;
                    }
                }
                kCount++;
                tCount = 0;
            }
        }
        Matrix minorMatrix = new Matrix(values, module);
        return minorMatrix.getDeterminant();
    }

    public double getDeterminant() {
        if (columnsNumber == 2) {
            return getDeterminant2();
        } else {
            double determinant = 0;
            for (int j = 0; j < columnsNumber; j++) {
                determinant += (values[0][j] * Math.pow(-1, j) * getMinor(0, j)) % module;
            }
            return determinant;
        }
    }

    public Matrix transposeVector() {
        double[][] newValues = new double[columnsNumber][1];
        for (int i = 0; i < columnsNumber; i++) {
            newValues[i][0] = this.values[0][i];
        }
        return new Matrix(newValues, module);
    }

    public Matrix reTransposeVector() {
        double[][] newValues = new double[1][linesNumber];
        for (int i = 0; i < linesNumber; i++) {
            newValues[0][i] = this.values[i][0];
        }
        return new Matrix(newValues, module);
    }

    public Matrix subMatrix(int y0, int x0, int width, int height) {
        Matrix matrix = new Matrix(height, width, module);
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                matrix.values[x][y] = values[x + x0][y + y0];
            }
        }

        return matrix;
    }

    public ArrayList<Double> getValuesArr() {
        ArrayList<Double> Vals = new ArrayList<>();
        for (int i = 0; i < linesNumber; i++) {
            for (int j = 0; j < columnsNumber; j++) {
                Vals.add(values[i][j]);
            }
        }
        return Vals;
    }

    public boolean compareMatrixValues(Matrix B) {
        ArrayList<Double> bVals = B.getValuesArr();
        ArrayList<Double> aVals = getValuesArr();
        for (int i = 0; i < bVals.size(); i++) {
            int count = 0;
            for (int j = 0; j < aVals.size(); j++) {
                if (count == 0 && aVals.get(j).equals(bVals.get(i))) {
                    aVals.remove(j);
                    count++;
                }
            }
            if (count == 0) {
                return false;
            }
        }
        if (aVals.size() == 0) {
            return true;
        }
        return false;
    }

    public int scalarMatrixMult(Matrix val) {
        int result = 0;
        for (int x = 0; x < linesNumber; ++x) {
            for (int y = 0; y < columnsNumber; ++y) {
                result += values[x][y] * val.values[x][y];
            }
        }

        return result;
    }

    public Matrix add(Matrix matrix) {
        Matrix result = new Matrix(linesNumber, columnsNumber, module);
        double sum;
        for (int x = 0; x < linesNumber; x++) {
            for (int y = 0; y < columnsNumber; y++) {
                sum = values[x][y] + matrix.values[x][y];
                result.values[x][y] = sum;
            }
        }

        return result;
    }

    public void printMatrix() {
        for (int i = 0; i < linesNumber; i++) {
            for (int j = 0; j < columnsNumber; j++) {
                System.out.print(values[i][j] + " ");
            }
            System.out.println("\n");
        }
    }

    public void writeMatrixAB(FileWriter fileWriter, Matrix B) throws IOException {
        for (int i = 0; i < linesNumber; i++) {
            for (int j = 0; j < columnsNumber; j++) {
                fileWriter.write(values[i][j] + " ");
            }
        }
        for (int i = 0; i < linesNumber; i++) {
            for (int j = 0; j < columnsNumber; j++) {
                fileWriter.write(B.values[i][j] + " ");
            }
        }
        fileWriter.write("\n");
    }

    public double getDeterminant2() {
        return getPositiveModuleFunc((values[0][0] * values[1][1]) - (values[0][1] * values[1][0]));
    }

    public double getMinor2(int i, int j) {
        return (values[(i + 1) % 2][(j + 1) % 2]);
    }

    public Matrix getInverse() {
        double[][] algCompl = new double[linesNumber][columnsNumber];
        for (int i = 0; i < linesNumber; i++) {
            for (int j = 0; j < columnsNumber; j++) {
                algCompl[i][j] = getPositiveModuleFunc((((Math.pow(-1.0, i + j)) % module) * getMinor(i, j)) % module);
            }
        }
        Matrix algebraicComlement = new Matrix(algCompl, module);
        double invertDet = getInversElement(determinant);
        algebraicComlement = algebraicComlement.multOnNumber(invertDet);
        return algebraicComlement.transpose();
    }

    public double getInversElement(double element) {
        TriplBig params = gcdWide(BigDecimal.valueOf(element).toBigInteger(), BigDecimal.valueOf(module).toBigInteger());

        if (params.d.doubleValue() == 1)
            return getPositiveModuleFunc(params.x.doubleValue());

        return 0;
    }

    public double getPositiveModuleFunc(double number) {
        if (number < 0) {
            return number + module;
        } else
            return number;
    }

    TriplBig gcdWide(BigInteger a, BigInteger b) {
        TriplBig temphere = new TriplBig(a, BigInteger.ONE, BigInteger.ZERO);
        TriplBig temphere2 = new TriplBig();

        if (b == BigInteger.ZERO) {
            return temphere;
        }

        temphere2 = gcdWide(b, a.mod(b));
        temphere = new TriplBig();

        temphere.d = temphere2.d;
        temphere.x = temphere2.y;
        temphere.y = temphere2.x.subtract(a.divide(b).multiply(temphere2.y));

        return temphere;
    }

}