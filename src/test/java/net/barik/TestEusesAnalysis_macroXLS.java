package net.barik;

import static org.junit.Assert.*;

import java.io.InputStream;


import org.junit.BeforeClass;
import org.junit.Test;

public class TestEusesAnalysis_macroXLS {
	
	private static SpreadsheetAnalyzer analyzer;

	@BeforeClass
	public static void loadSmallWorksheet() throws Exception{
		InputStream is = TestEusesAnalysis_macroXLS.class.getResourceAsStream("/wmacro.xls");
		assertNotNull(is);
		InputStream is2 = TestEusesAnalysis_macroXLS.class.getResourceAsStream("/wmacro.xls");
		assertNotNull(is2);
		analyzer = SpreadsheetAnalyzer.doEUSESAnalysis(is, is2);
		assertNotNull(analyzer);
		
	}

    @Test
    public void testContainsMacro() throws Exception{
    	boolean b = analyzer.getContainsMacro();
    	assertTrue(b);
    }
	
}
