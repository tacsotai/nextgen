package com.example.nextgen.controller.api.v1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.nextgen.model.Seat;

/**
 * Search API Sample
 * 
 * @author maruyama-ta
 * @see https://spring.io/guides/tutorials/rest
 * 
 */
@RestController
public class SearchApi {

	private final Sample sample = new Sample();

	/**
	 * 取得します。
	 * 
	 * Sample URI:
	 * api/v1/search/seats/1
	 */
	@GetMapping("/api/v1/search/seats/{id}")
	public Seat seat(@PathVariable("id") Long id) {
		return sample.seat(id);
	}

	/**
	 * 検索します。
	 * 
	 * Sample URI:
	 * /api/v1/search/seats?event="a"&type="b"&position="c"
	 */
	@GetMapping("/api/v1/search/seats")
	public List<Seat> seats(@RequestParam(value = "stand", defaultValue = "normal") String stand,
			@RequestParam(value = "event") String event, @RequestParam(value = "type") String type,
			@RequestParam(value = "position") String position) {
		return sample.seats(stand, event, type, position);
	}

}

/**
 * This class is just a sample data.
 * 
 * @author maruyama-ta
 */
class Sample {

	public Seat seat(Long id) {
		Seat seat = new Seat();
		seat.setId(id);
		seat.setStand("stand_" + id);
		seat.setEvent("event_" + id);
		seat.setType("type_" + id);
		seat.setPosition("position_" + id);
		seat.setId(id);
		return seat;
	}


	public List<Seat> seats(String stand, String event, String type, String position) {
		// TODO filter by condition
		return seatObjects(3L);
	}

	public List<Seat> seatObjects(Long count) {
		List<Seat> list = new ArrayList<Seat>();
		for (long i = 0; i < count; i++) {
			list.add(seat(i + 1));
		}
		return list;
	}
}