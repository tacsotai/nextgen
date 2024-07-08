package com.example.nextgen.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nextgen.entity.Seat;
import com.example.nextgen.mapper.SeatMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {

	private final SeatMapper fewColumnsTranMapper;

	public List<Seat> getSeatList() {
		return fewColumnsTranMapper.select();
	}

	@Transactional(rollbackFor = Exception.class)
	public int insertSeat(Seat fewColumnsTran) {
		return fewColumnsTranMapper.insert(fewColumnsTran);
	}

	@Transactional(rollbackFor = Exception.class)
	public int deleteSeat(Seat fewColumnsTran) {
		return fewColumnsTranMapper.deleteLast(fewColumnsTran);
	}
}
