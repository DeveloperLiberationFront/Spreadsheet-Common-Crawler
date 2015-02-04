package net.barik;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class SpreadsheetAnalyzer {
	
	private Workbook workbook;
	private Map<InputCellType, Integer> inputCellCounts = new EnumMap<>(InputCellType.class);
	private Map<String, Integer> functionCounts = new HashMap<>();

	private SpreadsheetAnalyzer(Workbook wb) {
		this.workbook = wb;
	}

	public static SpreadsheetAnalyzer doEUSESAnalysis(InputStream is) throws InvalidFormatException, IOException {
		SpreadsheetAnalyzer analyzer = new SpreadsheetAnalyzer(WorkbookFactory.create(is));
		
		analyzer.analyzeEUSESMetrics();

		return analyzer;
	}
	
	private static Integer incrementOrInitialize(Integer i) {
		if (i == null) {
			return 1;
		}
		return i + 1;
	}


	private void analyzeEUSESMetrics() {
		clearPreviousMetrics();
		
		for(int i = 0; i< workbook.getNumberOfSheets();i++) {
			Sheet s = workbook.getSheetAt(i);
			Iterator <Row> rowIterator = s.iterator();
			while(rowIterator.hasNext()){
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch(cell.getCellType()) {
                        case Cell.CELL_TYPE_BOOLEAN:
                        case Cell.CELL_TYPE_STRING:
                        case Cell.CELL_TYPE_ERROR:
                        case Cell.CELL_TYPE_NUMERIC:
                        	handleInputCell(cell);
                            break;
                        
                        case Cell.CELL_TYPE_FORMULA:
                        	System.out.print(cell.getCellFormula());
                        	break;
                        
                        }
                    }
                }
				System.out.println();
			}
		}
		
	private void handleInputCell(Cell cell) {
		//TODO dates
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			double d = cell.getNumericCellValue();
	    	if (Math.rint(d) == d) {  //integer check from http://stackoverflow.com/a/9898613/1447621
	    		inputCellCounts.put(InputCellType.INTEGER, 
	            		incrementOrInitialize(inputCellCounts.get(InputCellType.INTEGER)));
	    	} else {
	    		inputCellCounts.put(InputCellType.NON_INTEGER_NUMBER, 
	            		incrementOrInitialize(inputCellCounts.get(InputCellType.NON_INTEGER_NUMBER)));
	    	}
		} else {
			InputCellType type = InputCellType.fromCellType(cell.getCellType());
			inputCellCounts.put(type, incrementOrInitialize(inputCellCounts.get(type)));
		}
	}

	private void clearPreviousMetrics() {
		functionCounts.clear();
		inputCellCounts.clear();
	}

	public Map<String, Integer> getFunctionCounts() {
		return functionCounts;
	}

	public Map<InputCellType, Integer> getInputCellCounts() {
		return inputCellCounts;
	}

	public enum InputCellType {
		INTEGER,BOOLEAN,DATE,ERROR,NON_INTEGER_NUMBER,STRING;

		public static InputCellType fromCellType(int cellType) {
			switch (cellType) {
			case Cell.CELL_TYPE_BOOLEAN:
				return BOOLEAN;
			case Cell.CELL_TYPE_ERROR:
				return ERROR;
			case Cell.CELL_TYPE_STRING:
				return STRING;
			}
			return null;
		}
	}

}
