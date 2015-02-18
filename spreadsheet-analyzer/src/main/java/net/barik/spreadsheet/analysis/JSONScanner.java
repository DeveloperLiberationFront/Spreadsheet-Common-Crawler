package net.barik.spreadsheet.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONScanner extends AbstractScanner {

	private File outputDirectory;

	ObjectMapper mapper = new ObjectMapper();
	
	public static void main(String[] args) {
		AbstractScanner scanner = new JSONScanner();
		scanner.scan(args);
	}

	@Override
	protected void parseSpreadSheet(File file) throws Exception {
		AnalysisOutput analysis = SpreadsheetAnalyzer.doAnalysisAndGetObject(new FileInputStream(file), "[none]", file.getName());

		try (FileWriter outputWriter = new FileWriter(outputDirectory.getAbsolutePath() + "/" + file.getName());) {
			System.out.println("Writing to "+outputDirectory.getAbsolutePath() + "/" + file.getName());
			mapper.writeValue(outputWriter, analysis);
		}
       
	}

	@Override
	protected boolean setupOutputFile(File dirToScan) {
		outputDirectory = new File("output/");
		try {
			if (!outputDirectory.exists()) {
				if (!outputDirectory.mkdirs()) {
					throw new IOException();
				}
			} else if (!(outputDirectory.delete() && outputDirectory.mkdirs())) {
				throw new IOException();
			}
			return true;
		} catch (IOException e) {
			System.err.println("Could not write output to " + outputDirectory.getAbsolutePath());
		}
		return false;
	}

}
