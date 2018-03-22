import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;

import owlim.OWLimQueryEvaluator;
import edu.aueb.queries.ClauseParser;
import edu.aueb.queries.QueryOptimization;
import edu.ntua.image.incremental.Incremental;


public class IncEvalOntologiesOWLim {

	static Workbook workbook = new XSSFWorkbook();

	private static final DLliteParser parser = new DLliteParser();

	private int orderedQueryIndex=0;

	static String originalPath = "/Users/avenet/Academia/Ntua/Ontologies/";
	static String excelFile = "/Users/avenet/Academia/Aueb/Research/IncrementalQueryAnswering/Fly.xlsx";

	static String uri = "";

	static boolean print2Excel = false;
	static String addon = "";
	static String dbPath = "";

	public static void main(String[] args) throws Exception{
		PropertyConfigurator.configure("./logger.properties");

		String ontologyFile, queryPath, optPath, path, dataFiles;
		OWLimQueryEvaluator evaluator = null;

		boolean OWLimOpt = true;

//		/**
//		 * LUBM Tests
//		 */
//		path = originalPath + "LUBM/";
//		ontologyFile = "file:" + path + "univ-bench_DL-Lite_mine.owl";
//		queryPath = path + "QueriesWithConstantsOWLim/";
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
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, OWLimOpt, print2Excel);
//		evaluator.shutDown();
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
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, OWLimOpt, print2Excel);
//		evaluator.shutDown();
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
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, OWLimOpt, print2Excel);
//		evaluator.shutDown();
//
//		addon="-ex";
//		/**
//		 * LUBM-ex Tests
//		 */
//		path = originalPath + "LUBM-ex/";
//		ontologyFile = "file:" + path + "/EUGen/ontologies/lubm-ex-20.owl";
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
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, OWLimOpt, print2Excel);
//		evaluator.shutDown();
//		/*
//		 * LUBM-20ex-10 DB
//		 */
//		dataFiles = "/Users/avenet/Academia/Ntua/JavaWorkspace/uba1.7/lubmex20-10Univ/";
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/LUBM_20ex_10"; //This database contains data from 15 universities
//		optPath = path + "OptimizationClauses/optimizationClauses20ex10.txt";
//		System.out.println("**************************");
//		System.out.println("**\tLUBM-ex-10\t\t**");
//		System.out.println("**************************");
//		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, OWLimOpt, print2Excel);
//		evaluator.shutDown();
//		/*
//		 * LUBM-20ex-30 DB
//		 */
//		dataFiles = "/Users/avenet/Academia/Ntua/JavaWorkspace/uba1.7/lubmex20-30Univ/";
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/LUBM_20ex_30"; //This database contains data from 30 universities
//		optPath = path + "OptimizationClauses/optimizationClauses20ex30.txt";
//		System.out.println("**************************");
//		System.out.println("**\tLUBM-ex-30\t\t**");
//		System.out.println("**************************");
//		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, OWLimOpt, print2Excel);
//		evaluator.shutDown();
//		addon = "";
		/**
		 * UOBM Tests
		 */
		path = originalPath + "UOBM/";
		ontologyFile = "file:" + path + "univ-bench-dl-Zhou_DL-Lite_mine.owl";
		queryPath = path + "QueriesWithConstantsOWLim/";
		optPath = path + "OptimizationClausesOWLim/optimizationClauses.txt";
		uri = "http://semantics.crl.ibm.com/univ-bench-dl.owl#";
		dataFiles = path + "UOBMGenerator/preload_generated_uobm50_dbcreation/";
//		/*
//		 * UOBM DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/UOBM";
//		System.out.println("**************************");
//		System.out.println("**\tUOBM\t\t**");
//		System.out.println("**************************");
//		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, OWLimOpt, print2Excel);
//		evaluator.shutDown();
		/*
		 * UOBM10 DB
		 */
		optPath = path + "OptimizationClausesRDFox/optimizationClausesUOBM10.txt";
		dbPath = "jdbc:postgresql://127.0.0.1:5432/UOBM10"; //This database contains data from 15 universities
		System.out.println("**************************");
		System.out.println("**\tUOBM10\t\t**");
		System.out.println("**************************");
		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
		runTest(ontologyFile, queryPath, optPath, evaluator, true, OWLimOpt, print2Excel);
		evaluator.shutDown();
