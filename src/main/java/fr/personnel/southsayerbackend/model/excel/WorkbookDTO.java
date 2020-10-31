package fr.personnel.southsayerbackend.model.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.apache.poi.hssf.usermodel.*;

/**
 * @author Farouk KABOUCHE
 * Workbook DTO
 * @version 1.0
 */
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class WorkbookDTO {
    private HSSFWorkbook hssfWorkbook;
    private HSSFSheet hssfSheet;
    private HSSFCell hssfCell;
    private HSSFRow hssfRow;
    private HSSFCellStyle hssfCellStyle;
    private String sheetName;
}
