package com.yskj.push.framework.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** 
* @ClassName: ReadExcel 
* @Description: TODO
* @author yujian.yang   
* @date 2016年4月14日 上午9:40:50 
*  
*/

public class ReadExcel {

	/**
	 * 获取外部excel流 转换成 list
	 * @Title: readXlsByStream 
	 * @Description: 
	 * @param is
	 * @param filePath 文件名
	 * @return
	 */
	public static <T> T readXlsByStream(InputStream is, String originalFilename) {
		if (is != null) {
			try {
				Boolean isExcel2003 = isExcel2003(originalFilename);
				Workbook workBook = getWorkBook(is, isExcel2003);
				List<Object> sheetList = getXlsList(workBook);
				return (T) sheetList;
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		return null;
	}

	/**
	 * 获得本地excel 转换 成 list
	 * @Title: readXls 
	 * @Description: 
	 * @param filePath 本地文件路径
	 * @return
	 */
	public static <T> T readXls(String filePath) {
		InputStream is = null;
		try {
			is = importFile(filePath);
			Boolean isExcel2003 = isExcel2003(filePath);
			Workbook workBook = getWorkBook(is, isExcel2003);
			List<Object> sheetList = getXlsList(workBook);
			return (T) sheetList;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将excel转换成List
	 * @Title: getXlsList 
	 * @Description: 
	 * @param workBook
	 * @return
	 */
	private static List<Object> getXlsList(Workbook workBook) {
		List<Object> sheetList = new LinkedList<Object>();
		for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
			Sheet sheet = workBook.getSheetAt(numSheet);
			ArrayList<String[]> xlsLsit = new ArrayList<String[]>();
			if (sheet == null) {
				continue;
			}


			for (int numRow = 0; numRow <= sheet.getLastRowNum(); numRow++) {
				Row row = sheet.getRow(numRow);
				if (row != null) {
					Boolean tag = false;
					for (int i = 0; i < row.getLastCellNum(); i++) {
						if (row.getCell(i) != null && !String.valueOf(getStringVal(row.getCell(i))).trim().isEmpty()) {
							tag = true;
						}
					}
					if (tag) {
						int cells = row.getLastCellNum();
						for (int i = row.getLastCellNum() - 1; i < row.getLastCellNum() && i >= 0; i--) {
							if (row.getCell(i) == null || String.valueOf(getStringVal(row.getCell(i))).trim().isEmpty()) {
								cells--;
							} else {
								break;
							}
							if (i > 0) {
								if (row.getCell(i - 1) != null
										&& !String.valueOf(getStringVal(row.getCell(i - 1))).trim().isEmpty()) {
									break;
								}
							}
						}
						String[] rowData = new String[cells];
						for (int cellNum = 0; cellNum < cells; cellNum++) {
							if (row.getCell(cellNum) != null) {
								String data = getStringVal(row.getCell(cellNum));
								rowData[cellNum] = data;
							}
						}
						xlsLsit.add(rowData);
					}
				}
			}
			sheetList.add(xlsLsit);
		}
		return sheetList;
	}

	public static String getStringVal(Cell cell){
		switch(cell.getCellType()){
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue()?"TRUE":"FALSE";
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_NUMERIC:
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		default:
			return "EMPTY";
		}
	}
	
	
	/**
	 * 获取 Workbook
	 * @Title: getWorkBook 
	 * @Description: 
	 * @param inputStream
	 * @param isExcel2003
	 * @return
	 * @throws IOException
	 */
	private static Workbook getWorkBook(InputStream inputStream, Boolean isExcel2003) throws IOException {
		Workbook wb = null;
		if (isExcel2003) {
			wb = new HSSFWorkbook(inputStream);
		} else {
			wb = new XSSFWorkbook(inputStream);
		}
		return wb;
	}

	/**
	 * 导入文件获取文件流
	 * @Title: importFile 
	 * @Description: 
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException 
	 */
	private static InputStream importFile(String filePath) throws FileNotFoundException {
		InputStream is = null;
		File file = null;
		if (validateExcel(filePath)) {
			file = new File(filePath);
			is = new FileInputStream(file);
		}
		return is;
	}

	/**
	 * 验证文件是否合法
	 * @Title: validateExcel 
	 * @Description: 
	 * @param filePath
	 * @return
	 */
	private static boolean validateExcel(String filePath) {
		if (null == filePath || !(isExcel2003(filePath) || isExcel2007(filePath))) {
			System.out.println("文件名不是excel格式");
			return false;
		}

		File file = new File(filePath);
		if (null == file || !file.exists()) {
			System.out.println("文件不存在");
			return false;
		}

		return true;
	}

	/**
	 * 验证是否是 Excel2003
	 * @Title: isExcel2003 
	 * @Description: 
	 * @param filePath
	 * @return
	 */
	private static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	/**
	 * 验证是否是 Excel2007以上版本
	 * @Title: isExcel2007 
	 * @Description: 
	 * @param filePath
	 * @return
	 */
	private static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}
}
