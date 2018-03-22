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


public class testOWLimLUBM {
	
	static String originalPath = "/Users/avenet/Academia/Ntua/Ontologies/";
	static String path = originalPath + "LUBM/";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		PropertyConfigurator.configure("./logger.properties");
		
		String ontologyFile, queryPath, optPath, dataFilesPath;
		/**
		 * LUBM Tests
		 */
		
		ontologyFile = "file:" + path + "univ-bench.owl";
		String queryFile = path + "QueriesWithConstantsOWLim/Query_01.txt";
		dataFilesPath = "/Users/avenet/Academia/Ntua/JavaWorkspace/uba1.7/Ontos/";
//		String[] datasetFiles = {path + "DataOntos/University0_0.owl", path + "DataOntos/University0_1.owl", path + "DataOntos/University0_2.owl",path + "DataOntos/University0_3.owl",path + "DataOntos/University0_4.owl",path + "DataOntos/University0_5.owl",path + "DataOntos/University0_6.owl",path + "DataOntos/University0_7.owl",path + "DataOntos/University0_8.owl",path + "DataOntos/University0_9.owl",path + "DataOntos/University0_10.owl",};
		String[] datasetFiles = createDataSetFiles(dataFilesPath);
		
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
	
	public static String[] createDataSetFiles(String dataFilesPath) {

		File dataDir = new File( dataFilesPath );
		File[] dataFiles = dataDir.listFiles();
		
		int k = 0;
		
		for( int i=0 ; i<dataFiles.length ; i++ ) {
//			if( dataFiles[i] == null )
//				// Either dir does not exist or is not a directory
//				return;
//			else
			if( !dataFiles[i].toString().contains(".svn") && !dataFiles[i].toString().contains(".DS_Store") ){
				k++;
			}
		}
		
		System.out.println( "k = " + k);

		String[] result = new String[k];
		int globalCounter = 0;
		for ( int j = 0 ; globalCounter < k ; j++) {
//			System.out.println(globalCounter);
			
			int limit = 0;
			if (j==0)
				limit = 14;
			else if (j==1)
				limit = 18;
			else if (j==2)
				limit = 15;
			else if (j==3)
				limit = 20;
			else if (j==4)
				limit = 21;
			else if (j==5)
				limit = 14;
			else if (j==6)
				limit = 23;
			else if (j==7)
				limit = 16;
			else if (j==8)
				limit = 17;
			else if (j==9)
				limit = 21;
			else if (j==10)
				limit = 19;
			else if (j==11)
				limit = 23;
			else if (j==12)
				limit = 24;
			else if (j==13)
				limit = 18;
			else if (j==14)
				limit = 15;
			else if (j==15)
				limit = 20;
			else if (j==16)
				limit = 18;
			else if (j==17)
				limit = 22;
			else if (j==18)
				limit = 20;
			else if (j==19)
				limit = 24;
			else if (j==20)
				limit = 14;
			else if (j==21)
				limit = 18;
			else if (j==22)
				limit = 16;
			else if (j==23)
				limit = 20;
			else if (j==24)
				limit = 21;
			else if (j==25)
				limit = 15;
			else if (j==26)
				limit = 23;
			else if (j==27)
				limit = 17;
			else if (j==28)
				limit = 17;
			else if (j==29)
				limit = 22;
			for ( int i = 0 ; i <= limit ; i++) {
				result[globalCounter++] = dataFilesPath + "University"+ j + "_" + i + ".owl";
//				System.out.println(result[globalCounter]);
			}
			
		}
		return result;
	}
}
