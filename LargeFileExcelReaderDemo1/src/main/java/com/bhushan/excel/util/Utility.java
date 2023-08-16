package com.bhushan.excel.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhushan.excel.entity.SalesRecord;
import com.bhushan.excel.entity.UserAccount;
import com.bhushan.excel.repo.SalesRecordRepo;
import com.bhushan.excel.repo.UserAccountRepository;

@Service
public class Utility {

	@Autowired
	UserAccountRepository userAccountRepository;

	@Autowired
	SalesRecordRepo salesRecordRepo;

	public static final ExecutorService executor = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);

	public CompletableFuture<Void> executeBatchAccounts(List<UserAccount> userAccounts) {
		return CompletableFuture.runAsync(() -> {
			System.out.println("Current Thread Name: " + Thread.currentThread().getName());
			userAccountRepository.saveAll(userAccounts).clear();
		}, executor);
	}

	public List<List<UserAccount>> splitUserAccounts(List<UserAccount> userAccounts, int batchSize) {
		List<List<UserAccount>> batchUserAccounts = new ArrayList<>();
		int total = userAccounts.size() / batchSize;

		for (int i = 0; i < userAccounts.size(); i += total) {
			batchUserAccounts.add(userAccounts.subList(i, Math.min(i + total, userAccounts.size())));
		}
		return batchUserAccounts;
	}

	public CompletableFuture<Void> executeBatchSalesRecord(List<SalesRecord> salesRecords) {
		return CompletableFuture.runAsync(() -> {
			System.out.println("Current Thread Name: " + Thread.currentThread().getName());
			salesRecordRepo.saveAll(salesRecords).clear();
		}, executor);
	}

	public List<List<SalesRecord>> splitSalesRecord(List<SalesRecord> readLargeExcelSalesRecord, int batchSize) {
		List<List<SalesRecord>> readLargeExcelSalesRecords = new ArrayList<>();
		int total = readLargeExcelSalesRecord.size() / batchSize;

		for (int i = 0; i < readLargeExcelSalesRecord.size(); i += total) {
			readLargeExcelSalesRecords
					.add(readLargeExcelSalesRecord.subList(i, Math.min(i + total, readLargeExcelSalesRecord.size())));
		}
		return readLargeExcelSalesRecords;
	}

}
