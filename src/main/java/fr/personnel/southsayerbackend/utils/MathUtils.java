package fr.personnel.southsayerbackend.utils;

import lombok.Data;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Farouk KABOUCHE
 *
 * Math Utils
 *
 * @version 2.0
 */
@Data
public class MathUtils {

    // Avoid class instantiation
    private MathUtils() { }

    /** Cette méthode permet de calculer la multiplication de deux valeurs de type double avec une très bonne précision en utilisant le format IEEE 754R Decimal128
     * @param x : x
     * @param y : x
     * @return x*y
     */
    public static double multiplyDouble(final double x, final double y){
        final BigDecimal bx = new BigDecimal(Double.toString(x));
        final BigDecimal by = new BigDecimal(Double.toString(y));
        return bx.multiply(by, MathContext.DECIMAL128).doubleValue();
    }

    /** Cette méthode permet de formater un double en gardant que deux chiffres après la virgule
     * @param valeur à formater
     * @return la valeur formatée (deux chiffres après la virgule)
     */
    public static double arrondiMathematique(final double valeur){
        return arrondiMathematique(valeur, 2);
    }

    /**
     * Cette méthode permet d'arrondir un double suivant une précision voulue.
     * @param valeur à arrondir
     * @return la valeur arrondie
     */
    public static double arrondiMathematique(final double valeur, final int precision){
        double facteur = Math.pow(10, precision);
        return Math.round(valeur * facteur)/facteur;
    }

    /** Cette méthode permet de calculer la division de deux valeurs de type double avec une très bonne précision en utilisant le format IEEE 754R Decimal128
     * @param x : x
     * @param y : y
     * @return x*y
     */
    public static double divideDouble(final double x, final double y){
        final BigDecimal bx = new BigDecimal(Double.toString(x));
        final BigDecimal by = new BigDecimal(Double.toString(y));
        return bx.divide(by, MathContext.DECIMAL128).doubleValue();
    }

    /** Cette méthode permet de calculer le prix total selon la tva de type double
     * @param price : price
     * @param vat : vat
     * @return {@link double}
     */
    public static double calculatePrice(final double price, final double vat){
        return MathUtils.multiplyDouble(price, 1 + MathUtils.divideDouble(vat, 100));
    }

    public static double somme(final double s1, final double s2){
        return s1 + s2;
    }

    public static double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }

}
