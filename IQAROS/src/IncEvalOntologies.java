import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;

import edu.aueb.queries.ClauseParser;
import edu.aueb.queries.Evaluator;
import edu.aueb.queries.QueryOptimization;
import edu.ntua.image.incremental.Incremental;


public class IncEvalOntologies {

	static Workbook workbook = new XSSFWorkbook();
	private static final DLliteParser parser = new DLliteParser();

	static String originalPath = "/Users/avenet/Academia/Ntua/Ontologies/";
	static String excelFile = "";

	static String uri = "";

	static boolean print2Excel = true;
	static String addon = "";

	public static void main(String[] args) throws Exception{
		PropertyConfigurator.configure("./logger.properties");

		String ontologyFile, queryPath, optPath, path, dbPath;
//
//		/**
//		 * LUBM Tests
//		 */
//		path = originalPath + "LUBM/";
////		ontologyFile = "file:" + path + "univ-bench-gstoil.owl";
//		ontologyFile = "file:" + path + "univ-bench_DL-Lite_mine.owl";
//		queryPath = path + "QueriesWithConstants/";
////		uri = "http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#";
////		/*
////		 * LUBM DB
////		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/LUBM";
//		optPath = path + "OptimizationClauses/optimizationClausesLUBM.txt";
//		System.out.println("**************************");
//		System.out.println("**\tLUBM\t\t**");
//		System.out.println("**************************");
//		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, print2Excel);
//		/*
//		 * LUBM10 DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/LUBM10"; //This database contains data from 15 universities
//		optPath = path + "OptimizationClauses/optimizationClausesLUBM10.txt";
//		System.out.println("**************************");
//		System.out.println("**\tLUBM10\t\t**");
//		System.out.println("**************************");
//		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, print2Excel);
		/*
		 * LUBM30 DB
		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/LUBM30"; //This database contains data from 30 universities
//		optPath = path + "OptimizationClauses/optimizationClausesEntitiesOnly.txt";
////		optPath = path + "OptimizationClauses/optimizationClausesLUBM30.txt";
//		System.out.println("**************************");
//		System.out.println("**\tLUBM30\t\t**");
//		System.out.println("**************************");
//		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, print2Excel);
//		
//		excelFile = "/Users/avenet/Academia/Aueb/Research/IncrementalQueryAnswering/EvaluationResults/LUBM/IQAROS/DB/LUBMEntitiesOnly.xlsx";
//
//		addon="-ex";
//		/**
//		 * LUBM-ex Tests
//		 */
//		path = originalPath + "LUBM-ex/";
//		ontologyFile = "file:" + path + "/EUGen/ontologies/lubm-ex-20.owl";
//		queryPath = path + "EUGen/Queries/";
////		uri = "http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#";
//		/*
//		 * LUBM-20ex DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/LUBM_20ex_1";
//		optPath = path + "OptimizationClauses/optimizationClauses20ex.txt";
//		System.out.println("**************************");
//		System.out.println("**\tLUBM-ex\t\t**");
//		System.out.println("**************************");
//		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, print2Excel);
//		/*
//		 * LUBM-20ex-10 DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/LUBM_20ex_10"; //This database contains data from 15 universities
//		optPath = path + "OptimizationClauses/optimizationClauses20ex10.txt";
//		System.out.println("**************************");
//		System.out.println("**\tLUBM-ex-10\t\t**");
//		System.out.println("**************************");
//		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, print2Excel);
//		/*
//		 * LUBM-20ex-30 DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/LUBM_20ex_30"; //This database contains data from 30 universities
//		optPath = path + "OptimizationClauses/optimizationClauses20ex30.txt";
//		System.out.println("**************************");
//		System.out.println("**\tLUBM-ex-30\t\t**");
//		System.out.println("**************************");
//		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, print2Excel);
//
//		addon = "";
//		/**
//		 * UOBM Tests
//		 */
//		path = originalPath + "UOBM/";
//		ontologyFile = "file:" + path + "univ-bench-dl-Zhou_DL-Lite_mine.owl";
//		queryPath = path + "QueriesWithConstants/";
//		optPath = path + "OptimizationClauses/optimizationClausesUOBM.txt";
//		/*
//		 * UOBM DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/UOBM";
//		System.out.println("**************************");
//		System.out.println("**\tUOBM\t\t**");
//		System.out.println("**************************");
//		runTest(ontologyFile, queryPath, null, dbPath, true, 1, print2Excel);
//		/*
//		 * UOBM10 DB
//		 */
//		optPath = path + "OptimizationClauses/optimizationClausesUOBM10.txt";
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/UOBM10"; //This database contains data from 15 universities
//		System.out.println("**************************");
//		System.out.println("**\tUOBM10\t\t**");
//		System.out.println("**************************");
//		runTest(ontologyFile, queryPath, null, dbPath, true, 10, print2Excel);
//		/*
//		 * UOBM30 DB
//		 */
////		optPath = path + "OptimizationClauses/optimizationClausesUOBM30.txt";
//		optPath = path + "OptimizationClauses/optimizationClausesEntitiesOnly.txt";
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/UOBM30"; //This database contains data from 30 universities
//		System.out.println("**************************");
//		System.out.println("**\tUOBM30\t\t**");
//		System.out.println("**************************");
//		runTest(ontologyFile, queryPath, optPath, dbPath, true, 30, print2Excel);
//		excelFile = "/Users/avenet/Academia/Aueb/Research/IncrementalQueryAnswering/EvaluationResults/UOBM/IQAROS/DB/UOBM_EntitiesOnly.xlsx";
//		/**
//		 * Semintec Tests
//		 */
//		path = originalPath + "Semintec/";
//		ontologyFile = "file:" + path + "bigFile_DL-Lite_mine.owl";
//		queryPath = path + "Queries/";
//		optPath = path + "OptimizationClauses/optimizationClauses.txt";
//		uri = "http://www.owl-ontologies.com/unnamed.owl#";
//		/*
//		 * Semintex DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/Semintec";
//		System.out.println("**************************");
//		System.out.println("**\tSemintec\t\t**");
//		System.out.println("**************************");
//		runTest(ontologyFile, queryPath, optPath, dbPath, true, dataFiles, 0, print2Excel);
//
//		/**
//		 * Vicodi Tests
//		 */
//		path = originalPath + "Vicodi/";
//		ontologyFile = "file:" + path + "vicodi_all_DL-Lite_mine.owl";
//		queryPath = path + "Queries/";
//		optPath = path + "OptimizationClauses/optimizationClauses.txt";
//		uri = "file:/vicodi/27-7-2012/vicodi_all/vicodi_all.owl#";
//		/*
//		 * Vicodi DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/Vicodi";
//		System.out.println("**************************");
//		System.out.println("**\tVicodi\t\t**");
//		System.out.println("**************************");
//		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, print2Excel);
//
//		/**
//		 * Reactome tests
//		 */
//		path =  originalPath + "reactome/";
////		ontologyFile = "file:" + path + "biopax-level3-processed.owl";
//		ontologyFile = "file:" + path + "biopax-level3-processed_DL-Lite_mine.owl";
//		queryPath = path + "Queries/";
//		optPath = path + "OptimizationClauses/optimizationClausesEntitiesOnly.txt";
////		optPath = path + "OptimizationClauses/optimizationClauses.txt";
//		uri = "http://www.biopax.org/release/biopax-level3.owl#";
//		/*
//		 * Reactome DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/reactome(20160411)";
//		System.out.println("**************************");
//		System.out.println("**\tReactome\t\t**");
//		System.out.println("**************************");
//		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, print2Excel);
//
//		excelFile = "/Users/avenet/Academia/Aueb/Research/IncrementalQueryAnswering/EvaluationResults/reactome/IQAROS/DB/reactome-EntitiesEmptiness.xlsx";


		/**
		 * Fly tests
		 */
		path =  originalPath + "Fly/";
//		ontologyFile = "file:" + path + "fly_anatomy_XP_with_GJ_FC_individuals_owl-tBox-AssNorm.owl";
//		ontologyFile = "file:" + path + "fly_anatomy_XP_with_GJ_FC_individuals_owl-tBox-AssNorm-new.owl";
		ontologyFile = "file:" + path + "fly_anatomy_XP_with_GJ_FC_individuals_owl-tBox-AssNorm-new-noTrans.owl";
//		ontologyFile = "file:" + path + "Fly_testAxiomsInOntop.owl";
		queryPath = path + "Queries/";
//		queryPath = path + "QueriesOWLim/";
//		optPath = path + "OptimizationClauses/optimizationClauses.txt";
//		optPath = path + "OptimizationClauses/optimizationClauses-new.txt";
//		optPath = path + "OptimizationClauses/optimizationClauses-new-noTrans.txt";
		optPath = path + "OptimizationClauses/optimizationClausesEntitiesOnly-new-noTrans.txt";
//		uri = "http://www.biopax.org/release/biopax-level3.owl#";
		/*
		 * Fly DB
		 */
		dbPath = "jdbc:postgresql://127.0.0.1:5432/Fly";
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/FlyWithURIs";
		System.out.println("**************************");
		System.out.println("**\tFly\t\t**");
		System.out.println("**************************");
		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, true);

