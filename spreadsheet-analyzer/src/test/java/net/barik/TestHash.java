package net.barik;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestHash {

	private static SpreadsheetAnalyzer analyzer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStream is = TestEusesAnalysis_small.class.getResourceAsStream("/r1c1.xlsx");
		assertNotNull(is);
		analyzer = SpreadsheetAnalyzer.doEUSESAnalysis(is);
		assertNotNull(analyzer);
	}
	
	@Test
	public void testAnalysisOutput() throws Exception {
		String hash = analyzer.getFileHash();
		assertNotNull(hash);
		assertEquals(hash, "9059b8d01e0f936869b78b9d730a41659c772f7e286c47140bd9a4473e5a10ac53b551b44d1f2536ed18c006058c0025f8ee1a23aa22e0f8bece5a40905c867d");
	}

}
