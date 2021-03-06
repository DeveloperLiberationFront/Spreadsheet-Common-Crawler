package net.barik.spreadsheet.analysis;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

public class TestDifficultEnronSheets {

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
			InputStream is = getClass().getResourceAsStream("/bad_enron_1.xlsx");
			assertNotNull(is);
			AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad_enron_1.xlsx");
			assertNotNull(analysis);
			is.close();
		}
	}

	@Test
	public void testDifficultEnron1() throws Exception {
		InputStream is = getClass().getResourceAsStream("/bad_enron_1.xlsx");
		assertNotNull(is);
		AnalysisOutputAndFormulas aof = SpreadsheetAnalyzer.doAnalysisAndGetObjectAndFormulas(is, "[test]", "bad_enron_1.xlsx");
		AnalysisOutput analysis = aof.analysisObject;
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
		assertEquals(4843, analysis.formulaCellsReferencingOthers);
		assertEquals(1832, analysis.formulaCellsReferencedByOthers);
		assertEquals(163, analysis.formulaCellsOccuringOnce);
		assertEquals(401, analysis.formulaCellsOccuring2Plus);
		assertEquals(85, analysis.formulaCellsOccuring5Plus);
		assertEquals(19, analysis.formulaCellsOccuring10Plus);
		assertEquals(19, analysis.formulaCellsOccuring25Plus);
		assertEquals(17, analysis.formulaCellsOccuring50Plus);
		assertEquals(11, analysis.formulaCellsOccuring100Plus);
		assertEquals(521, analysis.mostFrequentFormulaCount);
		assertEquals("R[0]C[19]", analysis.mostFrequentFormula);
		assertEquals(0, analysis.numCharts);
		assertFalse(analysis.containsMacros);
		assertTrue(analysis.containsThirdPartyFunctions);
		assertEquals(1363, analysis.countPlus);
		assertEquals(933, analysis.countMinus);
		assertEquals(848, analysis.countDivide);
		assertEquals(531, analysis.countMultiply);
		assertEquals(0, analysis.numFormulasThatArePartOfArrayFormulaGroup);
		
		assertEquals(2, analysis.countCEILING);
		assertEquals(272, analysis.countDATE);
		assertEquals(3, analysis.countDAY);
		assertEquals(18, analysis.countIF);
		assertEquals(23, analysis.countMAX);
		assertEquals(282, analysis.countMONTH);
		assertEquals(1, analysis.countSUM);
		assertEquals(251, analysis.countVLOOKUP);
		assertEquals(280, analysis.countYEAR);
		assertEquals(6, analysis.numSheets);
		assertNull(analysis.stackTrace);
		//everything else is 0
		
		assertEquals("ahuang2", analysis.createdBy);
		assertEquals("Felienne", analysis.lastModifiedBy);
		assertEquals("2000-01-10T18:59:16Z", analysis.createdDate);
		assertEquals("2000-03-07T14:28:52Z", analysis.lastPrintedDate);
		assertEquals("2014-09-03T10:59:03Z", analysis.lastModifiedDate);
		assertEquals("ect", analysis.company);
		assertEquals("",analysis.keywords);
	}

	@Test
	public void testDifficultEnron2() throws Exception {
		InputStream is = getClass().getResourceAsStream("/bad_enron_2.xlsx");
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
		assertEquals(15, analysis.numCharts);
		assertFalse(analysis.containsMacros);
		assertFalse(analysis.containsThirdPartyFunctions);
		assertEquals(208, analysis.countPlus);
		assertEquals(2512, analysis.countMinus);
		assertEquals(0, analysis.countDivide);
		assertEquals(0, analysis.countMultiply);
		assertEquals(0, analysis.numFormulasThatArePartOfArrayFormulaGroup);

		assertEquals(416, analysis.countAVERAGE);
		assertEquals(4, analysis.countDATE);
		assertEquals(4, analysis.countDAY);
		assertEquals(1576, analysis.countIF);
		assertEquals(4, analysis.countMONTH);
		assertEquals(1, analysis.countSUM);
		assertEquals(1, analysis.countTODAY);
		assertEquals(4, analysis.countVLOOKUP);
		assertEquals(4, analysis.countYEAR);
		assertEquals(14, analysis.numSheets);
		assertNull(analysis.stackTrace);
		//everything else is 0
		
		assertEquals("", analysis.createdBy);
		assertEquals("Felienne", analysis.lastModifiedBy);
		assertEquals("1997-09-15T20:07:49Z", analysis.createdDate);
		assertEquals("2001-12-31T15:55:52Z", analysis.lastPrintedDate);
		assertEquals("2014-09-03T12:03:17Z", analysis.lastModifiedDate);
		assertEquals("", analysis.company);
		assertEquals("",analysis.keywords);
	}

	@Test
	public void testDifficultEnron3() throws Exception {
		InputStream is = getClass().getResourceAsStream("/bad_enron_3.xlsx");
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
		assertEquals(3210, analysis.formulaCellsReferencingOthers);
		assertEquals(2452, analysis.formulaCellsReferencedByOthers);
		assertEquals(636, analysis.formulaCellsOccuringOnce);
		assertEquals(134, analysis.formulaCellsOccuring2Plus);
		assertEquals(89, analysis.formulaCellsOccuring5Plus);
		assertEquals(80, analysis.formulaCellsOccuring10Plus);
		assertEquals(10, analysis.formulaCellsOccuring25Plus);
		assertEquals(8, analysis.formulaCellsOccuring50Plus);
		assertEquals(2, analysis.formulaCellsOccuring100Plus);
		assertEquals(288, analysis.mostFrequentFormulaCount);
		assertEquals("R[-1]C[0]-R[-3]C[0]", analysis.mostFrequentFormula);
		assertEquals(0, analysis.numCharts);
		assertFalse(analysis.containsMacros);
		assertTrue(analysis.containsThirdPartyFunctions);
		assertEquals(677, analysis.countPlus);
		assertEquals(1286, analysis.countMinus);
		assertEquals(142, analysis.countDivide);
		assertEquals(629, analysis.countMultiply);
		
		assertEquals(690, analysis.countIF);
		assertEquals(45, analysis.countINDEX);
		assertEquals(24, analysis.countISTEXT);
		assertEquals(609, analysis.countMATCH);
		assertEquals(48, analysis.countMIN);
		assertEquals(994, analysis.countOFFSET);
		assertEquals(12, analysis.countOR);
		assertEquals(24, analysis.countROUND);
		assertEquals(12, analysis.countROWS);
		assertEquals(782, analysis.countSUM);
		assertEquals(84, analysis.countSUMIF);
		assertEquals(440, analysis.numFormulasThatArePartOfArrayFormulaGroup);
		assertEquals(11, analysis.numSheets);
		assertNull(analysis.stackTrace);
		//everything else is 0
		
		assertEquals("", analysis.createdBy);
		assertEquals("Felienne", analysis.lastModifiedBy);
		assertEquals("1998-02-07T06:53:21Z", analysis.createdDate);
		assertEquals("2002-01-03T16:25:16Z", analysis.lastPrintedDate);
		assertEquals("2014-09-03T13:26:12Z", analysis.lastModifiedDate);
		assertEquals("", analysis.company);
		assertEquals("",analysis.keywords);
		
	}

	@Test
	public void testDifficultEnron4() throws Exception {
		InputStream is = getClass().getResourceAsStream("/bad_enron_4.xlsx");
		assertNotNull(is);
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(is, "[test]", "bad_enron_4.xlsx");
		assertNotNull(analysis);
		assertEquals("OK", analysis.errorNotification);
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
		assertFalse(analysis.containsThirdPartyFunctions);
		assertEquals(0, analysis.countPlus);
		assertEquals(2442, analysis.countMinus);
		assertEquals(0, analysis.countDivide);
		assertEquals(0, analysis.countMultiply);
		 
		assertEquals(270, analysis.countCORREL);
		assertEquals(528, analysis.countLINEST);
		assertEquals(528, analysis.numFormulasThatArePartOfArrayFormulaGroup);
		assertEquals(3, analysis.numSheets);
		assertNull(analysis.stackTrace);
		//everything else is 0		 

		assertEquals("mcaushol", analysis.createdBy);
		assertEquals("Felienne", analysis.lastModifiedBy);
		assertEquals("2001-08-06T21:11:38Z", analysis.createdDate);
		assertEquals("2001-08-07T13:44:58Z", analysis.lastPrintedDate);
		assertEquals("2014-09-05T10:38:27Z", analysis.lastModifiedDate);
		assertEquals("Enron Corp", analysis.company);
		assertEquals("",analysis.keywords);
	}
	
	@Test(timeout=120000)		//two minutes or less
	public void testDifficultEnron5() throws Exception {
		InputStream is = getClass().getResourceAsStream("/bad_enron_5.xlsx");
		assertNotNull(is);
		AnalysisOutputAndFormulas aof = SpreadsheetAnalyzer.doAnalysisAndGetObjectAndFormulas(is, "[test]", "bad_enron_5.xlsx");
		AnalysisOutput analysis = aof.analysisObject;
		assertNotNull(analysis);
		assertEquals("OK", analysis.errorNotification);
		assertEquals(1227, aof.uniqueFormulas.size());
		assertEquals(56044, analysis.totalInputCells);
		assertEquals(0, analysis.booleanInputCells);
		assertEquals(14021, analysis.dateTimeInputCells);
		assertEquals(0, analysis.errorInputCells);
		assertEquals(34610, analysis.integerInputCells);
		assertEquals(0, analysis.nonIntegerInputCells);
		assertEquals(7413, analysis.stringInputCells);
		assertEquals(48570, analysis.totalReferencedInput);
		assertEquals(0, analysis.booleanReferencedInput);
		assertEquals(13960, analysis.dateReferencedInput);
		assertEquals(0, analysis.errorReferencedInput);
		assertEquals(34610, analysis.integerReferencedInput);
		assertEquals(0, analysis.nonIntegerReferencedInput);
		assertEquals(0, analysis.stringReferencedInput);
		assertEquals(83081, analysis.totalFormulas);
		assertEquals(0, analysis.booleanFormulas);
		assertEquals(0, analysis.dateTimeFormulas);
		assertEquals(0, analysis.errorFormulas);
		assertEquals(76963, analysis.integerFormulas);
		assertEquals(0, analysis.nonIntegerFormulas);
		assertEquals(6118, analysis.stringFormulas);
		assertEquals(0, analysis.blankFormulas);
		assertEquals(83081, analysis.formulaCellsReferencingOthers);
		assertEquals(82798, analysis.formulaCellsReferencedByOthers);
		assertEquals(1008, analysis.formulaCellsOccuringOnce);
		assertEquals(219, analysis.formulaCellsOccuring2Plus);
		assertEquals(218, analysis.formulaCellsOccuring5Plus);
		assertEquals(218, analysis.formulaCellsOccuring10Plus);
		assertEquals(218, analysis.formulaCellsOccuring25Plus);
		assertEquals(218, analysis.formulaCellsOccuring50Plus);
		assertEquals(8, analysis.formulaCellsOccuring100Plus);
		assertEquals(20841, analysis.mostFrequentFormulaCount);
		assertEquals("IF(R[0]C1=R[-1]C1,R[-1]C[0]+R[0]C[-1],R[0]C[-1])", analysis.mostFrequentFormula);
		assertEquals(0, analysis.numCharts);
		assertFalse(analysis.containsMacros);
		assertFalse(analysis.containsThirdPartyFunctions);
		assertEquals(27789, analysis.countPlus);
		assertEquals(6948, analysis.countMinus);
		assertEquals(0, analysis.countDivide);
		assertEquals(0, analysis.countMultiply);
		
		assertEquals(20490, analysis.countCONCATENATE);
		assertEquals(69121, analysis.countIF);
		assertEquals(13542, analysis.countISNA);
		assertEquals(6948, analysis.countMAX);
		assertEquals(61, analysis.countSUM);
		assertEquals(13542, analysis.countVLOOKUP);
		assertEquals(3, analysis.numSheets);
		assertNull(analysis.stackTrace);
		
		assertEquals("jwebb", analysis.createdBy);
		assertEquals("Felienne", analysis.lastModifiedBy);
		assertEquals("2001-10-28T04:41:41Z", analysis.createdDate);
		assertEquals("", analysis.lastPrintedDate);
		assertEquals("2014-09-03T20:03:56Z", analysis.lastModifiedDate);
		assertEquals("ect", analysis.company);
		assertEquals("",analysis.keywords);
	}
	
	@Test
	public void testDifficultFuse1() throws Exception {
		InputStream is = getClass().getResourceAsStream("/bad_fuse_1.xls");
		assertNotNull(is);
		AnalysisOutputAndFormulas aof = SpreadsheetAnalyzer.doAnalysisAndGetObjectAndFormulas(is, "[test]", "bad_fuse_1.xls");
		AnalysisOutput analysis = aof.analysisObject;
		assertNotNull(analysis);
		assertEquals("OK", analysis.errorNotification);
		assertEquals(1118, analysis.totalInputCells);
		assertEquals(340, analysis.integerInputCells);
		assertEquals(778, analysis.stringInputCells);
		assertEquals(0, analysis.totalFormulas);
	}
}
