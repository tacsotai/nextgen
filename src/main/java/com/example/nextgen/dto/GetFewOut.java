package com.example.nextgen.dto;

import java.util.List;

import com.example.nextgen.entity.Seat;

import lombok.Data;

/**
 * 少数項目トランザクションテーブルのGETリクエスト時の出力DTO
 */
@Data
public class GetFewOut {

	/** 0なら正常終了、それ以外ならエラー */
	private Integer result;

	/** エラーメッセージリスト */
	private List<String> errMsgList;

	/** シートテーブルのレコードリスト */
	private List<Seat> seatList;
}
