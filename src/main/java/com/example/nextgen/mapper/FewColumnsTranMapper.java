package com.example.nextgen.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.nextgen.entity.FewColumnsTran;

@Mapper
public interface FewColumnsTranMapper {

	@Select("""
			SELECT
				ID, CHAR_COLUMN_001, CHAR_COLUMN_002, CHAR_COLUMN_003, CHAR_COLUMN_004, CHAR_COLUMN_005,
				IS_DELETED, CREATED_BY, CREATED_AT, UPDATED_BY, UPDATED_AT
			FROM FEW_COLUMNS_TRAN
			""")
	List<FewColumnsTran> select();

	@Insert("""
			INSERT INTO FEW_COLUMNS_TRAN(
				CHAR_COLUMN_001, CHAR_COLUMN_002, CHAR_COLUMN_003, CHAR_COLUMN_004, CHAR_COLUMN_005
			) VALUES(
				#{charColumn001}, #{charColumn002}, #{charColumn003}, #{charColumn004}, #{charColumn005}
			)
			""")
	int insert(FewColumnsTran fewColumnsTran);

	@Delete("""
			DELETE FROM FEW_COLUMNS_TRAN
			WHERE ID = #{id}
			""")
	int deleteLast(FewColumnsTran fewColumnsTran);
}
