package edu.aueb.NPD;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	static String originalPath;
	//static String originalPath = "/Users/avenet/Academia/Ntua/Ontologies/"; 
	//static String excelFile = "/Users/avenet/Academia/Aueb/Research/IncrementalQueryAnswering/EvaluationResults/NPD/IQAROS/DB/DB-EntitiesEmptiness-NEWMappings.xlsx";
	static String excelFile = "IQAROSMappingsOutput.xlsx"; 
	static String mappings;
	static String uri = "";

	static boolean print2Excel = true;
	static String addon = "";

	public static void main(String[] args) throws Exception{
		PropertyConfigurator.configure("./logger.properties");
		
		Path currentPath = Paths.get("");
		originalPath = currentPath.toAbsolutePath().toString();

		String ontologyFile, queryPath, optPath, path, dbPath;
		/**
		 * NPD Tests
		 */
		//path = originalPath + "npd-benchmark-master/";
		path = originalPath + "/Ontologies&Stuff/";
		//ontologyFile = "file:" + path + "ontology/npd-v2-ql.owl";
		ontologyFile = "file:" + "Ontologies/NPD/npd-v2-ql.owl";
//		ontologyFile = "file:" + path + "ontology/npd-v2-ql_checkExistentials.owl";
//		queryPath = path + "avenet_queriesWithURIs/";
		queryPath = path + "/avenet_queriesWithURIs/";
		mappings = path + "mappings/mysql/npd-v2-ql-mysql_gstoil_avenet.obda";
//		optPath = "";
//		optPath = path + "OptimizationClauses/optimizationClauses_npd-v2-ql-mysql_gstoil_avenet.obdav2.txt";
//		optPath = path + "OptimizationClauses/optimizationClausesEntitiesOnly.txt";
//		optPath = path + "OptimizationClauses/optimizationClausesEntitiesOnly.txt";
		optPath = path + "optimizationClausesEntitiesOnly.txt";
		uri = "http://semantics.crl.ibm.com/univ-bench-dl.owl#";
		/*
		 * NPD DB
		 */
		dbPath = "jdbc:mysql://localhost:3306/npd";
		System.out.println("**************************");
		System.out.println("**\tNPD\t\t**");
		System.out.println("**************************");
		runTest(ontologyFile, queryPath, optPath, dbPath, true, 1, print2Excel);
		
//		print2Excel = false;
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

	private static void runTest(String ontologyFile, String queryPath, String optPath, String dbPath, boolean print, int limit, boolean printToExcel) throws Exception 
	{
		long start=0,totalTime = 0;

		ArrayList<PI> tBoxAxioms = parser.getAxiomsWithURI(ontologyFile);
		ArrayList<Clause> rewriting = new ArrayList<Clause>();

//		System.out.println(tBoxAxioms.size());
//		System.exit(0);
		
		QueryOptimization qOpt = null;

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
			sheet = workbook.createSheet(dbPath.replace("jdbc:mysql://localhost:3306/",""));
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

		/**
		 * Emptiness optimization
		 */
		if (optPath != null && optPath != "") {
			qOpt = new QueryOptimization(optPath);
		}

		int queryIndex=1;
    	Evaluator ev = new Evaluator(dbPath,mappings,true);
		for( int i=0 ; i<queries.length ; i++ ) {
			System.out.println(i + " --- " + queries[i]);
			if( queries[i] == null ) {
				System.out.println(i + " " + queries[i]);
				// Either dir does not exist or is not a directory
				return;
			}
	        else if( !queries[i].toString().contains(".svn") && !queries[i].toString().contains(".DS_Store") && !queries[i].isDirectory() && !queries[i].toString().contains(".zip")){
//	        	if (!queries[i].toString().contains("test") )
//	        		continue;
	        	/*if (queries[i].toString().contains("05") || queries[i].toString().contains("06") || queries[i].toString().contains("09") 
	        			|| queries[i].toString().contains("11") || queries[i].toString().contains("23") 
	        			|| queries[i].toString().contains("26") || queries[i].toString().contains("27") 
	        			|| queries[i].toString().contains("28") || queries[i].toString().contains("29") )*/
	        	if (!queries[i].toString().equals("/media/kostis/EEB89555B8951D61/Users/Kostas/workspace/IQAROS/Ontologies&Stuff/avenet_queriesWithURIs/Q01.txt"))
//	        	if (queries[i].toString().contains("04"))
	        		continue;
	        	System.out.println( queryIndex++ + ": " + queries[i]);
        		System.out.println(new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine()));
//	        	start = System.currentTimeMillis();
	        	//use optimization to cut off queries with no answers
        		Incremental incremental = new Incremental(qOpt,false);
	        	rewriting = incremental.computeUCQRewriting(tBoxAxioms,new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine()), print);
	        	//no optimization
//        		Incremental incremental = new Incremental();
//	        	rewriting = incremental.computeUCQRewriting(tBoxAxioms,new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine()));
	        	/** OR, in order to run the evaluation using non-restricted subsumption */
//	    		Configuration c = new Configuration();
//	    		c.redundancyElimination=RedundancyEliminationStrategy.Full_Subsumption;
//	    		Incremental incremental = new Incremental( c );
//	    		incremental.computeUCQRewriting(tBoxAxioms,parser.getQuery(queries[i].toString()));

	        	System.out.println( "Original rew size = " + rewriting.size() );
	        	for (Clause cl: rewriting ) {
//	        		System.out.println(cl.toString());
	        		System.out.println(cl);
//	        		System.out.println("\n\n");
	        	}

//	        	/*
//	        	 * DB evaluation
//	        	 */
	        	boolean printZeros = true;
	        	if ( rewriting.size() != 0 )
	        	{
	        		System.out.println("Evaluating rewriting...");
	        		ev.evaluateSQL( null, ev.getSQLWRTMappings( rewriting ), true);
	        		printZeros = false;
	        	}

	        	/**
	        	 * Write to excel
	        	 */
	        	if (printToExcel) {
		        	if ( cellIndex == 0 ) {
		                row.createCell(cellIndex).setCellValue(dbPath.replace("jdbc:postgresql://127.0.0.1:5432/",""));
		        	}
		        	cellIndex = 1;
		        	row.createCell(cellIndex++).setCellValue("Query_" + queries[i].toString().replace("/Users/avenet/Academia/Ntua/Ontologies/npd-benchmark-master/avenet_queriesWithURIs/", ""));
		        	row.createCell(cellIndex++).setCellValue(incremental.getRewSizeOriginal());
		            row.createCell(cellIndex++).setCellValue(incremental.getRewTime());
		            row.createCell(cellIndex++).setCellValue(incremental.getRewSize());
		            row.createCell(cellIndex++).setCellValue(incremental.getSubTime());
		            row.createCell(cellIndex++).setCellValue(incremental.getJoinsThatCauseClausesNotToHaveAnswers());
		            if (printZeros) {
			            row.createCell(cellIndex++).setCellValue(0);
			            row.createCell(cellIndex++).setCellValue(0);
			            row.createCell(cellIndex++).setCellValue(0);
		            }
		            else
		            {
			            row.createCell(cellIndex++).setCellValue(ev.getNumOfAns());
			            row.createCell(cellIndex++).setCellValue(ev.getEvalTime());
			            row.createCell(cellIndex++).setCellValue(ev.getEvalTime()+incremental.getRewTime()+incremental.getSubTime());

		            }

		            row = sheet.createRow(rowIndex++);
		            cellIndex = 1;
	        	}
//				totalTime+= (System.currentTimeMillis()-start);
	        	System.out.println("\n\n\n");
	        }
	        else {
	        	System.out.println(i + " " + queries[i]);
	        }
		}
		ev.closeConn();

//		System.out.println( "Finished rewriting " + queries.length + " in " + totalTime + " ms" );
//		System.out.println( "Rew time = " + totalTime );

	}

}