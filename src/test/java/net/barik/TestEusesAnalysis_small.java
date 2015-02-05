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
	public void testCountingIntInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer count = counts.get(InputCellType.INTEGER);
		assertNotNull(count);
		assertEquals(36, count.intValue());
	}
	
	@Test
	public void testCountingDateInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer count = counts.get(InputCellType.DATE);
		assertNotNull(count);
		assertEquals(1, count.intValue());
	}
	
	@Test
	public void testCountingBooleanInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer count = counts.get(InputCellType.BOOLEAN);
		assertNotNull(count);
		assertEquals(2, count.intValue());
	}
	
	@Test
	public void testCountingStringInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer count = counts.get(InputCellType.STRING);
		assertNotNull(count);
		assertEquals(5, count.intValue());
	}
	
	@Test
	public void testCountingNonIntInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer count = counts.get(InputCellType.NON_INTEGER_NUMBER);
		assertNotNull(count);
		assertEquals(2, count.intValue());
	}
	
	@Test
	public void testCountingErrorInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer count = counts.get(InputCellType.ERROR);
		assertNotNull(count);
		assertEquals(1, count.intValue());
	}


	@Test
	public void testCountingSum() throws Exception {
		Map<String, Integer> counts = analyzer.getFunctionCounts();
		Integer count = counts.get("SUM");
		assertNotNull(count);
		assertEquals(13, count.intValue());
	}
	
	@Test
	public void testCountingPower() throws Exception {
		Map<String, Integer> counts = analyzer.getFunctionCounts();
		Integer count = counts.get("POWER");
		assertNotNull(count);
		assertEquals(130, count.intValue());
	}
	
	@Test
	public void testCountingCount() throws Exception {
		Map<String, Integer> counts = analyzer.getFunctionCounts();
		Integer count = counts.get("COUNT");
		assertNotNull(count);
		assertEquals(2, count.intValue());
	}
	
	@Test
	public void testCountingLog() throws Exception {
		Map<String, Integer> counts = analyzer.getFunctionCounts();
		Integer count = counts.get("LOG");
		assertNotNull(count);
		assertEquals(13, count.intValue());
	}
	
	@Test
	public void testCountingMax() throws Exception {
		Map<String, Integer> counts = analyzer.getFunctionCounts();
		Integer count = counts.get("MAX");
		assertNotNull(count);
		assertEquals(13, count.intValue());
	}
	
	@Test
	public void testReferencesBooleanInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputReferences();
		Integer count = counts.get(InputCellType.BOOLEAN);
		assertNull(count);
	}
	
	@Test
	public void testReferencesIntInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputReferences();
		Integer count = counts.get(InputCellType.INTEGER);
		assertNotNull(count);
		assertEquals(36, count.intValue());
	}
	
	@Test
	public void testReferencesNonIntInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputReferences();
		Integer count = counts.get(InputCellType.NON_INTEGER_NUMBER);
		assertNotNull(count);
		assertEquals(1, count.intValue());
	}
	
	@Test
	public void testReferencesStringInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputReferences();
		Integer count = counts.get(InputCellType.STRING);
		assertNotNull(count);
		assertEquals(2, count.intValue());
	}
	
	@Test
	public void testReferencesDateInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputReferences();
		Integer count = counts.get(InputCellType.DATE);
		assertNotNull(count);
		assertEquals(1, count.intValue());
	}
	
	@Test
	public void testReferencesErrorInput() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputReferences();
		Integer count = counts.get(InputCellType.ERROR);
		assertNull(count);
	}
	
	
	@Test
	public void testCountingIntFormula() throws Exception {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getFormulaCellCounts();
		Integer count = counts.get(InputCellType.INTEGER);
		assertNotNull(count);
		assertEquals(144, count.intValue());
	}

}
