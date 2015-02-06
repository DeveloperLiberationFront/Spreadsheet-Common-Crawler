package net.barik;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Map;

import net.barik.SpreadsheetAnalyzer.FunctionEvalType;
import net.barik.SpreadsheetAnalyzer.InputCellType;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestEusesAnalysis_r1c1 {

	private static SpreadsheetAnalyzer analyzer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStream is = TestEusesAnalysis_small.class.getResourceAsStream("/r1c1.xlsx");
		assertNotNull(is);
		analyzer = SpreadsheetAnalyzer.doEUSESAnalysis(is);
		assertNotNull(analyzer);
	}

	@Test
	public void testGetFunctionCounts() {
		Map<String, Integer> counts = analyzer.getFunctionCounts();
		Integer count = counts.get("SUM");
		assertNotNull(count);
		assertEquals(1, count.intValue());
		
		count = counts.get("AVERAGE");
		assertNotNull(count);
		assertEquals(1, count.intValue());
		
		assertTrue(counts.keySet().size() == 2);//sum and average is the only input
	}

	@Test
	public void testGetInputCellCounts() {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer count = counts.get(InputCellType.INTEGER);
		assertNotNull(count);
		assertEquals(4, count.intValue());

		assertTrue(counts.keySet().size() == 1);//only integer inputs
	}

	@Test
	public void testGetFormulaCellCounts() {
		Map<SpreadsheetAnalyzer.FunctionEvalType, Integer> counts = analyzer.getFormulaCellCounts();
		Integer count = counts.get(FunctionEvalType.INTEGER);
		assertNotNull(count);
		assertEquals(6,  count.intValue());
		
		count = counts.get(FunctionEvalType.NON_INTEGER_NUMBER);
		assertNotNull(count);
		assertEquals(1,  count.intValue());		//from the average
		
		assertTrue(counts.keySet().size() == 2);//only integer formula results
	}

	@Test
	public void testGetInputReferences() {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputReferences();
		Integer count = counts.get(InputCellType.INTEGER);
		assertNotNull(count);
		assertEquals(4, count.intValue());

		assertTrue(counts.keySet().size() == 1);//only integer inputs
	}

	@Test
	public void testGetFormulaReferencingOtherCells() {
		int count = analyzer.getFormulaReferencingOtherCells();
		assertEquals(7, count);
	}

	@Test
	public void testGetFormulasReferenced() {
		int count = analyzer.getFormulasReferenced();
		assertEquals(2, count);
	}

}
