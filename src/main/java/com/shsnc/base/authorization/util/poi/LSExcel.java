package com.shsnc.base.authorization.util.poi;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LSExcel {
	
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
	
	/**
	 * 创建一个excel文件
	 * 
	 * @return
	 */
	public static Workbook getWorkBook() {
		Workbook wb = null;
		try {
			wb = new XSSFWorkbook();
		} catch (Exception e) {
			wb = new HSSFWorkbook();// 兼容Excel2003
		}
		return wb;
	}
	
	
	/**
	 * 创建一个excel文件
	 * 
	 * @return
	 */
	public static Sheet getSheet(File file) {
		Sheet sheet=null;
		try {
			Workbook wb =WorkbookFactory.create(file);
			if(wb!=null){
				sheet=wb.getSheetAt(0);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return sheet;
	}
	
	
	/**
	 * 创建一个excel文件
	 * 
	 * @return
	 */
	public static Sheet getSheet(InputStream inputStream,int index) {
		Sheet sheet=null;
		try {
			Workbook wb =WorkbookFactory.create(inputStream);
			if(wb!=null){
				sheet=wb.getSheetAt(index);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return sheet;
	}
	
	
	/**
	 * 创建一个excel文件
	 * 
	 * @return
	 */
	public static Sheet getSheet(File file,int index) {
		Sheet sheet=null;
		try {
			Workbook wb =WorkbookFactory.create(file);
			if(wb!=null){
				sheet=wb.getSheetAt(index);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return sheet;
	}
	
	
	/**
	 * 创建一个excel文件
	 * 
	 * @return
	 */
	public static Sheet getSheet(InputStream inputStream) {
		Sheet sheet=null;
		try {
			Workbook wb =WorkbookFactory.create(inputStream);
			if(wb!=null){
				sheet=wb.getSheetAt(0);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return sheet;
	}
	
	
	/**
	 * 创建一个excel文件
	 * 
	 * @return
	 */
	public static List<String> getTitles(Sheet sheet) {
		List<String> titles = new ArrayList<String>();
		Row row0 = sheet.getRow(0);
		if (row0 != null) {			
			for (int i =0; i < row0.getLastCellNum(); i++) {
				titles.add(row0.getCell(i).getRichStringCellValue().getString());
			}
		}
		return titles;
	}
	
	
	/**
	 * 获得行数据
	 * @param titles
	 * @param row
	 * @return
	 */
	public static  Record getCellValues(List<String> titles, Row row) {
		Record record=new Record();
		if(!CollectionUtils.isEmpty(titles)){
			for(int i=0;i<titles.size();i++){
				//key  属性名  value  单元格值
				record.put(titles.get(i), getStringValueByCell(row.getCell(i+1)));
			}
		}
		return record;
	}
	
	
	/**
	 * 获取单元格内容
	 * @param cell 单元格
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getStringValueByCell(Cell cell) {
		if (cell != null) {
			//判断单元格格式
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_ERROR:
				return null;
			case Cell.CELL_TYPE_BLANK:
				return null;
			case Cell.CELL_TYPE_FORMULA:
				try {  
					return String.valueOf(cell.getStringCellValue());  
				} catch (IllegalStateException e) {  
     				return String.valueOf(cell.getNumericCellValue());  
				}  
			case Cell.CELL_TYPE_BOOLEAN://boolean类型
				boolean b = cell.getBooleanCellValue();
				return Boolean.valueOf(b).toString();
			case Cell.CELL_TYPE_NUMERIC://numeric类型	
				if(DateUtil.isCellDateFormatted(cell)){
					Date date = cell.getDateCellValue();
					return df.format(date);
				} else {		
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					double value=cell.getNumericCellValue();
					BigDecimal bigDecimal=new BigDecimal(value);
					if(String.valueOf(bigDecimal).lastIndexOf(".")!=-1){
						Double cellValue = new BigDecimal(value).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
						return String.valueOf(cellValue);	
					}else{
						return bigDecimal.toString();
					}
					
				}
			case Cell.CELL_TYPE_STRING://string类型
				return cell.getRichStringCellValue().getString();
			}
		}
		return null;
	}
	
	
	/**
	 * 头部格式
	 * 
	 * @param workbook
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static CellStyle getTitleStyle(Workbook workbook) {
	
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中

		Font font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		//font.setUnderline((byte) 1);
		style.setFont(font);
		return style;

	}
	
	
	/**
	 * 部格式
	 * 
	 * @param workbook
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static CellStyle setHeadStyle(Workbook workbook) {

		CellStyle style =getBorderStyle(workbook);
		Font font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);  //设置字体颜色  黑色
		font.setFontHeightInPoints((short) 13);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		return style;

	}
	
	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static CellStyle getBorderStyle(Workbook workbook) {
	
		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
		
	
		return style;

	}

}
