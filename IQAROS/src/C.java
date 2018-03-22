import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.oxford.comlab.perfectref.optimizer.Optimizer;
import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;
import org.oxford.comlab.perfectref.rewriter.Saturator;
import org.oxford.comlab.perfectref.rewriter.Saturator_Tree;
import org.oxford.comlab.perfectref.rewriter.Substitution;
import org.oxford.comlab.perfectref.rewriter.Term;
import org.oxford.comlab.perfectref.rewriter.TermFactory;
import org.oxford.comlab.perfectref.rewriter.Variable;

import edu.ntua.image.datastructures.Tree;
import edu.ntua.image.datastructures.TreeNode;
import edu.ntua.image.redundancy.RedundancyElimination;
import edu.ntua.image.redundancy.SimpleRedundancyEliminator;

public class C {

	private static final TermFactory m_termFactory = new TermFactory();
	private static final DLliteParser parser = new DLliteParser();

	private final Thread thisThread = Thread.currentThread();
	private final int timeToRun = 120000; // 2 minutes;

	private static Optimizer m_optimizer;

	static boolean finished=false;

	public static void main(String[] args) throws Exception{

		m_optimizer = new Optimizer(m_termFactory);

		String ontologyFile;
		String queryFile;
		ArrayList<PI> pis = new ArrayList<PI>();
		ArrayList<Clause> saturation = new ArrayList<Clause>();
		RedundancyElimination redundancyEliminator = new SimpleRedundancyEliminator();

//		if(args.length == 3){

		String path = System.getProperty("user.dir")+ "/dataset/Evaluation_ISWC'09/";

		queryFile = System.getProperty("user.dir")+ "/dataset/Tests/queries.txt";
		ontologyFile = "file:" + path + "Ontologies/S.owl";

		System.out.println( path );

		ArrayList<PI> newPIs = new ArrayList<PI>();

		pis = parser.getAxioms(ontologyFile);

//		ArrayList<PI> newPIs = new ArrayList<PI>();
//		for ( PI p : pis )
//			newPIs.add( new PI(p.m_type, p.m_left.replace("$", "-"), p.m_right.replace("$", "-")));

		Clause originalQuery = parser.getQuery(queryFile);
		Term[] queryBody = originalQuery.getBody();
		Term[] newQueryBody = new Term[ queryBody.length -1 ];

		System.out.println( originalQuery );


//		for ( int i = 0 ; i < queryBody.length - 1 ; i++ )
//			newQueryBody[i] = queryBody[i];
//		newQueryBody[0] = queryBody[0];
//		newQueryBody[1] = queryBody[2];

//		newQueryBody[0] = queryBody[1];
//		newQueryBody[1] = queryBody[2];
//		newQueryBody[2] = queryBody[3];
//		newQueryBody[3] = queryBody[4];

//		newQueryBody[0] = queryBody[0];
//		newQueryBody[1] = queryBody[1];
//		newQueryBody[2] = queryBody[3];
//		newQueryBody[3] = queryBody[4];

//		Clause reducedQuery = new Clause( newQueryBody , originalQuery.getHead() );
//		Term extraAtom = m_termFactory.getFunctionalTerm( "C", m_termFactory.getVariable(1) ); // , m_termFactory.getVariable(0) );
//		Term extraAtom = m_termFactory.getFunctionalTerm( "worksFor", m_termFactory.getVariable(0), m_termFactory.getVariable(1));
//		Term extraAtom = m_termFactory.getFunctionalTerm( "edge", m_termFactory.getVariable(4), m_termFactory.getVariable(5) );
//		Term extraAtom = queryBody[queryBody.length -1];

//		System.out.println( "Original query: " + originalQuery +  "\nquery for CGLLR = " + originalQuery );//+ "\nExtraAtom = " + extraAtom );
////		" ^ " + extraAtom +
//		//

		/**
		 * Run CGLLR original (with or without optimisations)
		 */

//		runEvaluation(path);

		/** Run CGLLR and build rewriting tree */
		long time = System.currentTimeMillis();
		Saturator_Tree cgllrRewriter = new Saturator_Tree(m_termFactory);
//		Saturator_Tassos cgllrRewriter = new Saturator_Tassos(m_termFactory);
//		Saturator cgllrRewriter = new Saturator(m_termFactory);
		Tree<Clause> rewritingTree = cgllrRewriter.saturate(pis, originalQuery);
//		Tree<Clause> rewritingTree = cgllrRewriter.saturateRetTree(newPIs, reducedQuery);
		Set<Clause> rewritingAsList = rewritingTree.getRootElement().getSubTreeAsSet();
		ArrayList<Clause> nonRed = m_optimizer.querySubsumptionCheck(new ArrayList<Clause>(rewritingAsList));
		nonRed = m_optimizer.querySubsumptionCheck(new ArrayList<Clause>(rewritingAsList));
		long timeCGLLR = System.currentTimeMillis()-time;
		System.out.println( "CGLLR finished returning " + rewritingAsList.size() + " clauses in "+ timeCGLLR + " ms");
		System.out.println( "CGLLR finished returning " + nonRed.size() + " clauses");

		printTree(rewritingTree);
		
//		int i = 1;
//		for (Clause c : rewritingAsList )
//			 if (c.getBody().length == 2 )
//				 System.out.println( i++ + ": " + c + "\t\t\t\t\t\t\t" + c.getUnifications() );

//		long t = System.currentTimeMillis();
//		long timeSubsumption_1 = System.currentTimeMillis()-time;
//		System.out.println( " in " + timeSubsumption_1 + " ms" );
//
//		int is = 1;
//		for ( Clause c : m_optimizer.querySubsumptionCheck( m_optimizer.pruneAUX( rewritingAsList ) ) ) {
//			System.out.println( is++ + " : " + c + "\t\t" + c.getLostVars() );
//		}
//
//		int isSubsumed = 0, gotSubsumerInTheSamePath = 0;
//		Set<TreeNode<Clause>> visited = new HashSet<TreeNode<Clause>>();
//		Queue<TreeNode<Clause>> queue = new LinkedList<TreeNode<Clause>>();
//		queue.add( rewritingTree.getRootElement());
//		while ( !queue.isEmpty() ) {
//			TreeNode<Clause> cn = queue.poll();
//			if ( visited.contains( cn ) )
//				continue;
//			visited.add( cn );
//			if  ( cn.getNodeLabel().getSubsumer() != null) {
//				isSubsumed++;
//				Set<TreeNode<Clause>> v = new HashSet<TreeNode<Clause>>();
//				System.out.println( cn.getNodeLabel() + "\t\t" + cn.getNodeLabel().getSubsumer() );
//				Queue<TreeNode<Clause>> q = new LinkedList<TreeNode<Clause>>();
//				q.add( cgllrRewriter.getEquivalentNodeAlreadyInDAG( new TreeNode( cn.getNodeLabel().getSubsumer() ) ) );
//				while( !q.isEmpty() ) {
//					TreeNode<Clause> current = q.poll();
//					if ( v.contains(current) )
//						continue;
//					v.add(current);
//					Set<TreeNode<Clause>> currentChildren = current.getChildren();
//					for ( TreeNode<Clause> ncc : currentChildren )
//						if ( ncc.getNodeLabel().isEquivalentUpToVariableRenaming( cn.getNodeLabel() ) )
//							gotSubsumerInTheSamePath++;
//					q.addAll(currentChildren);
//				}
// 			}
//			Set<TreeNode<Clause>> cnChl = cn.getChildren();
//			queue.addAll( cnChl );
//		}
//		System.out.println( isSubsumed + "\t\t" + gotSubsumerInTheSamePath );
//
//		System.exit( 0 );
//		rewritingTree.setRootElement(new TreeNode( originalQuery ));

//		/** REFINE!!! */
//		long trtime = System.currentTimeMillis();
//		RewritingExtensionManager refinementManager = new RewritingExtensionManager(redundancyEliminator);
////		Refine_Refactoring refinementManager = new Refine_Refactoring();
//		ArrayList<Clause> treeResult = new ArrayList<Clause>(refinementManager.extendRewriting(rewritingTree, extraAtom, newPIs, new HashSet<Term>(reducedQuery.getVariables())));
//
//		long timeRefinement = System.currentTimeMillis() - trtime;
//		System.out.println( "\nRefined-Rew returned " + treeResult.size() + " clauses in " + timeRefinement );
//		trtime = System.currentTimeMillis();
//		ArrayList<Clause> finalRes = m_optimizer.querySubsumptionCheck(treeResult);
//		long finalSubsumption = System.currentTimeMillis() - trtime;
//		System.out.println( "subsumption returned " + finalRes.size() + " clauses in " + finalSubsumption );
//		System.out.println( "Total Time Ref+Subs = " + ( finalSubsumption + timeRefinement ) );
//		System.out.println( "Overall Time  = " + (timeCGLLR + timeSubsumption_1 + finalSubsumption + timeRefinement ) );
//
//		int i = 0;
//		for ( Clause c : finalRes ) {
//////			if ( c.getBody().length == 2) {
//				i++;
//				System.out.println(i + " : " + c );
//////			}
////////			else
////				System.out.println( "\t\t" + c );
////		System.out.println( i );
//		}

	}



