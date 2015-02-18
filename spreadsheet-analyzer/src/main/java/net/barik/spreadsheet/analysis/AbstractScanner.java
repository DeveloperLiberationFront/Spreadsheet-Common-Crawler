package net.barik.spreadsheet.analysis;

import java.io.File;

public abstract class AbstractScanner {

	private int progress = 0;

	protected void scan(String[] args) {
		File dirOrFileToScan;
		if (args.length > 0) {
			dirOrFileToScan = new File(args[0]);
		}
		else {
			System.err.println("You must specify a directory or file to scan");
			System.err.println("Arguments: directoryOrFile [singleFile?]");
			System.err.println("If a second argument is specified, the first argument will be treated as a single file to scan");
			return;
		}
	
		if (!(dirOrFileToScan.exists() && dirOrFileToScan.isDirectory())) {
			System.err.println(dirOrFileToScan.getAbsolutePath() + " is not a readable directory");
			return;
		}
	
		if (!setupOutputFile(dirOrFileToScan)) {
			System.err.println("There was a problem setting up the output");
			return;
		}
	
		if (args.length == 1) {
			scanDirectory(dirOrFileToScan);
		} else {
			scanOneFile(dirOrFileToScan);
		}
		
	}
	
	protected void scanDirectory(File dirToScan) {
		System.out.println("Scanning " + dirToScan.getAbsolutePath());

		scanFiles(dirToScan.listFiles());
	}

	protected void scanOneFile(File dirToScan) {
		try {
			parseSpreadSheet(dirToScan);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void parseSpreadSheet(File file) throws Exception;

	public void scanFiles(File[] files) {
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
					System.out.println("Scanning " + file.getAbsolutePath());
					parseSpreadSheet(file);
					progress ++;
					if (progress % 100 == 0) {
						System.out.println("Scanned " + progress);
					}
				} catch (Exception e) {
					System.out.println("Could not analyze " + file);
					e.printStackTrace();
				}
			}
		}
	}

	protected abstract boolean setupOutputFile(File dirToScan);
}
