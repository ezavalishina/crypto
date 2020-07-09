package cryptonalysis;

public class Polynomial {
    public double[] polynomial;
    public double module;
    public int numberOfVariables;
    public Matrix resultVector;

    public Polynomial(double[] polynomial, double module, int numberOfVariables) {
        this.module = module;
        this.polynomial = polynomial;
        this.numberOfVariables = numberOfVariables;
    }

    public Polynomial(double module, int numberOfVariables) {
        this.module = module;
        this.polynomial = new double[numberOfVariables];
        this.numberOfVariables = numberOfVariables;
    }

    public Polynomial(double module, int numberOfVariables, int polynomialLength) {
        this.module = module;
        this.polynomial = new double[numberOfVariables];
        this.numberOfVariables = numberOfVariables;
    }

    public Polynomial multOnNumber(double number) {
        Polynomial newPolynomial = new Polynomial(module, polynomial.length);
        for (int i = 0; i < polynomial.length; i++) {
            newPolynomial.polynomial[i] = (polynomial[i] * number) % module;
        }
        return newPolynomial;
    }

    public Polynomial addPolynomials(Polynomial polynomial) {
        Polynomial newPolynomial = new Polynomial(module, this.polynomial.length);
        for (int i = 0; i < this.polynomial.length; i++) {
            newPolynomial.polynomial[i] = (this.polynomial[i] + polynomial.polynomial[i]) % module;
        }
        return newPolynomial;
    }

    /*Приходит полином вида a1*x1 a2*x2 a3*x3... в виде коэффициентов a1 a2 a3..*/
    public void to3power() {
        int cnk = Cnk(numberOfVariables, 3) + numberOfVariables + (numberOfVariables * (numberOfVariables - 1));
        double[] newPolynomial = new double[cnk];
        //0..n-1 места - кубы переменных
        for (int i = 0; i < numberOfVariables; i++) {
            newPolynomial[i] = myPow(polynomial[i], 3) % module;
        }
        //n..n*n - переменные вида x_i*x_i*y_j
        int varsCounter = 0;
        double[] squares = new double[numberOfVariables * numberOfVariables - numberOfVariables];
        for (int i = 0; i < numberOfVariables; i++) {
            for (int j = 0; j < numberOfVariables; j++) {
                if (i != j) {
                    squares[varsCounter] = (3 * ((((polynomial[i] * polynomial[i]) % module) * polynomial[j]) % module)) % module;
                    varsCounter++;
                }
            }
        }
        for (int i = 0; i < squares.length; i++) {
            newPolynomial[numberOfVariables + i] = squares[i];
        }
        //n*n + n..n*n+2n - переменные вида x_i*x_j*x_t
        int counter2 = (numberOfVariables + numberOfVariables * (numberOfVariables - 1));
        int iBorder = Cnk(numberOfVariables - 1, 2);
        int jBorder = numberOfVariables - 1;
        for (int i = 0; i < iBorder; i++) {
            for (int j = i + 1; j < jBorder; j++) {
                for (int k = j + 1; k < numberOfVariables; k++) {
                    newPolynomial[counter2] = (((polynomial[i] * polynomial[j]) % module) * polynomial[k] * 6) % module;
                    counter2++;
                }
            }
        }
        polynomial = newPolynomial;
    }

    public double myPow(double number, double power) {
        double result = number;
        for (int i = 0; i < power - 1; i++) {
            result = (result * number) % module;
        }
        return result;
    }

    public double substituteValues(Matrix valuesToCipher) {
        int cnk = Cnk(numberOfVariables, 3) + numberOfVariables + (numberOfVariables * (numberOfVariables - 1));
        double cipherValue = 0;
        //0..n-1 места - кубы переменных
        for (int i = 0; i < numberOfVariables; i++) {
            cipherValue += (polynomial[i] * (myPow(valuesToCipher.values[0][i], 3) % module)) % module;
        }
        //n..n*n - переменные вида x_i*x_i*y_j
        int varsCounter = 0;

        for (int i = 0; i < numberOfVariables; i++) {
            for (int j = 0; j < numberOfVariables; j++) {
                if (i != j) {
                    cipherValue += (polynomial[numberOfVariables + varsCounter] * ((((valuesToCipher.values[0][i] * valuesToCipher.values[0][i]) % module) * valuesToCipher.values[0][j]) % module)) % module;
                    varsCounter++;
                }
            }
        }

        //n*n + n..n*n+2n - переменные вида x_i*x_j*x_t
        int counter2 = (numberOfVariables + numberOfVariables * (numberOfVariables - 1));
        int iBorder = Cnk(numberOfVariables - 1, 2);
        int jBorder = numberOfVariables - 1;
        for (int i = 0; i < iBorder; i++) {
            for (int j = i + 1; j < jBorder; j++) {
                for (int k = j + 1; k < numberOfVariables; k++) {
                    cipherValue += (((((valuesToCipher.values[0][i] * valuesToCipher.values[0][j]) % module) * valuesToCipher.values[0][k]) % module) * polynomial[counter2]) % module;
                    counter2++;
                }
            }
        }
        return cipherValue % module;
    }

    public int Cnk(double n, double k) {
        double nFact = n;
        double kFact = k;
        double nkFact = n - k;
        for (int i = 1; i < n; i++) {
            nFact *= i;
        }
        for (int i = 1; i < k; i++) {
            kFact *= i;
        }
        if (n - k > 0) {
            for (int i = 1; i < (n - k); i++) {
                nkFact *= i;
            }
        } else nkFact = 1;
        return (int) (nFact / (kFact * nkFact));
    }

    public void printPolynomial() {
        for (int i = 0; i < polynomial.length; i++) {
            System.out.println(polynomial[i] + " ");
        }
    }
}
