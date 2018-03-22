package edu.aueb.NPD;


import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.coode.owl.rdf.turtle.TurtleOntologyFormat;
import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChangeException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;

import edu.aueb.queries.Evaluator;


public class FindContainmentAmongAnswers {

	static Workbook workbook = new XSSFWorkbook();
	private static final DLliteParser parser = new DLliteParser();

	static String originalPath = "/Users/avenet/Academia/Ntua/Ontologies/";
	static String excelFile = "/Users/avenet/Academia/Aueb/Research/IncrementalQueryAnswering/IQAROS_opt=true_DB_lubm_ex_noJoinOpt.xlsx";
	static String mappingFile;
	static String uri = "";

	static final String npdv = "http://sws.ifi.uio.no/vocab/npd-v2#";
	static final String ptl = "http://sws.ifi.uio.no/vocab/npd-v2-ptl#";

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
		mappingFile = path + "mappings/mysql/npd-v2-ql-mysql_gstoil_avenet.obda";
		optPath = path + "OptimizationClauses/atomicConceptsRoles.txt";
		/*
		 * NPD DB
		 */
		dbPath = "jdbc:mysql://127.0.0.1:3306/npd";
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

	/**
	 * The functionality of this class has been transfered to the Evaluator.java class
	 * 
	 * @param ontologyFile
	 * @param queryPath
	 * @param optPath
	 * @param dbPath
	 * @param print
	 * @param limit
	 * @param printToExcel
	 * @throws Exception
	 */
	private static void runTest(String ontologyFile, String queryPath, String optPath, String dbPath, boolean print, int limit, boolean printToExcel) throws Exception {

		/**
		 * Connect to DB to get instances of concepts and roles
		 */
//		ev.seeForeignKeys();
		

		long start = System.currentTimeMillis();
       	Evaluator ev = new Evaluator(dbPath,mappingFile);
//       	ev.seeForeignKeys();
       	Map<String, String> atomsToSPJs = ev.mappingManager.getAtomsToSPJ();
       	Map<String, Set<String>> atomsToNonRedundantSQLQueries = new HashMap<String, Set<String>>();
       	Map<String, Set<String>> atomToSQL = new HashMap<>();
       	int counter = 0;
       	String[] result;
       	for ( String str : atomsToSPJs.keySet() )
       	{
       		counter++;
           	Set<Map<String,Set<String>>> sqlToAnswers = new HashSet<Map<String, Set<String>>>();
       		ArrayList<Set<String>> answers = new ArrayList<Set<String>>();
//       		System.out.println(str);
       		Set<String> nonRedundantSQLs = new HashSet<String>();
       		result = atomsToSPJs.get(str).split(" UNION ");
       		for (int x=0; x<result.length; x++) {
           		String sqlQueries = result[x];
           		nonRedundantSQLs.add(sqlQueries);
//                System.out.println("\t" + sqlQueries);
           		Set<String> ans = ev.evaluateSQLReturnResults(null, sqlQueries);
//                if (ans.size()>0)
                answers.add(ans);
               	Map<String,Set<String>> sqlToAnswer = new HashMap<String, Set<String>>();
                sqlToAnswer.put(sqlQueries, ans);
                sqlToAnswers.add(sqlToAnswer);
//                answersToSQL.put(ans, sqlQueries);
           	}
       		System.out.println(str + "\t" + answers.size());

       		Set<String> redundantSQLs = new HashSet<String>();
       		for ( Map<String,Set<String>> sql2ans1: sqlToAnswers ) {
       			Set<String> sqls1 = sql2ans1.keySet();
       			for (String sql1: sqls1) {
       				if ( redundantSQLs.contains(sql1) )
       					continue;
       				
       				Set<String> sql2answers1 = sql2ans1.get(sql1);

       	       		for ( Map<String,Set<String>> sql2ans2: sqlToAnswers ) {
       	       			Set<String> sqls2 = sql2ans2.keySet();

       	       			for (String sql2: sqls2) {
       	       				if (!sql1.equals(sql2)) {
	       	       				Set<String> sql2answers2 = sql2ans2.get(sql2);

	       	       				if (containsAnswers(sql2answers1,sql2answers2)) {
	       	       					nonRedundantSQLs.remove(sql2);
	       	       					redundantSQLs.add(sql2);
	       	       					System.out.println("\tSQL " + sql2answers1.size() + "\n\tsubsumes" + sql2answers2.size() );
	       	       				}
       	       				}
       	       			}
       	       		}
       			}
       		}
       		atomsToNonRedundantSQLQueries.put(str, nonRedundantSQLs);


//       		for (int i = 0 ; i < answers.size() ; i++) {
//       			Set<String> ansi = answers.get(i);
//       			for (int j = 0; j < answers.size() ; j++ ) {
//       				Set<String> ansj = answers.get(j);
//       				if ( i != j && ansi.size() != 0 && ansj.size() != 0  )
//       				{
////       	       			System.out.println("\tset2 answersize = " + answers.get(j).size());
//       					if (containsAnswers(ansi,ansj))
//       					{
////       						System.out.println(answers.get(i));
////       						System.out.println(answers.get(j));
//       						System.out.println("\tSet " + i + " (" + answers.get(i).size() + ") contains Set " + j + " ("+ answers.get(j).size() + ")" );
//       						if (Objects.equals(answers.get(i),answers.get(j))) {
//       							System.out.println( "Objects are equal: " + answers.get(i).equals(answers.get(j)));
//       							System.out.println("--");
//       						}
////       						System.out.println(answersToSQL.keySet().size());
//
//       					}
//       				}
//       			}
//       		}
//           	atomToSQL.put(str, set);
       	}

//		String existingSQL = atomsToSPJ.get(atom);
//		if (existingSQL!=null)
//			atomsToSPJ.put(atom, existingSQL+" UNION " + select);
//		else
//			atomsToSPJ.put(atom, select);
       	Map<String, String> atomsToNonRedundantSPJ = new HashMap<String,String>();
       	for ( String atom: atomsToNonRedundantSQLQueries.keySet() ) {
       		System.out.println(atom + ": " + atomsToNonRedundantSQLQueries.get(atom).size() );
       		Set<String> sqls = atomsToNonRedundantSQLQueries.get(atom);
       		for (String sql: sqls) {
       			String existingSQL = atomsToNonRedundantSPJ.get(atom);
       			if (existingSQL!=null)
       				atomsToNonRedundantSPJ.put(atom, existingSQL+" UNION " + sql);
       			else
       				atomsToNonRedundantSPJ.put(atom, sql);

       		}
       	}
       	ev.mappingManager.setAtomsToSPJ(atomsToNonRedundantSPJ);
       	

       	System.out.println("\nPerformed in " + ((System.currentTimeMillis() - start)) + "ms");
       	System.out.println(counter);
		ev.closeConn();
	}

	private static boolean containsAnswers(Set<String> set1, Set<String> set2) {

		if (set1 ==null || set2 == null)
			System.out.println("null");
		if ( set2.size() == 0 )
			return true;

		for (String set2Ans: set2) {
			if ( !set1.contains(set2Ans) )
			{
				return false;
			}
		}

		return true;
	}


}