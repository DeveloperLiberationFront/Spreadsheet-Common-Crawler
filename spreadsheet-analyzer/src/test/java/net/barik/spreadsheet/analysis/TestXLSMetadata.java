package net.barik.spreadsheet.analysis;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

public class TestXLSMetadata {

	@Test
	public void test() {
		InputStream is = TestInputCounts.class.getResourceAsStream("/metadata.xls");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "metadata.xls");
		
		assertEquals("Aswath Damodaran", analysis.createdBy);
		assertEquals("Justin", analysis.lastModifiedBy);
		assertEquals("2000-11-15T22:33:24Z", analysis.createdDate);
		assertEquals("", analysis.lastPrintedDate);
		assertEquals("2015-01-29T20:17:14Z", analysis.lastModifiedDate);
		assertEquals("demo", analysis.company);
		assertEquals("tag1; tag2",analysis.keywords);
	}

}
