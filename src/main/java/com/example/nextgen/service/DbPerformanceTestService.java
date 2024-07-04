package com.example.nextgen.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nextgen.entity.FewColumnsTran;
import com.example.nextgen.entity.ManyColumnsTran;
import com.example.nextgen.mapper.FewColumnsTranMapper;
import com.example.nextgen.mapper.ManyColumnsTranMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DbPerformanceTestService {

	private final FewColumnsTranMapper fewColumnsTranMapper;
	private final ManyColumnsTranMapper manyColumnsTranMapper;

	public List<FewColumnsTran> selectFew() {
		return fewColumnsTranMapper.select();
	}

	@Transactional(rollbackFor = Exception.class)
	public int insertFew(FewColumnsTran fewColumnsTran) {
		return fewColumnsTranMapper.insert(fewColumnsTran);
	}

	@Transactional(rollbackFor = Exception.class)
	public int deleteFew(FewColumnsTran fewColumnsTran) {
		return fewColumnsTranMapper.deleteLast(fewColumnsTran);
	}

	public List<ManyColumnsTran> selectMany() {
		return manyColumnsTranMapper.select();
	}

	@Transactional(rollbackFor = Exception.class)
	public int insertMany(ManyColumnsTran manyColumnsTran) {
		return manyColumnsTranMapper.insert(manyColumnsTran);
	}

	@Transactional(rollbackFor = Exception.class)
	public int deleteMany(ManyColumnsTran manyColumnsTran) {
		return manyColumnsTranMapper.deleteLast(manyColumnsTran);
	}
}
