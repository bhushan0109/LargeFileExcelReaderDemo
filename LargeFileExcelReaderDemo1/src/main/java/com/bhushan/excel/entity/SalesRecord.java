package com.bhushan.excel.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "sales_record")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class SalesRecord {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    private String region;

    private String country;

    private String itemType;

    private String salesChannel;

    private  String orderPriority;

    private String orderDate;

    private double orderID;

    private String shipDate;

    private double unitSold;

    private double unitPrize;

    private double unitCost;

    private double totalRevenue;

    private double totalCost;

    private double totalProfit;

}
