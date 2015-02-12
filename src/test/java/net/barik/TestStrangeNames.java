package net.barik;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Map;

import net.barik.SpreadsheetAnalyzer.InputCellType;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestStrangeNames {

	private static SpreadsheetAnalyzer analyzer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/weirdNames.xlsx");
		assertNotNull(is);
		analyzer = SpreadsheetAnalyzer.doEUSESAnalysis(is);
		assertNotNull(analyzer);
	}

	@Test
	public void testFunctionCounts() throws Exception {
		Map<String, Integer> functionCounts = analyzer.getFunctionCounts();
		assertEquals(Integer.valueOf(1), functionCounts.get("T"));		//avoids an ambiguous call
		assertEquals(Integer.valueOf(1), functionCounts.get("Z.TEST"));
		assertEquals(Integer.valueOf(1), functionCounts.get("SUMX2MY2"));
		assertEquals(Integer.valueOf(1), functionCounts.get("T.TEST"));
		assertNull(functionCounts.get("STRANGE"));		//this is in a sheet name, shouldn't be afunction
	}
	
	@Test
	public void testFunctionReferences() throws Exception {
		assertEquals(0, analyzer.getFormulasReferenced());
	}
	
	@Test
	public void testInputCellsReferenced() throws Exception {
		Integer actual = analyzer.getInputReferences().get(InputCellType.INTEGER);
		assertNotNull(actual);
		assertEquals(22, actual.intValue());
	}

}
