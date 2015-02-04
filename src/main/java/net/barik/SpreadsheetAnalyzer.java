package net.barik;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import net.barik.SpreadsheetAnalyzer.InputCellType;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class SpreadsheetAnalyzer {
	
	public enum InputCellType {
		INTEGER,BOOLEAN,DATE,ERROR,NON_INTEGER_NUMBER,STRING
	}

	private Workbook workbook;

	private SpreadsheetAnalyzer(Workbook wb) {
		this.workbook = wb;
	}

	public static SpreadsheetAnalyzer parse(InputStream is) throws InvalidFormatException, IOException {
		SpreadsheetAnalyzer analyzer = new SpreadsheetAnalyzer(WorkbookFactory.create(is));
		
		analyzer.analyze();

		return analyzer;
	}


	private void analyze() {
		// TODO Auto-generated method stub
		
	}

	public Map<String, Integer> getFunctionCounts() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<InputCellType, Integer> getInputCellCounts() {
		// TODO Auto-generated method stub
		return null;
	}

}
