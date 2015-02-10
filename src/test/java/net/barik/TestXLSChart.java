package net.barik;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestXLSChart {

	private static SpreadsheetAnalyzer analyzer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStream is = TestXLSChart.class.getResourceAsStream("/chart.xls");
		assertNotNull(is);
		InputStream is2 = TestXLSChart.class.getResourceAsStream("/chart.xls");
		assertNotNull(is2);
		analyzer = SpreadsheetAnalyzer.doEUSESAnalysis(is, is2);
		assertNotNull(analyzer);
	}

	@Test
	public void testChart() {
		assertTrue(analyzer.containsChart());
		assertEquals(1, analyzer.getNumCharts());
	}

}
