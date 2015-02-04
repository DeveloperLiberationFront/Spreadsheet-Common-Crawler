package net.barik;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Map;

import net.barik.SpreadsheetAnalyzer.InputCellType;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestEusesAnalysis_small {
	
	private static SpreadsheetAnalyzer analyzer;

	@BeforeClass
	public static void loadSmallWorksheet() throws Exception{
		InputStream is = TestEusesAnalysis_small.class.getResourceAsStream("/small-worksheet.xlsx");
		assertNotNull(is);
		analyzer = SpreadsheetAnalyzer.doEUSESAnalysis(is);
		assertNotNull(analyzer);
		
	}

	
	@Test
	public void testCountingSum() throws Exception {
		Map<String, Integer> counts = analyzer.getFunctionCounts();
		Integer sumCounts = counts.get("SUM");
		assertNotNull(sumCounts);
		assertEquals(13, sumCounts.intValue());
	}
	
	@Test
	public void testCountingIntInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer sumCounts = counts.get(InputCellType.INTEGER);
		assertNotNull(sumCounts);
		assertEquals(36, sumCounts.intValue());
	}
	
	@Test
	public void testCountingDateInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer sumCounts = counts.get(InputCellType.DATE);
		assertNotNull(sumCounts);
		assertEquals(1, sumCounts.intValue());
	}
	
	@Test
	public void testCountingBooleanInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer sumCounts = counts.get(InputCellType.BOOLEAN);
		assertNotNull(sumCounts);
		assertEquals(2, sumCounts.intValue());
	}
	
	@Test
	public void testCountingStringInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer sumCounts = counts.get(InputCellType.STRING);
		assertNotNull(sumCounts);
		assertEquals(5, sumCounts.intValue());
	}
	
	@Test
	public void testCountingNonIntInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer sumCounts = counts.get(InputCellType.NON_INTEGER_NUMBER);
		assertNotNull(sumCounts);
		assertEquals(1, sumCounts.intValue());
	}
	
	@Test
	public void testCountingErrorInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer sumCounts = counts.get(InputCellType.ERROR);
		assertNotNull(sumCounts);
		assertEquals(1, sumCounts.intValue());
	}

}