		excelFile = "/Users/avenet/Academia/Aueb/Research/IncrementalQueryAnswering/EvaluationResults/Fly/IQAROS/DB/Fly-EntitiesEmptiness.xlsx";

//		/**
//		 * Chembl tests
//		 */
//		path =  originalPath + "Chembl/";
//		ontologyFile = "file:" + path + "cco-noDPR_rdfxml.owl";
////		ontologyFile = "file:" + path + "chembl.owl";
//		queryPath = path + "Queries/";
////		optPath = path + "OptimizationClauses/optimizationClauses.txt";
//		optPath = path + "OptimizationClauses/optimizationClausesEntitiesOnly.txt";
////		uri = "http://www.biopax.org/release/biopax-level3.owl#";
//		/*
//		 * Chembl DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/Chembl";
//		System.out.println("**************************");
//		System.out.println("**\tChembl\t\t**");
//		System.out.println("**************************");
////		runTest(ontologyFile, queryPath, null, dbPath, true, 0, false);
//		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, true);
//		
//		excelFile = "/Users/avenet/Academia/Aueb/Research/IncrementalQueryAnswering/EvaluationResults/Chembl/IQAROS/DB/Chembl-EntitiesEmptiness.xlsx";

//
//		/**
//		 * Uniprot tests
//		 */
//		path =  originalPath + "Uniprot/";
//		ontologyFile = "file:" + path + "core-sat-processed-cardNorm.owl";
//		queryPath = path + "Queries/";
////		optPath = path + "OptimizationClauses/optimizationClauses-noURIs.txt";
//		optPath = path + "OptimizationClauses/optimizationClausesEntitiesOnly.txt";
////		uri = "http://www.biopax.org/release/biopax-level3.owl#";
//		/*
//		 * Uniprot DB
//		 */
//		dbPath = "jdbc:postgresql://127.0.0.1:5432/UniprotNoURIs";
//		System.out.println("**************************");
//		System.out.println("**\tUniprot\t\t**");
//		System.out.println("**************************");
//		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, print2Excel);
//		
//		excelFile = "/Users/avenet/Academia/Aueb/Research/IncrementalQueryAnswering/EvaluationResults/Uniprot/IQAROS/DB/Uniprot-EntitiesEmptiness.xlsx";

