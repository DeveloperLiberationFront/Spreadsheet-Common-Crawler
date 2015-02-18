package net.barik.spreadsheet.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import net.barik.spreadsheet.analysis.SpreadsheetAnalyzer;
import net.barik.spreadsheet.analysis.SpreadsheetAnalyzer.FunctionEvalType;
import net.barik.spreadsheet.analysis.SpreadsheetAnalyzer.InputCellType;

/**
 * Runs analysis of a folder and spits out a csv with the meta data.
 *
 */
public class CSVScanner extends AbstractScanner {

	private File outputFile;
	private FileWriter csvWriter;

	public static void main(String[] args) {
		AbstractScanner scanner = new CSVScanner();
		scanner.scan(args);
	}

	@Override
	protected boolean setupOutputFile(File dirToScan) {
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
			return true;
		} catch (IOException e) {
			System.err.println("Could not write output to " + outputFile.getAbsolutePath());
		}
		return false;
	}

	@Override
	protected void parseSpreadSheet(File file) throws Exception {
		SpreadsheetAnalyzer analyzer = SpreadsheetAnalyzer.doAnalysis(new FileInputStream(file));
		try {
			writeOutAnalysisLine(file.getParent() + File.separator + file.getName(), analyzer);
		} finally {
			analyzer.close();
		}
	}

	private void printFirstLine() throws IOException {
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
				+ "Number of charts,Contains vba macros,SUMIF,COUNTIF,CHOOSE,HLOOKUP,INDEX,INDIRECT,LOOKUP," + "MATCH,OFFSET,IF");
		csvWriter.write("\n");
		csvWriter.flush();
	}

	private StringBuilder thisLine;

	private void writeOutAnalysisLine(String fileName, SpreadsheetAnalyzer analyzer) throws IOException {
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

	private int total(Map<?, Integer> inputCounts) {
		int total = 0;
		for (Integer i : inputCounts.values()) {
			total += i;
		}
		return total;
	}

	private void append(int i) {
		thisLine.append(Integer.toString(i));
		thisLine.append(',');
	}

	private void append(Integer i) {
		thisLine.append(i == null ? "0" : i.toString());
		thisLine.append(',');
	}

	private void append(String s) {
		thisLine.append('"'); // surround with quotes
		thisLine.append(s.replace("\"", "\"\"")); // escape quotes by making
													// them double quotes
		thisLine.append('"');
		thisLine.append(',');
	}

	private void append(Object o) {
		thisLine.append(o);
		thisLine.append(',');
	}

}
