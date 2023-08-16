package com.bhushan.excel.controller;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bhushan.excel.entity.SalesRecord;
import com.bhushan.excel.entity.UserAccount;
import com.bhushan.excel.repo.SalesRecordRepo;
import com.bhushan.excel.repo.UserAccountRepository;
import com.bhushan.excel.service.ExcelReaderService;
import com.bhushan.excel.service.ExcelServiceHandler;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserAccountController {

	@Autowired
	private final ExcelReaderService excelReaderService;

	@Autowired
	private final UserAccountRepository userAccountRepository;

	@Autowired
	SalesRecordRepo salesRecordRepo;

	@Autowired
	private final ExcelServiceHandler excelServiceHandler;

	private static final ExecutorService executor = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);

	@PostMapping("/readLargeExcelUser")
	public ResponseEntity<?> readLargeExcelUserAccount(@RequestParam("file") MultipartFile multipartFile)
			throws IOException {

		List<UserAccount> readLargeExcelUserAccount = excelReaderService.readLargeExcelUserAccount(multipartFile);

		// userAccountRepository.saveAll(readLargeExcelUserAccount); // all save one
		// time
		// executeBatch(readLargeExcelUserAccount);

		long start = System.currentTimeMillis();
		int splitSize = 25;
		List<List<UserAccount>> partitions = splitUserAccounts(readLargeExcelUserAccount, splitSize);
		System.out.println("sub partitions=" + partitions.size());
		CompletableFuture[] futures = new CompletableFuture[partitions.size()];
		for (int i = 0; i < partitions.size(); i++) {
			System.out.println("executeBatch=" + i);
			futures[i] = executeBatchAccounts(partitions.get(i));
		}
		System.out.println("execute Batch done");
		long end = System.currentTimeMillis();
		NumberFormat formatter1 = new DecimalFormat("#0.00000");
		System.out.println(
				"Execution time is for all save data=" + formatter1.format((end - start) / 1000d) + " seconds");
		// CompletableFuture.allOf(futures);

		return ResponseEntity.ok("File Reading complete with async");
	}

	@PostMapping("/readLargeExcelSalesRecord")
	public ResponseEntity<?> readLargeExcelSalesRecord(@RequestParam("file") MultipartFile multipartFile)
			throws IOException {
		List<SalesRecord> readLargeExcelSalesRecord = excelReaderService.readLargeExcelSalesRecord(multipartFile);

		long start = System.currentTimeMillis();
		int splitSize = 25;
		List<List<SalesRecord>> partitions = splitSalesRecord(readLargeExcelSalesRecord, splitSize);
		System.out.println("sub partitions=" + partitions.size());
		CompletableFuture[] futures = new CompletableFuture[partitions.size()];
		for (int i = 0; i < partitions.size(); i++) {
			System.out.println("executeBatch=" + i);
			futures[i] = executeBatchSalesRecord(partitions.get(i));
		}
		System.out.println("execute Batch done");
		long end = System.currentTimeMillis();
		NumberFormat formatter1 = new DecimalFormat("#0.00000");
		System.out.println(
				"Execution time is for all save data=" + formatter1.format((end - start) / 1000d) + " seconds");
		// CompletableFuture.allOf(futures);

		return ResponseEntity.ok("File Reading complete with async");
	}

	@PostMapping("/readExcel")
	public ResponseEntity<?> readExcel(@RequestParam("file") MultipartFile multipartFile) throws IOException {
		String originalFilename = multipartFile.getOriginalFilename();
		String originalFilenamePath = "/" + originalFilename;
		excelServiceHandler.readExcel(originalFilenamePath);
		return ResponseEntity.ok("File Reading complete with async object split");
	}

	private CompletableFuture<Void> executeBatchAccounts(List<UserAccount> userAccounts) {
		return CompletableFuture.runAsync(() -> {
			System.out.println("Current Thread Name: " + Thread.currentThread().getName());
			userAccountRepository.saveAll(userAccounts).clear();
		}, executor);
	}

	private List<List<UserAccount>> splitUserAccounts(List<UserAccount> userAccounts, int batchSize) {
		List<List<UserAccount>> batchUserAccounts = new ArrayList<>();
		int total = userAccounts.size() / batchSize;

		for (int i = 0; i < userAccounts.size(); i += total) {
			batchUserAccounts.add(userAccounts.subList(i, Math.min(i + total, userAccounts.size())));
		}
		return batchUserAccounts;
	}

	private CompletableFuture<Void> executeBatchSalesRecord(List<SalesRecord> salesRecords) {
		return CompletableFuture.runAsync(() -> {
			System.out.println("Current Thread Name: " + Thread.currentThread().getName());
			salesRecordRepo.saveAll(salesRecords).clear();
		}, executor);
	}

	private List<List<SalesRecord>> splitSalesRecord(List<SalesRecord> readLargeExcelSalesRecord, int batchSize) {
		List<List<SalesRecord>> readLargeExcelSalesRecords = new ArrayList<>();
		int total = readLargeExcelSalesRecord.size() / batchSize;

		for (int i = 0; i < readLargeExcelSalesRecord.size(); i += total) {
			readLargeExcelSalesRecords
					.add(readLargeExcelSalesRecord.subList(i, Math.min(i + total, readLargeExcelSalesRecord.size())));
		}
		return readLargeExcelSalesRecords;
	}

//	@PostMapping("/withoutAsync")
//	public ResponseEntity<?> batchInsertWithoutAsync(@RequestParam("file") MultipartFile multipartFile)
//			throws Exception {
//		
//		
//		
//		// userAccountService.batchInsertWithoutAsync(multipartFile);
//		System.out.println("started");
//		
//		
//		Workbook wb = StreamingReader.builder()   
//			    .rowCacheSize(100)    
//			    .open(multipartFile.getInputStream()); 
//		System.out.println("end");
//		//InputStream is = new FileInputStream(new File("G:\\Book1.xlsx"));
//	//	InputStream is = new FileInputStream(new File("G:\\Book1.xlsx"));
////		InputStream is = new FileInputStream(new File("G:\\Book1.xlsx"));
////	    Workbook  workbook = StreamingReader.builder()
////	            .rowCacheSize(100)    
////	            .bufferSize(4096)     
////	            .open(is);      
//	  //  Sheet sheet = workbook.getSheetAt(0);
////	    int rowStart = Math.min(0, sheet.getFirstRowNum());
////		int rowEnd = Math.max(0, sheet.getLastRowNum());
////		System.out.println("complet rowEnd"+rowEnd);
//
////		InputStream inputstream = multipartFile.getInputStream();
////		XSSFWorkbook workbook;
////		try {
////			workbook = new XSSFWorkbook(inputstream);
////
////			Sheet sheet = workbook.getSheetAt(0);
////			int rowStart = Math.min(0, sheet.getFirstRowNum());
////			int rowEnd = Math.max(0, sheet.getLastRowNum());
////			if (rowStart == 0 && rowEnd == 0 && sheet.getRow(rowStart) == null) {
////				throw new Exception();
////			}
////
////			if (sheet.getRow(rowStart + 1) == null) {
////				throw new Exception();
////			}
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//
//		return ResponseEntity.ok("File Reading complete without async");
//	}

}
