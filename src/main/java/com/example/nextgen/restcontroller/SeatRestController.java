package com.example.nextgen.restcontroller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.nextgen.dto.DeleteFewOut;
import com.example.nextgen.dto.GetFewOut;
import com.example.nextgen.dto.PostFewOut;
import com.example.nextgen.entity.Seat;
import com.example.nextgen.service.SeatService;
import com.example.nextgen.util.ExceptionUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SeatRestController {

	/** 業務ロジック(サービス) */
	private final SeatService service;

	/** 例外処理ユーティリティ */
	private final ExceptionUtil exceptionUti;

	@GetMapping("/seat/v1")
	public ResponseEntity<GetFewOut> getSeatList() {

		// 起動したコントローラメソッドをログ記録
		log.info("GET /seat/v1");

		var getFewOut = new GetFewOut();
		try {
			// 業務ロジックを呼び出す
			List<Seat> seatTranList = service.getSeatList();
			getFewOut.setSeatList(seatTranList);

		} catch (Exception e) {

			// 例外発生時は、何らかの方法でエラーメッセージを取得して出力パラメータに渡す
			getFewOut.setErrMsgList(exceptionUti.toErrMsgList(e));
		}

		// JSON値を返却する
		return ResponseEntity.ok(getFewOut);
	}

	@PostMapping("/seat/v1")
	public ResponseEntity<PostFewOut> insertSeat(@RequestBody Seat seat) {

		// 起動したコントローラメソッドをログ記録
		log.info("POST /seat/v1");

		var postFewOut = new PostFewOut();
		try {
			// 業務ロジックを呼び出す
			service.insertSeat(seat);
			List<Seat> seatList = service.getSeatList();
			postFewOut.setSeatList(seatList);

		} catch (Exception e) {

			// 例外発生時は、何らかの方法でエラーメッセージを取得して出力パラメータに渡す
			postFewOut.setErrMsgList(exceptionUti.toErrMsgList(e));
		}

		// JSON値を返却する
		return ResponseEntity.ok(postFewOut);
	}

	@DeleteMapping("/seat/v1")
	public ResponseEntity<DeleteFewOut> deleteSeat(@RequestParam Integer id) {

		// 起動したコントローラメソッドをログ記録
		log.info("DELETE /seat/v1");

		var deleteFewOut = new DeleteFewOut();
		try {
			// 業務ロジックを呼び出す
			var seat = new Seat();
			seat.setId(id);
			service.deleteSeat(seat);
			List<Seat> seatList = service.getSeatList();
			deleteFewOut.setSeatList(seatList);

		} catch (Exception e) {

			// 例外発生時は、何らかの方法でエラーメッセージを取得して出力パラメータに渡す
			deleteFewOut.setErrMsgList(exceptionUti.toErrMsgList(e));
		}

		// JSON値を返却する
		return ResponseEntity.ok(deleteFewOut);
	}
}
