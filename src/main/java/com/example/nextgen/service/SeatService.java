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

	private final SeatMapper seatMapper;

	public List<Seat> getSeatList() {
		return seatMapper.select();
	}

	@Transactional(rollbackFor = Exception.class)
	public int insertSeat(Seat seat) {
		return seatMapper.insert(seat);
	}

	@Transactional(rollbackFor = Exception.class)
	public int deleteSeat(Seat seat) {
		return seatMapper.deleteLast(seat);
	}
}
