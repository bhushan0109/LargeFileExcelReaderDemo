package com.bhushan.excel.service;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import com.bhushan.excel.entity.SalesRecord;
@Service
public class ExcelServiceHandler {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public List<SalesRecord> readExcel(String xlsFile) throws IOException {
        System.out.println("starting to file reader.......");
        long start=System.currentTimeMillis();
        List<SalesRecord> result = new ArrayList<>();
        int rowCount = 1;

        try (Workbook workbook = WorkbookFactory.create(new File(xlsFile))) {
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                if (rowCount == 1) {
                    // skip header
                    rowIterator.next();
                    rowCount++;
                } else {
                    Row row = rowIterator.next();

                    SalesRecord record = SalesRecord.builder()
                            .region(row.getCell(0).getStringCellValue().trim())
                            .country(row.getCell(1).getStringCellValue().trim())
                            .itemType(row.getCell(2).getStringCellValue().trim())
                            .salesChannel(row.getCell(3).getStringCellValue().trim())
                            .orderPriority(row.getCell(4).getStringCellValue().trim())
                            .orderDate( row.getCell(5).getStringCellValue().trim())
                            .orderID((double) row.getCell(8).getNumericCellValue())
                            .shipDate(row.getCell(7).getStringCellValue().trim())
                            .unitSold((double) row.getCell(8).getNumericCellValue())
                            .unitPrize((double) row.getCell(9).getNumericCellValue())
                            .unitCost((double) row.getCell(10).getNumericCellValue())
                            .totalRevenue((double) row.getCell(11).getNumericCellValue())
                            .totalCost((double) row.getCell(12).getNumericCellValue())
                            .totalProfit((double) row.getCell(13).getNumericCellValue())
                            .build();

                    result.add(record);
                    rowCount++;
                }
            }
        } catch (IOException e) {
            throw new IOException("fail to read excel file : " + e.getMessage(), e);
        }
        System.out.println("End of file reader......."+(System.currentTimeMillis()-start) +"  millisecond");

        return result;
    }
}
