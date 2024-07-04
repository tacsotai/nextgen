package com.example.nextgen.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.nextgen.entity.FewColumnsTran;
import com.example.nextgen.entity.ManyColumnsTran;
import com.example.nextgen.service.DbPerformanceTestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DbPerformanceTestController {

	/** 遷移先ページ */
	private static final String NEXT_PAGE = "dbPerformanceTest";

	/** 業務ロジック(サービス) */
	private final DbPerformanceTestService service;

	@GetMapping("/selectManyColumnsTable")
	public String selectManyColumnsTable() {

		// 起動したコントローラメソッドをログ記録
		log.info("/selectManyColumnsTable");

		// 次のページに遷移する
		return NEXT_PAGE;
	}

	@PostMapping("/selectFew")
	public String selectFew(Model model) {

		// 起動したコントローラメソッドをログ記録
		log.info("/selectFew");

		// 業務ロジックを呼び出す
		List<FewColumnsTran> fewColumnsTranList = service.selectFew();

		// モデルに業務ロジックの結果を設定する
		model.addAttribute("fewColumnsTranList", fewColumnsTranList);

		// 次のページに遷移する
		return NEXT_PAGE;
	}

	@PostMapping("/selectMany")
	public String selectMany(Model model) {

		// 起動したコントローラメソッドをログ記録
		log.info("/selectMany");

		// 業務ロジックを呼び出す
		List<ManyColumnsTran> manyColumnsTranList = service.selectMany();

		// モデルに業務ロジックの結果を設定する
		model.addAttribute("manyColumnsTranList", manyColumnsTranList);

		// 次のページに遷移する
		return NEXT_PAGE;
	}

	@PostMapping("/insertFew")
	public String insertFew(Model model) {

		// 起動したコントローラメソッドをログ記録
		log.info("/insertFew");

		// 業務ロジックを呼び出す
		var fewColumnsTran = new FewColumnsTran();
		fewColumnsTran.setCharColumn001("test01");
		fewColumnsTran.setCharColumn002("test02");
		fewColumnsTran.setCharColumn003("test03");
		fewColumnsTran.setCharColumn004("test04");
		fewColumnsTran.setCharColumn005("test05");
		service.insertFew(fewColumnsTran);

		// モデルに業務ロジックの結果を設定する
		List<FewColumnsTran> fewColumnsTranList = service.selectFew();
		model.addAttribute("fewColumnsTranList", fewColumnsTranList);

		// 次のページに遷移する
		return NEXT_PAGE;
	}

	@PostMapping("/insertMany")
	public String insertMany(Model model) {

		// 起動したコントローラメソッドをログ記録
		log.info("/insertMany");

		// 業務ロジックを呼び出す
		var manyColumnsTran = new ManyColumnsTran();
		manyColumnsTran.setCharColumn020("test020");
		manyColumnsTran.setCharColumn040("test040");
		manyColumnsTran.setCharColumn060("test060");
		manyColumnsTran.setCharColumn080("test080");
		manyColumnsTran.setCharColumn100("test100");
		service.insertMany(manyColumnsTran);

		// モデルに業務ロジックの結果を設定する
		List<ManyColumnsTran> manyColumnsTranList = service.selectMany();
		model.addAttribute("manyColumnsTranList", manyColumnsTranList);

		// 次のページに遷移する
		return NEXT_PAGE;
	}

	@PostMapping("/deleteFew")
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

	@PostMapping("/deleteMany")
	public String deleteMany(Model model) {

		// 起動したコントローラメソッドをログ記録
		log.info("/deleteMany");

		// 業務ロジックを呼び出す
		List<ManyColumnsTran> manyColumnsTranList = service.selectMany();
		var manyColumnsTran = new ManyColumnsTran();
		manyColumnsTran.setId(manyColumnsTranList.get(manyColumnsTranList.size() - 1).getId());
		service.deleteMany(manyColumnsTran);

		// モデルに業務ロジックの結果を設定する
		manyColumnsTranList = service.selectMany();
		model.addAttribute("manyColumnsTranList", manyColumnsTranList);

		// 次のページに遷移する
		return NEXT_PAGE;
	}
}
