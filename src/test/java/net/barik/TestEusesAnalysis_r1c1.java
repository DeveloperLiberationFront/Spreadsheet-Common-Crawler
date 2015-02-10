package net.barik;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import net.barik.SpreadsheetAnalyzer.FunctionEvalType;
import net.barik.SpreadsheetAnalyzer.InputCellType;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestEusesAnalysis_r1c1 {

	private static SpreadsheetAnalyzer analyzer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStream is = TestEusesAnalysis_small.class.getResourceAsStream("/r1c1.xlsx");
		assertNotNull(is);
		analyzer = SpreadsheetAnalyzer.doEUSESAnalysis(is);
		assertNotNull(analyzer);
	}

	@Test
	public void testGetFunctionCounts() {
		Map<String, Integer> counts = analyzer.getFunctionCounts();
		Integer count = counts.get("SUM");
		assertNotNull(count);
		assertEquals(1, count.intValue());
		
		count = counts.get("AVERAGE");
		assertNotNull(count);
		assertEquals(1, count.intValue());
		
		assertEquals(2,counts.keySet().size());//sum and average are the only input
	}

	@Test
	public void testGetInputCellCounts() {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputCellCounts();
		Integer count = counts.get(InputCellType.INTEGER);
		assertNotNull(count);
		assertEquals(4, count.intValue());

		assertEquals(1, counts.keySet().size());//only integer inputs
	}

	@Test
	public void testGetFormulaCellCounts() {
		Map<SpreadsheetAnalyzer.FunctionEvalType, Integer> counts = analyzer.getFormulaCellCounts();
		Integer count = counts.get(FunctionEvalType.INTEGER);
		assertNotNull(count);
		assertEquals(6,  count.intValue());
		
		count = counts.get(FunctionEvalType.NON_INTEGER_NUMBER);
		assertNotNull(count);
		assertEquals(1, count.intValue());		//from the average
		
		assertEquals(2, counts.keySet().size());//only integer formula results
	}

	@Test
	public void testGetInputReferences() {
		Map<SpreadsheetAnalyzer.InputCellType, Integer> counts = analyzer.getInputReferences();
		Integer count = counts.get(InputCellType.INTEGER);
		assertNotNull(count);
		assertEquals(4, count.intValue());

		assertEquals(1, counts.keySet().size());//only integer inputs
	}

	@Test
	public void testGetFormulaReferencingOtherCells() {
		int count = analyzer.getFormulaReferencingOtherCells();
		assertEquals(7, count);
	}

	@Test
	public void testGetFormulasReferenced() {
		int count = analyzer.getFormulasReferenced();
		assertEquals(2, count);
	}
	
	@Test
	public void testGetFormulasUsedOnce() {
		int count = analyzer.getFormulasUsedOnce();
		assertEquals(3, count);
	}
	
	@Test
	public void testGetFormulasUsedMoreThanOnce() {
		int count = analyzer.getFormulasUsedMoreThanOnce();
		assertEquals(1, count);
	}
	
	@Test
	public void testGetMostTimesMostFrequentlyOcurringFormulaWasUsed() {
		int count = analyzer.getMostTimesMostFrequentlyOccurringFormulaWasUsed();
		assertEquals(4, count);
	}
	
	@Test
	public void testR1C1Conversion() throws IOException {
		Workbook wb = new XSSFWorkbook();
	    Sheet sheet = wb.createSheet("new sheet");

	    // Rows are 0 based.
	    Row row = sheet.createRow(16);
	    // Create a cell and put a value in it.  Columns are 0-indexed
	    Cell C17 = row.createCell(2);
	    C17.setCellFormula("$C16+E$18+$B$18+$F$16");
	    //test assorted single cell references
	    assertEquals("R[-1]C3+R18C[2]+R18C2+R16C6",analyzer.convertToR1C1(C17));
	    
	    Cell D17 = row.createCell(3);
	    D17.setCellFormula("C16+E18+B18+F16");
	    //test assorted single cell references
	    assertEquals("R[-1]C[-1]+R[1]C[1]+R[1]C[-2]+R[-1]C[2]",analyzer.convertToR1C1(D17));
	    
	    Cell E17 = row.createCell(4);
	    E17.setCellFormula("SUM(E1:E15)");
	    //test range over rows
	    assertEquals("SUM(R[-16]C[0]:R[-2]C[0])",analyzer.convertToR1C1(E17));
	    
	    Cell F17 = row.createCell(5);
	    F17.setCellFormula("COUNT(Sheet1!A17:E17)");
	    //test range over columns, with sheet
	    assertEquals("COUNT(Sheet1!R[0]C[-5]:R[0]C[-1])",analyzer.convertToR1C1(F17));
	    
	    Cell G17 = row.createCell(6);
	    G17.setCellFormula("AVERAGE(A:A)");
	    //test single column range
	    assertEquals("AVERAGE(C[-6])",analyzer.convertToR1C1(G17));
	    
	    Cell H17 = row.createCell(7);
	    H17.setCellFormula("AVERAGE(I:AI)");
	    //test multi column range
	    assertEquals("AVERAGE(C[1]:C[27])",analyzer.convertToR1C1(H17));
	    
	    Cell I17 = row.createCell(8);
	    I17.setCellFormula("AVERAGE(5:5)");
	    //test single row range
	    assertEquals("AVERAGE(R[-12])",analyzer.convertToR1C1(I17));
	    
	    Cell J17 = row.createCell(9);
	    J17.setCellFormula("AVERAGE(20:108)");
	    //test multi row range
	    assertEquals("AVERAGE(R[3]:R[91])",analyzer.convertToR1C1(J17));
	    
	    Cell K17 = row.createCell(10);
	    K17.setCellFormula("AVERAGE($A:$A)");
	    //test single absolute column range
	    assertEquals("AVERAGE(C1)",analyzer.convertToR1C1(K17));
	    
	    Cell L17 = row.createCell(11);
	    L17.setCellFormula("AVERAGE(M:$AM)");
	    //test multi column range with one anchor
	    assertEquals("AVERAGE(C[1]:C39)",analyzer.convertToR1C1(L17));
	    
	    Cell M17 = row.createCell(12);
	    M17.setCellFormula("AVERAGE(5:$5)");
	    //test single row range with one anchor
	    assertEquals("AVERAGE(R[-12]:R5)",analyzer.convertToR1C1(M17));
	    
	    Cell N17 = row.createCell(13);
	    N17.setCellFormula("AVERAGE($5:5)");
	    //test single row range with one anchor (other)
	    assertEquals("AVERAGE(R5:R[-12])",analyzer.convertToR1C1(N17));
	    
	    Cell O17 = row.createCell(14);
	    O17.setCellFormula("AVERAGE($20:108)");
	    //test multi row range with on achor
	    assertEquals("AVERAGE(R20:R[91])",analyzer.convertToR1C1(O17));
	    
	    wb.close();
	}

}
