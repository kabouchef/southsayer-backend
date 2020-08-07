package fr.personnel.southsayerbackend.utils;

import fr.personnel.southsayerbackend.model.simulation.WorkbookDTO;
import fr.personnel.southsayerbackend.service.simulation.core.StaticPathService;
import fr.personnel.southsayerbackend.service.simulation.core.StyleOfCellsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.*;

import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.STATIC_DIRECTORY_IMAGES;
import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.XLS_EXTENSION;

/**
 * @author Farouk KABOUCHE
 * <p>
 * Excel Utils
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExcelUtils {

    /**
     * Initialization of WorkBook
     */
    public static WorkbookDTO workbookInit(String sheetName, String title)
            throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet spreadSheet = workbook.createSheet(sheetName);
        HSSFRow row0 = spreadSheet.createRow(0);
        HSSFCell cell = row0.createCell(0);

        for (int i = 0; i < 14; i++) {
            if (i == 0) spreadSheet.setColumnWidth(i, 500 * 25);
            else if (i == 2 || i == 3) spreadSheet.setColumnWidth(i, 156 * 25);
            else spreadSheet.setColumnWidth(i, 256 * 25);
        }

        // Style of Title Cell
        HSSFCellStyle styleTitle = StyleOfCellsService.getCustomStyleTitle(workbook, spreadSheet);

        // Creating Row of Title
        row0.setHeight((short) 1400);
        cell.setCellValue(title);
        cell.setCellStyle(styleTitle);

        //Ajout du logo LM
        HSSFCell cellPicture = row0.createCell(1);
        // Lire l'image à l'aide d'un stream
        InputStream inputStream = new FileInputStream(
                STATIC_DIRECTORY_IMAGES + "/" + "1200px-Leroy_Merlin.svg.jpeg");
        byte[] bytes = IOUtils.toByteArray(inputStream);
        //Ajouter l'image au classeur
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
        //fermer le stream
        inputStream.close();

        //Gérer l'aspect affichage de l'image
        ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
        anchor.setCol1(4);
        anchor.setRow1(0);
        Picture pict = spreadSheet.createDrawingPatriarch().createPicture(anchor, pictureIdx);
        pict.getPreferredSize();

        return new WorkbookDTO()
                .withHssfWorkbook(workbook)
                .withHssfSheet(spreadSheet)
                .withHssfCell(cell)
                .withHssfRow(row0);
    }

    public static <T> void writeToExcel(List<T> data, String sheetName, String path, String filterValue) throws IOException {
        FileOutputStream fos = null;
        HSSFWorkbook workbook = null;

        String currentDate =
                new SimpleDateFormat("yyyyMMdd-HHmmss")
                        .format(new Date(System.currentTimeMillis()));
        String fileName = sheetName + " - " + currentDate + "." + XLS_EXTENSION;

        File file = new File(path + "/" +fileName);

        //Drain Static directory
        FileUtils.cleanDirectory(new File(path));

        WorkbookDTO workbookDTO = workbookInit(sheetName, fileName);

        // Style of Title Cell
        HSSFCellStyle styleHead =  StyleOfCellsService.getCustomStyleHead(workbookDTO.getHssfWorkbook());
        // Style of Global Content Cell
        HSSFCellStyle styleGlobalContent =  StyleOfCellsService.getCustomGlobalContent(workbookDTO.getHssfWorkbook());
        // Style of Filter Value Cell
        HSSFCellStyle styleFilterValue = StyleOfCellsService.getCustomFilterValue(workbookDTO.getHssfWorkbook());

        try {
            workbook = workbookDTO.getHssfWorkbook();
            Sheet sheet = workbookDTO.getHssfSheet();
            List<String> fieldNames = getFieldNamesForClass(data.get(0).getClass());
            Row rowFilterValue = sheet.createRow(3);
            Cell cellHeadFilterValue = rowFilterValue.createCell(0);
            cellHeadFilterValue.setCellValue("filterValue");
            cellHeadFilterValue.setCellStyle(styleHead);
            Cell cellFilterValue = rowFilterValue.createCell(1);
            cellFilterValue.setCellValue(filterValue);
            cellFilterValue.setCellStyle(styleFilterValue);

            int rowCount = 6;
            int columnCount = 0;
            Row row = sheet.createRow(rowCount++);
            for (String fieldName : fieldNames) {
                Cell cell = row.createCell(columnCount++);
                cell.setCellValue(fieldName);
                cell.setCellStyle(styleHead);
            }
            Class<? extends Object> classz = data.get(0).getClass();
            for (T t : data) {
                row = sheet.createRow(rowCount++);
                columnCount = 0;
                for (String fieldName : fieldNames) {
                    Cell cell = row.createCell(columnCount);
                    Method method = null;
                    try {
                        method = classz.getMethod("get" + capitalize(fieldName));
                    } catch (NoSuchMethodException nme) {
                        method = classz.getMethod("get" + fieldName);
                    }
                    Object value = method.invoke(t, (Object[]) null);
                    if (value != null) {
                        if (value instanceof String) {
                            cell.setCellValue((String) value);
                        } else if (value instanceof Long) {
                            cell.setCellValue((Long) value);
                        } else if (value instanceof Integer) {
                            cell.setCellValue((Integer) value);
                        } else if (value instanceof Double) {
                            cell.setCellValue((Double) value);
                        }
                        cell.setCellStyle(styleGlobalContent);
                    }
                    columnCount++;
                }
            }
            fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                    log.info("*******************************");
                    if (file.exists()) {
                        log.info("The file \"" + fileName + "\" has been created in :");
                        log.info(path);
                    } else {
                        log.info("The file \"" + fileName + "\" has not been created...");
                    }
                    log.info("*******************************");
                }
            } catch (IOException e) {
            }
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
            }
        }
    }
    //retrieve field names from a POJO class
    private static List<String> getFieldNamesForClass(Class<?> clazz) throws Exception {
        List<String> fieldNames = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fieldNames.add(fields[i].getName());
        }
        return fieldNames;
    }
    //capitalize the first letter of the field name for retriving value of the field later
    private static String capitalize(String s) {
        if (s.length() == 0)
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }



}
