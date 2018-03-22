package edu.ntua.image.incremental;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;
import org.oxford.comlab.perfectref.rewriter.Substitution;
import org.oxford.comlab.perfectref.rewriter.Term;

import edu.ntua.image.datastructures.LabeledGraph;

public class IncExample {

	private static final DLliteParser parser = new DLliteParser();

	private int orderedQueryIndex=0;

	public static void main(String[] args) throws Exception{

		String ontologyFile;
		String queryFile;
		ArrayList<PI> pis = new ArrayList<PI>();
		Incremental incremental = new Incremental();

		String path = System.getProperty("user.dir")+ "/dataset/Evaluation_ISWC'09/";

//		run all Hector's Ontologies and queries
		runEvaluation(path);
		
//		//run Martha's queries
//		runEvaluation2(System.getProperty("user.dir")+ "/dataset/Marthas/");

//
//		queryFile = System.getProperty("user.dir")+ "/dataset/Tests/queries.txt";
//		ontologyFile = "file:" + path + "Ontologies/S.owl";
////
//		pis = parser.getAxioms(ontologyFile);
//
//		ArrayList<PI> tBoxAxioms = new ArrayList<PI>();
//		for ( PI p : pis ) {
////			PI newPI = new PI(p.m_type, p.m_left.replace("$", "-"), p.m_right.replace("$", "-"));
//			tBoxAxioms.add( new PI(p.m_type, p.m_left.replace("$", "-"), p.m_right.replace("$", "-")));
////			System.out.println( p.m_left + "\t" + p.m_right + "\t\t" + newPI.m_left + "\t" + newPI.m_right);
//		}
//		Clause originalQuery = parser.getQuery(queryFile);
//		System.out.println("Original Query = " + originalQuery);
//		ArrayList<Clause> result = incremental.computeUCQRewriting(tBoxAxioms,originalQuery);
//
////		int i = 1;
////		for ( Clause c :result )
////			System.out.println( i++ + " : " + c);

	}
	
	private static void runEvaluation2(String path) throws Exception {
		String ontologyFile,queryPath="";

//		
////		System.out.println( "Starting test for ontology P5X.owl");
		ontologyFile = "file:" + path + "Ontologies/P5X.owl";
		queryPath = path + "Queries/P5X/";
		runTest(ontologyFile,queryPath);
//		
////		System.out.println( "Starting test for ontology A.owl");
//		ontologyFile = "file:" + path + "Ontologies/A.owl";
//		queryPath = path + "Queries/A/";
//		runTest(ontologyFile,queryPath);
//		
////		System.out.println( "Starting test for ontology AX.owl");
//		ontologyFile = "file:" + path + "Ontologies/AX.owl";
//		queryPath = path + "Queries/AX/";
//		runTest(ontologyFile,queryPath);
//		
////		System.out.println( "Starting test for ontology S.owl");
//		ontologyFile = "file:" + path + "Ontologies/S.owl";
//		queryPath = path + "Queries/S/";
//		runTest(ontologyFile,queryPath);
//		
////		System.out.println( "Starting test for ontology U.owl");
//		ontologyFile = "file:" + path + "Ontologies/U.owl";
//		queryPath = path + "Queries/U/";
//		runTest(ontologyFile,queryPath);
//		
////		System.out.println( "Starting test for ontology UX.owl");
//		ontologyFile = "file:" + path + "Ontologies/UX.owl";
//		queryPath = path + "Queries/UX/";
//		runTest(ontologyFile,queryPath);
//		
////		System.out.println( "Starting test for ontology V.owl");
//		ontologyFile = "file:" + path + "Ontologies/V.owl";
//		queryPath = path + "Queries/V/";
//		runTest(ontologyFile,queryPath);
	}

	private static void runEvaluation(String path) throws Exception {
		String ontologyFile,queryPath="";

		System.out.println( "Starting test for ontology V.owl");
		ontologyFile = "file:" + path + "Ontologies/V.owl";
		queryPath = path + "Queries/V/";
		runTest(ontologyFile,queryPath);

		System.out.println( "Starting test for ontology P1.owl");
		ontologyFile = "file:" + path + "Ontologies/P1.owl";
		queryPath = path + "Queries/P1/";
		runTest(ontologyFile,queryPath);

		System.out.println( "Starting test for ontology P5.owl");
		ontologyFile = "file:" + path + "Ontologies/P5.owl";
		runTest(ontologyFile,queryPath);

		System.out.println( "Starting test for ontology P5X.owl");
		ontologyFile = "file:" + path + "Ontologies/P5X.owl";
		runTest(ontologyFile,queryPath);

		System.out.println( "Starting test for ontology S.owl");
		ontologyFile = "file:" + path + "Ontologies/S.owl";
		queryPath = path + "Queries/S/";
		runTest(ontologyFile,queryPath);

		System.out.println( "Starting test for ontology U.owl");
		ontologyFile = "file:" + path + "Ontologies/U.owl";
		queryPath = path + "Queries/U/";
		runTest(ontologyFile,queryPath);

		System.out.println( "Starting test for ontology UX.owl");
		ontologyFile = "file:" + path + "Ontologies/UX.owl";
		runTest(ontologyFile,queryPath);

		System.out.println( "Starting test for ontology A.owl");
		ontologyFile = "file:" + path + "Ontologies/A.owl";
		queryPath = path + "Queries/A/";
		runTest(ontologyFile,queryPath);

		System.out.println( "Starting test for ontology AX.owl");
		ontologyFile = "file:" + path + "Ontologies/AX.owl";
		runTest(ontologyFile,queryPath);
	}

