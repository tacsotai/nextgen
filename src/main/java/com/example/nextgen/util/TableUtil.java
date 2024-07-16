package com.example.nextgen.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
public class TableUtil {

	/** テーブル論理名行 */
	private static final int LOGICAL_TEABLE_NAME_ROW = 1;

	/** テーブル論理名列 */
	private static final int LOGICAL_TEABLE_NAME_COLUMN = 2;

	/** テーブル物理名行 */
	private static final int PHYSICAL_TEABLE_NAME_ROW = 2;

	/** テーブル物理名列 */
	private static final int PHYSICAL_TEABLE_NAME_COLUMN = 2;

	/** ループ開始行 */
	private static final int LOOP_START_ROW = 5;

	/** 改行文字*/
	private static final String LINE_SEPR = "\n";

	/** ベースとなるパッケージ名 */
	private static final String BASE_PKG = "com.example.nextgen.";

	/** 項目マップ */
	private Map<Integer, String> itemMap;

	/** テーブルの論理名 */
	private Map<String, String> logicalTableNameMap;

	/** テーブルの物理名 */
	private Map<String, String> physicalTableNameMap;

	public TableUtil() {

		// メンバを初期化する
		logicalTableNameMap = new LinkedHashMap<String, String>();
		physicalTableNameMap = new LinkedHashMap<String, String>();

		// 項目マップを生成する
		itemMap = new LinkedHashMap<Integer, String>();
		itemMap.put(0, "No");
		itemMap.put(1, "項目名");
		itemMap.put(2, "カラム名");
		itemMap.put(3, "項目種別");
		itemMap.put(4, "データ型");
		itemMap.put(5, "サイズ");
		itemMap.put(6, "文字型形式");
		itemMap.put(7, "半角数字");
		itemMap.put(8, "半角英字");
		itemMap.put(9, "半角記号");
		itemMap.put(10, "半角カナ");
		itemMap.put(11, "全角");
		itemMap.put(12, "コード定義名");
		itemMap.put(13, "NULL許可");
		itemMap.put(14, "プライマリキー");
		itemMap.put(15, "セカンダリ１");
		itemMap.put(16, "セカンダリ２");
		itemMap.put(17, "セカンダリ３");
		itemMap.put(18, "セカンダリ４*");
		itemMap.put(19, "セカンダリ５");
		itemMap.put(20, "項目説明");
		itemMap.put(21, "備考");
		itemMap.put(22, "販売専用");
		itemMap.put(23, "媒体用");
		itemMap.put(24, "＠用");
	}

	@Data
	public static class ExcelContents {

		/** シート名(キー)と項目名と項目値(値)のマップ */
		private Map<String, Map<String, String>> sheetMap;
	}

	/**
	 * 読み込み条件に従った、EXCELファイルの読み込み
	 * 
	 * @param dirPath EXCELファイルがあるディレクトリのパス
	 * @throws Exception 例外
	 */
	public void createTableResourceByExcel(String dirPath, String outputPath) throws Exception {

		// ディレクトリ内の全てのファイルを処理するまでループ
		File dir = new File(dirPath);
		for (File fileOrDir : dir.listFiles()) {

			// ディレクトリ、EXCELファイル以外は処理しない
			if (fileOrDir.isDirectory()) {
				continue;
			}
			if (!(fileOrDir.getName().endsWith(".xls") || fileOrDir.getName().endsWith(".xlsx"))) {
				continue;
			}

			// エンティティ定義のEXCELファイルを読み込む
			var sheetMap = readExcel(fileOrDir.getAbsolutePath());

			// 読み込んだすべてのシートを処理するまでループ
			for (Map.Entry<String, List<Map<String, String>>> entry : sheetMap.entrySet()) {

				// DDLを生成する
				createDdl(logicalTableNameMap.get(entry.getKey()), physicalTableNameMap.get(entry.getKey()),
						entry.getValue(), outputPath);

				// エンティティクラスを生成する
				createEntity(logicalTableNameMap.get(entry.getKey()), physicalTableNameMap.get(entry.getKey()),
						entry.getValue(), outputPath);

				// マッパークラスを生成する
				createMapper(logicalTableNameMap.get(entry.getKey()), physicalTableNameMap.get(entry.getKey()),
						entry.getValue(), outputPath);
			}
		}
	}

