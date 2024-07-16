package com.example.nextgen.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nextgen.dto.SearchSeatCondIn;
import com.example.nextgen.entity.Seat;
import com.example.nextgen.restcontroller.SeatRestController;
import com.example.nextgen.service.SeatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SeatController {

	private final SeatRestController seatRestController;
	private final SeatService service;

	/** 遷移先ページ */
	private static final String NEXT_PAGE = "seat";

	/** 詳細ページ */
	private static final String DETAIL_PAGE = "seatDetail";

	@GetMapping("/getSeats")
	public String getSeats(Model model) throws Exception {

		// 起動したコントローラメソッドをログ記録
		log.info("/getSeats");

		// APIを呼び出す
		List<Seat> seatList = service.getSeatList();

		// モデルに業務ロジックの結果を設定する
		model.addAttribute("seatList", seatList);

		// 次のページに遷移する
		return NEXT_PAGE;
	}

	@GetMapping("/getSeatsDetail")
	public String getSeatDetail(Model model, @RequestParam String id) throws Exception {

		// IDで示されるレコードを取得する
		Seat seat = service.getSeat(Integer.valueOf(id));

		// モデルに業務ロジックの結果を設定する
		model.addAttribute("seat", seat);

		// 詳細ページに遷移する
		return DETAIL_PAGE;
	}

	/**
	 * プロパティがnullの場合に、null以外の値を設定。(SQLエラー回避のため)
	 * カラム数が大きいテーブルで
	 * 
	 * @param target 対象インスタンス
	 * @throws Exception 例外
	 */
	protected void setDefaultValueIfNull(Object target) throws Exception {

		// リフレクションにより、全てのメソッドを処理するまでループ
		List<String> memberNameList = new ArrayList<String>();
		Class<?> targetClass = target.getClass();
		for (Method method : targetClass.getMethods()) {

			// メソッドが "get" で始まるもの以外は処理しない
			if (!method.getName().startsWith("get")) {
				continue;
			}

			// getterメソッドにてnull以外が検知された場合は、それ以上処理せずループの先頭に戻る
			if (method.invoke(target) != null) {
				continue;
			}

			// getterが対象としているメンバ名を取得し、リストに追加する
			memberNameList.add(method.getName().substring("get".length(), method.getName().length()));
		}

		for (Method method : targetClass.getMethods()) {

			// メソッドが "set" で始まるもの以外は処理しない
			if (!method.getName().startsWith("set")) {
				continue;
			}

			String methodName = method.getName().substring("get".length(), method.getName().length());
			if (!memberNameList.contains(methodName)) {
				continue;
			}

			// setterにてnull以外の値を設定する
			try {
				method.invoke(target, "1");
			} catch (Exception e) {
				try {
					method.invoke(target, 0);
				} catch (Exception e1) {
					try {
						method.invoke(target, new java.sql.Date(new Date().getTime()));
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}
	}

	//---------------------------------------------------------------------//
	// 以降のメソッドは、現在呼ばれていない
	// (ajax経由でRestControllerを呼び出しているため)
	//---------------------------------------------------------------------//

	@PostMapping("/selectSeats")
	public String selectSeat(Model model, @RequestBody SearchSeatCondIn searchSeatCondIn)
			throws Exception {

		// 起動したコントローラメソッドをログ記録
		log.info("/selectSeats");

		// APIを呼び出す
		List<Seat> seatList = service.searchSeat(searchSeatCondIn);

		// モデルに業務ロジックの結果を設定する
		model.addAttribute("seatList", seatList);

		// 次のページに遷移する
		return NEXT_PAGE;
	}

	@PostMapping("/insertSeats")
	public String insertSeat(Model model) {

		// 起動したコントローラメソッドをログ記録
		log.info("/insertSeats");

		// APIを呼び出す
		var seat = new Seat();
		String randId = String.valueOf(new Date().getTime() / 100);
		seat.setStand("stand" + randId);
		seat.setEvent("event" + randId);
		seat.setType("type" + randId);
		seat.setPosition("position" + randId);
		seat.setPlace("place01");
		List<Seat> seatList = seatRestController.insertSeat(seat).getBody().getSeatList();

		// モデルに業務ロジックの結果を設定する
		model.addAttribute("seatList", seatList);

		// 次のページに遷移する
		return NEXT_PAGE;
	}

	@PostMapping("/deleteSeats")
	public String deleteSeat(Model model) {

		// 起動したコントローラメソッドをログ記録
		log.info("/deleteSeats");

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
