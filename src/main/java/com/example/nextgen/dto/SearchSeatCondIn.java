package com.example.nextgen.dto;

import lombok.Data;

/**
 * 検索条件の入力DTO
 */
@Data
public class SearchSeatCondIn {
	private String stand;
	private String event;
	private String type;
	private String position;
}