		if ( print2Excel ) {
			FileOutputStream fos = new FileOutputStream(excelFile);
	        workbook.write(fos);
	        fos.close();
		}
	}

	private static void runTest(String ontologyFile, String queryPath, String optPath, String dbPath, boolean print) throws Exception {
		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0);
	}

	private static void runTest(String ontologyFile, String queryPath, String optPath, String dbPath, boolean print, int limit) throws Exception {
		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, false);
	}

	private static void runTest(String ontologyFile, String queryPath, String optPath, String dbPath, boolean print, int limit, boolean printToExcel) throws Exception {
		long start=0,totalTime = 0;

		QueryOptimization qOpt = null;

		ArrayList<PI> tBoxAxioms = parser.getAxioms(ontologyFile);
//		ArrayList<PI> tBoxAxioms = parser.getAxiomsWithURI(ontologyFile);
		ArrayList<Clause> rewriting = new ArrayList<Clause>();

//		for (PI p: tBoxAxioms) 
//			System.out.println(p.m_left + "\t\t" + p.m_type + "\t\t" + p.m_right);
//		
//		System.exit(0);
		
		File queriesDir = new File( queryPath );
		File[] queries = queriesDir.listFiles();
		if (!print)
			System.out.println("...Warming up...");

		int cellIndex = 0, rowIndex = 0;
		Row row = null;
		Sheet sheet = null;
		if (printToExcel) {
		/**
		 * Initializing excel
		 */
			sheet = workbook.createSheet(dbPath.replace("jdbc:postgresql://127.0.0.1:5432/",""));
			rowIndex = 0;
			row = sheet.createRow(rowIndex++);
	        cellIndex = 2;
	        row.createCell(cellIndex++).setCellValue("FinalBeforeSub");
	        row.createCell(++cellIndex).setCellValue("");

	        row.createCell(cellIndex++).setCellValue("Final-Sub");
	        row.createCell(++cellIndex).setCellValue("");

	        row.createCell(cellIndex++).setCellValue("JoinsWIthNoAnswers");

	        row.createCell(cellIndex++).setCellValue("Evaluation");
	        row.createCell(++cellIndex).setCellValue("");

	        row.createCell(cellIndex++).setCellValue("Total");

	        row = sheet.createRow(rowIndex++);
	        cellIndex = 2;
	        row.createCell(cellIndex++).setCellValue("size");
	        row.createCell(cellIndex++).setCellValue("t");
	        row.createCell(cellIndex++).setCellValue("size");
	        row.createCell(cellIndex++).setCellValue("t-sub");
	        row.createCell(cellIndex++).setCellValue("size");
	        row.createCell(cellIndex++).setCellValue("Ans");
	        row.createCell(cellIndex++).setCellValue("t-Ans");
	        row.createCell(cellIndex++).setCellValue("Total");


	        row = sheet.createRow(rowIndex++);
	        cellIndex = 0;
		}
        /**
		 * END Initializing excel
		 */

//		System.out.println("EVALUATION STARTT");
    	Evaluator ev = new Evaluator(dbPath);
//    	System.out.println("EVALUATION INITIALIZED");

		/**
		 * Emptiness optimization
		 */
    	System.out.println("OptPath = " + optPath);
		if (optPath != null && optPath != "") {
			qOpt = new QueryOptimization(optPath);
		}


		for( int i=0 ; i<queries.length ; i++ ) {
			if( queries[i] == null )
				// Either dir does not exist or is not a directory
				return;
	        else if( !queries[i].toString().contains(".svn") && !queries[i].toString().contains(".DS_Store") ){
	        	if (queries[i].toString().contains("01") || queries[i].toString().contains("02"))
	        		continue;
	        	System.out.println( (i+1) + ": " + queries[i]);
        		System.out.println(new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine()));
//	        	start = System.currentTimeMillis();
	        	/** EMPTINESS OPTIMIZATION && RESTRICTED (OPTIMIZED) SUBSUMPTION */
        		Incremental incremental = new Incremental(qOpt,false);
	        	rewriting = incremental.computeUCQRewriting(tBoxAxioms,new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine()), print);
	        	/** NO EMPTINESS OPTIMIZATION && RESTRICTED (OPTIMIZED) SUBSUMPTION */
//        		Incremental incremental = new Incremental();
//	        	rewriting = incremental.computeUCQRewriting(tBoxAxioms,new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine()));
	        	/** OR, in order to run the evaluation using non-restricted subsumption */
//	    		Configuration c = new Configuration();
//	    		c.redundancyElimination=RedundancyEliminationStrategy.Full_Subsumption;
//	    		Incremental incremental = new Incremental( c );
//	    		incremental.computeUCQRewriting(tBoxAxioms,parser.getQuery(queries[i].toString()));

//	        	for (Clause cl: rewriting ) {
////	        		System.out.println(cl.toString() + "\t\t" + cl.getToBeEvaluated());
//	        		System.out.println(cl);
////	        		System.out.println("\n\n");
//	        	}

	        	System.out.println( "Original rew size = " + rewriting.size() );
//
	        	/*
	        	 * DB evaluation
	        	 */
	        	if ( rewriting.size() != 0 )
	        	{
	        		System.out.println("Evaluating rewriting...");
	        		ev.evaluateSQL( null, ev.getSQL( rewriting ), true);
	        	}

	        	/**
	        	 * Write to excel
	        	 */
	        	if (printToExcel) {
		        	if ( cellIndex == 0 ) {
		                row.createCell(cellIndex).setCellValue(dbPath.replace("jdbc:postgresql://127.0.0.1:5432/",""));
		        	}
		        	cellIndex = 1;
		        	row.createCell(cellIndex++).setCellValue(queries[i].toString().replace(queryPath, ""));
		        	row.createCell(cellIndex++).setCellValue(incremental.getRewSizeOriginal());
		            row.createCell(cellIndex++).setCellValue(incremental.getRewTime());
		            row.createCell(cellIndex++).setCellValue(incremental.getRewSize());
		            row.createCell(cellIndex++).setCellValue(incremental.getSubTime());
		            row.createCell(cellIndex++).setCellValue(incremental.getJoinsThatCauseClausesNotToHaveAnswers());
		            row.createCell(cellIndex++).setCellValue(ev.getNumOfAns());
		            row.createCell(cellIndex++).setCellValue(ev.getEvalTime());
		            row.createCell(cellIndex++).setCellValue(ev.getEvalTime()+incremental.getRewTime()+incremental.getSubTime());

		            row = sheet.createRow(rowIndex++);
		            cellIndex = 1;
	        	}
////				totalTime+= (System.currentTimeMillis()-start);
	        	System.out.println("\n\n\n");
	        }
		}

		ev.closeConn();

//		System.out.println( "Finished rewriting " + queries.length + " in " + totalTime + " ms" );
//		System.out.println( "Rew time = " + totalTime );

	}

}