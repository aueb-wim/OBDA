package edu.aueb.OWLim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.oxford.comlab.perfectref.rewriter.Clause;

import edu.aueb.queries.ClauseParser;
import owlim.OWLimQueryEvaluator;

//import edu.aueb.queries.ClauseParser;


public class testOWLimReactome {
	
	static String originalPath = "/Users/avenet/Academia/Ntua/Ontologies/";
	static String path = originalPath + "reactome/";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		PropertyConfigurator.configure("./logger.properties");
		
		String ontologyFile, queryPath, optPath, dataFilesPath;
		/**
		 * LUBM Tests
		 */
		
//		ontologyFile = "file:" + path + "biopax-level3-processed.owl";
		ontologyFile = "file:" + path + "biopax-level3-processed_DL-Lite_mine.owl";
		String queryFile = path + "QueriesOWLim/Query_02.txt";
//		String queryFile = path + "test.txt";
		dataFilesPath = path + "";
//		String[] datasetFiles = {path + "DataOntos/University0_0.owl", path + "DataOntos/University0_1.owl", path + "DataOntos/University0_2.owl",path + "DataOntos/University0_3.owl",path + "DataOntos/University0_4.owl",path + "DataOntos/University0_5.owl",path + "DataOntos/University0_6.owl",path + "DataOntos/University0_7.owl",path + "DataOntos/University0_8.owl",path + "DataOntos/University0_9.owl",path + "DataOntos/University0_10.owl",};
		String[] datasetFiles = new String[4];
		datasetFiles[0] = path + "sample_101.rdf";
		datasetFiles[1] = path + "sample_102.rdf";
		datasetFiles[2] = path + "sample_103.rdf";
		datasetFiles[3] = path + "sample_104.rdf";
		
		OWLimQueryEvaluator evaluator = new OWLimQueryEvaluator();
//		for ( int i = 0 ; i < datasetFiles.length ; i++ )
//			System.out.println(i + ": " + datasetFiles[i]);
		evaluator.loadInputToSystem(ontologyFile, datasetFiles);
		Clause query = new ClauseParser().parseClause((new BufferedReader(new FileReader(queryFile))).readLine());
		System.out.println(query);
		long start = System.currentTimeMillis();
		evaluator.evaluateQuery(query);
		System.out.println(evaluator.getNumberOfReturnedAnswersCompletePart() + " in " + ( System.currentTimeMillis() - start ) );
		for ( int i = 0 ; i < evaluator.getNumberOfReturnedAnswersCompletePart() ; i++ )
			System.out.println(evaluator.getValueOfCompletePartAt(i, 0));
//		System.out.println(evaluator.getNumberOfReturnedAnswersIncompletePart());
		
		System.exit(0);
	}
	
}
