package edu.aueb.OWLim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;

import edu.aueb.queries.ClauseParser;
import owlim.OWLimQueryEvaluator;

//import edu.aueb.queries.ClauseParser;


public class testOWLimUOBM {
	
	static String originalPath = "/Users/avenet/Academia/Ntua/Ontologies/";
	static String path = originalPath + "UOBM/";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		PropertyConfigurator.configure("./logger.properties");
		
		String ontologyFile, queryPath, optPath, dataFilesPath;
		/**
		 * LUBM Tests
		 */
		
		ontologyFile = "file:" + path + "univ-bench-dl-Zhou_DL-Lite_mine.owl";
		String queryFile = path + "QueriesWithConstantsOWLim/Query_01.txt";
		dataFilesPath = path + "UOBMGenerator/preload_generated_uobm50_dbcreation/";
//		String[] datasetFiles = {path + "DataOntos/University0_0.owl", path + "DataOntos/University0_1.owl", path + "DataOntos/University0_2.owl",path + "DataOntos/University0_3.owl",path + "DataOntos/University0_4.owl",path + "DataOntos/University0_5.owl",path + "DataOntos/University0_6.owl",path + "DataOntos/University0_7.owl",path + "DataOntos/University0_8.owl",path + "DataOntos/University0_9.owl",path + "DataOntos/University0_10.owl",};
		String[] datasetFiles = createDataSetFiles(dataFilesPath, 1);
		
		OWLimQueryEvaluator evaluator = new OWLimQueryEvaluator();
//		for ( int i = 0 ; i < datasetFiles.length ; i++ )
//			System.out.println(i + ": " + datasetFiles[i]);
		evaluator.loadInputToSystem(ontologyFile, datasetFiles);
		evaluator.evaluateQuery(new ClauseParser().parseClause((new BufferedReader(new FileReader(queryFile))).readLine()));
		System.out.println(evaluator.getNumberOfReturnedAnswersCompletePart());
		for ( int i = 0 ; i < evaluator.getNumberOfReturnedAnswersCompletePart() ; i++ )
			System.out.println(evaluator.getValueOfCompletePartAt(i, 0));
//		System.out.println(evaluator.getNumberOfReturnedAnswersIncompletePart());
		
		System.exit(0);
	}
	
	public static String[] createDataSetFiles(String dataFilesPath, int limit) {
		String[] result = new String[limit];
		
		for ( int j = 0 ; j < limit ; j++) {
			result[j] = dataFilesPath + "univ"+ j + ".owl";
		}

		return result;
	}
}
