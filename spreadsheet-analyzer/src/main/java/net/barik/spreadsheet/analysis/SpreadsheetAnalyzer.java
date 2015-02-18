package net.barik.spreadsheet.analysis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.hssf.usermodel.HSSFChart;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.filesystem.POIFSDocumentPath;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SpreadsheetAnalyzer {
	
	private Workbook workbook;
	
	private Map<SheetLocation, InputCellReferencePackage> inputCellByReferenceMap = new HashMap<>();
	private Map<String, Set<InputCellReferencePackage>> unreferencedInputCellMap = new HashMap<>();		//maps sheet name to inputCellPackage
	
	private Map<SheetLocation, CellReferencePackage> formulaCellByReferenceMap = new HashMap<>();
	private Map<String, Set<CellReferencePackage>> unreferencedFormulaCellMap = new HashMap<>();		//maps sheet name to formulaCellPackage
	
	private Map<InputCellType, Integer> inputCellCounts = new EnumMap<>(InputCellType.class);
	private Map<String, Integer> functionCounts = new HashMap<>();
	private Map<FunctionEvalType, Integer> evalTypeCounts = new EnumMap<>(FunctionEvalType.class);
	private boolean containsMacros = false;
	
	private final Pattern findFunctions = Pattern.compile("[A-Z][A-Z\\.0-9]*\\(");
	private Pattern findPotentialCellReferences;

	private Sheet currentSheet;

	private InputCellType lastInputCellType;

	private int formulasThatReferenceOtherCells;

	private Map<InputCellType, Integer> referencedInputCells = new EnumMap<>(InputCellType.class);
	
	private Map<String, Integer> r1c1FormulaToCountMap = new HashMap<>();
	
	private int formulasReferencedByOtherCells;

	private int sizeInBytes ;

	private String fileHash;

	private int numFormulasThatArePartOfArrayFormulaGroup;

	private SpreadsheetAnalyzer(Workbook wb) {
		setWorkBook(wb);
	}

	void setWorkBook(Workbook wb) {
		this.workbook = wb;
		
		compileReferenceRegexFromSheetNames(wb);
	}

	private void compileReferenceRegexFromSheetNames(Workbook wb) {
		//((\'?Sheet 1!\'?)|(\'?Sheet 2!\'?))?[A-Z0-9$]+(:[$A-Z0-9]+)?
		StringBuilder builder = new StringBuilder();
		StringBuilder sheetsBuilder = new StringBuilder();
		sheetsBuilder.append('(');
		for(int i = 0;i<wb.getNumberOfSheets();i++) {
			if (i != 0) {
				sheetsBuilder.append('|');
			}
			String sheetName = wb.getSheetName(i);
			sheetName = sheetName.replace("'","''");	//in our search, single quotes will be escaped by putting two of them together
			sheetsBuilder.append("(\\\'?");
			sheetsBuilder.append(Pattern.quote(sheetName)); //escapes things like () and \
			sheetsBuilder.append("\\\'?!)");
		}
		sheetsBuilder.append(")?");
		builder.append(sheetsBuilder);
		
		builder.append("[A-Z0-9$]+(:");
		builder.append(sheetsBuilder);
		builder.append("[$A-Z0-9]+)?");
		
		findPotentialCellReferences = Pattern.compile(builder.toString());
	}

	public static SpreadsheetAnalyzer doAnalysis(InputStream is) throws IOException, ParsingException {
		byte[] byteArray = readInInputStream(is);
		//can't use a bufferedInputStream and reset it because WorkbookFactory.create closes the stream
		InputStream inputStreamForWorkbook = new ByteArrayInputStream(byteArray); 
		InputStream inputStreamForMacro = new ByteArrayInputStream(byteArray); 
		InputStream inputStreamForHash = new ByteArrayInputStream(byteArray);
		 
		// Calculates the sha512 digest of the given InputStream object.
        // It will generate a 32 characters hex string.        
		String digest = DigestUtils.sha512Hex(inputStreamForHash);
		
		
		SpreadsheetAnalyzer analyzer;
		try {
			analyzer = new SpreadsheetAnalyzer(WorkbookFactory.create(inputStreamForWorkbook));
		} catch (InvalidFormatException e) {
			throw new ParsingException("Problem reading in the workbook", e);
		}
		
		
		analyzer.fileHash = digest;
		analyzer.sizeInBytes = byteArray.length;
		analyzer.analyzeEUSESMetrics(inputStreamForMacro);

		return analyzer;
	}
	
	public static AnalysisOutput doAnalysisAndGetObject(InputStream is, String corpusName, String identifier) {
		SpreadsheetAnalyzer analyzer = null;
		try {
			analyzer = doAnalysis(is);
			return new AnalysisOutput(corpusName, identifier,
					analyzer.getInputCellCounts(), 
					analyzer.getInputReferences(), 
					analyzer.getFormulaCellCounts(), 
					analyzer.getFormulaReferencingOtherCells(), 
					analyzer.getFormulasReferenced(), 
					analyzer.getFormulasUsedOnce(), 
					analyzer.getFormulasUsedNOrMoreTimes(2), 
					analyzer.getFormulasUsedNOrMoreTimes(5), 
					analyzer.getFormulasUsedNOrMoreTimes(10), 
					analyzer.getFormulasUsedNOrMoreTimes(25), 
					analyzer.getFormulasUsedNOrMoreTimes(50), 
					analyzer.getFormulasUsedNOrMoreTimes(100),
					analyzer.getMostTimesMostFrequentlyOccurringFormulaWasUsed(), 
					analyzer.getMostFrequentlyOccurringFormula(), 
					analyzer.getNumCharts(), 
					analyzer.containsMacros, 
					analyzer.getNumSheets(),
					analyzer.getNumFormulasThatArePartOfArrayFormulaGroup(),
					analyzer.getFunctionCounts());
		}
		catch (Exception e) {
			return new AnalysisOutput(corpusName, identifier,
					e.toString()+" : "+Arrays.toString(e.getStackTrace()));
		}
		finally {
			if (analyzer != null) {
				analyzer.close();
			}
		}
		
		
	}

	private int getNumSheets() {
		return workbook.getNumberOfSheets();
	}
	
	private int getNumFormulasThatArePartOfArrayFormulaGroup() {
		return numFormulasThatArePartOfArrayFormulaGroup;
	}

	private static byte[] readInInputStream(InputStream is) throws IOException {
		byte[] ba =  IOUtils.toByteArray(is);
		IOUtils.closeQuietly(is);
		return ba;
		
	}
	
	private static Integer incrementOrInitialize(Integer i) {
		if (i == null) {
			return 1;
		}
		return i + 1;
	}


	private void analyzeEUSESMetrics(InputStream inputStreamForMacroChecking) throws IOException {
		clearPreviousMetrics();

		checkForMacros(inputStreamForMacroChecking);
		
		findInputCells();
		
		findReferencedCells();
	}

	private void checkForMacros(InputStream inputStream) throws IOException {
		if (this.workbook instanceof XSSFWorkbook) {
			this.containsMacros = ((XSSFWorkbook) workbook).isMacroEnabled();
		} else if (POIFSFileSystem.hasPOIFSHeader(inputStream)) {
			// Looking at HSSF
			POIFSReader r = new POIFSReader();
			MacroListener ml = new MacroListener();
			r.registerListener(ml);
			r.read(inputStream);
			this.containsMacros = ml.isMacroDetected();
		}
		inputStream.close();
	}
			

	private void findInputCells() {
		for(int i = 0; i< workbook.getNumberOfSheets();i++) {
			currentSheet = workbook.getSheetAt(i);
				
				
			Iterator <Row> rowIterator = currentSheet.iterator();
			while(rowIterator.hasNext()){
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					this.lastInputCellType = null;

					switch(cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
					case Cell.CELL_TYPE_STRING:
					case Cell.CELL_TYPE_ERROR:
					case Cell.CELL_TYPE_NUMERIC:
						handleInputCell(cell);
						break;

					case Cell.CELL_TYPE_FORMULA:
						try {
							handleFormulas(cell);
						} catch (ParsingException e) {
							//ignore bad formulas
						}
						break;
					default:
						break;
					}

					if (lastInputCellType != null) {
						inputCellCounts.put(lastInputCellType,
								incrementOrInitialize(inputCellCounts.get(lastInputCellType)));

						InputCellReferencePackage inputCellPackage = new InputCellReferencePackage(cell, lastInputCellType);
						Set<InputCellReferencePackage> oldSet = unreferencedInputCellMap.get(currentSheet.getSheetName());
						if (oldSet == null) {
							oldSet = new HashSet<>();
							unreferencedInputCellMap.put(currentSheet.getSheetName(), oldSet);
						}
						
						oldSet.add(inputCellPackage);
						
						inputCellByReferenceMap.put(new SheetLocation(cell), inputCellPackage);
					}
					
				}

			}
		}
	}

	private FunctionEvalType getAndConvertCachedType(Cell cell){
		
		if (cell.isPartOfArrayFormulaGroup()) {
			this.numFormulasThatArePartOfArrayFormulaGroup++;
		}
		
		// this is better than checking cell.getCachedFormulaResultType(), which doesn't
		// handle arrayFormulaGroups
		try {
			String s = cell.getStringCellValue();		//do String first to ferret out blank cells
			if (s.isEmpty()) {
				return FunctionEvalType.BLANK;
			}
			return FunctionEvalType.STRING;
		} catch (IllegalStateException e) {
			//squash and move on
		}
		
		try {
			cell.getErrorCellValue();
			return FunctionEvalType.ERROR;
		} catch (IllegalStateException e) {
			//squash and move on
		}
		
		try {
			int cachedFormulaResultType = cell.getCachedFormulaResultType();
			if ( cachedFormulaResultType != Cell.CELL_TYPE_NUMERIC) {
				return FunctionEvalType.fromCellType(cachedFormulaResultType);
			}

		} catch (IllegalStateException e) {
			//squash and move on
		}
		
		try {
			double number = cell.getNumericCellValue();
			if (DateUtil.isCellDateFormatted(cell)) {
				return FunctionEvalType.DATE;
			}
			if (Math.rint(number) == number) {  //integer check from http://stackoverflow.com/a/9898613/1447621
	    		return FunctionEvalType.INTEGER;
	    	} else {
	    		return FunctionEvalType.NON_INTEGER_NUMBER;
	    	}
		} catch (IllegalStateException e) {
			//squash and move on
		}

		return FunctionEvalType.ERROR;			// we have no idea what this is
		
	}
	
	private void handleFormulas(Cell cell) throws ParsingException {
		addFormulaToReferenceMaps(cell);
		addFormulaToUniqueFormulas(cell);
		
    	//Formula cell evaluation type
		FunctionEvalType evaluatingType = getAndConvertCachedType(cell);
		if (evaluatingType != null){ //Null signals function or blank from call to fromCellType
			evalTypeCounts.put(evaluatingType,incrementOrInitialize(evalTypeCounts.get(evaluatingType)));
		}
		
		String cellFormula = null;
		try {
			 cellFormula = cell.getCellFormula();
		}
		catch (FormulaParseException fpe) {
			throw new ParsingException("Problem with cell formula", fpe);
		}
    	if (cellFormula.startsWith("#")) {
    		lastInputCellType = InputCellType.ERROR;
		} else {
			findFunctionsUsed(cellFormula);
			
		}
	}

	private void addFormulaToUniqueFormulas(Cell formulaCell) {
		String formulaString;
		try {
			formulaString = convertToR1C1(formulaCell);
		} catch (ParsingException e) {
			return;		//skip this because it was badly parsed
		}
		r1c1FormulaToCountMap.put(formulaString, incrementOrInitialize(r1c1FormulaToCountMap.get(formulaString)));
	}

	
	String convertToR1C1(Cell formulaCell) throws ParsingException {
		String cellFormula = null;
		try {
			 cellFormula = formulaCell.getCellFormula();
		}
		catch (FormulaParseException fpe) {
			throw new ParsingException("Problem with cell formula", fpe);
		}

		String adjustedFormula = cellFormula;
		
		Matcher m = findPotentialCellReferences.matcher(cellFormula);
		
		while (m.find()) {
			String maybeCell = m.group();
			try {
				if (maybeCell.length() <= 1 || isInQuotes(m.start(), cellFormula)) {
					continue;
				}
				//look for colon to detect range
				if (maybeCell.indexOf(':') == -1) {
					if (maybeCell.matches("[A-Z]+")) {	// skip LOG, SUM and other functions
						continue;
					}
					CellReference cr = new CellReference(maybeCell);
					
					String cellReference = convertToR1C1(cr, formulaCell);
					String sheetReference = cr.getSheetName();
					if (sheetReference == null) {
						 sheetReference = "";
					} else {
						sheetReference += "!";
					}
					
					String convertedReference = String.format("%s%s", sheetReference, cellReference);
					
					adjustedFormula = adjustedFormula.replace(maybeCell, convertedReference);
				}
				else {
					CellReferencePair cellRange = parseCellRange(maybeCell);
					
					String sheetReference = cellRange.first.getSheetName();
					if (sheetReference == null) {
						 sheetReference = "";
					} else {
						sheetReference += "!";
					}
					
					String firstPointInRange = convertToR1C1(cellRange.first, formulaCell);
					String secondPointInRange = convertToR1C1(cellRange.second, formulaCell);
					
					String convertedReference;
					if (firstPointInRange.equals(secondPointInRange)) {
						//it's a single row or single column
						convertedReference = String.format("%s%s", sheetReference, 
								firstPointInRange);
					}else {
						convertedReference = String.format("%s%s:%s", sheetReference, 
								firstPointInRange,
								secondPointInRange);
					}
					
					adjustedFormula = adjustedFormula.replace(maybeCell, convertedReference);
					
				}
			} catch (Exception e) {
				System.out.println("Making formula unique failed for " + maybeCell);
			}
		}
		
		return adjustedFormula;
	}

	private boolean isInQuotes(int start, String cellFormula) {
		boolean inDoubleQuotes = false;
		boolean inSingleQuotes = false;
		for(int i = 0;i<start;i++) {
			if (cellFormula.charAt(i) == '"') {
				inDoubleQuotes = !inDoubleQuotes;
			}
			else if (cellFormula.charAt(i) == '\'') {
				inSingleQuotes = !inSingleQuotes;
			}
		}
		return inDoubleQuotes || inSingleQuotes;
	}

	private CellReferencePair parseCellRange(String ref) {
		int sep = ref.indexOf(":");
        CellReference a;
        CellReference b;
        if (sep == -1) {
            a = new CellReference(ref);
            b = a;
        } else {
            a = new CellReference(ref.substring(0, sep));
            b = new CellReference(ref.substring(sep + 1));
        }
        return new CellReferencePair(a,b);
	}

	private String convertToR1C1(CellReference cr, Cell startingCell) {
		boolean isRowOnly = false, isColOnly = false;
		
		int col = cr.getCol();
		if (col == -1) {
			isRowOnly = true;
		} else if (cr.isColAbsolute()) {
			col += 1;		//0 indexed, converting to 1 indexed
		} else {
			col -= startingCell.getColumnIndex(); //both are 0 indexed
		}
		
		int row = cr.getRow();		//we must compute col only and then row only because of the 
		if (row == -1) {			//absolute glitch referenced below.
			isColOnly = true;
		} else if (cr.isRowAbsolute() || (isRowOnly && cr.isColAbsolute())) {
			row += 1;		//0 indexed, converting to 1 indexed
		} else {
			row -= startingCell.getRowIndex(); //both are 0 indexed
		}

		if (isColOnly) {
			//there appears to be a glitch with Apache POI that thinks $5 in 5:$5
			// makes the column absolute, despite it being a row.
			return String.format("C%s%d%s", 
					cr.isColAbsolute() || cr.isRowAbsolute() ? "" : "[",
					col,
					cr.isColAbsolute() || cr.isRowAbsolute() ? "" : "]"
					);
		} else if (isRowOnly) {
			return String.format("R%s%d%s", 
					cr.isRowAbsolute() || cr.isColAbsolute() ? "" : "[",
					row,
					cr.isRowAbsolute() || cr.isColAbsolute() ? "" : "]"
					);
		}
		
		return String.format("R%s%d%sC%s%d%s", 
				cr.isRowAbsolute() ? "" : "[",
				row,
				cr.isRowAbsolute() ? "" : "]",
				cr.isColAbsolute() ? "" : "[",
				col,
				cr.isColAbsolute() ? "" : "]"
				);

	}

	private void addFormulaToReferenceMaps(Cell cell) {
		CellReferencePackage inputCellPackage = new CellReferencePackage(cell);
		Set<CellReferencePackage> oldSet = unreferencedFormulaCellMap.get(currentSheet.getSheetName());
		if (oldSet == null) {
			oldSet = new HashSet<>();
			unreferencedFormulaCellMap.put(currentSheet.getSheetName(), oldSet);
		}
		oldSet.add(inputCellPackage);
		formulaCellByReferenceMap.put(new SheetLocation(cell), inputCellPackage);
	}

	private void findFunctionsUsed(String functionString) {
		Matcher m = findFunctions.matcher(functionString);
		while(m.find()) {
			String function = m.group();
			if (isInQuotes(m.start(), functionString)) {
				continue;
			}
			function = function.substring(0, function.length()-1);
			functionCounts.put(function, incrementOrInitialize(functionCounts.get(function)));
		}
	}

	private void handleInputCell(Cell cell) {
		
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				lastInputCellType = InputCellType.DATE;
				return;
			}
			double d = cell.getNumericCellValue();
	    	if (Math.rint(d) == d) {  //integer check from http://stackoverflow.com/a/9898613/1447621
	    		lastInputCellType = InputCellType.INTEGER;
	    	} else {
	    		lastInputCellType = InputCellType.NON_INTEGER_NUMBER;
	    	}
		} else {
			lastInputCellType = InputCellType.fromCellType(cell.getCellType());
		}
	}

	private void clearPreviousMetrics() {
		functionCounts.clear();
		inputCellCounts.clear();
		unreferencedInputCellMap.clear();
		unreferencedFormulaCellMap.clear();
		evalTypeCounts.clear();
		inputCellByReferenceMap.clear();
		r1c1FormulaToCountMap.clear();	
		formulaCellByReferenceMap.clear();
		containsMacros = false;
		currentSheet = null;
		lastInputCellType=null;
		formulasThatReferenceOtherCells=0;
		referencedInputCells.clear();	
		formulasReferencedByOtherCells=0;
		sizeInBytes = 0;
		fileHash = "";
		numFormulasThatArePartOfArrayFormulaGroup = 0;	
		formulasThatReferenceOtherCells = 0;
		formulasReferencedByOtherCells = 0;
		numFormulasThatArePartOfArrayFormulaGroup = 0;
	}

	private void findReferencedCells() {
		// pass through all formula cells again and look for references
		for(int i = 0; i< workbook.getNumberOfSheets();i++) {
			currentSheet = workbook.getSheetAt(i);
			Iterator <Row> rowIterator = currentSheet.iterator();
			while(rowIterator.hasNext()){
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					if(cell.getCellType()==Cell.CELL_TYPE_FORMULA) {
						try {
							processFormulaReferences(cell);
						} catch (ParsingException e) {
							//ignore bad formulas
							continue;
						}
					}
				}
			}
		}
		condenseInputCellReferencesFromAllSheets();
		countFormulaCellsReferenced();
	}

	private void countFormulaCellsReferenced() {
		for(CellReferencePackage formulaPackage : formulaCellByReferenceMap.values()) {
			if (formulaPackage.isReferenced)
				formulasReferencedByOtherCells++;
		}
		
	}

	private void processFormulaReferences(Cell cell) throws ParsingException {
		String formula = null;
		try {
			 formula = cell.getCellFormula();
		}
		catch (FormulaParseException fpe) {
			throw new ParsingException("Problem with cell formula", fpe);
		}
		//we look for anything that might be a cell reference and use
		// POI's parsers to see if they are valid references
		Matcher m = findPotentialCellReferences.matcher(formula);
		
		boolean wasThereAReference = false;
		while (m.find()) {
			String maybeCell = m.group();
			
			if (maybeCell.length() <= 1 || isInQuotes(m.start(), formula)) {		//skip quoted things
				continue;
			}
			try {
				//look for colon to detect range
				if (maybeCell.indexOf(':') == -1) {
					if (maybeCell.matches("([A-Z]+)||([0-9]+)")) {	// skip LOG, SUM and other functions and numbers
						continue;
					}
					CellReference cr = new CellReference(maybeCell);
					wasThereAReference = true;
					
					checkInputCellReferences(cr);
					checkFormulaCellReferences(cr);
				}
				else {
					CellReferencePair crp = parseCellRange(maybeCell);				//to get the sheet name			
					CellRangeAddress cra = CellRangeAddress.valueOf(maybeCell);
					wasThereAReference = true;
					
					String sheetName = crp.first.getSheetName();
					if (sheetName == null) {
						// we are in the current sheet.
						sheetName = cell.getSheet().getSheetName();
					}
					
					checkInputCellReferences(cra, sheetName);
					checkFormulaCellReferences(cra, sheetName);
				}
			} catch (Exception e) {
				System.out.println("Processing formula references failed for " + maybeCell);
			}
		}
		if (wasThereAReference) {
			formulasThatReferenceOtherCells++;
		}
	}

	private void checkInputCellReferences(CellRangeAddress cra, String sheetName) {
		checkReferences(cra, unreferencedInputCellMap.get(sheetName));
	}
	
	private void checkFormulaCellReferences(CellRangeAddress cra, String sheetName) {
		checkReferences(cra, unreferencedFormulaCellMap.get(sheetName));
	}

	private void checkReferences(CellRangeAddress cra, Set<? extends CellReferencePackage> set) {
		if (set != null) {
			for (Iterator<? extends CellReferencePackage> iterator = set.iterator(); iterator.hasNext();) {
				CellReferencePackage p = iterator.next();
				if (cra.isInRange(p.rowIndex, p.colIndex)) {
					p.isReferenced = true;
					iterator.remove();
				}
			}
		}
	}

	private void checkInputCellReferences(CellReference cr) {
		CellReferencePackage p = inputCellByReferenceMap.get(new SheetLocation(cr, currentSheet));
		if (p != null) {
			p.isReferenced = true;
		}
	}
	
	private void checkFormulaCellReferences(CellReference cr) {
		CellReferencePackage p = formulaCellByReferenceMap.get(new SheetLocation(cr, currentSheet));
		if (p != null) {
			p.isReferenced = true;
		}
	}

	private void condenseInputCellReferencesFromAllSheets() {
		referencedInputCells = new EnumMap<>(InputCellType.class);
		
		for(InputCellReferencePackage p : inputCellByReferenceMap.values()) {
			if (p.isReferenced) {
				referencedInputCells.put(p.type, incrementOrInitialize(referencedInputCells.get(p.type)));
			}
		}
	}
	
	public String getFileHash(){
		return fileHash;
	}

	public boolean getContainsMacro(){
		return containsMacros;
	}
	
	public Map<String, Integer> getFunctionCounts() {
		return functionCounts;
	}

	public int getSizeInBytes() {
		return sizeInBytes;
	}

	public Map<InputCellType, Integer> getInputCellCounts() {
		return inputCellCounts;
	}
	
	public Map<FunctionEvalType, Integer> getFormulaCellCounts() {
		return evalTypeCounts; 
	}


	public Map<InputCellType, Integer> getInputReferences() {		
		return referencedInputCells;
	}

	public enum FunctionEvalType {
		//Includes Blank
		INTEGER,BOOLEAN,DATE,ERROR,NON_INTEGER_NUMBER,STRING, BLANK;

		public static FunctionEvalType fromCellType(int cellType) {
			switch (cellType) {
			case Cell.CELL_TYPE_BOOLEAN:
				return BOOLEAN;
			case Cell.CELL_TYPE_ERROR:
				return ERROR;
			case Cell.CELL_TYPE_STRING:
				return STRING;
			case Cell.CELL_TYPE_BLANK:
				return BLANK;
			}
			return null;
		}
	}
	
	public enum InputCellType {
		INTEGER,BOOLEAN,DATE,ERROR,NON_INTEGER_NUMBER,STRING;

		public static InputCellType fromCellType(int cellType) {
			switch (cellType) {
			case Cell.CELL_TYPE_BOOLEAN:
				return BOOLEAN;
			case Cell.CELL_TYPE_ERROR:
				return ERROR;
			case Cell.CELL_TYPE_STRING:
				return STRING;
			}
			return null;
		}
	}

	private static class CellReferencePackage {
		public Cell cell;
		public boolean isReferenced;
		public int rowIndex;
		public int colIndex;
		
		
		public CellReferencePackage(Cell cell) {
			this.cell = cell;
			this.rowIndex = cell.getRowIndex();
			this.colIndex = cell.getColumnIndex();
		}

	}
	
	private static class InputCellReferencePackage extends CellReferencePackage {
		public InputCellType type;
		
		public InputCellReferencePackage(Cell cell, InputCellType type) {
			super(cell);
			this.type = type;
		}
	}
	
	private static class SheetLocation {
		private final String s; 

		public SheetLocation(Cell c) {
			s = c.getSheet().getSheetName() +"!"+ c.getColumnIndex() +","+ c.getRowIndex();
		}

		public SheetLocation(CellReference cr, Sheet currentSheet) {
			String sheetName = cr.getSheetName();
			if (sheetName == null) {		//currentSheet is only used if sheetName is null
				sheetName = currentSheet.getSheetName();
			}
			s = sheetName +"!"+ cr.getCol() +","+cr.getRow();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			return prime * result + ((s == null) ? 0 : s.hashCode());
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SheetLocation other = (SheetLocation) obj;
			if (s == null) {
				if (other.s != null)
					return false;
			} else if (!s.equals(other.s))
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return s;
		}
			
	}
	
	public static class MacroListener implements POIFSReaderListener {
		//From http://www.rgagnon.com/javadetails/java-detect-if-xls-excel-file-contains-a-macro.html
		boolean macroDetected = false;

		public boolean isMacroDetected() {
			return macroDetected;
		}

		public void processPOIFSReaderEvent(POIFSReaderEvent event) {
			POIFSDocumentPath path = event.getPath();
			if (path.length() == 0) {
				return;
			}
			String firstFolder = path.getComponent(0);
			if(firstFolder.startsWith("Macro") || firstFolder.startsWith("_VBA")) {
				macroDetected = true;
			}
		}
	}
	private static class CellReferencePair {
		public final CellReference first, second;
		public CellReferencePair(CellReference first, CellReference second) {
			this.first = first;
			this.second = second;
		}
		
	}

	public int getFormulaReferencingOtherCells() {
		return formulasThatReferenceOtherCells;
	}

	public int getFormulasReferenced() {
		return formulasReferencedByOtherCells;
	}

	public int getFormulasUsedOnce() {
		int formulasUsedOnce = 0;
		for (Integer value : r1c1FormulaToCountMap.values()) {
			if (value.intValue() == 1) {
				formulasUsedOnce++;
			}
		}
		return formulasUsedOnce;
	}
	
	public int getFormulasUsedMoreThanOnce() {
		return getFormulasUsedNOrMoreTimes(2);
	}
	
	public int getFormulasUsedNOrMoreTimes(int n) {
		int formulasNOrMoreTimes = 0;
		for (Integer value : r1c1FormulaToCountMap.values()) {
			if (value.intValue() >= n) {
				formulasNOrMoreTimes++;
			}
		}
		return formulasNOrMoreTimes;
	}
	public String getMostFrequentlyOccurringFormula() {
		int targetN = getMostTimesMostFrequentlyOccurringFormulaWasUsed();
		for(Entry<String, Integer> s: r1c1FormulaToCountMap.entrySet()) {
			if (s.getValue() == targetN) {
				return s.getKey();
			}
		}
		return "[Could not find Most Frequently OccurringFormula]";
	}

	public int getMostTimesMostFrequentlyOccurringFormulaWasUsed() {
		Collection<Integer> c = r1c1FormulaToCountMap.values();
		if (c.isEmpty()) {
			return 0;
		}
		return Collections.max(c);
	}
	
	public boolean containsChart() {
		return getNumCharts() > 0;
	}

	public int getNumCharts() {
		int numCharts = 0;
		if (workbook instanceof XSSFWorkbook) {
			XSSFWorkbook xWorkbook = (XSSFWorkbook) workbook;
			for (XSSFSheet sheet : xWorkbook) {
				numCharts += countCharts(sheet);
			}
		}
		//Check for charts in xls 
		else if (workbook instanceof HSSFWorkbook){
			HSSFWorkbook hWorkbook = (HSSFWorkbook) workbook;
			for (int i = 0; i < hWorkbook.getNumberOfSheets(); i++){
				HSSFSheet sheet = hWorkbook.getSheetAt(i);
				numCharts += HSSFChart.getSheetCharts(sheet).length;
			}
		}
		

		return numCharts;
	}

	private int countCharts(XSSFSheet sheet) {
		int numCharts = 0;
		for(POIXMLDocumentPart p : sheet.getRelations()){
            if(p instanceof XSSFDrawing) {
                numCharts += ((XSSFDrawing) p).getCharts().size();
                break;
            }
        }
		return numCharts;
	}

	public void close() {
		try {
			this.clearPreviousMetrics();
			this.workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static class ParsingException extends Exception {

		private static final long serialVersionUID = -3707695603582413832L;

		public ParsingException() {
			super();
		}

		public ParsingException(String arg0, Throwable arg1) {
			super(arg0, arg1);
		}

		public ParsingException(String arg0) {
			super(arg0);
		}

		public ParsingException(Throwable arg0) {
			super(arg0);
		}
	
	}


}
