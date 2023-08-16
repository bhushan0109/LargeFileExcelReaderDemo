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

@Table(name = "user_accounts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class UserAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	private String firstName;

	private String lastName;

	private String email;

	private String gender;

	private String jobTitle;
	
	private String ram1;
	private String ram2;
	private String ram3;
	private String ram4;
	private String ram5;
	private String ram6;
	private String ram7;
	private String ram8;
	private String ram9;
	private String ram10;
	private String ram11;
	private String ram12;
	private String ram13;
	private String ram14;
	private String ram15;
	private String ram16;
	private String ram17;
	private String ram18;
	private String ram19;
	private String ram20;
	private String ram21;
	private String ram22;
	private String ram23;
	private String ram24;
	private String ram25;
	
	
}