	private static void runEvaluation(String path) throws Exception {
		String ontologyFile,queryPath="";

//		System.out.println( "Starting test for ontology V.owl");
//		ontologyFile = "file:" + path + "Ontologies/V.owl";
//		queryPath = path + "Queries/V/";
//		runTest(ontologyFile,queryPath);
//
//		System.out.println( "Starting test for ontology P1.owl");
//		ontologyFile = "file:" + path + "Ontologies/P1.owl";
//		queryPath = path + "Queries/P1/";
//		runTest(ontologyFile,queryPath);
//
//		System.out.println( "Starting test for ontology P5.owl");
//		ontologyFile = "file:" + path + "Ontologies/P5.owl";
//		runTest(ontologyFile,queryPath);
//
//		System.out.println( "Starting test for ontology P5X.owl");
//		ontologyFile = "file:" + path + "Ontologies/P5X.owl";
//		runTest(ontologyFile,queryPath);
//
//		System.out.println( "Starting test for ontology S.owl");
//		ontologyFile = "file:" + path + "Ontologies/S.owl";
//		queryPath = path + "Queries/S/";
//		runTest(ontologyFile,queryPath);
//
//		System.out.println( "Starting test for ontology U.owl");
//		ontologyFile = "file:" + path + "Ontologies/U.owl";
		queryPath = path + "Queries/U/";
//		runTest(ontologyFile,queryPath);
//
		System.out.println( "Starting test for ontology UX.owl");
		ontologyFile = "file:" + path + "Ontologies/UX.owl";
		runTest(ontologyFile,queryPath);

//		System.out.println( "Starting test for ontology A.owl");
//		ontologyFile = "file:" + path + "A.owl";
//		queryPath = path + "Queries/A/";
//		runTest(ontologyFile,queryPath);
//
//		System.out.println( "Starting test for ontology AX.owl");
//		ontologyFile = "file:" + path + "AX.owl";
//		runTest(ontologyFile,queryPath);
	}

