package fr.personnel.southsayerbackend.service.simulation.core;

import fr.personnel.southsayerbackend.configuration.constant.StyleCellConstant;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

/**
 * @author Farouk KABOUCHE
 * Style Of Cells Service
 * @version 1.0
 */
@Slf4j
@Service
@NoArgsConstructor
public class StyleOfCellsService {


    /**
     * Get Style Of Title cell
     * @param wb : wb
     * @return {@link HSSFCellStyle}
     */
    public HSSFCellStyle getCustomStyleTitle(HSSFWorkbook wb, HSSFSheet spreadSheet){
        HSSFFont font = wb.createFont();
        spreadSheet.addMergedRegion(new CellRangeAddress(0,0,0,3));
        HSSFCellStyle style = wb.createCellStyle();
        font.setFontName(StyleCellConstant.LEROY_MERLIN_SANS_BOLD);
        font.setFontHeightInPoints((short) 20);
        style.setFont(font);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * Get Style Of Head cell
     * @param wb : wb
     * @return {@link HSSFCellStyle}
     */
    public HSSFCellStyle getCustomStyleHead(HSSFWorkbook wb){
        HSSFCellStyle style = wb.createCellStyle();
        HSSFPalette palette = wb.getCustomPalette();
        palette.setColorAtIndex(HSSFColor.HSSFColorPredefined.GREEN.getIndex(), (byte) 124, (byte) 178, (byte) 32);
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
        font.setFontName(StyleCellConstant.LEROY_MERLIN_SANS_BOLD);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        return style;
    }

    /**
     * Get Style Of Global Content
     * @param wb : wb
     * @return {@link HSSFCellStyle}
     */
    public HSSFCellStyle getCustomGlobalContent(HSSFWorkbook wb){
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.getIndex());
        font.setFontName(StyleCellConstant.LEROY_MERLIN_SANS);
        style.setFont(font);
        return style;
    }

    /**
     * Get Style Of Price Content
     * @param wb : wb
     * @return {@link HSSFCellStyle}
     */
    public HSSFCellStyle getCustomPriceContent(HSSFWorkbook wb){
        HSSFCellStyle style = wb.createCellStyle();
        DataFormat df = wb.createDataFormat();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.getIndex());
        font.setFontName(StyleCellConstant.LEROY_MERLIN_SANS);
        style.setFont(font);
        style.setDataFormat(df.getFormat(StyleCellConstant.FORMAT_EURO));
        return style;
    }

    /**
     * Get Style Of Global Content
     * @param wb : wb
     * @return {@link HSSFCellStyle}
     */
    public HSSFCellStyle getCustomQuantifyContent(HSSFWorkbook wb){
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.getIndex());
        font.setFontName("Leroy Merlin Sans");
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    /**
     * Get Style Of Head Total Price Effected
     * @param wb : wb
     * @return {@link HSSFCellStyle}
     */
    public HSSFCellStyle getCustomHeadTotalPriceEffected(HSSFWorkbook wb){
        HSSFCellStyle style = wb.createCellStyle();
        HSSFPalette palette = wb.getCustomPalette();
        HSSFColor myColor = palette.findSimilarColor(124, 178, 32);
        short palIndex = myColor.getIndex();
        HSSFFont font = wb.createFont();
        font.setColor(palIndex);
        font.setFontName(StyleCellConstant.LEROY_MERLIN_SANS_BOLD);
        style.setFont(font);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        return style;
    }

    /**
     * Get Style Of Total Price Effected
     * @param wb : wb
     * @return {@link HSSFCellStyle}
     */
    public HSSFCellStyle getCustomTotalPriceEffected(HSSFWorkbook wb){
        HSSFCellStyle style = wb.createCellStyle();
        DataFormat df = wb.createDataFormat();
        HSSFPalette palette = wb.getCustomPalette();
        HSSFColor myColor = palette.findSimilarColor(124, 178, 32);
        short palIndex = myColor.getIndex();
        HSSFFont font = wb.createFont();
        font.setColor(palIndex);
        font.setFontName(StyleCellConstant.LEROY_MERLIN_SANS_BOLD);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setDataFormat(df.getFormat(StyleCellConstant.FORMAT_EURO));
        return style;
    }

    /**
     * Get Style Of Head Total Price
     * @param wb : wb
     * @return {@link HSSFCellStyle}
     */
    public HSSFCellStyle getCustomHeadTotalPrice(HSSFWorkbook wb){
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.getIndex());
        font.setFontName(StyleCellConstant.LEROY_MERLIN_SANS_BOLD);
        style.setFont(font);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        return style;
    }

    /**
     * Get Style Of Total Price
     * @param wb : wb
     * @return {@link HSSFCellStyle}
     */
    public HSSFCellStyle getCustomTotalPrice(HSSFWorkbook wb){
        HSSFCellStyle style = wb.createCellStyle();
        DataFormat df = wb.createDataFormat();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.getIndex());
        font.setFontName(StyleCellConstant.LEROY_MERLIN_SANS);
        style.setFont(font);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setDataFormat(df.getFormat(StyleCellConstant.FORMAT_EURO));
        return style;
    }

    public HSSFCellStyle getCustomConversionRate(HSSFWorkbook wb){
        HSSFCellStyle style = wb.createCellStyle();
        DataFormat df = wb.createDataFormat();
        HSSFPalette palette = wb.getCustomPalette();
        HSSFColor myColor = palette.findSimilarColor(124, 178, 32);
        short palIndex = myColor.getIndex();
        HSSFFont font = wb.createFont();
        font.setColor(palIndex);
        font.setFontName(StyleCellConstant.LEROY_MERLIN_SANS_BOLD);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setDataFormat(df.getFormat(StyleCellConstant.FORMAT_PERCENTAGE_2));
        return style;
    }
}
