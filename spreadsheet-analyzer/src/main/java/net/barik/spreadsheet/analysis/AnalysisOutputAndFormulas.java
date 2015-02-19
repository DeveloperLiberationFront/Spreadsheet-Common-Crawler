package net.barik.spreadsheet.analysis;

import java.util.Set;

public class AnalysisOutputAndFormulas {

	public AnalysisOutput analysisObject;
	public Set<String> uniqueFormulas;

	public AnalysisOutputAndFormulas(AnalysisOutput analysisOutput, Set<String> uniqueFormulas) {
		this.analysisObject = analysisOutput;
		this.uniqueFormulas = uniqueFormulas;
	}


}