	private Map<String, List<Map<String, String>>> readExcel(String filePath) throws Exception {

		// EXCELファイルを開く
		Map<String, List<Map<String, String>>> sheetMap = new LinkedHashMap<String, List<Map<String, String>>>();
		try (Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath))) {

			// シート名を取得する
			List<String> sheetNameList = getSheetNameList(workbook);
			sheetNameList.stream().forEach(sheetName -> {

				// シートからテーブル定義情報を読み込む
				Sheet sheet = workbook.getSheet(sheetName);
				readTableName(sheet);
				sheetMap.put(sheetName, readSheet(sheet, LOOP_START_ROW, itemMap));
			});
		}

		// 生成したシートの情報を呼び出し側に返却する
		return sheetMap;
	}

	private List<String> getSheetNameList(Workbook workbook) throws Exception {

		// ワークシートの名前をリストにして呼び出し側に返却する
		return IntStream.range(0, workbook.getNumberOfSheets())
				.mapToObj(workbook::getSheetAt)
				.map(Sheet::getSheetName)
				.collect(Collectors.toList());
	}

	private void readTableName(Sheet sheet) {

		// テーブル物理名と論理名をマップに追加する
		logicalTableNameMap.put(sheet.getSheetName(), getCellAsString(
				sheet.getRow(LOGICAL_TEABLE_NAME_ROW).getCell(LOGICAL_TEABLE_NAME_COLUMN)));
		physicalTableNameMap.put(sheet.getSheetName(), getCellAsString(
				sheet.getRow(PHYSICAL_TEABLE_NAME_ROW).getCell(PHYSICAL_TEABLE_NAME_COLUMN)));
	}

	private List<Map<String, String>> readSheet(Sheet sheet, int startRow, Map<Integer, String> itemMap) {

		// 開始行から処理を開始する(最大1000行、空行を検知したらループを終了する)
		List<Map<String, String>> sheetItemMapList = new ArrayList<Map<String, String>>();
		for (int rowNum = startRow; rowNum < 1000; rowNum++) {

			// 空行ならば処理を終了する
			Row row = sheet.getRow(rowNum);
			if (row == null || isEof(row, itemMap)) {
				break;
			}

			// 項目マップに従い、各列の値をマップに追加する
			Map<String, String> sheetItemMap = new LinkedHashMap<String, String>();
			for (Map.Entry<Integer, String> entry : itemMap.entrySet()) {
				sheetItemMap.put(entry.getValue(), getCellAsString(row.getCell(entry.getKey())));
			}

			// 項目マップのリストに項目マップを追加する
			sheetItemMapList.add(sheetItemMap);
		}

		// 作成した項目マップを呼び出し側に返却する
		return sheetItemMapList;
	}

	private boolean isEof(Row row, Map<Integer, String> itemMap) {

		// A列が空ならばEOFと判断する
		Cell cell = row.getCell(0);
		if (cell == null) {
			return true;
		}
		if (CellType.BLANK.equals(cell.getCellType())) {
			return true;
		}

		// 処理がここまで到達した場合はEOFでないとみなし、戻り値falseで呼び出し側に復帰する
		return false;
	}

	private String getCellAsString(Cell cell) {

		if (cell == null) {
			return "NULL";
		}
		switch (cell.getCellType()) {
		case BLANK:
			return "BLANK";
		case BOOLEAN:
			return cell.getBooleanCellValue() ? "true" : "false";
		case ERROR:
			return "ERROR";
		case FORMULA:
			return "FORMULA";
		case NUMERIC:
			String cellValue = Double.valueOf(cell.getNumericCellValue()).toString();
			cellValue = cellValue.endsWith(".0") ? cellValue.substring(0, cellValue.lastIndexOf(".0")) : cellValue;
			return cellValue;
		case STRING:
			return cell.getStringCellValue();
		case _NONE:
			return "_NONE";
		default:
			return "DEFAULT";
		}
	}

	private void createDdl(String logicalTableName, String physicalTableName,
			List<Map<String, String>> sheetItemList, String outputPath) throws Exception {

		// 論理名が読めなかったシートは処理を行わない
		if ("NULL".equals(logicalTableName)) {
			return;
		}

		// 出力ファイルを生成する
		String ddlFilePath = outputPath + "/ddl/" + logicalTableName + ".txt";
		try (BufferedWriter ddlFile = new BufferedWriter(new FileWriter(ddlFilePath))) {

			// DDL文字列を作成する
			StringBuilder contents = new StringBuilder();
			contents.append("-- DROP TABLE " + physicalTableName + ";" + LINE_SEPR);
			contents.append("CREATE TABLE " + physicalTableName + "(" + LINE_SEPR);

			// DB定義を全て処理するまでループ
			StringBuilder part = new StringBuilder();
			for (Map<String, String> sheetItemMap : sheetItemList) {

				// カラム名とデータ型を記述する
				part.append("\t" + sheetItemMap.get("カラム名") + " " + sheetItemMap.get("データ型"));
				if ("CHAR".equals(sheetItemMap.get("データ型")) || "NVARCHAR2".equals(sheetItemMap.get("データ型"))
						|| "VARCHAR2".equals(sheetItemMap.get("データ型"))) {
					part.append("(" + sheetItemMap.get("サイズ") + ")");
				}

				// NOT NULL制約があるカラムはキーワードを追加する
				if ("Not NULL".equals(sheetItemMap.get("NULL許可"))) {
					part.append(" NOT NULL");
				}

				// カンマを打って改行する
				part.append("," + LINE_SEPR);
			}

			// 生成した部分文字列を追加する
			contents.append(part);

			// 主キーの記述を追加する
			part = new StringBuilder();
			for (Map<String, String> sheetItemMap : sheetItemList) {
				if (!"BLANK".equals(sheetItemMap.get("プライマリキー"))) {
					if (part.length() == 0) {
						part.append("\tPRIMARY KEY(" + sheetItemMap.get("カラム名"));
					} else {
						part.append(", " + sheetItemMap.get("カラム名"));
					}
				}
			}
			part.append(")" + LINE_SEPR);
			contents.append(part);

			// CREATE TABLEの末尾の閉じかっこを追加
			contents.append(");" + LINE_SEPR);

			// DDLをファイルに書き込む
			ddlFile.write(contents.toString());
		}
	}

	private void createEntity(String logicalTableName, String physicalTableName,
			List<Map<String, String>> sheetItemList, String outputPath) throws Exception {

		// 論理名が読めなかったシートは処理を行わない
		if ("NULL".equals(logicalTableName)) {
			return;
		}

		// 出力ファイルを生成する
		String ddlFilePath = outputPath + "/entity/" + toCamel(physicalTableName) + ".java";
		try (BufferedWriter ddlFile = new BufferedWriter(new FileWriter(ddlFilePath))) {

			// エンティティの文字列を作成する
			StringBuilder contents = new StringBuilder();
			contents.append("package " + BASE_PKG + "entity;" + LINE_SEPR);
			contents.append(LINE_SEPR + "import java.sql.Date;" + LINE_SEPR);
			contents.append(LINE_SEPR + "import lombok.Data;" + LINE_SEPR);
			contents.append(LINE_SEPR + "/**" + LINE_SEPR);
			contents.append(" * " + logicalTableName + "のエンティティ" + LINE_SEPR);
			contents.append(" */ " + LINE_SEPR);
			contents.append("@Data" + LINE_SEPR);
			contents.append("public class " + toCamel(physicalTableName) + " {" + LINE_SEPR);

			// DB定義を全て処理するまでループ
			for (Map<String, String> sheetItemMap : sheetItemList) {
				contents.append("\t/** " + sheetItemMap.get("項目名") + " */" + LINE_SEPR);
				contents.append("\tprivate " + toJavaType(sheetItemMap.get("データ型")) + " "
						+ toLowerCamel(sheetItemMap.get("カラム名")) + ";" + LINE_SEPR);
			}

			// クラス定義末尾の閉じかっこを追加
			contents.append("}" + LINE_SEPR);

			// DDLをファイルに書き込む
			ddlFile.write(contents.toString());
		}
	}

	private void createMapper(String logicalTableName, String physicalTableName,
			List<Map<String, String>> sheetItemList, String outputPath) throws Exception {

		// 論理名が読めなかったシートは処理を行わない
		if ("NULL".equals(logicalTableName)) {
			return;
		}

		// 出力ファイルを生成する
		String ddlFilePath = outputPath + "/mapper/" + toCamel(physicalTableName) + "Mapper.java";
		try (BufferedWriter ddlFile = new BufferedWriter(new FileWriter(ddlFilePath))) {

			// マッパーの文字列を作成する
			StringBuilder contents = new StringBuilder();
			contents.append("package " + BASE_PKG + "mapper;" + LINE_SEPR);
			contents.append(LINE_SEPR + "import java.util.List;" + LINE_SEPR);
			contents.append(LINE_SEPR + "import org.apache.ibatis.annotations.Insert;" + LINE_SEPR);
			contents.append("import org.apache.ibatis.annotations.Mapper;" + LINE_SEPR);
			contents.append("import org.apache.ibatis.annotations.Select;" + LINE_SEPR);
			contents.append(LINE_SEPR + "import " + BASE_PKG + "entity."
					+ toCamel(physicalTableName) + ";" + LINE_SEPR);
			contents.append(LINE_SEPR + "/**" + LINE_SEPR);
			contents.append(" * " + logicalTableName + "のマッパー" + LINE_SEPR);
			contents.append(" */ " + LINE_SEPR);
			contents.append("@Mapper" + LINE_SEPR);
			contents.append("public interface " + toCamel(physicalTableName) + "Mapper {" + LINE_SEPR);

			// findAllの文字列を生成し、追加する
			contents.append(createFindAllString(physicalTableName, sheetItemList));

			// insertの文字列を生成し、追加する
			contents.append(createInsertString(physicalTableName, sheetItemList));

			// インターフェース定義末尾の閉じかっこを追加
			contents.append("}" + LINE_SEPR);

			// DDLをファイルに書き込む
			ddlFile.write(contents.toString());
		}
	}

	private String createFindAllString(String physicalTableName, List<Map<String, String>> sheetItemList) {

		// Selectの文字列を生成する
		StringBuilder contents = new StringBuilder();
		for (Map<String, String> sheetItemMap : sheetItemList) {
			if (contents.length() == 0) {
				contents.append(LINE_SEPR + "\t@Select(\"\"\"" + LINE_SEPR);
				contents.append("\t\t\tSELECT" + LINE_SEPR);
				contents.append("\t\t\t\t" + sheetItemMap.get("カラム名") + LINE_SEPR);
			} else {
				contents.append("\t\t\t\t, " + sheetItemMap.get("カラム名") + LINE_SEPR);
			}
		}
		contents.append("\t\t\tFROM " + physicalTableName + LINE_SEPR);
		contents.append("\t\t\t\"\"\")" + LINE_SEPR);
		contents.append("\tList<" + toCamel(physicalTableName) + "> findAll();" + LINE_SEPR);

		// 生成した文字列を呼び出し側に返却する
		return contents.toString();
	}

	private String createInsertString(String physicalTableName, List<Map<String, String>> sheetItemList) {

		// Selectの文字列を生成する
		StringBuilder contents = new StringBuilder();
		for (Map<String, String> sheetItemMap : sheetItemList) {
			if (contents.length() == 0) {
				contents.append(LINE_SEPR + "\t@Insert(\"\"\"" + LINE_SEPR);
				contents.append("\t\t\tINSERT INTO " + physicalTableName + "(" + LINE_SEPR);
				contents.append("\t\t\t\t" + sheetItemMap.get("カラム名") + LINE_SEPR);
			} else {
				contents.append("\t\t\t\t, " + sheetItemMap.get("カラム名") + LINE_SEPR);
			}
		}
		contents.append("\t\t\t) VALUES(" + LINE_SEPR);
		StringBuilder part = new StringBuilder();
		for (Map<String, String> sheetItemMap : sheetItemList) {
			if (part.length() == 0) {
				part.append("\t\t\t\t#{" + toLowerCamel(sheetItemMap.get("カラム名")) + "}" + LINE_SEPR);
			} else {
				part.append("\t\t\t\t, #{" + toLowerCamel(sheetItemMap.get("カラム名")) + "}" + LINE_SEPR);
			}
		}
		part.append("\t\t\t)" + LINE_SEPR);
		part.append("\t\t\t\"\"\")" + LINE_SEPR);
		part.append("\tint insert(" + toCamel(physicalTableName) + " "
				+ toLowerCamel(physicalTableName) + ");" + LINE_SEPR);
		contents.append(part);

		// 生成した文字列を呼び出し側に返却する
		return contents.toString();
	}

	private String toCamel(String target) {

		// 処理効率のため、一度すべて小文字に変換する
		target = target.toLowerCase();

		// 処理対象文字列を1文字ずつ処理していく
		StringBuilder after = new StringBuilder();
		for (int index = 0; index < target.length(); index++) {

			if (index == 0) {

				// 最初の1文字は大文字とする
				after.append(String.valueOf(target.charAt(index)).toUpperCase());

			} else if ("_".equals(String.valueOf(target.charAt(index)))) {

				// アンダーバーを検知した場合は、次の文字を大文字とする
				index++;
				after.append(String.valueOf(target.charAt(index)).toUpperCase());

			} else {

				// それ以外の場合は小文字とする
				after.append(String.valueOf(target.charAt(index)));
			}
		}

		// 変換後の文字列を呼び出し側に返却する
		return after.toString();
	}

	private String toLowerCamel(String target) {

		// キャメルケースの最初の1文字だけを小文字に変換した文字列を作成し、呼び出し側に返却する
		String camel = toCamel(target);
		return String.valueOf(camel.charAt(0)).toLowerCase() + camel.substring(1, camel.length());
	}

	private String toJavaType(String type) {

		// エンティティ定義書上のデータ型をJavaの型にマッピングする
		if ("CHAR".equals(type) || "VARCHAR2".equals(type) || "NVARCHAR2".equals(type)) {
			return "String";
		} else if ("DATE".equals(type)) {
			return "Date";
		} else if ("NUMBER".equals(type)) {
			return "Long";
		} else {
			return "Object";
		}
	}
}
