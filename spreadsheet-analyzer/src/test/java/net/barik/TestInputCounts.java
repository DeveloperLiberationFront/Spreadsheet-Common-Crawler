package net.barik;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Map;

import net.barik.SpreadsheetAnalyzer.FunctionEvalType;
import net.barik.SpreadsheetAnalyzer.InputCellType;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestInputCounts {
	
	private static SpreadsheetAnalyzer analyzer;

	@BeforeClass
	public static void loadSmallWorksheet() throws Exception{
		InputStream is = TestInputCounts.class.getResourceAsStream("/non-integer.xlsm");
		assertNotNull(is);
		analyzer = SpreadsheetAnalyzer.doEUSESAnalysis(is);
		assertNotNull(analyzer);
		
	}

	
	@Test
	public void testCountingIntInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer count = counts.get(InputCellType.INTEGER);
		assertNull(count);
	}
	
	@Test
	public void testCountingDateInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer count = counts.get(InputCellType.DATE);
		assertNull(count);
	}
	
	@Test
	public void testCountingBooleanInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer count = counts.get(InputCellType.BOOLEAN);
		assertNull(count);
	}
	
	@Test
	public void testCountingStringInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer count = counts.get(InputCellType.STRING);
		assertNotNull(count);
	}
	
	@Test
	public void testCountingNonIntInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer count = counts.get(InputCellType.NON_INTEGER_NUMBER);
		assertNotNull(count);
		assertEquals(1, count.intValue());
	}
	
	@Test
	public void testCountingErrorInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer count = counts.get(InputCellType.ERROR);
		assertNull(count);
	}

	
	@Test
	public void testNoChart() throws Exception {
		assertFalse(analyzer.containsChart());
	}
		
}
