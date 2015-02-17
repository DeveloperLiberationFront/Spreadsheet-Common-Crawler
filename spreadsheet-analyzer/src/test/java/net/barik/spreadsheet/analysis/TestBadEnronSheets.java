package net.barik.spreadsheet.analysis;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

public class TestBadEnronSheets {

	@Test
	public void testBadEnron1() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/bad_enron_1.xlsx");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad_enron_1.xlsx");
		assertNotNull(analysis);
		assertEquals("OK", analysis.errorNotification);
		
	}
	
	@Test
	public void testBadEnron2() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/bad_enron_2.xlsx");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad_enron_2.xlsx");
		assertNotNull(analysis);
		assertEquals("OK", analysis.errorNotification);
	}
	
	@Test
	public void testBadEnron3() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/bad_enron_3.xlsx");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad_enron_3.xlsx");
		assertNotNull(analysis);
		assertEquals("OK", analysis.errorNotification);
	}

}
