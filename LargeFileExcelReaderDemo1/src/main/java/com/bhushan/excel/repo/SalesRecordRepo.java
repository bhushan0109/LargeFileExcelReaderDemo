package com.bhushan.excel.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bhushan.excel.entity.SalesRecord;

@Repository
public interface SalesRecordRepo  extends JpaRepository<SalesRecord, Long> {

}
