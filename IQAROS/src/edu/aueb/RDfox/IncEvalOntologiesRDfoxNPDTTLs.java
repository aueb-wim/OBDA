package edu.aueb.RDfox;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;
import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;
import org.oxford.comlab.perfectref.rewriter.Substitution;
import org.oxford.comlab.perfectref.rewriter.Term;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import owlim.OWLimQueryEvaluator;
import sun.security.pkcs11.Secmod.DbMode;
import uk.ac.ox.cs.JRDFox.JRDFStoreException;
import edu.aueb.queries.ClauseParser;
import edu.aueb.queries.Evaluator;
import edu.aueb.queries.QueryOptimization;
import edu.ntua.image.Configuration;
import edu.ntua.image.Configuration.RedundancyEliminationStrategy;
import edu.ntua.image.datastructures.LabeledGraph;
import edu.ntua.image.incremental.Incremental;


public class IncEvalOntologiesRDfoxNPDTTLs {

	private static final DLliteParser parser = new DLliteParser();

	private int orderedQueryIndex=0;

	static String originalPath = "/Users/avenet/Academia/Ntua/Ontologies/";

	static String uri = "";

	public static void main(String[] args) throws Exception{
		PropertyConfigurator.configure("./logger.properties");

		String ontologyFile, queryPath, optPath, path, dbPath, dataFiles;
		RDFoxQueryEvaluator evaluator = null;

		boolean RDFoxOpt = true;

//		/**
//		 * LUBM Tests
//		 */
//		path = originalPath + "LUBM/";
//		ontologyFile = "file:" + path + "univ-bench_DL-Lite_mine.owl";
//		queryPath = path + "QueriesWithConstants/";
//		uri = "http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#";
//		/*
//		 * LUBM DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/LUBM";
//		optPath = path + "OptimizationClausesOWLim/optimizationClauses.txt";
//		dataFiles = "/Users/avenet/Academia/Ntua/JavaWorkspace/uba1.7/Ontos1/";
//		System.out.println("**************************");
//		System.out.println("**\tLUBM\t\t**");
//		System.out.println("**************************");
//		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
//		/*
//		 * LUBM10 DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/LUBM10"; //This database contains data from 15 universities
//		optPath = path + "OptimizationClausesOWLim/optimizationClauses10.txt";
//		dataFiles = "/Users/avenet/Academia/Ntua/JavaWorkspace/uba1.7/Ontos10/";
//		System.out.println("**************************");
//		System.out.println("**\tLUBM10\t\t**");
//		System.out.println("**************************");
//		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
//		/*
//		 * LUBM30 DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/LUBM30"; //This database contains data from 30 universities
//		optPath = path + "OptimizationClausesOWLim/optimizationClauses30.txt";
//		dataFiles = "/Users/avenet/Academia/Ntua/JavaWorkspace/uba1.7/Ontos30/";
//		System.out.println("**************************");
//		System.out.println("**\tLUBM30\t\t**");
//		System.out.println("**************************");
//		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
		/**
		 * LUBM-ex Tests
		 */
//		path = originalPath + "LUBM-ex/";
//		ontologyFile = "file:" + path + "EUGen/ontologies/lubm-ex-20.owl";
//		queryPath = path + "EUGen/QueriesOWLim/";
//		uri = "";
//		/*
//		 * LUBM-20ex DB
//		 */
//		dataFiles = "/Users/avenet/Academia/Ntua/JavaWorkspace/uba1.7/lubmex20-1Univ/";
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/LUBM_20ex_1";
//		optPath = path + "OptimizationClausesOWLim/optimizationClauses20ex.txt";
//		System.out.println("**************************");
//		System.out.println("**\tLUBM-ex\t\t**");
//		System.out.println("**************************");
//		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		/**
//		 * UOBM Tests
//		 */
//		path = originalPath + "UOBM/";
//		ontologyFile = "file:" + path + "univ-bench-dl-Zhou_DL-Lite_mine.owl";
//		queryPath = path + "QueriesWithConstants/";
//		optPath = path + "OptimizationClausesOWLim/optimizationClauses.txt";
//		uri = "http://semantics.crl.ibm.com/univ-bench-dl.owl#";
//		dataFiles = path + "UOBMGenerator/preload_generated_uobm50_dbcreation/";
//		/*
//		 * UOBM DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/UOBM";
//		System.out.println("**************************");
//		System.out.println("**\tUOBM\t\t**");
//		System.out.println("**************************");
//		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
//		/*
//		 * UOBM10 DB
//		 */
//		optPath = path + "OptimizationClausesOWLim/optimizationClauses10.txt";
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/UOBM10"; //This database contains data from 15 universities
//		System.out.println("**************************");
//		System.out.println("**\tUOBM10\t\t**");
//		System.out.println("**************************");
//		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		System.exit(0);
//		/*
//		 * UOBM30 DB
//		 */
//		optPath = path + "OptimizationClausesOWLim/optimizationClauses30.txt";
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/UOBM30"; //This database contains data from 30 universities
//		System.out.println("**************************");
//		System.out.println("**\tUOBM30\t\t**");
//		System.out.println("**************************");
//		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		/**
////		 * Semintec Tests
////		 */
////		path = originalPath + "Semintec/";
////		ontologyFile = "file:" + path + "bigFile_DL-Lite_mine.owl";
////		queryPath = path + "Queries/";
////		optPath = path + "OptimizationClausesOWLim/optimizationClauses.txt";
////		uri = "http://www.owl-ontologies.com/unnamed.owl#";
////		/*
////		 * Semintex DB
////		 */
////		dbPath = "jdbc:postgresql://127.0.0.1:5432/Semintec";
////		dataFiles = path;
////		System.out.println("**************************");
////		System.out.println("**\tSemintec\t\t**");
////		System.out.println("**************************");
////		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		evaluator.shutDown();
////		/**
////		 * Vicodi Tests
////		 */
////		path = originalPath + "Vicodi/";
////		ontologyFile = "file:" + path + "vicodi_all_DL-Lite_mine.owl";
////		queryPath = path + "Queries/";
////		optPath = path + "OptimizationClausesOWLim/optimizationClauses.txt";
////		uri = "http://vicodi.org/ontology#";
////		/*
////		 * Vicodi DB
////		 */
////		dbPath = "jdbc:postgresql://127.0.0.1:5432/Vicodi";
////		dataFiles = path;
////		System.out.println("**************************");
////		System.out.println("**\tVicodi\t\t**");
////		System.out.println("**************************");
////		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
////		evaluator.shutDown();
////		/**
////		 * Reactome tests
////		 */
////		path =  originalPath + "reactome/";
////		ontologyFile = "file:" + path + "biopax-level3-processed_DL-Lite_mine.owl";
////		queryPath = path + "Queries/";
////		optPath = path + "OptimizationClausesOWLim/optimizationClauses.txt";
////		uri = "http://www.biopax.org/release/biopax-level3.owl#";
////		/*
////		 * Reactome DB
////		 */
////		dbPath = "jdbc:postgresql://127.0.0.1:5432/reactome";
////		dataFiles = path;
////		System.out.println("**************************");
////		System.out.println("**\tReactome\t\t**");
////		System.out.println("**************************");
////		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
//////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
//////		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);
//		
//		/**
//		 * Fly tests
//		 */
//		path =  originalPath + "Fly/";
//		ontologyFile = "file:" + path + "fly_anatomy_XP_with_GJ_FC_individuals_owl-tBox-AssNorm-new-noTrans.owl";
//		queryPath = path + "QueriesOWLim/";
//		optPath = path + "OptimizationClausesOWLim/optimizationClauses.txt";
//		dataFiles = path;
//		uri = "";
//		/*
//		 * Fly DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/Fly";
//		System.out.println("**************************");
//		System.out.println("**\tFly\t\t**");
//		System.out.println("**************************");
//		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);

		/**
		 * NPD tests
		 */
//		path =  originalPath + "npd_original/";
		path =  originalPath + "npd-benchmark-master/";
		ontologyFile = "file:" + path + "ontology/npd-v2-ql.owl";
//		ontologyFile = "file:" + path + "npd.owl";
		queryPath = path + "avenet_queriesWithURIs/";
		optPath = path + "OptimizationClauses/atomicConceptsRoles.txt";
		dataFiles = path;
		uri = "";
		/*
		 * Fly DB
		 */
		dbPath = "";
		System.out.println("**************************");
		System.out.println("**\tNPD\t\t**");
		System.out.println("**************************");
			evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
		runTest(ontologyFile, queryPath, optPath, evaluator, true, RDFoxOpt);

	}


