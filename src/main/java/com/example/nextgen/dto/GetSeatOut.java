package com.example.nextgen.dto;

import java.util.List;

import com.example.nextgen.entity.Seat;

import lombok.Data;

/**
 * シートテーブルのGETリクエスト時の出力DTO
 */
@Data
public class GetSeatOut {

	/** 0なら正常終了、それ以外ならエラー */
	private Integer result;

	/** エラーメッセージリスト */
	private List<String> errMsgList;

	/** シートテーブルのレコードリスト */
	private List<Seat> seatList;
}