	private static void runTest(String ontologyFile, String queryPath) throws Exception {

		ArrayList<PI> pis = parser.getAxioms(ontologyFile);

		final ArrayList<PI> tBoxAxioms = new ArrayList<PI>();
		for ( PI p : pis )
			tBoxAxioms.add( new PI(p.m_type, p.m_left.replace("$", "-"), p.m_right.replace("$", "-")));

		File queriesDir = new File( queryPath );
		final File[] queries = queriesDir.listFiles();
		for( int i=0 ; i<queries.length ; i++ )
			//			int i = 5;
	        if( queries[i] == null )
	    	   // Either dir does not exist or is not a directory
	    	   return;
	        else if( !queries[i].toString().contains(".svn") ){
				final String query = queries[i].toString();
				finished=false;
	        	Thread t = new Thread(new Runnable() {
	        	    public void run() {
        	    		try {
							new Saturator(m_termFactory).saturate(tBoxAxioms, parser.getQuery(query));
							finished=true;
        	    		} catch (Exception e) {
        	    			e.printStackTrace();
        	    		}
	        	    }
	        	} );

	        	t.start();
	    		try {
	    			t.join( 600000 );	//600 sec
	    		} catch(InterruptedException e) {
	    			e.printStackTrace();
	    		}
	    		if (!finished) {
	    			System.out.println("Iterrupted");
	    			t.interrupt();
	    			t.stop();
	    		}

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

	private static void compareContents( ArrayList<Clause> clauses1 , ArrayList<Clause> clauses2 ) {

		ArrayList<Clause> moreInCl1 = new ArrayList<Clause>();
		ArrayList<Clause> moreInCl2 = new ArrayList<Clause>();

		for ( Clause cl1 : clauses1 ) {
			boolean found = false;
			for ( Clause cl2 : clauses2 )
				if ( cl1.isEquivalentUpToVariableRenaming(cl2) )
					found = true;
			if ( !found )
				moreInCl1.add(cl1);
		}

		for ( Clause cl2 : clauses2 ) {
			boolean found = false;
			for ( Clause cl1 : clauses1 )
				if ( cl1.isEquivalentUpToVariableRenaming(cl2) )
					found = true;
			if ( !found )
				moreInCl2.add(cl2);
		}

		System.out.println("\n\nMoreClauses in 1 - " + moreInCl1.size());
		for ( Clause more1 : moreInCl1 )
			System.out.println( more1 );
		System.out.println("\n\n");


		System.out.println("\n\nMoreClauses in 2 - " + moreInCl2.size());
		for ( Clause more1 : moreInCl2 )
			System.out.println( more1 );
		System.out.println("\n\n");



	}

	public static void printTree( Tree<Clause> tree) {

		System.out.println("START");
//		Queue<Node<T>> queue = new LinkedList<Node<T>>();
		Set<TreeNode<Clause>> queue = new HashSet<TreeNode<Clause>>();
		queue.add( tree.getRootElement() );
		Set<TreeNode<Clause>> alreadyAddedToQueue = new HashSet<TreeNode<Clause>>();
		Set<TreeNode<Clause>> alreadyPrinted = new HashSet<TreeNode<Clause>>();
		alreadyAddedToQueue.add( tree.getRootElement() );

		while ( !queue.isEmpty() ) {
//			Node<T> cn = queue.poll();
			TreeNode<Clause> cn = queue.iterator().next();
			queue.remove(cn);
			if ( alreadyPrinted.contains(cn) )
				continue;
//			if ( !cn.getNodeLabel().containsAUXPredicates() )
				System.out.println( cn.getNodeLabel() );
			alreadyPrinted.add( cn );
			Set<TreeNode<Clause>> cnChl = cn.getChildren();
			for ( TreeNode<Clause> nc : cnChl )
			{
//				if ( !nc.getNodeLabel().containsAUXPredicates() )
					System.out.println( "\t\t\t\t" + nc.getNodeLabel() );
				if ( !alreadyAddedToQueue.contains( nc ))
				{
					queue.add( nc );
					alreadyAddedToQueue.add( nc );
				}
			}
		}
		System.out.println("END Printing tree \n");
	}

}