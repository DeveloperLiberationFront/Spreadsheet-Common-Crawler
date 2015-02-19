package net.barik.spreadsheet.analysis;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

public class TestEncryptedAndOldBooks {

	@Test
	public void testEncryptedXLS() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/encrypted.xls");
		assertNotNull(is);
		AnalysisOutputAndFormulas aof = SpreadsheetAnalyzer.doAnalysisAndGetObjectAndFormulas(is, "[test]", "bad");
		AnalysisOutput analysis = aof.analysisObject;
		assertNotNull(analysis);
		assertEquals("ENCRYPTED", analysis.errorNotification);
		assertNotNull(analysis.stackTrace);
	}
	
	
	@Test
	public void testEncryptedBookXLSX() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/encrypted-whole-book.xlsx");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad");
		assertNotNull(analysis);
		assertEquals("ENCRYPTED", analysis.errorNotification);
		assertNotNull(analysis.stackTrace);
	}
	
	@Test
	public void testEncryptedSheetXLSX() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/encrypted-one-sheet.xlsx");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad");
		assertNotNull(analysis);
		assertEquals("ENCRYPTED", analysis.errorNotification);
		assertNotNull(analysis.stackTrace);
	}
	
	@Test
	public void testOldXLS() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/old-biff5.xls");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad");
		assertNotNull(analysis);
		assertEquals("BIFF5", analysis.errorNotification);
		assertNotNull(analysis.stackTrace);
	}
	
	@Test
	public void testInvalidXLS() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/invalidStream.xls");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad");
		assertNotNull(analysis);
		assertEquals("CORRUPT", analysis.errorNotification);
		assertNotNull(analysis.stackTrace);
	}
	
	@Test
	public void testXLSB() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/binaryWorkbook.xlsb");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad");
		assertNotNull(analysis);
		assertEquals("XLSB", analysis.errorNotification);
		assertNotNull(analysis.stackTrace);
	}
	
}
