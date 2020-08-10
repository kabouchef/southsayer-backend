package fr.personnel.southsayerbackend.service.simulation.core;

import fr.personnel.southsayerbackend.model.simulation.PriceLine;
import fr.personnel.southsayerbackend.model.excel.WorkbookDTO;
import fr.personnel.southsayerbackend.model.simulation.InputRate;
import fr.personnel.southsayerbackend.utils.ExcelUtils;
import fr.personnel.southsayerbackend.utils.MathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.*;
import static org.apache.commons.io.FileUtils.cleanDirectory;

/**
 * @author Farouk KABOUCHE
 * XML To Excel Service
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelConverterService {

    private final StaticPathService staticPathService;

    public List<PriceLine> generatePriceLinesExcel(String simulationCode)
            throws ParserConfigurationException, IOException, SAXException {

        String pathXmlSimulation = this.staticPathService.getPath(XML_EXTENSION, STATIC_DIRECTORY_SIMULATION);
        String pathXlsSimulation = this.staticPathService.getPath(XLS_EXTENSION, STATIC_DIRECTORY_SIMULATION);

        String priceLines;
        List<PriceLine> priceLineList = new ArrayList<PriceLine>();
        String[] priceElement;
        FileOutputStream fos = null;

        // Drain static repository
        cleanDirectory(new File(pathXlsSimulation));

        try {
            //Init Worbook
            WorkbookDTO workbookDTO =
                    ExcelUtils.workbookInit(simulationCode, "PRICE_FROM_" + simulationCode);

            // Parsing XML Document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmlDocument = builder.parse(pathXmlSimulation + "/" + simulationCode + "." + XML_EXTENSION);

            /**
             * Definition of Cells Style
             */
            // Style of Title Cell
            HSSFCellStyle styleHead =
                    StyleOfCellsService.getCustomStyleHead(workbookDTO.getHssfWorkbook());

            // Style of Global Content Cells
            HSSFCellStyle styleGlobalContent =
                    StyleOfCellsService.getCustomGlobalContent(workbookDTO.getHssfWorkbook());

            // Style of Price Content Cells
            HSSFCellStyle stylePriceContent =
                    StyleOfCellsService.getCustomPriceContent(workbookDTO.getHssfWorkbook());

            // Style of Quantifier Content Cells
            HSSFCellStyle styleQuantifyContent =
                    StyleOfCellsService.getCustomQuantifyContent(workbookDTO.getHssfWorkbook());

            // Style of Head Total Price Effected Cells
            HSSFCellStyle styleHeadTotalPriceEffected =
                    StyleOfCellsService.getCustomHeadTotalPriceEffected(workbookDTO.getHssfWorkbook());

            // Style of Total Price Effected Cells
            HSSFCellStyle styleTotalPriceEffected =
                    StyleOfCellsService.getCustomTotalPriceEffected(workbookDTO.getHssfWorkbook());

            // Style of Head Total Price Cells
            HSSFCellStyle styleHeadTotalPrice =
                    StyleOfCellsService.getCustomHeadTotalPrice(workbookDTO.getHssfWorkbook());

            // Style of Total Price Cells
            HSSFCellStyle styleTotalPrice =
                    StyleOfCellsService.getCustomTotalPrice(workbookDTO.getHssfWorkbook());


            //Entete
            HSSFRow row = workbookDTO.getHssfSheet().createRow(2);
            HSSFCell cell;
            String[] head = {
                    "IDENTIFIANT",
                    "DETAIL_PRESTATION",
                    "QUANTITE",
                    "TARIF_UNITAIRE",
                    "TARIF_PRESTATION",
                    "TYPE_PRESTATION",
                    "PRESTATION_DE",
                    "TVA_REDUITE",
                    "TVA_INTER",
                    "TVA_NORMALE",
                    "CODE_49",
                    "COD_TYPE_PRESTATION",
                    "TEMP_POSE",
                    "ORDRE"};

            // Creating Row of Head
            for (int a = 0; a < head.length; a++) {
                cell = row.createCell(a);
                cell.setCellValue(head[a]);
                cell.setCellStyle(styleHead);
            }

            /**
             * Getting Price Lines
             */
            String nbLines = "count(//*[@name='fpPriceHtChiffragePrecisSurrogate']" +
                    "//Value[not(preceding-sibling::Value = .)])";

            XPath xPath = XPathFactory.newInstance().newXPath();
            String nbPrice = xPath.compile(nbLines).evaluate(xmlDocument, XPathConstants.STRING).toString();

            int nbPriceLines = Integer.parseInt(nbPrice);
            for (int i = 1; i < nbPriceLines + 1; i++) {
                /**
                 * Extraction of Price Lines from XML File
                 */
                String cible = "//*[@name='fpPriceHtChiffragePrecisSurrogate']//Value[" + i + "]/longTextValue";

                HSSFRow row1 = workbookDTO.getHssfSheet().createRow(i + 2);
                priceLines = xPath.compile(cible).evaluate(xmlDocument, XPathConstants.STRING).toString();
                priceElement = priceLines.split("#");

                PriceLine priceLine = new PriceLine();
                if (priceElement.length >= 1) priceLine.setIdentifiant(priceElement[0]);
                if (priceElement.length >= 2) priceLine.setDetailPrestation(priceElement[1]);
                if (priceElement.length >= 3) priceLine.setQuantite(priceElement[2]);
                if (priceElement.length >= 4) priceLine.setTarifUnitaire(priceElement[3]);
                if (priceElement.length >= 5) priceLine.setTarifPrestation(priceElement[4]);
                if (priceElement.length >= 6) priceLine.setTypePrestation(priceElement[5]);
                if (priceElement.length >= 7) priceLine.setPrestationDe(priceElement[6]);
                if (priceElement.length >= 8) priceLine.setTvaReduite(priceElement[7]);
                if (priceElement.length >= 9) priceLine.setTvaInter(priceElement[8]);
                if (priceElement.length >= 10) priceLine.setTvaNormale(priceElement[9]);
                if (priceElement.length >= 11) priceLine.setCode49(priceElement[10]);
                if (priceElement.length >= 12) priceLine.setCod_typePrestation(priceElement[11]);
                if (priceElement.length >= 13) priceLine.setTempPose(priceElement[12]);
                if (priceElement.length >= 14) priceLine.setOrdre(priceElement[13]);

                priceLineList.add(priceLine);

                for (int j = 0; j < priceElement.length; j++) {
                    cell = row1.createCell(j);

                    if (j == 3 || j == 4) {
                        double price = Double.parseDouble(priceElement[j]);
                        cell.setCellValue(price);
                        cell.setCellStyle(stylePriceContent);
                    } else if (j < 14 && (j == 2 || j > 6)) {
                        int quantity = Integer.parseInt(priceElement[j]);
                        cell.setCellValue(quantity);
                        cell.setCellStyle(styleQuantifyContent);
                    } else {
                        cell.setCellValue(priceElement[j]);
                        cell.setCellStyle(styleGlobalContent);
                    }
                }
            }
            TotalPricesService totalPricesService = new TotalPricesService();
            totalPricesService.getTotalPrice(priceLineList);

            priceLineList.forEach(x -> x.setTotalPriceHT(totalPricesService.getTotalPriceHT()));
            priceLineList.forEach(x -> x.setTotalPriceTVAReduite(totalPricesService.getTotalPriceTVAReduce()));
            priceLineList.forEach(x -> x.setTotalPriceTVAInter(totalPricesService.getTotalPriceTVAInter()));
            priceLineList.forEach(x -> x.setTotalPriceTVANormale(totalPricesService.getTotalPriceTVANormal()));


            Sheet sheet = workbookDTO.getHssfWorkbook().getSheetAt(0);

            boolean tvaReduceAllowed = true;
            boolean tvaInterAllowed = true;

            for (int i = 4; i < nbPriceLines + 4; i++) {
                CellReference cellReferenceReduce = new CellReference("H" + i);
                Row lineReduce = sheet.getRow(cellReferenceReduce.getRow());
                Cell celluleReduce = lineReduce.getCell(cellReferenceReduce.getCol());
                String cReduceString = celluleReduce.toString();

                CellReference cellReferenceInter = new CellReference("I" + i);
                Row ligneInter = sheet.getRow(cellReferenceReduce.getRow());
                Cell celluleInter = ligneInter.getCell(cellReferenceInter.getCol());
                String cInterString = celluleInter.toString();

                if (cReduceString.equals("0.0")) tvaReduceAllowed = false;
                if (cInterString.equals("0.0")) tvaInterAllowed = false;

            }
            /**
             * Calcul of HT Price
             */
            int index = nbPriceLines + 5;
            HSSFRow rowTotalHT = workbookDTO.getHssfSheet().createRow(index);
            cell = rowTotalHT.createCell(3);
            cell.setCellValue("Prix HT");
            cell.setCellStyle(styleHeadTotalPrice);
            cell = rowTotalHT.createCell(4);
            int indexHT = nbPriceLines + 3;
            cell.setCellFormula("SUM(E4:E" + indexHT + ")");
            cell.setCellStyle(styleTotalPrice);

            /**
             * Calcul of TVA 5.5% Price
             */
            index++;
            HSSFRow rowTotalReduite = workbookDTO.getHssfSheet().createRow(index);
            cell = rowTotalReduite.createCell(3);
            cell.setCellValue("Prix TVA 5.5%");
            if (!tvaReduceAllowed) cell.setCellStyle(styleHeadTotalPrice);
            else cell.setCellStyle(styleHeadTotalPriceEffected);
            cell = rowTotalReduite.createCell(4);
            cell.setCellFormula("E" + index + "*1.055");
            int indexSortie = index;
            cell.setCellStyle(styleTotalPrice);
            if (!tvaReduceAllowed) rowTotalReduite.setZeroHeight(true);
            else cell.setCellStyle(styleTotalPriceEffected);

            /**
             * Calcul of TVA 10% Price
             */
            index++;
            HSSFRow rowTotalInter = workbookDTO.getHssfSheet().createRow(index);
            cell = rowTotalInter.createCell(3);
            cell.setCellValue("Prix TVA 10%");
            if (!tvaReduceAllowed) cell.setCellStyle(styleHeadTotalPriceEffected);
            else cell.setCellStyle(styleHeadTotalPrice);
            cell = rowTotalInter.createCell(4);
            cell.setCellFormula("E$" + indexSortie + "*1.1");
            cell.setCellStyle(styleTotalPrice);
            if (!tvaInterAllowed) rowTotalInter.setZeroHeight(true);
            else cell.setCellStyle(styleTotalPriceEffected);
            /**
             * Calcul of TVA 20% Price
             */
            index++;
            HSSFRow rowTotalNormale = workbookDTO.getHssfSheet().createRow(index);
            cell = rowTotalNormale.createCell(3);
            cell.setCellValue("Prix TVA 20%");
            if (!tvaReduceAllowed && !tvaInterAllowed) cell.setCellStyle(styleHeadTotalPriceEffected);
            else cell.setCellStyle(styleHeadTotalPrice);
            cell = rowTotalNormale.createCell(4);
            cell.setCellFormula("E$" + indexSortie + "*1.2");
            cell.setCellStyle(styleTotalPriceEffected);
            if (tvaReduceAllowed || tvaInterAllowed) cell.setCellStyle(styleTotalPrice);

            //Hidding column not useful to display
            for (int i = 5; i < 20; i++) workbookDTO.getHssfSheet().setColumnHidden(i, true);


            /**
             * Outputting to Excel spreadsheet
             */
            fos = new FileOutputStream(
                    new File(
                            pathXlsSimulation + "/PRICE_FROM_" + simulationCode + "." + XLS_EXTENSION));
            workbookDTO.getHssfWorkbook().write(fos);
            fos.flush();
            fos.close();

        }catch (XPathExpressionException e) {
            e.printStackTrace();
        }finally {
            log.info("*******************************");
            File file = new File(pathXlsSimulation + "/PRICE_FROM_" + simulationCode + "." + XLS_EXTENSION);
            if (file.exists()) {
                log.info("The file \"PRICE_FROM_" + simulationCode + "." + XLS_EXTENSION +
                        "\" has been created in :");
                log.info(pathXlsSimulation);
            } else {
                log.info("The file \"PRICE_FROM_" + simulationCode + "." + XLS_EXTENSION +
                        "\" has not been created...");
            }
            log.info("*******************************");

        }
        return priceLineList;
    }

    public void generateConversionRateExcel(
            double totalRate, double valueRate, double rate,
            InputRate inputRate) throws IOException {
        /**
         * Drain static repository
         */
        FileOutputStream fos;
        String currentDate =
                new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(System.currentTimeMillis()));
        String pathXlsCR = this.staticPathService.getPath(XLS_EXTENSION, STATIC_DIRECTORY_CONVERSION_RATE);

        String fileName = "LM - Conversion Rate";


        cleanDirectory(new File(pathXlsCR));

        //Init Worbook
        WorkbookDTO workbookDTO =
                ExcelUtils.workbookInit(inputRate.getXpathDefinition().getSimulationCode(), fileName);

        fileName = fileName + " - " + currentDate + "." + XLS_EXTENSION;

        /**
         * Definition of Cells Style
         */
        // Style of Title Cell
        HSSFCellStyle styleHead =
                StyleOfCellsService.getCustomStyleHead(workbookDTO.getHssfWorkbook());

        // Style of Price Content Cells
        HSSFCellStyle stylePriceContent =
                StyleOfCellsService.getCustomPriceContent(workbookDTO.getHssfWorkbook());

        // Style of Head Total Price Effected Cells
        HSSFCellStyle styleHeadTotalPriceEffected =
                StyleOfCellsService.getCustomHeadTotalPriceEffected(workbookDTO.getHssfWorkbook());

        // Style of Head Total Price Cells
        HSSFCellStyle styleHeadTotalPrice =
                StyleOfCellsService.getCustomHeadTotalPrice(workbookDTO.getHssfWorkbook());

        // Style of Total Price Effected Cells
        HSSFCellStyle styleConversionRate =
                StyleOfCellsService.getCustomConversionRate(workbookDTO.getHssfWorkbook());

        //Entete
        HSSFRow rowHead = workbookDTO.getHssfSheet().createRow(2);
        HSSFRow rowInput = workbookDTO.getHssfSheet().createRow(3);
        HSSFRow rowSimulation = workbookDTO.getHssfSheet().createRow(5);
        HSSFRow rowValueRate = workbookDTO.getHssfSheet().createRow(6);
        HSSFRow rowTotalRate = workbookDTO.getHssfSheet().createRow(7);
        HSSFRow rowRating = workbookDTO.getHssfSheet().createRow(8);

        String[] head = {"PERIODE", "VALEUR RECHERCHÉE", "OUTIL"};
        String[] input = {
                inputRate.getPeriod(),
                inputRate.getValueDescription(),
                inputRate.getXpathDefinition().getIdOAP()};

        HSSFCell cell = workbookDTO.getHssfCell();

        // Creating Row of Head
        for (int a = 0; a < head.length; a++) {
            cell = rowHead.createCell(a);
            cell.setCellValue(head[a]);
            cell.setCellStyle(styleHead);
        }
        // Creating Row of Input
        for (int a = 0; a < input.length; a++) {
            cell = rowInput.createCell(a);
            cell.setCellValue(input[a]);
            cell.setCellStyle(stylePriceContent);
        }

        //Title
        cell = rowSimulation.createCell(0);
        cell.setCellValue("SIMULATION");
        cell.setCellStyle(styleHead);

        //Value Rate
        cell = rowValueRate.createCell(0);
        cell.setCellValue("Avec " + inputRate.getValueDescription());
        cell.setCellStyle(styleHeadTotalPrice);
        cell = rowValueRate.createCell(1);
        cell.setCellValue(valueRate);
        cell.setCellStyle(styleHeadTotalPrice);

        //Total Rate
        cell = rowTotalRate.createCell(0);
        cell.setCellValue("Total");
        cell.setCellStyle(styleHeadTotalPrice);
        cell = rowTotalRate.createCell(1);
        cell.setCellValue(totalRate);
        cell.setCellStyle(styleHeadTotalPrice);

        //Conversion Rate
        cell = rowRating.createCell(0);
        cell.setCellValue("Taux de conversion");
        cell.setCellStyle(styleHeadTotalPriceEffected);
        cell = rowRating.createCell(1);
        cell.setCellValue(MathUtils.arrondiMathematique(rate, 2) + "%");
        cell.setCellFormula("B7/B8");
        cell.setCellStyle(styleConversionRate);

        //Outputting to Excel spreadsheet
        fos = new FileOutputStream(pathXlsCR + "/" + fileName);
        workbookDTO.getHssfWorkbook().write(fos);
        fos.flush();
        fos.close();

        log.info("*******************************");
        File file = new File(pathXlsCR + "/" + fileName);
        if (file.exists()) {
            log.info("The file \"" + fileName + "\" has been created in :");
            log.info(pathXlsCR);
        } else {
            log.info("The file \"" + fileName + "\" has not been created...");
        }
        log.info("*******************************");
    }


}
