package net.barik.spreadsheet.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONScanner extends AbstractScanner {

	private String corpusName;

	private File outputDirectory;

	private ObjectMapper mapper = new ObjectMapper();

	private boolean skipFormulas;

	private File formulasDirectory;
	
	public static void main(String[] args) {
		
		AbstractScanner scanner = new JSONScanner();
		scanner.scan(args);
	}
	
	public JSONScanner() {
		corpusName = System.getenv("BARIK_CORPUS_NAME");
		if (corpusName == null) {
			corpusName = "[none]";
		}
		String skipFormulasString = System.getenv("BARIK_SKIP_FORMULAS");
		if (skipFormulasString == null) {
			skipFormulas = true;
		} else {
			skipFormulas = Boolean.parseBoolean(skipFormulasString);
		}
	}

	@Override
	protected void parseSpreadSheet(File file) throws Exception {
		
		AnalysisOutputAndFormulas analysis = SpreadsheetAnalyzer.doAnalysisAndGetObjectAndFormulas(new FileInputStream(file), corpusName, file.getName());

		try (FileWriter outputWriter = new FileWriter(outputDirectory.getAbsolutePath() + "/" + file.getName());) {
			System.out.println("Writing to "+outputDirectory.getAbsolutePath() + "/" + file.getName());
			mapper.writeValue(outputWriter, analysis.analysisObject);
		}
		
		if (!skipFormulas) {
			try (FileWriter outputWriter = new FileWriter(formulasDirectory.getAbsolutePath() + "/" + file.getName());) {
				System.out.println("Writing formulas to "+formulasDirectory.getAbsolutePath() + "/" + file.getName());
				
				for(String f : analysis.uniqueFormulas) {
					outputWriter.write(f);
					outputWriter.write("\n");
				}
				
			}
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
		} catch (IOException e) {
			System.err.println("Could not write output to " + outputDirectory.getAbsolutePath());
			return false;
		}
		
		formulasDirectory = new File("formulas/");
		try {
			if (!formulasDirectory.exists()) {
				if (!formulasDirectory.mkdirs()) {
					throw new IOException();
				}
			} else if (!(formulasDirectory.delete() && formulasDirectory.mkdirs())) {
				throw new IOException();
			}
			return true;
		} catch (IOException e) {
			System.err.println("Could not write formulas to " + formulasDirectory.getAbsolutePath());
		}
		return false;
	}

}
