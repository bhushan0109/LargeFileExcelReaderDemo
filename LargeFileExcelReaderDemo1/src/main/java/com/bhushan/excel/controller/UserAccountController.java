package com.bhushan.excel.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.bhushan.excel.util.Utility;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserAccountController {

	@Autowired
	private ExcelReaderService excelReaderService;

	@Autowired
	private ExcelServiceHandler excelServiceHandler;

	@Autowired
	Utility utility;

	@PostMapping("/readLargeExcelUser")
	public ResponseEntity<?> readLargeExcelUserAccount(@RequestParam("file") MultipartFile multipartFile)
			throws IOException {

		List<UserAccount> readLargeExcelUserAccount = excelReaderService.readLargeExcelUserAccount(multipartFile);

		// utility.executeBatch(readLargeExcelUserAccount); // all save one time

		long start = System.currentTimeMillis();
		int splitSize = 25;
		List<List<UserAccount>> partitions = utility.splitUserAccounts(readLargeExcelUserAccount, splitSize);
		System.out.println("sub partitions=" + partitions.size());
		CompletableFuture[] futures = new CompletableFuture[partitions.size()];
		for (int i = 0; i < partitions.size(); i++) {
			System.out.println("executeBatch=" + i);
			futures[i] = utility.executeBatchAccounts(partitions.get(i));
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
		List<List<SalesRecord>> partitions = utility.splitSalesRecord(readLargeExcelSalesRecord, splitSize);
		System.out.println("sub partitions=" + partitions.size());
		CompletableFuture[] futures = new CompletableFuture[partitions.size()];
		for (int i = 0; i < partitions.size(); i++) {
			System.out.println("executeBatch=" + i);
			futures[i] = utility.executeBatchSalesRecord(partitions.get(i));
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

}
