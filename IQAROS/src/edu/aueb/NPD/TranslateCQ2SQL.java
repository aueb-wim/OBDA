package edu.aueb.NPD;
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


public class TranslateCQ2SQL {

	static Workbook workbook = new XSSFWorkbook();
	private static final DLliteParser parser = new DLliteParser();

	static String originalPath = "/Users/avenet/Academia/Ntua/Ontologies/";
	static String excelFile = "/Users/avenet/Academia/Aueb/Research/IncrementalQueryAnswering/EvaluationResults/NPD/IQAROS/DB/IQAROS_avenetODBA_EmpitnessOpt_MappingOpt_new_rsClose.xlsx";
	static String mappings;
	static String uri = "";

	static boolean print2Excel = false;
	static String addon = "";

	public static void main(String[] args) throws Exception{
		PropertyConfigurator.configure("./logger.properties");

		String ontologyFile, queryPath, optPath, path, dbPath;
		/**
		 * NPD Tests
		 */
		path = originalPath + "npd-benchmark-master/";
		ontologyFile = "file:" + path + "ontology/npd-v2-ql.owl";
		queryPath = path + "avenet_queries/";
		mappings = path + "mappings/mysql/npd-v2-ql-mysql_gstoil_avenet.obda";
//		optPath = "";
		optPath = path + "OptimizationClauses/optimizationClauses_npd-v2-ql-mysql_gstoil_avenet.obda.txt";
		uri = "http://semantics.crl.ibm.com/univ-bench-dl.owl#";
		/*
		 * NPD DB
		 */
		dbPath = "jdbc:mysql://localhost:3306/npd";
		System.out.println("**************************");
		System.out.println("**\tNPD\t\t**");
		System.out.println("**************************");
//		runTest(ontologyFile, queryPath, optPath, dbPath, true, 1, print2Excel);
		
		if ( print2Excel ) {
			FileOutputStream fos = new FileOutputStream(excelFile);
	        workbook.write(fos);
	        fos.close();
		}
	}

	/*private static void runTest(String ontologyFile, String queryPath, String optPath, String dbPath, boolean print) throws Exception {
		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0);
	}*/

	/*private static void runTest(String ontologyFile, String queryPath, String optPath, String dbPath, boolean print, int limit) throws Exception {
		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, false);
	}*/

	/*private static void runTest(String ontologyFile, String queryPath, String optPath, String dbPath, boolean print, int limit, boolean printToExcel) throws Exception {
		long start=0,totalTime = 0;

		ArrayList<PI> tBoxAxioms = parser.getAxioms(ontologyFile);
		ArrayList<Clause> rewriting = new ArrayList<Clause>();


	   Clause c = new ClauseParser().parseClause("");

    	ev.getSQLWRTMappings( rewriting ), true);
		ev.closeConn();

//		System.out.println( "Finished rewriting " + queries.length + " in " + totalTime + " ms" );
//		System.out.println( "Rew time = " + totalTime );

	}*/

}