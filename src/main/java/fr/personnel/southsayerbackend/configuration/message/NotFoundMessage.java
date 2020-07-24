package fr.personnel.southsayerbackend.configuration.message;

import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * @author Farouk KABOUCHE
 * Not Found Message
 * @version 1.0
 */

@Service
@Data
public class NotFoundMessage {
    /**
     * Not Found Message Constructor by String
     *
     * @param a : string not found
     * @return {@link String}
     */
    public String toString(String a) {
        return a + " was not found...";
    }

    /**
     * Not Found Message Constructor by Long
     *
     * @param a : long not found
     * @return {@link String}
     */
    public String toLong(Long a) {
        return a + " was not found...";
    }

    /**
     * Not Found Message Constructor by int
     *
     * @param a : int not found
     * @return {@link String}
     */
    public String toInt(int a) {
        return a + " was not found...";
    }

    /**
     * Not Found Message Constructor by String
     *
     * @param a : string not found
     * @return {@link String}
     */
    public String toString(String a, String b) {
        return "{\"" + a + "\", \"" + b + "\"} was not found...";
    }

    /**
     * Not Found Message Constructor by Long
     *
     * @param a : long not found
     * @return {@link String}
     */
    public String toLong(Long a, Long b) {
        return "{\"" + a + "\", \"" + b + "\"} was not found...";
    }

    /**
     * Not Found Message Constructor by int
     *
     * @param a : int not found
     * @return {@link String}
     */
    public String toInt(int a, int b) {
        return "{\"" + a + "\", \"" + b + "\"} was not found...";
    }
}