//////		System.exit(0);
//		/*
//		 * UOBM30 DB
//		 */
//		optPath = path + "OptimizationClausesOWLim/optimizationClauses30.txt";
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/UOBM30"; //This database contains data from 30 universities
//		System.out.println("**************************");
//		System.out.println("**\tUOBM30\t\t**");
//		System.out.println("**************************");
//		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, OWLimOpt, print2Excel);
//		evaluator.shutDown();
//////		/**
//////		 * Semintec Tests
//////		 */
//////		path = originalPath + "Semintec/";
//////		ontologyFile = "file:" + path + "bigFile_DL-Lite_mine.owl";
//////		queryPath = path + "Queries/";
//////		optPath = path + "OptimizationClausesOWLim/optimizationClauses.txt";
//////		uri = "http://www.owl-ontologies.com/unnamed.owl#";
//////		/*
//////		 * Semintex DB
//////		 */
//////		dbPath = "jdbc:postgresql://127.0.0.1:5432/Semintec";
//////		dataFiles = path;
//////		System.out.println("**************************");
//////		System.out.println("**\tSemintec\t\t**");
//////		System.out.println("**************************");
//////		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
//////		runTest(ontologyFile, queryPath, optPath, evaluator, true, OWLimOpt, print2Excel);
//////		evaluator.shutDown();
//////		/**
//////		 * Vicodi Tests
//////		 */
//////		path = originalPath + "Vicodi/";
//////		ontologyFile = "file:" + path + "vicodi_all_DL-Lite_mine.owl";
//////		queryPath = path + "Queries/";
//////		optPath = path + "OptimizationClausesOWLim/optimizationClauses.txt";
//////		uri = "http://vicodi.org/ontology#";
//////		/*
//////		 * Vicodi DB
//////		 */
//////		dbPath = "jdbc:postgresql://127.0.0.1:5432/Vicodi";
//////		dataFiles = path;
//////		System.out.println("**************************");
//////		System.out.println("**\tVicodi\t\t**");
//////		System.out.println("**************************");
//////		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
//////		runTest(ontologyFile, queryPath, optPath, evaluator, true, OWLimOpt, print2Excel);
//////		evaluator.shutDown();
//		/**
//		 * Reactome tests
//		 */
//		path =  originalPath + "reactome/";
//		ontologyFile = "file:" + path + "biopax-level3-processed_DL-Lite_mine.owl";
//		queryPath = path + "QueriesOWLim/";
//		optPath = path + "OptimizationClausesOWLim/optimizationClauses.txt";
//		uri = "http://www.biopax.org/release/biopax-level3.owl#";
//		/*
//		 * Reactome DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/reactome";
//		dataFiles = path;
//		System.out.println("**************************");
//		System.out.println("**\tReactome\t\t**");
//		System.out.println("**************************");
//		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
//		runTest(ontologyFile, queryPath, optPath, evaluator, true, OWLimOpt, print2Excel);
//		evaluator.shutDown();
		/**
		 * Fly tests
		 */
		path =  originalPath + "Fly/";
//		String onto = "";
		String onto = "-new";
//		String onto = "-new-noTrans";
		ontologyFile = "file:" + path + "fly_anatomy_XP_with_GJ_FC_individuals_owl-tBox-AssNorm" + onto + ".owl";
		queryPath = path + "QueriesOWLim/";
