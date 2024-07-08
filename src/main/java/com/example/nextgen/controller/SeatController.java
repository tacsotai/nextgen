package com.example.nextgen.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.nextgen.entity.Seat;
import com.example.nextgen.restcontroller.SeatRestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SeatController {

	private final SeatRestController seatRestController;

	/** 遷移先ページ */
	private static final String NEXT_PAGE = "seat";

	@GetMapping("/getSeatList")
	public String getSeatList() {

		// 起動したコントローラメソッドをログ記録
		log.info("/getSeatList");

		// 次のページに遷移する
		return NEXT_PAGE;
	}

	@PostMapping("/selectSeat")
	public String selectSeat(Model model) {

		// 起動したコントローラメソッドをログ記録
		log.info("/selectSeat");

		// APIを呼び出す
		List<Seat> seatList = seatRestController.getSeatList().getBody().getSeatList();

		// モデルに業務ロジックの結果を設定する
		model.addAttribute("seatList", seatList);

		// 次のページに遷移する
		return NEXT_PAGE;
	}

	@PostMapping("/insertSeat")
	public String insertSeat(Model model) {

		// 起動したコントローラメソッドをログ記録
		log.info("/insertSeat");

		// APIを呼び出す
		var seat = new Seat();
		seat.setStand("stand01");
		seat.setEvent("event01");
		seat.setType("type01");
		seat.setPosition("position01");
		seat.setPlace("place01");
		List<Seat> seatList = seatRestController.insertSeat(seat).getBody().getSeatList();

		// モデルに業務ロジックの結果を設定する
		model.addAttribute("seatList", seatList);

		// 次のページに遷移する
		return NEXT_PAGE;
	}

	@PostMapping("/deleteSeat")
	public String deleteSeat(Model model) {

		// 起動したコントローラメソッドをログ記録
		log.info("/deleteSeat");

		// APIを呼び出す
		List<Seat> seatList = seatRestController.getSeatList().getBody().getSeatList();
		Integer id = seatList.get(seatList.size() - 1).getId();
		seatList = seatRestController.deleteSeat(id).getBody().getSeatList();

		// モデルに業務ロジックの結果を設定する
		model.addAttribute("seatList", seatList);

		// 次のページに遷移する
		return NEXT_PAGE;
	}
}
