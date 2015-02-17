package net.barik.spreadsheet.analysis;

import static org.junit.Assert.*;

import java.io.InputStream;

import net.barik.spreadsheet.analysis.SpreadsheetAnalyzer;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestXLSXChart {

	private static SpreadsheetAnalyzer analyzer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStream is = TestXLSXChart.class.getResourceAsStream("/chart.xlsx");
		assertNotNull(is);
		analyzer = SpreadsheetAnalyzer.doEUSESAnalysis(is);
		assertNotNull(analyzer);
	}

	@Test
	public void testChart() {
		assertTrue(analyzer.containsChart());
		assertEquals(1, analyzer.getNumCharts());
	}

}
