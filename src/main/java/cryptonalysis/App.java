package cryptonalysis;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class App {
    public static PublicKey publicKey;
    public static int flag = 0;
    public static double[][] matrixA = {{1, 9, 10}, {7, 8, 9}, {10, 6, 4}};
    public static double[][] matrixB = {{1, 1, 1}, {9, 9, 2}, {8, 6, 8}};
    public static double[][] values = {{1, 2, 4}};
    public static double module = 11;
    public static Matrix A = new Matrix(matrixA, module);
    public static Matrix B = new Matrix(matrixB, module);
    public static Matrix vals;
    public static int numberOfVariables = 3;
    public static double inverseDegree = modularInverseMultUniver(3, euler(module));

    public static void main(String[] args) {
        publicKey = new PublicKey(A, B, module);
//        System.out.println("YAY");
//        for (int i = 3; i < 4; i++) {
//            for (int j = 0; j < 1; j++) {
//                values[0][0] = i;
//                values[0][1] = j;
        vals = new Matrix(values, module);
        System.out.println("Message: ");
        vals.printMatrix();
        Matrix message = publicKey.encrypt(vals);
        System.out.println("Encrypt message: ");
        message.printMatrix();
        System.out.println("DEGREE " + inverseDegree);
        Matrix encrVals = publicKey.decrypt(message.transposeVector(), A.getInverse(), B.getInverse(), inverseDegree);
        System.out.println("EncrVal = ");
        encrVals.printMatrix();
//        hackPrivateKey3();
        checkSimilar();
//                if (flag == 0) {


//        hackPrivateKey();
//check();
//        if (fitToAll(A.getInverse(), B.getInverse())) {
//            System.out.println("OK");
//        } else {
//            System.out.println("ERROR");
//        }

//                }
//                deleteNotFittedMatrices(message);
//                flag++;
//            }
//        }
    }

    public static void hackPrivateKey2() {
        int fitted = 0;
        int notFitted = 0;
        try (FileWriter fileWriter = new FileWriter("matrices.txt", false);
             FileWriter fileWriter2 = new FileWriter("invert_matrices.txt", false)) {
            for (int a11 = 0; a11 < module; a11++) {
                for (int a21 = 0; a21 < module; a21++) {
                    for (int b11 = 0; b11 < module; b11++) {
                        for (int b12 = 0; b12 < module; b12++) {
                            if (((Math.pow(a11, 3) * b11) + (Math.pow(a21, 3) * b12)) % module == publicKey.publicKey.polynomials[0].polynomial[0]) {
                                for (int a12 = 0; a12 < module; a12++) {
                                    for (int a22 = 0; a22 < module; a22++) {
                                        if (((Math.pow(a12, 3) * b11) + (Math.pow(a22, 3) * b12)) % module == publicKey.publicKey.polynomials[0].polynomial[1]) {
                                            if ((3 * a11 * a11 * a12 * b11 + 3 * a21 * a21 * a22 * b12) % module == publicKey.publicKey.polynomials[0].polynomial[2]) {
                                                if ((3 * a11 * a12 * a12 * b11 + 3 * a21 * a22 * a22 * b12) % module == publicKey.publicKey.polynomials[0].polynomial[3]) {
                                                    for (int b21 = 0; b21 < module; b21++) {
                                                        for (int b22 = 0; b22 < module; b22++) {
                                                            if (((Math.pow(a11, 3) * b21) + (Math.pow(a21, 3) * b22)) % module == publicKey.publicKey.polynomials[1].polynomial[0]) {
                                                                if (((Math.pow(a12, 3) * b21) + (Math.pow(a22, 3) * b22)) % module == publicKey.publicKey.polynomials[1].polynomial[1]) {
                                                                    if ((3 * a11 * a11 * a12 * b21 + 3 * a21 * a21 * a22 * b22) % module == publicKey.publicKey.polynomials[1].polynomial[2]) {
                                                                        if ((3 * a11 * a12 * a12 * b21 + 3 * a21 * a22 * a22 * b22) % module == publicKey.publicKey.polynomials[1].polynomial[3]) {
                                                                            double[][] aVals = {{a11, a12}, {a21, a22}};
                                                                            double[][] bVals = {{b11, b12}, {b21, b22}};
                                                                            Matrix A = new Matrix(aVals, module);
                                                                            Matrix B = new Matrix(bVals, module);
                                                                            if ((A.determinant != 0) && (B.determinant != 0)) {
                                                                                A.writeMatrixAB(fileWriter, B);
                                                                                Matrix invA = A.getInverse();
                                                                                Matrix invB = B.getInverse();
                                                                                invA.writeMatrixAB(fileWriter2, invB);
                                                                                System.out.println(fitted);
                                                                                fitted++;
//                                                                                if (fitToAll(invA, invB)) {
//                                                                                    System.out.println(fitted);
//                                                                                    fitted++;
//                                                                                } else {
//                                                                                    notFitted++;
//                                                                                }
                                                                            } else {
                                                                                notFitted++;
                                                                            }
//                                                                            fileWriter.write(a11 + " " + a12 + " " + a21 + " " + a22 + " " + b11 + " " + b12 + " " + b21 + " " + b22 + "\n");
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            fileWriter.close();
            fileWriter2.close();
            System.out.println("All possible matrix sets number: " + (module * module * module * module * module * module * module * module) + "\nFind matrices: " + (fitted + notFitted) + "\nNot fitted because determinant: " + notFitted + "\nFitted on first step: " + fitted);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void hackPrivateKey3() {
        int fitted = 0;
        int notFitted = 0;
        try (FileWriter fileWriter = new FileWriter("matrices.txt", false);
             FileWriter fileWriter2 = new FileWriter("invert_matrices.txt", false)) {
            for (int b11 = 0; b11 < module; b11++) {
                for (int b12 = 0; b12 < module; b12++) {
                    for (int b13 = 0; b13 < module; b13++) {
                        for (int a11 = 0; a11 < module; a11++) {
                            for (int a21 = 0; a21 < module; a21++) {
                                for (int a31 = 0; a31 < module; a31++) {
                                    if ((Math.pow(a11, 3) * b11 % module + Math.pow(a21, 3) * b12 % module + Math.pow(a31, 3) * b13 % module) % module == publicKey.publicKey.polynomials[0].polynomial[0]) {
                                        for (int a12 = 0; a12 < module; a12++) {
                                            for (int a22 = 0; a22 < module; a22++) {
                                                for (int a32 = 0; a32 < module; a32++) {
                                                    if ((Math.pow(a12, 3) * b11 % module + Math.pow(a22, 3) * b12 % module + Math.pow(a32, 3) * b13 % module) % module == publicKey.publicKey.polynomials[0].polynomial[1]) {
                                                        if ((3 * a11 * a12 * a12 * b11 % module + 3 * a21 * a22 * a22 * b12 % module + 3 * a31 * a32 * a32 * b13 % module) % module == publicKey.publicKey.polynomials[0].polynomial[5]) {
                                                            if ((3 * a11 * a11 * a12 * b11 % module + 3 * a21 * a21 * a22 * b12 % module + 3 * a31 * a31 * a32 * b13 % module) % module == publicKey.publicKey.polynomials[0].polynomial[3]) {
                                                                for (int a13 = 0; a13 < module; a13++) {
                                                                    for (int a23 = 0; a23 < module; a23++) {
                                                                        for (int a33 = 0; a33 < module; a33++) {
                                                                            if ((Math.pow(a13, 3) * b11 % module + Math.pow(a23, 3) * b12 % module + Math.pow(a33, 3) * b13 % module) % module == publicKey.publicKey.polynomials[0].polynomial[2]) {
                                                                                if ((3 * a11 * a11 * a13 * b11 % module + 3 * a21 * a21 * a23 * b12 % module + 3 * a31 * a31 * a33 * b13 % module) % module == publicKey.publicKey.polynomials[0].polynomial[4]) {
                                                                                    if ((3 * a12 * a12 * a13 * b11 % module + 3 * a22 * a22 * a23 * b12 % module + 3 * a32 * a32 * a33 * b13 % module) % module == publicKey.publicKey.polynomials[0].polynomial[6]) {
                                                                                        if ((3 * a11 * a13 * a13 * b11 % module + 3 * a21 * a23 * a23 * b12 % module + 3 * a31 * a33 * a33 * b13 % module) % module == publicKey.publicKey.polynomials[0].polynomial[7]) {
                                                                                            if ((3 * a12 * a13 * a13 * b11 % module + 3 * a22 * a23 * a23 * b12 % module + 3 * a32 * a33 * a33 * b13 % module) % module == publicKey.publicKey.polynomials[0].polynomial[8]) {
                                                                                                if ((6 * a11 * a12 * a13 * b11 % module + 6 * a21 * a22 * a23 * b12 % module + 6 * a31 * a32 * a33 * b13 % module) % module == publicKey.publicKey.polynomials[0].polynomial[9]) {
                                                                                                    for (int b21 = 0; b21 < module; b21++) {
                                                                                                        for (int b22 = 0; b22 < module; b22++) {
                                                                                                            for (int b23 = 0; b23 < module; b23++) {
                                                                                                                if ((Math.pow(a11, 3) * b21 % module + Math.pow(a21, 3) * b22 % module + Math.pow(a31, 3) * b23 % module) % module == publicKey.publicKey.polynomials[1].polynomial[0]) {
                                                                                                                    if ((Math.pow(a12, 3) * b21 % module + Math.pow(a22, 3) * b22 % module + Math.pow(a32, 3) * b23 % module) % module == publicKey.publicKey.polynomials[1].polynomial[1]) {
                                                                                                                        if ((Math.pow(a13, 3) * b21 % module + Math.pow(a23, 3) * b22 % module + Math.pow(a33, 3) * b23 % module) % module == publicKey.publicKey.polynomials[1].polynomial[2]) {
                                                                                                                            if ((3 * a11 * a11 * a12 * b21 % module + 3 * a21 * a21 * a22 * b22 % module + 3 * a31 * a31 * a32 * b23 % module) % module == publicKey.publicKey.polynomials[1].polynomial[3]) {
                                                                                                                                if ((3 * a11 * a11 * a13 * b21 % module + 3 * a21 * a21 * a23 * b22 % module + 3 * a31 * a31 * a33 * b23 % module) % module == publicKey.publicKey.polynomials[1].polynomial[4]) {
                                                                                                                                    if ((3 * a11 * a12 * a12 * b21 % module + 3 * a21 * a22 * a22 * b22 % module + 3 * a31 * a32 * a32 * b23 % module) % module == publicKey.publicKey.polynomials[1].polynomial[5]) {
                                                                                                                                        if ((3 * a12 * a12 * a13 * b21 % module + 3 * a22 * a22 * a23 * b22 % module + 3 * a32 * a32 * a33 * b23 % module) % module == publicKey.publicKey.polynomials[1].polynomial[6]) {
                                                                                                                                            if ((3 * a11 * a13 * a13 * b21 % module + 3 * a21 * a23 * a23 * b22 % module + 3 * a31 * a33 * a33 * b23 % module) % module == publicKey.publicKey.polynomials[1].polynomial[7]) {
                                                                                                                                                if ((3 * a12 * a13 * a13 * b21 % module + 3 * a22 * a23 * a23 * b22 % module + 3 * a32 * a33 * a33 * b23 % module) % module == publicKey.publicKey.polynomials[1].polynomial[8]) {
                                                                                                                                                    if ((6 * a11 * a12 * a13 * b21 % module + 6 * a21 * a22 * a23 * b22 % module + 6 * a31 * a32 * a33 * b23 % module) % module == publicKey.publicKey.polynomials[1].polynomial[9]) {
                                                                                                                                                        for (int b31 = 0; b31 < module; b31++) {
                                                                                                                                                            for (int b32 = 0; b32 < module; b32++) {
                                                                                                                                                                for (int b33 = 0; b33 < module; b33++) {
                                                                                                                                                                    if ((Math.pow(a11, 3) * b31 % module + Math.pow(a21, 3) * b32 % module + Math.pow(a31, 3) * b33 % module) % module == publicKey.publicKey.polynomials[2].polynomial[0]) {
                                                                                                                                                                        if ((Math.pow(a12, 3) * b31 % module + Math.pow(a22, 3) * b32 % module + Math.pow(a32, 3) * b33 % module) % module == publicKey.publicKey.polynomials[2].polynomial[1]) {
                                                                                                                                                                            if ((Math.pow(a13, 3) * b31 % module + Math.pow(a23, 3) * b32 % module + Math.pow(a33, 3) * b33 % module) % module == publicKey.publicKey.polynomials[2].polynomial[2]) {
                                                                                                                                                                                if ((3 * a11 * a11 * a12 * b31 % module + 3 * a21 * a21 * a22 * b32 % module + 3 * a31 * a31 * a32 * b33 % module) % module == publicKey.publicKey.polynomials[2].polynomial[3]) {
                                                                                                                                                                                    if ((3 * a11 * a11 * a13 * b31 % module + 3 * a21 * a21 * a23 * b32 % module + 3 * a31 * a31 * a33 * b33 % module) % module == publicKey.publicKey.polynomials[2].polynomial[4]) {
                                                                                                                                                                                        if ((3 * a11 * a12 * a12 * b31 % module + 3 * a21 * a22 * a22 * b32 % module + 3 * a31 * a32 * a32 * b33 % module) % module == publicKey.publicKey.polynomials[2].polynomial[5]) {
                                                                                                                                                                                            if ((3 * a12 * a12 * a13 * b31 % module + 3 * a22 * a22 * a23 * b32 % module + 3 * a32 * a32 * a33 * b33 % module) % module == publicKey.publicKey.polynomials[2].polynomial[6]) {
                                                                                                                                                                                                if ((3 * a11 * a13 * a13 * b31 % module + 3 * a21 * a23 * a23 * b32 % module + 3 * a31 * a33 * a33 * b33 % module) % module == publicKey.publicKey.polynomials[2].polynomial[7]) {
                                                                                                                                                                                                    if ((3 * a12 * a13 * a13 * b31 % module + 3 * a22 * a23 * a23 * b32 % module + 3 * a32 * a33 * a33 * b33 % module) % module == publicKey.publicKey.polynomials[2].polynomial[8]) {
                                                                                                                                                                                                        if ((6 * a11 * a12 * a13 * b31 % module + 6 * a21 * a22 * a23 * b32 % module + 6 * a31 * a32 * a33 * b33 % module) % module == publicKey.publicKey.polynomials[2].polynomial[9]) {
                                                                                                                                                                                                            double[][] aVals = {{a11, a12, a13}, {a21, a22, a23}, {a31, a32, a33}};
                                                                                                                                                                                                            double[][] bVals = {{b11, b12, b13}, {b21, b22, b23}, {b31, b32, b33}};
                                                                                                                                                                                                            Matrix A = new Matrix(aVals, module);
                                                                                                                                                                                                            Matrix B = new Matrix(bVals, module);
                                                                                                                                                                                                            if ((A.determinant != 0) && (B.determinant != 0)) {
                                                                                                                                                                                                                A.writeMatrixAB(fileWriter, B);
                                                                                                                                                                                                                Matrix invA = A.getInverse();
                                                                                                                                                                                                                Matrix invB = B.getInverse();
                                                                                                                                                                                                                invA.writeMatrixAB(fileWriter2, invB);
                                                                                                                                                                                                                System.out.println(fitted);
                                                                                                                                                                                                                fitted++;
                                                                                                                                                                                                            } else {
                                                                                                                                                                                                                notFitted++;
                                                                                                                                                                                                            }
                                                                                                                                                                                                        }
                                                                                                                                                                                                    }
                                                                                                                                                                                                }
                                                                                                                                                                                            }
                                                                                                                                                                                        }
                                                                                                                                                                                    }
                                                                                                                                                                                }
                                                                                                                                                                            }
                                                                                                                                                                        }

                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }

                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }

                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            fileWriter.close();
            fileWriter2.close();
            System.out.println("All possible matrix sets number: " + (module * module * module * module * module * module * module * module) + "\nFind matrices: " + (fitted + notFitted) + "\nNot fitted because determinant: " + notFitted + "\nFitted on first step: " + fitted);
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }

    }

    public static void check() {
        try (BufferedReader br = new BufferedReader(new FileReader("invert_matrices.txt"))) {
            String line;
            int fit = 0;
            int nfit = 0;
            Matrix matrixA, matrixB;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(" ");
                int count = 0;
                double[][] valuesA = new double[numberOfVariables][numberOfVariables];
                for (int j = 0; j < numberOfVariables; j++) {
                    for (int k = 0; k < numberOfVariables; k++) {
                        valuesA[j][k] = Double.valueOf(vals[count]);
                        count++;
                    }
                }
                matrixA = new Matrix(valuesA, module);
                double[][] valuesB = new double[numberOfVariables][numberOfVariables];
                count = numberOfVariables * 2;
                for (int j = 0; j < numberOfVariables; j++) {
                    for (int k = 0; k < numberOfVariables; k++) {
                        valuesB[j][k] = Double.valueOf(vals[count]);
                        count++;
                    }
                }
                matrixB = new Matrix(valuesB, module);
                if (fitToAll(matrixA, matrixB)) {
                    fit++;
                    System.out.println("FIT " + fit);
                } else {
                    nfit++;
                    System.out.println("NOT FIT " + nfit);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean fitToAll(Matrix aInv, Matrix bInv) {
        for (int i = 1; i < module; i++) {
            for (int j = 1; j < module; j++) {
                values[0][0] = i;
                values[0][1] = j;
                vals = new Matrix(values, module);
                Matrix message = publicKey.encrypt(vals);
                Matrix decryptMessage = publicKey.decrypt(message.transposeVector(), aInv, bInv, inverseDegree);
                if (!(vectorsAreEquals(values, decryptMessage.reTransposeVector().values))) {
                    System.out.println("ERROR: " + values[0][0] + " and " + values[0][1] + " not equals " + decryptMessage.reTransposeVector().values[0][0] + " and " + decryptMessage.reTransposeVector().values[0][1]);
                    return false;
                }
            }
        }
        return true;
    }

    public static void checkSimilar() {
        try (FileWriter fileWriter = new FileWriter("similar_matrices.txt", false)) {
int c = 0;
            double[][] valuesP = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
            for (int i = 1; i < module; i++) {
                for (int p = 1; p < module; p++) {
                    for (int t = 1; t < module; t++) {
                        valuesP[0][0] = p;
                        valuesP[1][1] = i;
                        valuesP[2][2] = t;
                        Matrix matrixP = new Matrix(valuesP, module);
                        A = matrixP.multOnMatrix(A);
                        try (BufferedReader br = new BufferedReader(new FileReader("matrices.txt"))) {
                            int counter = 0;
                            String line;
                            int fit = 0;
                            int nfit = 0;
                            Matrix matrixA, matrixB;
                            fileWriter.write("\n**************************\n");
                            matrixP.writeMatrixAB(fileWriter, A);
                            fileWriter.write("\n***\n");
                            while ((line = br.readLine()) != null) {
                                String[] vals = line.split(" ");
                                int count = 0;
                                double[][] valuesA = new double[numberOfVariables][numberOfVariables];
                                for (int j = 0; j < numberOfVariables; j++) {
                                    for (int k = 0; k < numberOfVariables; k++) {
                                        valuesA[j][k] = Double.valueOf(vals[count]);
                                        count++;
                                    }
                                }
                                matrixA = new Matrix(valuesA, module);

                                double[][] valuesB = new double[numberOfVariables][numberOfVariables];
                                count = numberOfVariables * numberOfVariables;
                                for (int j = 0; j < numberOfVariables; j++) {
                                    for (int k = 0; k < numberOfVariables; k++) {
                                        valuesB[j][k] = Double.valueOf(vals[count]);
                                        count++;
                                    }
                                }
                                matrixB = new Matrix(valuesB, module);

                                if (matrixA.compareMatrixValues(A)) {
                                    matrixA.writeMatrixAB(fileWriter, matrixB);
                                    counter++;
                                    c++;
                                }
                            }
                            System.out.println("SIMILAR " + counter);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            }
            System.out.println("ALL" + c);

        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    public static void decryptMessage(Matrix message) {
        try (FileWriter fileWriter = new FileWriter("notes2.txt", false)) {
            for (int i = 0; i < module; i++) {
                for (int j = 0; j < module; j++) {
                    double[][] values = {{i, j}};
                    Matrix vector = publicKey.encrypt(new Matrix(values, module));
                    if ((vector.values[0][1] == message.values[0][1]) && (vector.values[0][0] == message.values[0][0])) {
                        fileWriter.write(i + " " + j + "\n");
                    }
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static double euler(double n) {
        double result = n;
        int i;
        for (i = 2; i * i <= n; ++i)
            if (n % i == 0) {
                while (n % i == 0)
                    n /= i;
                result -= result / i;
            }
        if (n > 1)
            result -= result / n;
        return result;
    }

    public static double modularInverseMultUniver(int a, double module) {
        return modularDegUniver(a, euler(module) - 1, module);
    }

    public static double modularDegUniver(int a, double k, double module) {
        double res = 1;
        while (k > 0) {
            if (k % 2 == 1) {
                k--;
                res = (res * a) % module;

            }
            k /= 2;
            a = (a * a) % (int) module;

        }
        return res;
    }

//        try {
//            File tmp = File.createTempFile("tmp", "");
//
//            BufferedReader br = new BufferedReader(new FileReader("note1.txt"));
//            BufferedReader br2 = new BufferedReader(new FileReader("note3.txt"));
//            BufferedWriter bw = new BufferedWriter(new FileWriter(tmp));
//
//            String line;
//            List<String> lines = new ArrayList<String>();
//            while ((line = br.readLine()) != null) {
//                lines.add(line);
//            }
//
//            List<String> lines2 = new ArrayList<String>();
//            while ((line = br.readLine()) != null) {
//                lines2.add(line);
//            }
//
//            //взять обратные матрицы, расшифровать сообщение, проверить, сходится ли с исходным
//            //записать в тмп файл обратные и оригинальные к ним матрицы, если сходится
//            //не записывать, если не сходятся
//            //в конце тмп файл заменить на обычный
//
//            //в идеале - составить вектор, где каждый элемент - матрицы А, Б и обратные к ним.
//            //создать цикл, который будет
//            // каждый раз шифровать на открытом ключе новое значение
//            // проверять это новое значение на всех оставшихся матрицах
//            // удалять из вектора те матрицы, которые не подошли
//            // в конце выведет количество шагов, которое понадобилось, чтобы дойти до 1 матрицы
//
//            // можно объединить это с первоначальной проверкой матриц на обратимость
//
//
//            String l;
//            while (null != (l = br.readLine()))
//                bw.write(String.format("%s%n", l));
//
//            br.close();
//            bw.close();
//
//            File oldFile = new File(f);
//            if (oldFile.delete())
//                tmp.renameTo(oldFile);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }

    public static boolean vectorsAreEquals(double[][] A, double[][] b) {
        int counter = 0;
        for (int i = 0; i < numberOfVariables; i++) {
            if (A[0][i] == b[0][i]) {
                counter++;
            }
        }
        if (counter == numberOfVariables) {
            return true;
        } else {
            return false;
        }
    }
}