	private static void runTest(String ontologyFile, String queryPath) throws Exception {
		long start=0,totalTime = 0;
		ArrayList<PI> tBoxAxioms = parser.getAxioms(ontologyFile);

		File queriesDir = new File( queryPath );
		File[] queries = queriesDir.listFiles();
		System.out.println( queriesDir );
		for( int i=0 ; i<queries.length ; i++ ) {
	        if( queries[i] == null )
	    	   // Either dir does not exist or is not a directory
	    	   return;
	        else if( !queries[i].toString().contains(".svn") ) {//&& !queries[i].toString().contains("QTB") ) {
	        	start = System.currentTimeMillis();
				new Incremental().computeUCQRewriting(tBoxAxioms,parser.getQuery(queries[i].toString()));
				totalTime+= (System.currentTimeMillis()-start);
	    		/** OR, in order to run the evaluation using non-restricted subsumption */
//	    		Configuration c = new Configuration();
//	    		c.redundancyElimination=RedundancyEliminationStrategy.Full_Subsumption;
//	    		Incremental incremental = new Incremental( c);
//	    		incremental.computeUCQRewriting(tBoxAxioms,parser.getQuery(queries[i].toString()));
	        }
		}
//		System.out.println( "Finished rewriting " + queries.length + " in " + totalTime + " ms" );
//		System.out.println( totalTime );
	}
	

	private void call(Term currentVar, LabeledGraph<Term, Term, Term> queryGraph, Set<Term> visitedNodes, Term[] body) {
    	if (visitedNodes.contains(currentVar))
    		return;
    	visitedNodes.add( currentVar );
    	for( Term nodeLabel : queryGraph.getLabelsOfNode(currentVar))
    		body[orderedQueryIndex++] = nodeLabel;
    	for( LabeledGraph<Term,Term,Term>.Edge edge : queryGraph.getSuccessors(currentVar)) {
    		body[orderedQueryIndex++] = edge.getEdgeLabel();
    		call(edge.getToElement(),queryGraph,visitedNodes,body);
    	}
	}

	private static void printClausesToFile(String outputFilestr, ArrayList<Clause> rewriting) throws Exception{
		int counter = 0;
		File outputFile = new File(outputFilestr + "res.txt");
//		while (outputFile.exists()) {
//			counter++;
//			outputFile = new File(outputFilestr + counter + ".txt");
//		}

       FileWriter out = new FileWriter(outputFile);
//        out.write("==================SUMMARY==================\n");
//        out.write("Ontology file:             " + ontologyFile.substring(ontologyFile.lastIndexOf("/") + 1) + "\n");
//        out.write("Query:                     " + query + "\n");
//        out.write("Number of assertions:      " + numberOfInclusionAssertions + "\n");
//		out.write("Number of concept symbols: " + numberOfConcepts + "\n");
//		out.write("Number of role symbols:    " + numberOfRoles + "\n");
//		out.write("Running time:              " + time + " milliseconds \n");
//		out.write("Size of the rewriting (queries):     " + rewritingSize + "\n");
//		int size = 0;
//		for(Clause c: rewriting){
//			size += c.toString().length();
//		}
//		out.write("Size of the rewriting (symbols):     " + size + "\n");

		Collections.sort(rewriting, new Comparator<Clause>(){
			public int compare(Clause c1, Clause c2){
			    return c1.m_canonicalRepresentation.compareTo(c2.m_canonicalRepresentation);
			}
		});

		int i = 0;
		for(Clause c: rewriting)
			if ( !(c.getSubsumer()==null) )
				out.write(i++ + ":1" + c.m_canonicalRepresentation + "\n");
			else
				out.write(i++ + ":0" + c.m_canonicalRepresentation + "\n");
//		out.write("==================SUMMARY==================\n");
       out.close();
	}

	private static Term findUnificationForterm( Set<Substitution> unifications , Term term) {

		for ( Substitution sub : unifications )
			if ( sub.containsKey( term ) ) {
				System.out.println( sub.get( term ) );
				return sub.get( term );
			}
		return null;
	}

}