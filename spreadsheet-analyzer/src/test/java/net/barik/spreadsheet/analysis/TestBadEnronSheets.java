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
	
	@Test
	public void testMemoryConsumption() throws Exception {
		String memoryTestLimit = System.getenv("BARIK_MEMORY_TEST_LIMIT");
		int memoryLimit = 50;
		if (memoryTestLimit != null) {
			System.out.println("BARIK_MEMORY_TEST_LIMIT was "+ memoryTestLimit +" using that");
			try {
				memoryLimit = Integer.parseInt(memoryTestLimit);
			}
			catch (NumberFormatException e) {
				e.printStackTrace();		//possibly a conflict
			}
		} else {
			System.out.println("BARIK_MEMORY_TEST_LIMIT was null.  Defaulting to run "+memoryLimit+" times");
		}
		for (int i = 0; i < memoryLimit; i++) {
			System.out.println("Memory test: "+(i+1));
			InputStream is = TestInputCounts.class.getResourceAsStream("/bad_enron_1.xlsx");
			assertNotNull(is);
			AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad_enron_1.xlsx");
			assertNotNull(analysis);
			is.close();
		}
	}

}
