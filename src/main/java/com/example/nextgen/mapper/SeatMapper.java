package com.example.nextgen.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.nextgen.entity.Seat;

@Mapper
public interface SeatMapper {

	@Select("""
			SELECT
				ID, STAND, EVENT, TYPE, POSITION, PLACE,
				IS_DELETED, CREATED_BY, CREATED_AT, UPDATED_BY, UPDATED_AT
			FROM SEAT
			""")
	List<Seat> select();

	@Insert("""
			INSERT INTO SEAT(
				STAND, EVENT, TYPE, POSITION, PLACE
			) VALUES(
				#{stand}, #{event}, #{type}, #{position}, #{place}
			)
			""")
	int insert(Seat seat);

	@Delete("""
			DELETE FROM SEAT
			WHERE ID = #{id}
			""")
	int deleteLast(Seat seat);
}
