package cryptonalysis;

public class PolynomialVector {
    public int numberOfVariables;
    public Polynomial[] polynomials;
    public double module;

    public PolynomialVector(int numberOfVariables, Polynomial[] polynomials) {
        this.numberOfVariables = numberOfVariables;
        this.polynomials = polynomials;
        this.module = polynomials[0].module;
    }

    public PolynomialVector(int numberOfVariables, double module) {
        this.numberOfVariables = numberOfVariables;
        this.polynomials = new Polynomial[numberOfVariables];
        this.module = module;
    }

    public PolynomialVector(Matrix matrix, double module) {
        numberOfVariables = matrix.linesNumber;
        this.polynomials = new Polynomial[numberOfVariables];
        for (int i = 0; i < numberOfVariables; i++) {
            polynomials[i] = new Polynomial(matrix.values[i], module, numberOfVariables);
        }
        this.module = module;
    }

    public PolynomialVector to3Power() {
        PolynomialVector newVector = this;
        for (int i = 0; i < numberOfVariables; i++) {
            newVector.polynomials[i].to3power();
        }
        return newVector;
    }

    public PolynomialVector multWithMatrix(Matrix matrix) {
        PolynomialVector newVector = new PolynomialVector(numberOfVariables, module);
        for (int i = 0; i < numberOfVariables; i++) {
            newVector.polynomials[i] = polynomials[i];
        }
        for (int i = 0; i < numberOfVariables; i++) {
            Polynomial elem = new Polynomial(module, polynomials[i].polynomial.length);
                for (int k = 0; k < numberOfVariables; k++) {
                    elem = elem.addPolynomials(polynomials[k].multOnNumber(matrix.values[i][k]));
                }
            newVector.polynomials[i] = elem;
                newVector.polynomials[i].numberOfVariables = numberOfVariables;
        }
        return newVector;
    }

    public Matrix encrypt(Matrix valuesForEncryption) {
        double[][] vector = new double[1][numberOfVariables];
        for (int i = 0; i < numberOfVariables; i++) {
            vector[0][i] = polynomials[i].substituteValues(valuesForEncryption);
        }
        return new Matrix(vector, module);
    }

    public void printPolynomialVector() {
        for (int i = 0; i < numberOfVariables; i++) {
            polynomials[i].printPolynomial();
            System.out.println("\n");
        }
    }
}
