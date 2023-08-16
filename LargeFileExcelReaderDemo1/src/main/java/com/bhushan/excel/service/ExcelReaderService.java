package com.bhushan.excel.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bhushan.excel.entity.SalesRecord;
import com.bhushan.excel.entity.UserAccount;
import com.monitorjbl.xlsx.StreamingReader;

@Service
public class ExcelReaderService {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	public List<UserAccount> readLargeExcelUserAccount(MultipartFile multipartFile) throws IOException {

		System.out.println("starting to file reader.......");
		long start = System.currentTimeMillis();
		long start1 = System.currentTimeMillis();
		List<UserAccount> listUserAccount = new ArrayList<>();
		int rowCount = 1;
		try (Workbook workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096)
				.open(multipartFile.getInputStream())) {
			
			Sheet sheet1 = workbook.getSheetAt(0);

			int lastRowNum = sheet1.getLastRowNum();
			System.out.println("total lastRowNum==>" + lastRowNum);
			
//			int rowStart = Math.min(0, sheet1.getFirstRowNum());
//			int rowEnd = Math.max(0, sheet1.getLastRowNum());
//			System.out.println("total rowEnd==>" + rowEnd);
			
			long end1 = System.currentTimeMillis();
			NumberFormat formatter1 = new DecimalFormat("#0.00000");
			System.out.println(
					"Execution time workbook convert=" + formatter1.format((end1 - start1) / 1000d) + " seconds");
			for (Sheet sheet : workbook) {
				for (Row row : sheet) {
					if (rowCount == 1) {
						rowCount++;
					} else {
						UserAccount record = UserAccount.builder().firstName(row.getCell(0).getStringCellValue().trim())
								.lastName(row.getCell(1).getStringCellValue().trim())
								.email(row.getCell(2).getStringCellValue().trim())
								.gender(row.getCell(3).getStringCellValue().trim())
								.jobTitle(row.getCell(4).getStringCellValue().trim())
								.ram1(row.getCell(5).getStringCellValue().trim())
								.ram3(row.getCell(6).getStringCellValue().trim())
								.ram4(row.getCell(7).getStringCellValue().trim())
								.ram5(row.getCell(8).getStringCellValue().trim())
								.ram6(row.getCell(9).getStringCellValue().trim())
								.ram7(row.getCell(10).getStringCellValue().trim())
								.ram8(row.getCell(11).getStringCellValue().trim())
								.ram9(row.getCell(12).getStringCellValue().trim())
								.ram10(row.getCell(13).getStringCellValue().trim())
								.ram11(row.getCell(14).getStringCellValue().trim())
								.ram12(row.getCell(15).getStringCellValue().trim())
								.ram13(row.getCell(16).getStringCellValue().trim())
								.ram14(row.getCell(17).getStringCellValue().trim())
								.ram15(row.getCell(18).getStringCellValue().trim())
								.ram16(row.getCell(19).getStringCellValue().trim())
								.ram17(row.getCell(20).getStringCellValue().trim())
								.ram18(row.getCell(21).getStringCellValue().trim())
								.ram19(row.getCell(22).getStringCellValue().trim())
								.ram20(row.getCell(23).getStringCellValue().trim())
								.ram21(row.getCell(24).getStringCellValue().trim())
								.ram22(row.getCell(25).getStringCellValue().trim())
								.ram23(row.getCell(26).getStringCellValue().trim())
								.ram24(row.getCell(27).getStringCellValue().trim())
								.ram25(row.getCell(28).getStringCellValue().trim()).build();

						listUserAccount.add(record);
					}
				}
			}
		} catch (IOException e) {
			throw new IOException("fail to read excel file : " + e.getMessage(), e);
		}
		System.out.println(" record size is ===>" + listUserAccount.size());
		long end = System.currentTimeMillis();
		NumberFormat formatter1 = new DecimalFormat("#0.00000");
		System.out
				.println("Execution time End of file reader=" + formatter1.format((end - start) / 1000d) + " seconds");
		return listUserAccount;
	}

	public List<SalesRecord> readLargeExcelSalesRecord(MultipartFile multipartFile) throws IOException {

		System.out.println("starting to file reader.......");
		long start = System.currentTimeMillis();
		long start1 = System.currentTimeMillis();
		List<SalesRecord> listSalesRecord = new ArrayList<>();
		int rowCount = 1;
		try (Workbook workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096)
				.open(multipartFile.getInputStream())) {
			Sheet sheet1 = workbook.getSheetAt(0);
	
			int lastRowNum = sheet1.getLastRowNum();
			System.out.println("total lastRowNum==>" + lastRowNum);
			
//			int rowStart = Math.min(0, sheet1.getFirstRowNum());
//			int rowEnd = Math.max(0, sheet1.getLastRowNum());
//			System.out.println("total rowEnd==>" + rowEnd);
			long end1 = System.currentTimeMillis();
			NumberFormat formatter1 = new DecimalFormat("#0.00000");
			System.out.println(
					"Execution time workbook convert=" + formatter1.format((end1 - start1) / 1000d) + " seconds");
			for (Sheet sheet : workbook) {
				for (Row row : sheet) {
					if (rowCount == 1) {
						rowCount++;
					} else {
						SalesRecord record = SalesRecord.builder().region(row.getCell(0).getStringCellValue().trim())
								.country(row.getCell(1).getStringCellValue().trim())
								.itemType(row.getCell(2).getStringCellValue().trim())
								.salesChannel(row.getCell(3).getStringCellValue().trim())
								.orderPriority(row.getCell(4).getStringCellValue().trim())
								.orderDate(row.getCell(5).getStringCellValue().trim())
								.orderID((double) row.getCell(8).getNumericCellValue())
								.shipDate(row.getCell(7).getStringCellValue().trim())
								.unitSold((double) row.getCell(8).getNumericCellValue())
								.unitPrize((double) row.getCell(9).getNumericCellValue())
								.unitCost((double) row.getCell(10).getNumericCellValue())
								.totalRevenue((double) row.getCell(11).getNumericCellValue())
								.totalCost((double) row.getCell(12).getNumericCellValue())
								.totalProfit((double) row.getCell(13).getNumericCellValue()).build();

						listSalesRecord.add(record);
					}
				}
			}
		} catch (IOException e) {
			throw new IOException("fail to read excel file : " + e.getMessage(), e);
		}
		System.out.println(" record size is ===>" + listSalesRecord.size());
		long end = System.currentTimeMillis();
		NumberFormat formatter1 = new DecimalFormat("#0.00000");
		System.out
				.println("Execution time End of file reader=" + formatter1.format((end - start) / 1000d) + " seconds");
		return listSalesRecord;

	}

}
