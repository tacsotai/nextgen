package com.example.nextgen.restcontroller;

import java.util.List;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.nextgen.dto.DeleteSeatOut;
import com.example.nextgen.dto.GetSeatOut;
import com.example.nextgen.dto.PostSeatOut;
import com.example.nextgen.dto.SearchSeatCondIn;
import com.example.nextgen.entity.Seat;
import com.example.nextgen.service.SeatService;
import com.example.nextgen.util.ExceptionUtil;
import com.example.nextgen.util.PropCopyUtil;

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

	/** プロパティコピーユーティリティ */
	private final PropCopyUtil propCopyUtil;

	@InitBinder
	public void initBuilder(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@GetMapping("/api/v1/seats")
	public ResponseEntity<GetSeatOut> getSeatList() {

		// 起動したコントローラメソッドをログ記録
		log.info("GET /api/v1/seats");

		var getSeatOut = new GetSeatOut();
		try {
			// 業務ロジックを呼び出す
			List<Seat> seatTranList = service.getSeatList();
			getSeatOut.setSeatList(seatTranList);

		} catch (Exception e) {

			// 例外発生時は、何らかの方法でエラーメッセージを取得して出力パラメータに渡す
			getSeatOut.setErrMsgList(exceptionUti.toErrMsgList(e));
		}

		// JSON値を返却する
		return ResponseEntity.ok(getSeatOut);
	}

	@PostMapping("/api/v1/search")
	public ResponseEntity<PostSeatOut> searchSeat(@RequestBody SearchSeatCondIn searchSeatCondIn) {

		// 起動したコントローラメソッドをログ記録
		log.info("POST /api/v1/search");

		var postSeatOut = new PostSeatOut();
		try {
			// 業務ロジックを呼び出す
			var searchSeatCondInNonBlank = new SearchSeatCondIn();
			propCopyUtil.copyBlankAsNull(searchSeatCondIn, searchSeatCondInNonBlank);
			List<Seat> seatList = service.searchSeat(searchSeatCondInNonBlank);
			postSeatOut.setSeatList(seatList);

		} catch (Exception e) {

			// 例外発生時は、何らかの方法でエラーメッセージを取得して出力パラメータに渡す
			postSeatOut.setErrMsgList(exceptionUti.toErrMsgList(e));
		}

		// JSON値を返却する
		return ResponseEntity.ok(postSeatOut);
	}

	@PostMapping("/api/v1/seats")
	public ResponseEntity<PostSeatOut> insertSeat(@RequestBody Seat seat) {

		// 起動したコントローラメソッドをログ記録
		log.info("POST /api/v1/seats");

		var postSeatOut = new PostSeatOut();
		try {
			// 業務ロジックを呼び出す
			service.insertSeat(seat);
			List<Seat> seatList = service.getSeatList();
			postSeatOut.setSeatList(seatList);

		} catch (Exception e) {

			// 例外発生時は、何らかの方法でエラーメッセージを取得して出力パラメータに渡す
			postSeatOut.setErrMsgList(exceptionUti.toErrMsgList(e));
		}

		// JSON値を返却する
		return ResponseEntity.ok(postSeatOut);
	}

	@DeleteMapping("/api/v1/seats")
	public ResponseEntity<DeleteSeatOut> deleteSeat(@RequestParam Integer id) {

		// 起動したコントローラメソッドをログ記録
		log.info("DELETE /api/v1/seats");

		var deleteSeatOut = new DeleteSeatOut();
		try {
			// 業務ロジックを呼び出す
			var seat = new Seat();
			seat.setId(id);
			service.deleteSeat(seat);
			List<Seat> seatList = service.getSeatList();
			deleteSeatOut.setSeatList(seatList);

		} catch (Exception e) {

			// 例外発生時は、何らかの方法でエラーメッセージを取得して出力パラメータに渡す
			deleteSeatOut.setErrMsgList(exceptionUti.toErrMsgList(e));
		}

		// JSON値を返却する
		return ResponseEntity.ok(deleteSeatOut);
	}
}
