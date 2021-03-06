package edu.aueb.NPD;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;
import org.oxford.comlab.perfectref.rewriter.Substitution;
import org.oxford.comlab.perfectref.rewriter.Term;

import owlim.OWLimQueryEvaluator;
import edu.aueb.queries.ClauseParser;
import edu.aueb.queries.Evaluator;
import edu.ntua.image.Configuration;
import edu.ntua.image.Configuration.RedundancyEliminationStrategy;
import edu.ntua.image.datastructures.LabeledGraph;
import edu.ntua.image.incremental.Incremental;


public class EvaluateMappings {

	static Workbook workbook = new XSSFWorkbook();
	private static final DLliteParser parser = new DLliteParser();

	static String originalPath = "/Users/avenet/Academia/Ntua/Ontologies/";
	static String excelFile = "/Users/avenet/Academia/Aueb/Research/IncrementalQueryAnswering/IQAROS_opt=true_DB_lubm_ex_noJoinOpt.xlsx";
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
		mappings = path + "mappings/mysql/npd-v2-ql-mysql_gstoil_sample.obda";
		optPath = path + "OptimizationClauses/atomicConceptsRoles.txt";
		uri = "http://semantics.crl.ibm.com/univ-bench-dl.owl#";
		/*
		 * NPD DB
		 */
		dbPath = "jdbc:mysql://127.0.0.1:3306/npdfactpages";
		System.out.println("**************************");
		System.out.println("**\tNPD\t\t**");
		System.out.println("**************************");
		runTest(ontologyFile, queryPath, optPath, dbPath, true, 1, print2Excel);
	}

	private static void runTest(String ontologyFile, String queryPath, String optPath, String dbPath, boolean print) throws Exception {
		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0);
	}

	private static void runTest(String ontologyFile, String queryPath, String optPath, String dbPath, boolean print, int limit) throws Exception {
		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, false);
	}
	
	private static void runTest(String ontologyFile, String queryPath, String optPath, String dbPath, boolean print, int limit, boolean printToExcel) throws SQLException {
		long start=0,totalTime = 0;
		
       	Evaluator ev = null;
		try {
			ev = new Evaluator(dbPath,mappings);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(-1);
		}
       	Set<String> selectQueries = ev.mappingManager.getSpjs();
       	Map<String, String> atomsToSPJs = ev.mappingManager.atomsToSPJ;
       	
       	for ( String str : atomsToSPJs.keySet() )
       	{
       		String[] result = atomsToSPJs.get(str).split(" UNION ");
           	for (int x=0; x<result.length; x++)
                System.out.println(result[x]);
       	}
       	System.exit(0);
       	int counter = 1;
       	for ( String str: selectQueries ) {
       		System.out.println(counter++ + ": " + str);
       		try {
				ev.evaluateSQL( null, str, true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}