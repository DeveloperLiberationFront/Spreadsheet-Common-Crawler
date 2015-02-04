package net.barik;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Map;

import net.barik.SpreadsheetAnalyzer.InputCellType;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestSpreadsheetAnalyzer {
	
	private static SpreadsheetAnalyzer analyzer;

	@BeforeClass
	public static void loadSmallWorksheet() throws Exception{
		InputStream is = TestSpreadsheetAnalyzer.class.getResourceAsStream("/small-worksheet.xlsx");
		assertNotNull(is);
		analyzer = SpreadsheetAnalyzer.parse(is);
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

}
