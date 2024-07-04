package com.example.nextgen.entity;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FewColumnsTran {
	private Integer id;
	private String charColumn001;
	private String charColumn002;
	private String charColumn003;
	private String charColumn004;
	private String charColumn005;
	private String isDeleted;
	private String createdBy;
	private Timestamp createdAt;
	private String updatedBy;
	private Timestamp updatedAt;
}
