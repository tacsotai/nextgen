package com.example.nextgen.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Seat {
	private Integer id;
	private String stand;
	private String event;
	private String type;
	private String position;
	private String place;
	private String isDeleted;
	private String createdBy;
	private Timestamp createdAt;
	private String updatedBy;
	private Timestamp updatedAt;
}
