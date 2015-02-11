package net.barik;

import java.util.Map;

import net.barik.SpreadsheetAnalyzer.FunctionEvalType;
import net.barik.SpreadsheetAnalyzer.InputCellType;

public class AnalysisOutput {
	private String fileName;
	private int sizeInBytes;
	
	private int totalInputCells;
	private int booleanInputCells;
	private int dateTimeInputCells;
	private int errorInputCells;
	private int integerInputCells;
	private int nonIntegerInputCells;
	private int stringInputCells;
	
	private int totalReferencedInput;
	private int booleanReferencedInput;
	private int dateReferencedInput;
	private int errorReferencedInput;
	private int integerReferencedInput;
	private int nonIntegerReferencedInput;
	private int stringReferencedInput;
	
	private int totalFormulas;
	private int booleanFormulas;
	private int dateTimeFormulas;
	private int errorFormulas;
	private int integerFormulas;
	private int nonIntegerFormulas;
	private int stringFormulas;
	private int blankFormulas;
	
	private int formulaCellsReferencingOthers;
	private int formulaCellsReferencedByOthers;
	
	private int formulaCellsOccuringOnce;
	private int formulaCellsOccuring2Plus;
	private int formulaCellsOccuring5Plus;
	private int formulaCellsOccuring10Plus;
	private int formulaCellsOccuring25Plus;
	private int formulaCellsOccuring50Plus;
	private int formulaCellsOccuring100Plus;
	
	private int mostFrequentFormulaCount;

	private String mostFrequentFormula;
	private int numCharts;
	private Boolean containsMacros;
	
	private int sumifCount;
	private int countifCount;
	private int chooseCount;
	private int hlookupCount;
	private int indexCount;
	private int indirectCount;
	private int lookupCount;
	private int matchCount;
	private int offsetCount;
	private int ifCount;
	
	public AnalysisOutput(String fileName,
			int sizeInBytes,
			Map<InputCellType, Integer> inputCounts,
			Map<InputCellType, Integer> inputReferences,
			Map<FunctionEvalType, Integer> formulaCells,
			int formulasReferencing,
			int formulasReferenced,
			int occur1,
			int occur2,
			int occur5,
			int occur10,
			int occur25,
			int occur50,
			int occur100,
			int mostFreqFormulaCount,
			String mostFreqFormula,
			int numCharts,
			Boolean containsMacro,
			Map<String, Integer> functionCounts
			){

		this.fileName = fileName; 
		
		this.sizeInBytes = sizeInBytes; 
		
		//INPUT CELLS
		this.totalInputCells = total(inputCounts);
		
		this.booleanInputCells = inputCounts.get(InputCellType.BOOLEAN);
		this.dateTimeInputCells = inputCounts.get(InputCellType.DATE);
		this.errorInputCells = inputCounts.get(InputCellType.ERROR);
		this.integerInputCells = inputCounts.get(InputCellType.INTEGER);
		this.nonIntegerInputCells = inputCounts.get(InputCellType.NON_INTEGER_NUMBER);
		this.stringInputCells = inputCounts.get(InputCellType.STRING);
		//-------------------------------------------------------
		
		
		//References
		this.totalReferencedInput = total(inputReferences);
		
		this.booleanReferencedInput = inputReferences.get(InputCellType.BOOLEAN);
		this.dateReferencedInput = inputReferences.get(InputCellType.DATE);
		this.errorReferencedInput = inputReferences.get(InputCellType.ERROR);
		this.integerReferencedInput = inputReferences.get(InputCellType.INTEGER);
		this.nonIntegerReferencedInput = inputReferences.get(InputCellType.NON_INTEGER_NUMBER);
		this.stringReferencedInput = inputReferences.get(InputCellType.STRING);
		//----------------------------------------------------------
		
		
		//Formula Eval Types
		this.totalFormulas = total(formulaCells);

		this.booleanFormulas = formulaCells.get(FunctionEvalType.BOOLEAN);
		this.dateTimeFormulas = formulaCells.get(FunctionEvalType.DATE);
		this.errorFormulas = formulaCells.get(FunctionEvalType.ERROR);
		this.integerFormulas = formulaCells.get(FunctionEvalType.INTEGER);
		this.nonIntegerFormulas = formulaCells.get(FunctionEvalType.NON_INTEGER_NUMBER);
		this.stringFormulas = formulaCells.get(FunctionEvalType.STRING);
		this.blankFormulas = formulaCells.get(FunctionEvalType.BLANK);
		//--------------------------------------------------------
		
		
		this.formulaCellsReferencingOthers = formulasReferencing;
		this.formulaCellsReferencedByOthers = formulasReferenced;
		
		this.formulaCellsOccuringOnce = occur1;
		this.formulaCellsOccuring2Plus = occur2;
		this.formulaCellsOccuring5Plus = occur5;
		this.formulaCellsOccuring10Plus = occur10;
		this.formulaCellsOccuring25Plus = occur25;
		this.formulaCellsOccuring50Plus = occur50;
		this.formulaCellsOccuring100Plus = occur100;
		
		this.mostFrequentFormulaCount = mostFreqFormulaCount;

		this.mostFrequentFormula= mostFreqFormula;
		this.numCharts = numCharts;
		this.containsMacros = containsMacro;
		
		this.sumifCount = functionCounts.get("SUMIF");
		this.countifCount = functionCounts.get("COUNTIF");
		this.chooseCount = functionCounts.get("CHOOSE");
		this.hlookupCount = functionCounts.get("HLOOKUP");
		this.indexCount = functionCounts.get("INDEX");
		this.indirectCount = functionCounts.get("INDIRECT");
		this.lookupCount = functionCounts.get("LOOKUP");
		this.matchCount = functionCounts.get("MATCH");
		this.offsetCount = functionCounts.get("OFFSET");
		this.ifCount = functionCounts.get("IF");

		
	}

	private static int total(Map<?, Integer> inputCounts) {
		int total = 0;
		for(Integer i: inputCounts.values()) {
			total+=i;
		}
		return total;

	}
}