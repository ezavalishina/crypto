package cryptonalysis;

public class PublicKey {
    public PolynomialVector publicKey;

    public PublicKey(Matrix matrixA, Matrix matrixB, double module) {
        publicKey = new PolynomialVector(matrixA, module);
//        publicKey.printPolynomialVector();
        publicKey = publicKey.to3Power();
//        publicKey.printPolynomialVector();
        publicKey = publicKey.multWithMatrix(matrixB);
//        publicKey.printPolynomialVector();
    }

    public Matrix encrypt(Matrix valuesForEncrypt) {
        Matrix vector = publicKey.encrypt(valuesForEncrypt);
//        vector.printMatrix();
        return vector;
    }

    public Matrix decrypt(Matrix valuesForDecrypt, Matrix inverseA, Matrix inverseB, double inversDeg) {
        Matrix encryptionValues = inverseB.multOnVector(valuesForDecrypt);
//        System.out.println("Ainv");
//        inverseA.printMatrix();
//        System.out.println("Binv");
//        inverseB.printMatrix();
//        System.out.println("invDeg " + inversDeg);
//        System.out.println("values");
//        valuesForDecrypt.printMatrix();
//        System.out.println("First step");
//        encryptionValues.printMatrix();
        encryptionValues = encryptionValues.powNumber(inversDeg);
//        System.out.println("Second step");
//        encryptionValues.printMatrix();
        encryptionValues = inverseA.multOnVector(encryptionValues);
//        System.out.println("Third step");
//        encryptionValues.printMatrix();
        return encryptionValues;
    }
}
