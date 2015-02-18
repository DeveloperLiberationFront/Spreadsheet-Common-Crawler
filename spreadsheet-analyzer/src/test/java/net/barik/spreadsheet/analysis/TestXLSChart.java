package net.barik.spreadsheet.analysis;

import static org.junit.Assert.*;

import java.io.InputStream;

import net.barik.spreadsheet.analysis.SpreadsheetAnalyzer;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestXLSChart {

	private static SpreadsheetAnalyzer analyzer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStream is = TestXLSChart.class.getResourceAsStream("/chart.xls");
		assertNotNull(is);
		analyzer = SpreadsheetAnalyzer.doAnalysis(is);
		assertNotNull(analyzer);
	}

	@Test
	public void testChart() {
		assertTrue(analyzer.containsChart());
		assertEquals(1, analyzer.getNumCharts());
	}

}