	private static void runTest(String ontologyFile, String queryPath, String optPath, RDFoxQueryEvaluator evaluator, boolean print, boolean RDFoxOpt) throws Exception {
		long start=0,totalTime = 0;
		ArrayList<PI> tBoxAxioms = parser.getAxiomsWithURI(ontologyFile);
		ArrayList<Clause> rewriting = new ArrayList<Clause>();
		
//		for( PI p: tBoxAxioms )
//		{
//			System.out.println(p.m_left + "\t" + p.m_right);
//		}
//		System.exit(0);
		

		File queriesDir = new File( queryPath );
		File[] queries = queriesDir.listFiles();

		/**
		 * Emptiness optimization
		 */
		QueryOptimization qOpt = null;
		if (optPath != null && optPath != "") {
			long startOpt = System.currentTimeMillis();
			qOpt = new QueryOptimization(optPath);
            System.out.println("Optimization Took " + (System.currentTimeMillis() - startOpt) + "ms");
		}
		
		int queryIndex = 1;
		System.out.println( queriesDir );
		for( int i=0 ; i<queries.length ; i++ ) {
			if( queries[i] == null )
				// Either dir does not exist or is not a directory
				return;
			else if( !queries[i].toString().contains(".svn") && !queries[i].toString().contains(".DS_Store") && !queries[i].isDirectory()){
//	        	if ( ontologyFile.contains("UOBM") && queries[i].toString().contains("Query_11") )
//	        	if (!queries[i].toString().contains("Q01"))
//	        		continue;
//	        		&& !queries[i].toString().contains("Query_11.txt")) {
//	        	if (print)
//	        		System.out.println(queries[i] + "\n" + parser.getQuery(queries[i].toString()));
	        	BufferedReader readQueries = new BufferedReader(new FileReader(queries[i]));
	        	String query = readQueries.readLine();
	        	readQueries.close();
				System.out.println( queryIndex++ + ": " + queries[i]);
	        	System.out.println((i+1) + ": " + queries[i] + "\n" + new ClauseParser().parseClause(query));
	        	start = System.currentTimeMillis();
				//use optimization to cut off queries with no answers
        		Incremental incremental = new Incremental(qOpt,RDFoxOpt);
	        	rewriting = incremental.computeUCQRewriting(tBoxAxioms,new ClauseParser().parseClause(query), print);
	        	//no optimization
//	        	rewriting = new Incremental().computeUCQRewriting(tBoxAxioms,new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine()));
	        	/** OR, in order to run the evaluation using non-restricted subsumption */
//	    		Configuration c = new Configuration();
//	    		c.redundancyElimination=RedundancyEliminationStrategy.Full_Subsumption;
//	    		Incremental incremental = new Incremental( c );
//	    		incremental.computeUCQRewriting(tBoxAxioms,parser.getQuery(queries[i].toString()));

//	        	for (Clause cl: rewriting )
//	        		System.out.println(cl + "\t");

//	        	System.out.println( "Original rew size = " + rewriting.size() );
	        	Set<Clause> rdfoxRew = new HashSet<Clause>();
	        	for ( Clause c: rewriting )
	        		if (c.getToBeSentToOWLim()) {
	        			rdfoxRew.add(c);
	        			System.out.println(c);
	        		}
	        	System.out.println( "RFDox rew size = " + rdfoxRew.size() );
	        	
	        	
//	        	Clause newClause = new ClauseParser().parseClause("Q(?0,?1,?2) <- http://sws.ifi.uio.no/vocab/npd-v2#dateLicenseeValidFrom(?44,?2), http://sws.ifi.uio.no/data/norlex#licenseeInterest(?44,?1),http://sws.ifi.uio.no/vocab/npd-v2#licenseeForLicence(?44,?0)");
//	        	Clause newClause = new ClauseParser().parseClause("Q(?0) <- http://sws.ifi.uio.no/vocab/npd-v2#licenseeForLicence(?44,?0)");
//	        	Clause newClause = new ClauseParser().parseClause("Q(?0) <- http://sws.ifi.uio.no/data/norlex#licenseeForLicence(?44,?0)");
//	        	Clause newClause = new ClauseParser().parseClause("Q(?1) <- http://sws.ifi.uio.no/data/norlex#licenseeInterest(?44,?1)");
//	        	Clause newClause = new ClauseParser().parseClause("Q(?1) <- http://sws.ifi.uio.no/vocab/npd-v2#licenseeInterest(?44,?1)");
//	        	Clause newClause = new ClauseParser().parseClause("Q(?2) <- http://sws.ifi.uio.no/vocab/npd-v2#dateLicenseeValidFrom(?44,?2)");
////	        	Clause newClause = new ClauseParser().parseClause("Q(?2) <- http://sws.ifi.uio.no/data/norlex#dateLicenseeValidFrom(?44,?2)");
//	        	
//	        	System.out.println("MY QUERY: " + newClause);
//	        	rdfoxRew.clear();
//	        	rdfoxRew.add(newClause);
	        	/*
	        	 * OWLim evaluation
	        	 */
	        	if ( rdfoxRew.size() != 0 ) {
	        		long startTime = System.currentTimeMillis();
	        		long ans = evaluator.evaluateQuery(rdfoxRew, uri);
	        		long end = System.currentTimeMillis() - startTime;
	        		System.out.println( "RDFox Answers: " + ans + " in " + end + "ms");
	        	}
//	        	else if (  )
        		System.out.println("\n\n");
//        		System.exit(0);

//				totalTime+= (System.currentTimeMillis()-start);
//	        	System.out.println("\n\n\n");
	        }
		}
//		System.out.println( "Finished rewriting " + queries.length + " in " + totalTime + " ms" );
//		System.out.println( "Rew time = " + totalTime );

	}
	
