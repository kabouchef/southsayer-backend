package fr.personnel.southsayerbackend.configuration.constant;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Farouk KABOUCHE
 * Rest Constants
 * @version 1.0
 */
@UtilityClass
public final class RestConstantUtils {

    /**
     * Default Path
     */
    public static final String DEFAULT_PATH = "api/v1/";
    /**
     * Endpoints
     */
    public static final String ACTIVITY_CODE_PATH = DEFAULT_PATH + "activity-code";
    public static final String DELIVERY_RATE_PATH = DEFAULT_PATH + "delivery-rate";
    public static final String TYPE_RATE_PATH = DEFAULT_PATH + "type-rate";
    public static final String SIMULATION_PATH = DEFAULT_PATH + "simulation";
    public static final String USER_PATH = DEFAULT_PATH + "user";

    /**
     * Statics
     */
    public static final String STATIC_DIRECTORY_FILES = "src/main/resources/static/files";
    public static final String STATIC_DIRECTORY_IMAGES = "src/main/resources/static/images";
    public static final String XML_EXTENSION = "xml";
    public static final String XLS_EXTENSION = "xls";
    public static final String STATIC_DIRECTORY_SIMULATION = "simulation";
    public static final String STATIC_DIRECTORY_CONVERSION_RATE = "conversion_rate";
    public static final String STATIC_DIRECTORY_DELIVERY_RATE = "delivery_rate";
    public static final String STATIC_DIRECTORY_TYPE_RATE = "type_rate";
    public static final String STATIC_DIRECTORY_ACTIVITY_CODE = "activity_code";
    public static final String STATIC_DIRECTORY_OUTPUT_FILE = "output_file";



}

