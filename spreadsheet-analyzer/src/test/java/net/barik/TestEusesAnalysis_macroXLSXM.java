package net.barik;

import static org.junit.Assert.*;

import java.io.InputStream;


import org.junit.BeforeClass;
import org.junit.Test;

public class TestEusesAnalysis_macroXLSXM {
	
	private static SpreadsheetAnalyzer analyzer;

	@BeforeClass
	public static void loadSmallWorksheet() throws Exception{
		InputStream is = TestEusesAnalysis_macroXLSXM.class.getResourceAsStream("/wmacro.xlsm");
		assertNotNull(is);
		analyzer = SpreadsheetAnalyzer.doEUSESAnalysis(is);
		assertNotNull(analyzer);
		
	}

    @Test
    public void testContainsMacro() throws Exception{
    	boolean b = analyzer.getContainsMacro();
    	assertTrue(b);
    }
	
}