//		optPath = path + "OptimizationClausesOWLim/OptimizationClauses"+ onto + ".txt";
//		optPath = path + "OptimizationClausesOWLim/OptimizationClauses"+ onto + "_noConceptCombinations.txt";
		optPath = "";
		dataFiles = path;
		excelFile = "/Users/avenet/Academia/Aueb/Research/IncrementalQueryAnswering/Fly" + onto + "_" + optPath + ".xlsx";
		print2Excel = true;
		uri = "";
		/*
		 * Fly DB
		 */
		dbPath = "jdbc:postgresql://127.0.0.1:5432/Fly";
		System.out.println("**************************");
		System.out.println("**\tFly\t\t**");
		System.out.println("**************************");
		evaluator = createDataSetsAndEvaluator(dataFiles, dbPath, ontologyFile);
		runTest(ontologyFile, queryPath, optPath, evaluator, true, OWLimOpt, print2Excel);

		if ( print2Excel ) {
			FileOutputStream fos = new FileOutputStream(excelFile);
	        workbook.write(fos);
	        fos.close();
		}
		System.exit(0);
	}

	
	private static void runTest(String ontologyFile, String queryPath, String optPath, OWLimQueryEvaluator evaluator, boolean print, boolean OWLimOpt, boolean printToExcel) throws Exception {
		long start=0,totalTime = 0;
		ArrayList<PI> tBoxAxioms = parser.getAxiomsWithURI(ontologyFile);
		ArrayList<Clause> rewriting = new ArrayList<Clause>();
		
		QueryOptimization qOpt = null;

		File queriesDir = new File( queryPath );
		File[] queries = queriesDir.listFiles();

//		System.out.println(queryPath);
		
		int cellIndex = 0, rowIndex = 0;
		Row row = null;
		Sheet sheet = null;
		if (printToExcel) {
		/**
		 * Initializing excel
		 */
			sheet = workbook.createSheet(dbPath.replace("jdbc:postgresql://127.0.0.1:5432/","")+addon);
			rowIndex = 0;
			row = sheet.createRow(rowIndex++);
	        cellIndex = 2;
	        row.createCell(cellIndex++).setCellValue("FinalBeforeSub");
	        row.createCell(++cellIndex).setCellValue("");

	        row.createCell(cellIndex++).setCellValue("Final-Sub");
	        row.createCell(++cellIndex).setCellValue("");

	        row.createCell(cellIndex++).setCellValue("Evaluation");
	        row.createCell(++cellIndex).setCellValue("");

	        row.createCell(cellIndex++).setCellValue("Total");

	        row = sheet.createRow(rowIndex++);
	        cellIndex = 2;
	        row.createCell(cellIndex++).setCellValue("size");
	        row.createCell(cellIndex++).setCellValue("t");
	        row.createCell(cellIndex++).setCellValue("size");
	        row.createCell(cellIndex++).setCellValue("t-sub");
	        row.createCell(cellIndex++).setCellValue("Ans");
	        row.createCell(cellIndex++).setCellValue("t-Ans");
	        row.createCell(cellIndex++).setCellValue("Total");

	        row = sheet.createRow(rowIndex++);
	        cellIndex = 0;
		}
        /**
		 * END Initializing excel
		 */

		/**
		 * Emptiness optimization
		 */
		if (optPath != null && optPath != "") {
			long startOpt = System.currentTimeMillis();
			qOpt = new QueryOptimization(optPath);
            System.out.println("Optimization Took " + (System.currentTimeMillis() - startOpt) + "ms");
		}

//		System.out.println( queriesDir );
		for( int i=0 ; i<queries.length ; i++ ) {
			if( queries[i] == null )
				// Either dir does not exist or is not a directory
				return;
	        else if( !queries[i].toString().contains(".svn") && !queries[i].toString().contains(".DS_Store") ){

//	        	if ( ontologyFile.contains("UOBM") && queries[i].toString().contains("Query_11") )
	        	if (!queries[i].toString().contains("11") )
	        		continue;
//	        		&& !queries[i].toString().contains("Query_11.txt")) {
//	        	if (print)
//	        	System.out.println(queries[i] + "\n" + parser.getQuery(queries[i].toString()));
        		System.out.println((i+1) + ": " + queries[i] + "\n" + new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine()));
//	        	start = System.currentTimeMillis();
	        	//use optimization to cut off queries with no answers
        		Incremental incremental = new Incremental(qOpt,OWLimOpt);
	        	rewriting = incremental.computeUCQRewriting(tBoxAxioms,new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine()), print);
	        	//no optimization
//	        	Incremental incremental = new Incremental();
//	        	rewriting = incremental.computeUCQRewriting(tBoxAxioms,new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine()));
	        	/** OR, in order to run the evaluation using non-restricted subsumption */
//	    		Configuration c = new Configuration();
//	    		c.redundancyElimination=RedundancyEliminationStrategy.Full_Subsumption;
//	    		Incremental incremental = new Incremental( c );
//	    		incremental.computeUCQRewriting(tBoxAxioms,parser.getQuery(queries[i].toString()));

//	        	for (Clause cl: rewriting )
//	        		System.out.println(cl + "\t");

	        	System.out.println( "Original rew size = " + rewriting.size() );
	        	Set<Clause> owlimRew = new HashSet<Clause>();
	        	for ( Clause c: rewriting )
	        		/**
	        		 * We must use batch processing to send the queries to OWLim
	        		 * because it gives an StackOverFlow error
	        		 */
	        		if (c.getToBeSentToOWLim() ) {
	        			owlimRew.add(c);
//	        			System.out.println(c);
	        		}
	        	System.out.println( "OWLim rew size = " + owlimRew.size() );

	        	/*
	        	 * OWLim evaluation
	        	 */
	        	long end = 0;
	        	if ( owlimRew.size() != 0 ) {
	        		long startTime = System.currentTimeMillis();
//	        		evaluator.evaluateQuery(owlimRew, uri);
	        		evaluator.evaluateQuery(owlimRew);
	        		end = System.currentTimeMillis() - startTime;
	        		System.out.println( "OWLim Answers: " + evaluator.getNumberOfReturnedAnswersCompletePart() + " in " + end + "ms");
//	        		if (evaluator.getNumberOfReturnedAnswersCompletePart()!=0)
//	        			System.out.println(evaluator.getValueOfCompletePartAt(0, 0));
	        	}
	        	/**
	        	 * Write to excel
	        	 */
	        	if (printToExcel) {
		        	if ( cellIndex == 0 ) {
		                row.createCell(cellIndex).setCellValue(dbPath.replace("jdbc:postgresql://127.0.0.1:5432/",""));
		        	}
		        	cellIndex = 1;
		        	row.createCell(cellIndex++).setCellValue("Query_" + (i+1));
		        	row.createCell(cellIndex++).setCellValue(incremental.getRewSizeOriginal());
		            row.createCell(cellIndex++).setCellValue(incremental.getRewTime());
		            row.createCell(cellIndex++).setCellValue(incremental.getRewSize());
		            row.createCell(cellIndex++).setCellValue(incremental.getSubTime());
		            row.createCell(cellIndex++).setCellValue(evaluator.getNumberOfReturnedAnswersCompletePart());
		            row.createCell(cellIndex++).setCellValue(end);
		            row.createCell(cellIndex++).setCellValue(end+incremental.getRewTime()+incremental.getSubTime());
		        	
		            row = sheet.createRow(rowIndex++);
		            cellIndex = 1;
	        	}
////				totalTime+= (System.currentTimeMillis()-start);
	        	System.out.println("\n\n\n");
	        }
		}
