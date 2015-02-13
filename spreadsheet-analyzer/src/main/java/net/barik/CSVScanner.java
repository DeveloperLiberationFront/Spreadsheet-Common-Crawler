package net.barik;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import net.barik.SpreadsheetAnalyzer;
import net.barik.SpreadsheetAnalyzer.FunctionEvalType;
import net.barik.SpreadsheetAnalyzer.InputCellType;

/**
 * Runs analysis of a folder and spits out a csv with the meta data.
 *
 */
public class CSVScanner {

	private static File outputFile;
	private static FileWriter csvWriter;
	private static int progress = 0;

	public static void main(String[] args) {
		File dirToScan = new File("C:/euses/");
		if (args.length > 0) {
			dirToScan = new File(args[0]);
		} 
		
		if (!(dirToScan.exists() && dirToScan.isDirectory())) {
			System.err.println(dirToScan.getAbsolutePath() +" is not a readable directory");
			return;
		}
		
		outputFile = new File(dirToScan, "output.csv");
		try {
			if (!outputFile.exists()) {
				if (!outputFile.createNewFile()) {
					throw new IOException();
				}
			} else if (!(outputFile.delete() && outputFile.createNewFile())) {
				throw new IOException();
			}
			printFirstLine();
		} catch (IOException e) {
			System.err.println("Could not write output to " + outputFile.getAbsolutePath());
			return;
		}
		
		scanDirectory(dirToScan);
		//scanOneFile("C:\\euses\\database\\processed\\comprehensiveschool.xls");
		//scanOneFile("C:\\euses\\database\\bad\\ContingencyMgmtPlanner.xls");
	}

	protected static void scanDirectory(File dirToScan) {
		System.out.println("Scanning " + dirToScan.getAbsolutePath() +" to "+outputFile.getAbsolutePath());
		
		scanFiles(dirToScan.listFiles());
	}

