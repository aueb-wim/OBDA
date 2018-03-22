import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;

import edu.aueb.queries.ClauseParser;
import edu.aueb.queries.Evaluator;
import edu.aueb.queries.QueryOptimization;
import edu.ntua.image.incremental.Incremental;

public class IncEval {

	private static final DLliteParser parser = new DLliteParser();

	private int orderedQueryIndex=0;

	public static void main(String[] args) throws Exception{

		String path = "/Users/avenet/Academia/Ntua/Ontologies/";
		
		
//		String ontologyFile = "file:" + path + "LUBM/univ-bench_DL-Lite_mine.owl";
//		String queryPath = path + "LUBM/QueriesNoConstants/";
//		
		String ontologyFile = "file:" + path + "UOBM/univ-bench-dl-Zhou_DL-Lite_mine.owl";
		String queryPath = path + "UOBM/QueriesWithConstants/";
		String optPath = path + "UOBM/OptimizationClauses/optimizationClausesUOBM.txt";

//		String ontologyFile = "file:" + path + "LUBM/univ-bench_DL-Lite_mine.owl";
//		String queryPath = path + "LUBM/QueriesWithConstants/";
//		String optPath = path + "LUBM/OptimizationClauses/optimizationClausesLUBM.txt";
		
//		String optPath = null;
//		
//		String ontologyFile = "file:" + path + "LUBM/univ-bench_DL-Lite_owlapi.owl";
//		String queryPath = path + "LUBM/QueriesNoConstants/";
		
//		runTest(ontologyFile, queryPath, optPath, false);
//		runTest(ontologyFile, queryPath, optPath, false);
		runTest(ontologyFile, queryPath, optPath, true);
	
	}
	
	private static void runTest(String ontologyFile, String queryPath, String optPath, boolean print) throws Exception {
		long start=0,totalTime = 0;
		ArrayList<PI> tBoxAxioms = parser.getAxioms(ontologyFile);
		ArrayList<Clause> rewriting = new ArrayList<Clause>();
		
		QueryOptimization qOpt = null;
		
		
		/**
		 * Emptiness optimization
		 */
		if (optPath != null && optPath != "") {
			long startOpt = System.currentTimeMillis();
			qOpt = new QueryOptimization(optPath);
            System.out.println("Optimization Took " + (System.currentTimeMillis() - startOpt) + "ms");
		}


		File queriesDir = new File( queryPath );
		File[] queries = queriesDir.listFiles();
		if (!print)
			System.out.println("...Warming up...");
//		System.out.println( queriesDir );
		for( int i=0 ; i<queries.length ; i++ ) {
			if( queries[i] == null )
				// Either dir does not exist or is not a directory
				return;
	        else if( !queries[i].toString().contains(".svn") && !queries[i].toString().contains(".DS_Store") ){ 
//	        		&& !queries[i].toString().contains("Query_11.txt")) {
//	        	if (print)
//	        		System.out.println(queries[i] + "\n" + parser.getQuery(queries[i].toString()));
        		System.out.println(queries[i] + "\n" + new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine()));
//	        	start = System.currentTimeMillis();
	        	//use optimization to cut off queries with no answers
	        	rewriting = new Incremental(qOpt).computeUCQRewriting(tBoxAxioms,new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine()), print);
	        	//no optimization
//	        	rewriting = new Incremental().computeUCQRewriting(tBoxAxioms,new ClauseParser().parseClause((new BufferedReader(new FileReader(queries[i]))).readLine()));
	        	/** OR, in order to run the evaluation using non-restricted subsumption */
//	    		Configuration c = new Configuration();
//	    		c.redundancyElimination=RedundancyEliminationStrategy.Full_Subsumption;
//	    		Incremental incremental = new Incremental( c );
//	    		incremental.computeUCQRewriting(tBoxAxioms,parser.getQuery(queries[i].toString()));

	        	for (Clause cl: rewriting )
	        		System.out.println(cl + "\t");

	        	if ( rewriting.size() != 0 )
	        	{
	        		Evaluator ev = new Evaluator();
	        		ev.evaluateSQL( null, ev.getSQL( rewriting ), true);
	        		ev.closeConn();
	        	}
////				
				if (print)
					System.out.println("\n\n");

//				totalTime+= (System.currentTimeMillis()-start);
//	        	System.out.println("\n\n\n");
	        }
		}
//		System.out.println( "Finished rewriting " + queries.length + " in " + totalTime + " ms" );
//		System.out.println( "Rew time = " + totalTime );
		
	}
	

}