	private static RDFoxQueryEvaluator createDataSetsAndEvaluator(String dataFiles, String dbPath, String ontologyFile) throws FileNotFoundException, OWLOntologyCreationException, URISyntaxException, JRDFStoreException {

		String[] datasetFiles = null;
		RDFoxQueryEvaluator evaluator = new RDFoxQueryEvaluator();
		long t = System.currentTimeMillis();
		System.out.println( "dataFiles = " + dataFiles  );
		if ( dataFiles.contains("uba1.7") ){
			System.out.println("LUBM" + dataFiles);
			datasetFiles = createDataSetFilesLUBM(dataFiles);
		}
		else if (dbPath.contains("UOBM10") ) {
			System.out.println("UOBM10");
			datasetFiles = createDataSetFilesUOBM(dataFiles,10);
		}
		else if (dbPath.contains("UOBM30") ) {
			System.out.println("UOBM30");
			datasetFiles = createDataSetFilesUOBM(dataFiles,30);
		}
		else if (dbPath.contains("UOBM") ) {
			System.out.println("UOBM");
			datasetFiles = createDataSetFilesUOBM(dataFiles,1);
		}
		else if (dataFiles.contains("Semintec") ) {
			System.out.println("Semintec");
			datasetFiles = createDataSetFilesSemintec(dataFiles);
		}
		else if (dataFiles.contains("Vicodi") ) {
			System.out.println("Vicodi");
			datasetFiles = createDataSetFilesVicodi(dataFiles);
		}
		else if (dataFiles.contains("reactome") ) {
			System.out.println("reactome");
			datasetFiles = createDataSetFilesReactome(dataFiles);
		}
		else if (dataFiles.contains("Fly") ) {
			System.out.println("Fly");
			datasetFiles = createDataSetFilesFly(dataFiles);
		}
		else if (dataFiles.contains("npd")  ) {
			System.out.println("NPD");
			datasetFiles = createDataSetFilesNPD(dataFiles);
		}
		evaluator.loadInputToSystem(ontologyFile, datasetFiles);
		System.out.println("loading took: " + (System.currentTimeMillis()-t));
    	return evaluator;
	}

