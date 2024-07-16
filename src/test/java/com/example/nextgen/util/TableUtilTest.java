package com.example.nextgen.util;

import org.junit.jupiter.api.Test;

public class TableUtilTest {

	private static final String RESOURCE_PATH = "src/test/resources/com/example/nextgen/";
	private static final String TEST_PATH = RESOURCE_PATH + "util/PoiUtilTest/input/";
	private static final String OUTPUT_PATH = RESOURCE_PATH + "util/PoiUtilTest/output/";

	@Test
	void test01() throws Exception {
		String dirPath = TEST_PATH;
		new TableUtil().createTableResourceByExcel(dirPath, OUTPUT_PATH);
	}
}