	protected static void scanOneFile(String file) {
		try {
			parseSpreadSheet(new File(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void scanFiles(File[] files){
		if (files == null) {
			return;
		}
	    for (File file : files) {
	        if (file.isDirectory()) {
	        	if ("duplicates".equals(file.getName())) {
	        		continue;
	        	}
	            scanFiles(file.listFiles()); // Calls same method again.
	        } else {
	            try {
	            	System.out.println("Scanning "+file.getAbsolutePath());
					parseSpreadSheet(file);
					progress++;
					if (progress % 100 == 0) {
						System.out.println("Scanned "+progress);
					}
				} catch (Exception e) {
					System.out.println("Could not analyze " + file);
					e.printStackTrace();
				}
	        }
	    }
	}

	private static void parseSpreadSheet(File file) throws IOException {
		SpreadsheetAnalyzer analyzer = SpreadsheetAnalyzer.doEUSESAnalysis(new FileInputStream(file));
		try {
			writeOutAnalysisLine(file.getParent()+File.separator+file.getName(), analyzer);
		} finally {
			analyzer.close();
		}
	}

	private static void printFirstLine() throws IOException {
		csvWriter = new FileWriter(outputFile);
		csvWriter.write("File Name,Size (bytes),Total Input Cells,Boolean Input Cells,Date Input Cells,"
				+ "Error Input Cells,Integer Input Cells,Non-Integer Input Cells,String Input Cells,"
				+ "Total Referenced Input Cells,Boolean Referenced Input Cells,Date Referenced Input Cells,"
				+ "Error Referenced Input Cells,Integer Referenced Input Cells,Non-Integer Referenced Input Cells,"
				+ "String Referenced Input Cells,Total Formula Cells,Formula Cells (Boolean),Formula Cells (Date),"
				+ "Formula Cells (Error),Formula Cells (Integer),Formula Cells (Non-Integer),Formula Cells (String),"
				+ "Formula Cells (Blank),Number of Formula Cells Referencing other cells,"
				+ "Number of Formula Cells Referenced by other cells,Number of Formulas occurring once,"
				+ "Number of Formulas occurring two or more times,Number of Formulas occurring five or more times,"
				+ "Number of Formulas occurring ten or more times,Number of Formulas occurring twenty-five or more times,"
				+ "Number of Formulas occurring fifty or more times,Number of Formulas occurring 100 or more times,"
				+ "Number of times the most frequently occurring formula occurs,Most frequently occurring formula,"
				+ "Number of charts,Contains vba macros,SUMIF,COUNTIF,CHOOSE,HLOOKUP,INDEX,INDIRECT,LOOKUP,"
				+ "MATCH,OFFSET,IF");
		csvWriter.write("\n");
		csvWriter.flush();
	}

	private static StringBuilder thisLine;
	private static void writeOutAnalysisLine(String fileName, SpreadsheetAnalyzer analyzer) throws IOException {
		thisLine = new StringBuilder();

		append(fileName);
		append(analyzer.getSizeInBytes());
		
		Map<InputCellType, Integer> inputCounts = analyzer.getInputCellCounts();
		append(total(inputCounts));
		append(inputCounts.get(InputCellType.BOOLEAN));
		append(inputCounts.get(InputCellType.DATE));
		append(inputCounts.get(InputCellType.ERROR));
		append(inputCounts.get(InputCellType.INTEGER));
		append(inputCounts.get(InputCellType.NON_INTEGER_NUMBER));
		append(inputCounts.get(InputCellType.STRING));
		
		Map<InputCellType, Integer> inputReferences = analyzer.getInputReferences();
		append(total(inputReferences));
		append(inputReferences.get(InputCellType.BOOLEAN));
		append(inputReferences.get(InputCellType.DATE));
		append(inputReferences.get(InputCellType.ERROR));
		append(inputReferences.get(InputCellType.INTEGER));
		append(inputReferences.get(InputCellType.NON_INTEGER_NUMBER));
		append(inputReferences.get(InputCellType.STRING));
		
		Map<FunctionEvalType, Integer> formulaCells = analyzer.getFormulaCellCounts();
		append(total(formulaCells));
		append(formulaCells.get(FunctionEvalType.BOOLEAN));
		append(formulaCells.get(FunctionEvalType.DATE));
		append(formulaCells.get(FunctionEvalType.ERROR));
		append(formulaCells.get(FunctionEvalType.INTEGER));
		append(formulaCells.get(FunctionEvalType.NON_INTEGER_NUMBER));
		append(formulaCells.get(FunctionEvalType.STRING));
		append(formulaCells.get(FunctionEvalType.BLANK));
		
		append(analyzer.getFormulaReferencingOtherCells());
		append(analyzer.getFormulasReferenced());
		
		append(analyzer.getFormulasUsedOnce());
		append(analyzer.getFormulasUsedNOrMoreTimes(2));
		append(analyzer.getFormulasUsedNOrMoreTimes(5));
		append(analyzer.getFormulasUsedNOrMoreTimes(10));
		append(analyzer.getFormulasUsedNOrMoreTimes(25));
		append(analyzer.getFormulasUsedNOrMoreTimes(50));
		append(analyzer.getFormulasUsedNOrMoreTimes(100));
		
		append(analyzer.getMostTimesMostFrequentlyOccurringFormulaWasUsed());
		append(analyzer.getMostFrequentlyOccurringFormula());
		
		append(analyzer.getNumCharts());
		append(analyzer.getContainsMacro());
		
		Map<String, Integer> functionCounts = analyzer.getFunctionCounts();
		append(functionCounts.get("SUMIF"));
		append(functionCounts.get("COUNTIF"));
		append(functionCounts.get("CHOOSE"));
		append(functionCounts.get("HLOOKUP"));
		append(functionCounts.get("INDEX"));
		append(functionCounts.get("INDIRECT"));
		append(functionCounts.get("LOOKUP"));
		append(functionCounts.get("MATCH"));
		append(functionCounts.get("OFFSET"));
		append(functionCounts.get("IF"));
		
		thisLine.append('\n');
		csvWriter.write(thisLine.toString());
		csvWriter.flush();

	}

	private static int total(Map<?, Integer> inputCounts) {
		int total = 0;
		for(Integer i: inputCounts.values()) {
			total+=i;
		}
		return total;
	}

	private static void append(int i) {
		thisLine.append(Integer.toString(i));
		thisLine.append(',');
	}
	
	private static void append(Integer i) {
		thisLine.append(i == null ? "0" : i.intValue());
		thisLine.append(',');
	}

	private static void append(String s) {
		thisLine.append('"');			//surround with quotes
		thisLine.append(s.replace("\"","\"\""));		//escape quotes by making them double quotes
		thisLine.append('"');
		thisLine.append(',');
	}
	
	private static void append(Object o) {
		thisLine.append(o);
		thisLine.append(',');
	}
	

	
}
