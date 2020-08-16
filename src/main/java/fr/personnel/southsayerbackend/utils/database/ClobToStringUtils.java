package fr.personnel.southsayerbackend.utils.database;

import lombok.extern.slf4j.Slf4j;

import javax.sql.rowset.serial.SerialClob;
import java.io.*;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * @author Farouk KABOUCHE
 * <p>
 * Clob To String Utils
 * @version 1.0
 */
@Slf4j
public class ClobToStringUtils {

    public static String clobToString(Clob clob) {
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

    public static Clob stringToClob(String source)
    {
        try
        {
            return new SerialClob(source.toCharArray());
        }
        catch (Exception e)
        {
            log.error("Could not convert string to a CLOB",e);
            return null;
        }
    }

}
