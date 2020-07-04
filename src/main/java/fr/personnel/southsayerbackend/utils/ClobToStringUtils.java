package fr.personnel.southsayerbackend.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * @author Farouk KABOUCHE
 * <p>
 * Clob To String Utils
 */
@Slf4j
public class ClobToStringUtils {
    public String clobToString(Clob clob) {
        final StringBuilder sb = new StringBuilder();
        try {
            final Reader reader = clob.getCharacterStream();
            final BufferedReader br = new BufferedReader(reader);
            int b;
            while (-1 != (b = br.read())) sb.append((char) b);
            br.close();
        } catch (SQLException e) {
            log.error("SQL. Could not convert CLOB to string", e);
            return e.toString();
        } catch (IOException e) {
            log.error("IO. Could not convert CLOB to string", e);
            return e.toString();
        }
        return sb.toString();
    }

}
