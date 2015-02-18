package net.barik.spreadsheet.analysis;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

public class TestBadEnronSheets {

	@Test
	public void testBadEnron1() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/bad_enron_1.xlsx");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad_enron_1.xlsx");
		assertNotNull(analysis);
		assertEquals("OK", analysis.errorNotification);
		assertEquals(12646, analysis.totalInputCells);
		assertEquals(13, analysis.booleanInputCells);
		assertEquals(1299, analysis.dateTimeInputCells);
		assertEquals(0, analysis.errorInputCells);
		assertEquals(1696, analysis.integerInputCells);
		assertEquals(9301, analysis.nonIntegerInputCells);
		assertEquals(337, analysis.stringInputCells);
		assertEquals(1772, analysis.totalReferencedInput);
		assertEquals(13, analysis.booleanReferencedInput);
		assertEquals(368, analysis.dateReferencedInput);
		assertEquals(0, analysis.errorReferencedInput);
		assertEquals(96, analysis.integerReferencedInput);
		assertEquals(1271, analysis.nonIntegerReferencedInput);
		assertEquals(24, analysis.stringReferencedInput);
		assertEquals(5091, analysis.totalFormulas);
		assertEquals(0, analysis.booleanFormulas);
		assertEquals(1269, analysis.dateTimeFormulas);
		assertEquals(1520, analysis.errorFormulas);
		assertEquals(351, analysis.integerFormulas);
		assertEquals(1927, analysis.nonIntegerFormulas);
		assertEquals(24, analysis.stringFormulas);
		assertEquals(0, analysis.blankFormulas);
		assertEquals(4048, analysis.formulaCellsReferencingOthers);
		assertEquals(1828, analysis.formulaCellsReferencedByOthers);
		assertEquals(121, analysis.formulaCellsOccuringOnce);
		assertEquals(273, analysis.formulaCellsOccuring2Plus);
		assertEquals(21, analysis.formulaCellsOccuring5Plus);
		assertEquals(19, analysis.formulaCellsOccuring10Plus);
		assertEquals(19, analysis.formulaCellsOccuring25Plus);
		assertEquals(17, analysis.formulaCellsOccuring50Plus);
		assertEquals(11, analysis.formulaCellsOccuring100Plus);
		assertEquals(521, analysis.mostFrequentFormulaCount);
		assertEquals("R[0]C[19]", analysis.mostFrequentFormula);
		assertFalse(analysis.containsMacros);
		assertEquals(2, analysis.countCEILING);
		assertEquals(272, analysis.countDATE);
		assertEquals(17, analysis.countDAY);
		assertEquals(32, analysis.countIF);
		assertEquals(23, analysis.countMAX);
		assertEquals(292, analysis.countMONTH);
		assertEquals(1, analysis.countSUM);
		assertEquals(251, analysis.countVLOOKUP);
		assertEquals(288, analysis.countYEAR);
		assertEquals(6, analysis.numSheets);
		assertEquals(0, analysis.numFormulasThatArePartOfArrayFormulaGroup);
		// everything else is 0
	}

	@Test
	public void testBadEnron2() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/bad_enron_2.xlsx");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad_enron_2.xlsx");
		assertNotNull(analysis);
		assertEquals("OK", analysis.errorNotification);
		assertEquals(2268, analysis.totalInputCells);
		assertEquals(0, analysis.booleanInputCells);
		assertEquals(416, analysis.dateTimeInputCells);
		assertEquals(0, analysis.errorInputCells);
		assertEquals(1664, analysis.integerInputCells);
		assertEquals(0, analysis.nonIntegerInputCells);
		assertEquals(188, analysis.stringInputCells);
		assertEquals(1873, analysis.totalReferencedInput);
		assertEquals(0, analysis.booleanReferencedInput);
		assertEquals(208, analysis.dateReferencedInput);
		assertEquals(0, analysis.errorReferencedInput);
		assertEquals(1664, analysis.integerReferencedInput);
		assertEquals(0, analysis.nonIntegerReferencedInput);
		assertEquals(1, analysis.stringReferencedInput);
		assertEquals(5431, analysis.totalFormulas);
		assertEquals(0, analysis.booleanFormulas);
		assertEquals(209, analysis.dateTimeFormulas);
		assertEquals(0, analysis.errorFormulas);
		assertEquals(3520, analysis.integerFormulas);
		assertEquals(816, analysis.nonIntegerFormulas);
		assertEquals(718, analysis.stringFormulas);
		assertEquals(168, analysis.blankFormulas);
		assertEquals(5426, analysis.formulaCellsReferencingOthers);
		assertEquals(2616, analysis.formulaCellsReferencedByOthers);
		assertEquals(480, analysis.formulaCellsOccuringOnce);
		assertEquals(72, analysis.formulaCellsOccuring2Plus);
		assertEquals(19, analysis.formulaCellsOccuring5Plus);
		assertEquals(17, analysis.formulaCellsOccuring10Plus);
		assertEquals(16, analysis.formulaCellsOccuring25Plus);
		assertEquals(16, analysis.formulaCellsOccuring50Plus);
		assertEquals(11, analysis.formulaCellsOccuring100Plus);
		assertEquals(1145, analysis.mostFrequentFormulaCount);
		assertEquals("Data!R[76]C[0]", analysis.mostFrequentFormula);
		assertFalse(analysis.containsMacros);
		assertEquals(15, analysis.numCharts);
		assertEquals(832, analysis.countAVERAGE);
		assertEquals(4, analysis.countDATE);
		assertEquals(4, analysis.countDAY);
		assertEquals(1576, analysis.countIF);
		assertEquals(4, analysis.countMONTH);
		assertEquals(1, analysis.countSUM);
		assertEquals(1, analysis.countTODAY);
		assertEquals(4, analysis.countVLOOKUP);
		assertEquals(4, analysis.countYEAR);
		assertEquals(14, analysis.numSheets);
		assertEquals(0, analysis.numFormulasThatArePartOfArrayFormulaGroup);
		// everything else is 0
	}

	@Test
	public void testBadEnron3() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/bad_enron_3.xlsx");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad_enron_3.xlsx");
		assertNotNull(analysis);
		assertEquals("OK", analysis.errorNotification);
		assertEquals(3503, analysis.totalInputCells);
		assertEquals(0, analysis.booleanInputCells);
		assertEquals(20, analysis.dateTimeInputCells);
		assertEquals(0, analysis.errorInputCells);
		assertEquals(1503, analysis.integerInputCells);
		assertEquals(1239, analysis.nonIntegerInputCells);
		assertEquals(741, analysis.stringInputCells);
		assertEquals(1093, analysis.totalReferencedInput);
		assertEquals(0, analysis.booleanReferencedInput);
		assertEquals(0, analysis.dateReferencedInput);
		assertEquals(0, analysis.errorReferencedInput);
		assertEquals(1011, analysis.integerReferencedInput);
		assertEquals(53, analysis.nonIntegerReferencedInput);
		assertEquals(29, analysis.stringReferencedInput);
		assertEquals(3298, analysis.totalFormulas);
		assertEquals(0, analysis.booleanFormulas);
		assertEquals(0, analysis.dateTimeFormulas);
		assertEquals(1101, analysis.errorFormulas);
		assertEquals(1999, analysis.integerFormulas);
		assertEquals(102, analysis.nonIntegerFormulas);
		assertEquals(96, analysis.stringFormulas);
		assertEquals(0, analysis.blankFormulas);
		assertEquals(2661, analysis.formulaCellsReferencingOthers);
		assertEquals(2452, analysis.formulaCellsReferencedByOthers);
		assertEquals(134, analysis.formulaCellsOccuringOnce);
		assertEquals(111, analysis.formulaCellsOccuring2Plus);
		assertEquals(89, analysis.formulaCellsOccuring5Plus);
		assertEquals(80, analysis.formulaCellsOccuring10Plus);
		assertEquals(10, analysis.formulaCellsOccuring25Plus);
		assertEquals(8, analysis.formulaCellsOccuring50Plus);
		assertEquals(2, analysis.formulaCellsOccuring100Plus);
		assertEquals(288, analysis.mostFrequentFormulaCount);
		assertEquals("R[-1]C[0]-R[-3]C[0]", analysis.mostFrequentFormula);
		assertFalse(analysis.containsMacros);
		assertEquals(1389, analysis.countIF);
		assertEquals(45, analysis.countINDEX);
		assertEquals(24, analysis.countISTEXT);
		assertEquals(60, analysis.countMATCH);
		assertEquals(384, analysis.countMIN);
		assertEquals(780, analysis.countOFFSET);
		assertEquals(12, analysis.countOR);
		assertEquals(24, analysis.countROUND);
		assertEquals(24, analysis.countROWS);
		assertEquals(981, analysis.countSUM);
		assertEquals(84, analysis.countSUMIF);
		assertEquals(11, analysis.numSheets);
		assertEquals(440, analysis.numFormulasThatArePartOfArrayFormulaGroup);
		// all others are 0
	}

	@Test
	public void testMemoryConsumption() throws Exception {
		String memoryTestLimit = System.getenv("BARIK_MEMORY_TEST_LIMIT");
		int memoryLimit = 20;
		if (memoryTestLimit != null) {
			System.out.println("BARIK_MEMORY_TEST_LIMIT was " + memoryTestLimit + " using that");
			try {
				memoryLimit = Integer.parseInt(memoryTestLimit);
			} catch (NumberFormatException e) {
				e.printStackTrace(); // possibly a conflict
			}
		} else {
			System.out.println("BARIK_MEMORY_TEST_LIMIT was null.  Defaulting to run " + memoryLimit + " times");
		}
		for (int i = 0; i < memoryLimit; i++) {
			System.out.println("Memory test: " + (i + 1));
			InputStream is = TestInputCounts.class.getResourceAsStream("/bad_enron_1.xlsx");
			assertNotNull(is);
			AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad_enron_1.xlsx");
			assertNotNull(analysis);
			is.close();
		}
	}
	
	@Test
	public void testBadEnron4() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/bad_enron_4.xlsx");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad_enron_4.xlsx");
		assertNotNull(analysis);
		assertEquals("OK", analysis.errorNotification);
		assertEquals(3, analysis.numSheets);
		assertEquals(528, analysis.numFormulasThatArePartOfArrayFormulaGroup);
		assertEquals(5707, analysis.totalInputCells);
		assertEquals(0, analysis.booleanInputCells);
		assertEquals(707, analysis.dateTimeInputCells);
		assertEquals(0, analysis.errorInputCells);
		assertEquals(4725, analysis.integerInputCells);
		assertEquals(0, analysis.nonIntegerInputCells);
		assertEquals(275, analysis.stringInputCells);
		assertEquals(4714, analysis.totalReferencedInput);
		assertEquals(0, analysis.booleanReferencedInput);
		assertEquals(0, analysis.dateReferencedInput);
		assertEquals(0, analysis.errorReferencedInput);
		assertEquals(4714, analysis.integerReferencedInput);
		assertEquals(0, analysis.nonIntegerReferencedInput);
		assertEquals(0, analysis.stringReferencedInput);
		assertEquals(3242, analysis.totalFormulas);
		assertEquals(0, analysis.booleanFormulas);
		assertEquals(0, analysis.dateTimeFormulas);
		assertEquals(399, analysis.errorFormulas);
		assertEquals(2456, analysis.integerFormulas);
		assertEquals(387, analysis.nonIntegerFormulas);
		assertEquals(0, analysis.stringFormulas);
		assertEquals(0, analysis.blankFormulas);
		assertEquals(3242, analysis.formulaCellsReferencingOthers);
		assertEquals(2442, analysis.formulaCellsReferencedByOthers);
		assertEquals(528, analysis.formulaCellsOccuringOnce);
		assertEquals(29, analysis.formulaCellsOccuring2Plus);
		assertEquals(28, analysis.formulaCellsOccuring5Plus);
		assertEquals(28, analysis.formulaCellsOccuring10Plus);
		assertEquals(1, analysis.formulaCellsOccuring25Plus);
		assertEquals(1, analysis.formulaCellsOccuring50Plus);
		assertEquals(1, analysis.formulaCellsOccuring100Plus);
		assertEquals(2442, analysis.mostFrequentFormulaCount);
		assertEquals("R[0]C[-14]-R[-1]C[-14]", analysis.mostFrequentFormula);
		assertEquals(0, analysis.numCharts);
		assertFalse(analysis.containsMacros);
		assertEquals(270, analysis.countCORREL);
		assertEquals(528, analysis.countLINEST);
		//every thing else is 0
	}
	
	@Test(timeout=120000)		//two minutes
	public void testBadEnron5() throws Exception {
		InputStream is = TestInputCounts.class.getResourceAsStream("/bad_enron_5.xlsx");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad_enron_5.xlsx");
		assertNotNull(analysis);
		assertEquals("OK", analysis.errorNotification);
	}
}