//		System.out.println( "Finished rewriting " + queries.length + " in " + totalTime + " ms" );
//		System.out.println( "Rew time = " + totalTime );

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
			if( !dataFiles[i].toString().contains(".svn") && !dataFiles[i].toString().contains(".DS_Store") && dataFiles[i].toString().contains(".owl")){
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
				result[globalCounter++] = dataFilesPath + "University"+ j + "_" + i + ".owl";
//				System.out.println(result[globalCounter]);
			}

		}
		return result;
	}

	public static String[] createDataSetFilesLUBM_ex(String dataFilesPath) {

		File dataDir = new File( dataFilesPath );
		File[] dataFiles = dataDir.listFiles();

		int k = 0;

		for( int i=0 ; i<dataFiles.length ; i++ ) {
//			if( dataFiles[i] == null )
//				// Either dir does not exist or is not a directory
//				return;
//			else
			if( !dataFiles[i].toString().contains(".svn") && !dataFiles[i].toString().contains(".DS_Store") && dataFiles[i].toString().contains(".owl")){
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
				result[globalCounter++] = dataFilesPath + "University"+ j + "_" + i + ".owl";
//				System.out.println(result[globalCounter]);
			}

		}
		return result;
	}
	
	public static String[] createDataSetFilesUOBM(String dataFilesPath, int limit) {
		String[] result = new String[limit];

		for ( int j = 0 ; j < limit ; j++) {
			result[j] = dataFilesPath + "univ"+ j + ".owl";
		}

		return result;
	}

	public static String[] createDataSetFilesVicodi(String dataFilesPath) {
		String[] result = new String[1];

		result[0] = dataFilesPath + "vicodi_all.owl";

		return result;
	}

	public static String[] createDataSetFilesSemintec(String dataFilesPath) {
		String[] result = new String[1];

		result[0] = dataFilesPath + "bigFile.owl";

		return result;
	}

	public static String[] createDataSetFilesReactome(String dataFilesPath) {
		String[] datasetFiles = new String[4];
		datasetFiles[0] = dataFilesPath + "sample_101.rdf";
		datasetFiles[1] = dataFilesPath + "sample_102.rdf";
		datasetFiles[2] = dataFilesPath + "sample_103.rdf";
		datasetFiles[3] = dataFilesPath + "sample_104.rdf";

		return datasetFiles;
	}

	public static String[] createDataSetFilesFly(String dataFilesPath) {
		String[] result = new String[1];

		result[0] = dataFilesPath + "fly_anatomy_XP_with_GJ_FC_individuals_owl-aBox-AssNorm.owl";

		return result;
	}
	
	private static OWLimQueryEvaluator createDataSetsAndEvaluator(String dataFiles, String dbPath, String ontologyFile) {

		String[] datasetFiles = null;
		OWLimQueryEvaluator evaluator = new OWLimQueryEvaluator();

//		System.out.println( "dataFiles = " + dataFiles  );
		if ( dataFiles.contains("uba1.7/Onto") ){
			System.out.println("LUBM" + dataFiles);
			datasetFiles = createDataSetFilesLUBM(dataFiles);
		}
		if ( dataFiles.contains("uba1.7/lubm") ){
			System.out.println("LUBM" + dataFiles);
			datasetFiles = createDataSetFilesLUBM_ex(dataFiles);
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
		evaluator.loadInputToSystem(ontologyFile, datasetFiles);
    	return evaluator;
	}
}