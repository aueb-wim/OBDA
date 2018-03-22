package edu.aueb.NPD;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;

import edu.aueb.queries.ClauseParser;
import edu.aueb.queries.Evaluator;
import edu.aueb.queries.QueryOptimization;
import edu.ntua.image.incremental.Incremental;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RunQueryWEB extends HttpServlet
{
	public static String ontologyFile, queryPath, optPath, path, dbPath, mappings;
	
	public static final DLliteParser parser = new DLliteParser();
	/**
	 * Takes as inputs:
	 * @param the db connection - which has already taken the mappings -
	 * @param the query
	 * @param (the path of?) the ontology
	 * @param (the path of?) the optimization
	 * @return a list of tuples (every tuple is an ArrayList since
	 * we do not know its columns)
	 * @throws Exception 
	 */
	public static Set<String> runQuery(Connection con, String query, ArrayList<PI> tBoxAxioms, QueryOptimization qOpt) throws Exception
	{
		ArrayList<Clause> rewriting = new ArrayList<Clause>();
		//use optimization to cut off queries with no answers
		Incremental incremental = new Incremental(qOpt, false);
		//new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine())
    	rewriting = incremental.computeUCQRewriting(tBoxAxioms, new ClauseParser().parseClause((new BufferedReader(new FileReader(query))).readLine()), false);
    	System.out.println( "Original rew size = " + rewriting.size() );
    	for (Clause cl: rewriting)
    		System.out.println( "caluse " + cl);
    	
    	/*
//    	 * DB evaluation
//    	 */
    	//boolean printZeros = true;
    	Evaluator ev = new Evaluator(dbPath, mappings, true);
    	Set<String> tuples = new HashSet<String>();
    	if ( rewriting.size() != 0 )
    	{
    		System.out.println("Evaluating rewriting...");
    		tuples = ev.evaluateSQLReturnResults( null, ev.getSQLWRTMappings( rewriting ));
    		//printZeros = false;
    	}

    	
		return tuples;
	}
	
	public static Set<String> runQuery2(Connection con, String query, ArrayList<PI> tBoxAxioms, QueryOptimization qOpt) throws Exception
	{
		ArrayList<Clause> rewriting = new ArrayList<Clause>();
		//use optimization to cut off queries with no answers
		Incremental incremental = new Incremental(qOpt, false);
		//new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine())
    	rewriting = incremental.computeUCQRewriting(tBoxAxioms, new ClauseParser().parseClause(query), false);
    	System.out.println( "Original rew size = " + rewriting.size() );
    	for (Clause cl: rewriting)
    		System.out.println( cl);
    	
    	/*
//    	 * DB evaluation
//    	 */
    	//boolean printZeros = true;
    	Evaluator ev = new Evaluator(dbPath, mappings, true);
    	//Evaluator ev = new Evaluator(dbPath, mappings, false);
    	Set<String> tuples = new HashSet<String>();
    	if ( rewriting.size() != 0 )
    	{
    		System.out.println("Evaluating rewriting...");
    		tuples = ev.evaluateSQLReturnResults( null, ev.getSQLWRTMappings( rewriting ));
    		//printZeros = false;
    	}

    	
		return tuples;
	}
	
	public static void main(String[] args)
	{
		setPaths();
		
		Evaluator eval = null;
		ArrayList<PI> tBoxAxioms = null;
		try {
			eval = new Evaluator(dbPath, mappings, true);
			eval.createConnection();
			tBoxAxioms = parser.getAxiomsWithURI(ontologyFile);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Smthin went wrong in the beginnin...");
			System.exit(-1);
		}
		QueryOptimization qOpt = null;
		if (optPath != null && optPath != "") {
			qOpt = new QueryOptimization(optPath);
		}
		
		Set<String> tuples = null;
		try {
			//tuples = runQuery(eval.conn, queryPath, tBoxAxioms, qOpt);
			
			String queryText = "Q(?0,?1,?2) <- http://sws.ifi.uio.no/vocab/npd-v2#ProductionLicence(?0), http://sws.ifi.uio.no/vocab/npd-v2#licenseeForLicence(?44,?0), http://sws.ifi.uio.no/vocab/npd-v2#ProductionLicenceLicensee(?44), http://sws.ifi.uio.no/vocab/npd-v2#dateLicenseeValidFrom(?44,?2), http://sws.ifi.uio.no/vocab/npd-v2#licenseeInterest(?44,?1)";
			
			tuples = runQuery2(eval.conn, queryText, tBoxAxioms, qOpt);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Smthing went wrong while evaluating query....");
			System.exit(-1);
		}
		
		Iterator<String> it = tuples.iterator();
		
		
		if ( tuples.size() == 0 ) {
			System.out.println( "empty :(" );
		}else {
			System.out.println("ok i have something :)" );
		}
		
		while (it.hasNext())
			System.out.println(">>>"+it.next()+"<<<");
	}
	
	public static void setPaths()
	{
		//System.out.println("*** 1 ***");
		Path currentPath = Paths.get("");
		//System.out.println("*** 2 ***");
		String originalPath = currentPath.toAbsolutePath().toString();
		//System.out.println("*** 3 ***");
		path = originalPath + "/Ontologies&Stuff/";
		//System.out.println("*** 4 ***");
		ontologyFile = "file:" + "Ontologies/NPD/npd-v2-ql.owl";
		//System.out.println("*** 5 ***");
		queryPath = path + "/avenet_queriesWithURIs/Q02.txt";
		//System.out.println("*** 6 ***");
		mappings = path + "mappings/mysql/npd-v2-ql-mysql_gstoil_avenet.obda";
		//System.out.println("*** 7 ***");
		optPath = path + "optimizationClausesEntitiesOnly.txt";
		//System.out.println("*** 8 ***");
		String uri = "http://semantics.crl.ibm.com/univ-bench-dl.owl#";
		//System.out.println("*** 9 ***");
		/*
		 * NPD DB
		 */
		dbPath = "jdbc:mysql://localhost:3306/npd";
		//System.out.println("*** 10 ***");
	}
	
	public void setPathsWEB(String originalPath)
	{
		//Path currentPath = Paths.get("");
		//String originalPath = currentPath.toAbsolutePath().toString();
		
		path = originalPath + "/Ontologies&Stuff/";
		
		//ontologyFile = "file:" + "Ontologies/NPD/npd-v2-ql.owl";
		ontologyFile = "file:" + originalPath + "/Ontologies/NPD/npd-v2-ql.owl";
		queryPath = path + "/avenet_queriesWithURIs/Q01.txt";
		mappings = path + "mappings/mysql/npd-v2-ql-mysql_gstoil_avenet.obda";
		optPath = path + "optimizationClausesEntitiesOnly.txt";

		String uri = "http://semantics.crl.ibm.com/univ-bench-dl.owl#";
		/*
		 * NPD DB
		 */
		
		dbPath = "jdbc:mysql://localhost:3306/npd";

		//dbPath = "jdbc:postgres://localhost:5432/npd";

		
	}
	
	public static String GetDbPath( ) {
		return ( dbPath );
	}
	
	public static Set<String> getTuples(String query)
	{
		Evaluator eval = null;
		ArrayList<PI> tBoxAxioms = null;
		try {
			eval = new Evaluator(dbPath, mappings, true);
			eval.createConnection();
			tBoxAxioms = parser.getAxiomsWithURI(ontologyFile);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Smthin went wrong in the beginnin...");
			System.exit(-1);
		}
		QueryOptimization qOpt = null;
		if (optPath != null && optPath != "") {
			qOpt = new QueryOptimization(optPath);
		}
		
		Set<String> tuples = null;
		try {
			tuples = runQuery2(eval.conn, query, tBoxAxioms, qOpt);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Smthing went wrong while evaluating query....");
			System.exit(-1);
		}
		return tuples;
	}

}