	public static String[] createDataSetFilesLUBM(String dataFilesPath) {

		File dataDir = new File( dataFilesPath );
		File[] dataFiles = dataDir.listFiles();

		int k = 0;

		for( int i=0 ; i<dataFiles.length ; i++ ) {
//			if( dataFiles[i] == null )
//				// Either dir does not exist or is not a directory
//				return;
//			else
			if( !dataFiles[i].toString().contains(".svn") && !dataFiles[i].toString().contains(".DS_Store") && dataFiles[i].toString().contains(".ttl") ){
				k++;
			}
		}

//		System.out.println( "k = " + k);

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
				result[globalCounter++] = dataFilesPath + "University"+ j + "_" + i + ".ttl";
//				System.out.println(result[globalCounter]);
			}

		}
		return result;
	}


	public static String[] createDataSetFilesUOBM(String dataFilesPath, int limit) {
		String[] result = new String[limit];

		for ( int j = 0 ; j < limit ; j++) {
			result[j] = dataFilesPath + "univ"+ j + ".ttl";
		}

		return result;
	}

	public static String[] createDataSetFilesVicodi(String dataFilesPath) {
		String[] result = new String[1];

		result[0] = dataFilesPath + "vicodi_all.ttl";

		return result;
	}

	public static String[] createDataSetFilesSemintec(String dataFilesPath) {
		String[] result = new String[1];

		result[0] = dataFilesPath + "bigFile.ttl";

		return result;
	}

	public static String[] createDataSetFilesReactome(String dataFilesPath) {
		String[] datasetFiles = new String[1];
		datasetFiles[0] = dataFilesPath + "sample_10.ttl";
		return datasetFiles;
	}

	public static String[] createDataSetFilesFly(String dataFilesPath) {
		String[] result = new String[1];

		result[0] = dataFilesPath + "fly_anatomy_XP_with_GJ_FC_individuals_owl-aBox-AssNorm.ttl";

		return result;
	}

	public static String[] createDataSetFilesNPD(String dataFilesPath) {
		String[] result = new String[1];

		result[0] = dataFilesPath + "ontology/npd-v2-ql-abox.ttl";

		return result;
	}
}