package com.example.nextgen.restcontroller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.nextgen.dto.GetFewOut;
import com.example.nextgen.entity.FewColumnsTran;
import com.example.nextgen.service.DbPerformanceTestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DbPerformanceTestRestController {

	/** 業務ロジック(サービス) */
	private final DbPerformanceTestService service;

	@GetMapping("/few/v1")
	public ResponseEntity<GetFewOut> selectManyColumnsTable() {

		// 起動したコントローラメソッドをログ記録
		log.info("/selectManyColumnsTable");

		// 業務ロジックを呼び出す
		List<FewColumnsTran> fewColumnsTranList = service.selectFew();

		var getFewOut = new GetFewOut();
		getFewOut.setFewColumnsTranList(null);
		return ResponseEntity.ok(getFewOut);
	}

	@PostMapping("/few/v1")
	public String selectFew(Model model) {

		// 起動したコントローラメソッドをログ記録
		log.info("/select");

		// 業務ロジックを呼び出す
		List<FewColumnsTran> fewColumnsTranList = service.selectFew();

		// モデルに業務ロジックの結果を設定する
		model.addAttribute("fewColumnsTranList", fewColumnsTranList);

		// 次のページに遷移する
		return NEXT_PAGE;
	}

	@DeleteMapping("/few/v1")
	public String deleteFew(Model model) {

		// 起動したコントローラメソッドをログ記録
		log.info("/deleteFew");

		// 業務ロジックを呼び出す
		List<FewColumnsTran> fewColumnsTranList = service.selectFew();
		var fewColumnsTran = new FewColumnsTran();
		fewColumnsTran.setId(fewColumnsTranList.get(fewColumnsTranList.size() - 1).getId());
		service.deleteFew(fewColumnsTran);

		// モデルに業務ロジックの結果を設定する
		fewColumnsTranList = service.selectFew();
		model.addAttribute("fewColumnsTranList", fewColumnsTranList);

		// 次のページに遷移する
		return NEXT_PAGE;
	}
}
