package fr.personnel.southsayerbackend.utils;

import java.io.*;

/**
 * @author Farouk KABOUCHE
 *
 * Read Replace Utils
 */
public class ReadReplaceUtils {
    public static boolean readReplace(String fileName, String oldPattern,
                                      String replPattern, int lineNumber) {
        String line;
        StringBuffer sb = new StringBuffer();
        int nbLinesRead = 0;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    fis));
            while ((line = reader.readLine()) != null) {
                nbLinesRead++;
                /*line = line.toLowerCase();*/

                if (nbLinesRead == lineNumber) {
                    line = line.replaceFirst(oldPattern.toLowerCase(),
                            replPattern);
                }
                sb.append(line + "\n");
            }
            reader.close();
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(sb.toString());
            out.close();

        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
