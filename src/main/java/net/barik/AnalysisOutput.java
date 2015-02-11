package net.barik;

public class AnalysisOutput {
	public int sizeInBytes;
	
	public int totalInputCells;
	public int booleanInputCells;
	public int dateTimeInputCells;
	public int errorInputCells;
	public int integerInputCells;
	public int nonIntegerInputCells;
	public int stringInputCells;
	
	public int totalReferencedInput;
	public int booleanReferencedInput;
	public int dateReferencedInput;
	public int errorReferencedInput;
	public int integerReferencedInput;
	public int nonIntegerReferencedInput;
	public int stringReferencedInput;
	
	public int totalFormulas;
	public int booleanFormulas;
	public int dateTimeFormulas;
	public int errorFormulas;
	public int integerFormulas;
	public int nonIntegerFormulas;
	public int stringFormulas;
	public int blankFormulas;
	
	public int formulaCellsReferencingOthers;
	public int formulaCellsReferencedByOthers;
	
	public int formulaCellsOccuringOnce;
	public int formulaCellsOccuring2Plus;
	public int formulaCellsOccuring5Plus;
	public int formulaCellsOccuring10Plus;
	public int formulaCellsOccuring25Plus;
	public int formulaCellsOccuring50Plus;
	public int formulaCellsOccuring100Plus;
	
	public int mostFrequentFormulaCount;

	public String mostFrequentFormula;
	public int numCharts;
	public Boolean containsMacros;
	
	public int sumifCount;
	public int countifCount;
	public int chooseCount;
	public int hlookupCount;
	public int indexCount;
	public int indirectCount;
	public int lookupCount;
	public int matchCount;
	public int offsetCount;
	public int ifCount;
	
}
