package com.example.nextgen.dto;

import java.util.List;

import com.example.nextgen.entity.FewColumnsTran;

import lombok.Data;

/**
 * 少数項目トランザクションテーブル
 */
@Data
public class GetFewOut {

	/** 0なら正常終了、それ以外ならエラー */
	private Integer result;

	/** エラーメッセージリスト */
	private List<String> errMsgList;

	/** 少数項目トランザクションテーブルのレコードリスト */
	private List<FewColumnsTran> fewColumnsTranList;
}
