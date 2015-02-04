package net.barik;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.Map;

import org.junit.Test;

public class TestSpreadsheetAnalyzer {

	@Test
	public void testFindWorksheet() throws Exception {
		InputStream is = TestSpreadsheetAnalyzer.class.getResourceAsStream("/small-worksheet.xlsx");
		assertNotNull(is);
	}
	
	@Test
	public void testCountingSum() throws Exception {
		InputStream is = TestSpreadsheetAnalyzer.class.getResourceAsStream("/test/small-worksheet.xlsx");
		SpreadsheetAnalyzer analyzer = SpreadsheetAnalyzer.parse(is);
		
		Map<String, Integer> counts = analyzer.getFunctionCounts();
		Integer sumCounts = counts.get("SUM");
		assertNotNull(sumCounts);
		assertEquals(13, sumCounts.intValue());
	}

}
