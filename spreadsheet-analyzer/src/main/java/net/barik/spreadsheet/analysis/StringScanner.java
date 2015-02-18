package net.barik.spreadsheet.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class StringScanner extends AbstractScanner {

	private File outputFile;
	private FileWriter outputWriter;
	
	public static void main(String[] args) {
		AbstractScanner scanner = new StringScanner();
		scanner.scan(args);
	}

	@Override
	protected void parseSpreadSheet(File file) throws Exception {
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(new FileInputStream(file), "[none]", file.getName());
		outputWriter.write(analysis.toString());
		outputWriter.write("\n");
		outputWriter.flush();
	}

	@Override
	protected boolean setupOutputFile(File dirToScan) {
		outputFile = new File(dirToScan, "output.txt");
		try {
			if (!outputFile.exists()) {
				if (!outputFile.createNewFile()) {
					throw new IOException();
				}
			} else if (!(outputFile.delete() && outputFile.createNewFile())) {
				throw new IOException();
			}
			outputWriter = new FileWriter(outputFile);
			return true;
		} catch (IOException e) {
			System.err.println("Could not write output to " + outputFile.getAbsolutePath());
		}
		return false;
	}

}
