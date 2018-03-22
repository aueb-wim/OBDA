import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;

import owlim.OWLimQueryEvaluator;
import edu.aueb.queries.ClauseParser;
import edu.aueb.queries.QueryOptimization;
import edu.ntua.image.incremental.Incremental;

public class IncSingleEx {

	private static final DLliteParser parser = new DLliteParser();

	private int orderedQueryIndex=0;

	public static void main(String[] args) throws Exception{

		String ontologyFile;
		String queryFile;
		ArrayList<PI> tBoxAxioms = new ArrayList<PI>();

//		String path = System.getProperty("user.dir")+ "/dataset/Evaluation_ISWC'09/";
		String path = "/Users/avenet/Academia/Ntua/Ontologies/";
//		String path = "/Users/avenet/";


//		queryFile = System.getProperty("user.dir")+ "/dataset/Tests/queries.txt";
//		queryFile = "/Users/avenet/Ntua/JavaWorkspace/IQAROS/dataset/Tests/queries.txt";
//		queryFile = path + "LUBM/QueriesWithConstants/Query_01.txt";
//		queryFile = path + "reactome/Queries/Query_02.txt";
//		queryFile = path + "UOBM/QueriesWithConstants/Query_11.txt";
		queryFile = path + "UOBM/QueriesWithConstants/Test.txt";
//		queryFile = path + "/TestOnto/Queries/query_01.txt";
//		ontologyFile = "file:" + path + "onto1.owl";
//		ontologyFile = "file:" + System.getProperty("user.dir") + "/dataset/Tests/SimpleSub.owl";
//		ontologyFile = "file:" + path + "Ontologies/AX.owl";
//		ontologyFile = "file:" + path + "LUBM/univ-bench_DL-Lite_mine.owl";
		ontologyFile = "file:" + path + "UOBM/univ-bench-dl-Zhou_DL-Lite_mine.owl";
//		ontologyFile = "file:" + path + "TestOnto/testOnto.owl";
//		ontologyFile = "file:" + path +"reactome/biopax-level3-processed_DL-Lite_mine.owl";
//		reactome uri "http://www.biopax.org/release/biopax-level3.owl#";
//		ontologyFile = "file:" + path + "Vicodi/vicodi_all_DL-Lite_mine.owl";
//		ontologyFile = "file:" + path + "Semintec/bigFile_DL-Lite_mine.owl";
//		String newOnto = "file:/Users/avenet/newOnto.owl";

//		ontologyFile = "file:" + path + "LUBM/univ-bench.owl";

//		tBoxAxioms = parser.getAxioms(ontologyFile);

//		for ( PI pi : tBoxAxioms )
//			System.out.println(pi.m_left + "\t" + pi.m_type + "\t" + pi.m_right );
//		System.out.println(tBoxAxioms.size());

 
		Clause originalQuery = new ClauseParser().parseClause((new BufferedReader(new FileReader(queryFile))).readLine());
		System.out.println("Original Query = " + originalQuery );
		
		System.out.println(originalQuery.getBody()[0].getArguments()[2]);

//		System.exit(0);
		
		
		String optFilePath = path + "reactome/OptimizationClausesOWLim/optimizationClauses.txt";
//		String optFilePath = path + "UOBM/OptimizationClauses/optimizationClausesUOBM.txt";
//		String optFilePath = path + "queryOpt.txt";

		QueryOptimization qOpt = null;
		/**
		 * Emptiness optimization
		 */
		if (optFilePath != null && optFilePath != "") {
			long startOpt = System.currentTimeMillis();
			qOpt  = new QueryOptimization(optFilePath);
            System.out.println("Optimization Took " + (System.currentTimeMillis() - startOpt) + "ms");
		}

		
//		Incremental incremental = new Incremental();
		Incremental incremental = new Incremental(qOpt,true);
		ArrayList<Clause> result = incremental.computeUCQRewriting(tBoxAxioms,originalQuery);
		/** OR, in order to run the evaluation using non-restricted subsumption */
//		Configuration c = new Configuration();
//		c.redundancyElimination=RedundancyEliminationStrategy.Full_Subsumption;
//		Incremental incremental = new Incremental( c );

//		incremental.printTree(incremental.computeRewritingOfAtomAsTree(originalQuery.getBody()[0], tBoxAxioms, new HashSet<Term>( originalQuery.getDistinguishedVariables() ), true));
//		ArrayList<Clause> result = incremental.computeUCQRewriting(tBoxAxioms,originalQuery);
		
		Set<Clause> owlimRew = new HashSet<Clause>();
		int i = 1;
		for ( Clause c1 : result ) {
			if (c1.getToBeSentToOWLim()) {
				owlimRew.add(c1);
				System.out.println( i++ + ": " + c1 + "\t\t" + c1.getToBeSentToOWLim() );
			}
		}
		System.out.println(owlimRew.size());

//		String serqlQuery = "SELECT DISTINCT XX0,XX1 FROM "
//				+ "{XX0} <http://www.biopax.org/release/biopax-level3.owl#displayName> {XX1}, "
//				+ "{XX0} <http://www.biopax.org/release/biopax-level3.owl#pathwayComponent> {XX2},"
//				+ "{XX2} rdf:type {<http://www.biopax.org/release/biopax-level3.owl#BiochemicalReaction>},"
//				+ "{XX2} <http://www.biopax.org/release/biopax-level3.owl#participant> {XX3}, "
//				+ "{XX3} rdf:type {<http://www.biopax.org/release/biopax-level3.owl#Protein>}";
//		String[] datasetFiles = new String[1];
//		datasetFiles[0] = ontologyFile;
		OWLimQueryEvaluator evaluator = createDataSetsAndEvaluator(path + "/UOBM/UOBMGenerator/preload_generated_uobm50_dbcreation/", "UOBM10", ontologyFile);
		long start = System.currentTimeMillis();
//		evaluator.evaluateQueryIncompletePart(serqlQuery);
		evaluator.evaluateQuery(owlimRew,"http://semantics.crl.ibm.com/univ-bench-dl.owl#");
		long end = System.currentTimeMillis() - start;
//		System.out.println("Ans = " + evaluator.getNumberOfReturnedAnswersIncompletePart() + " in " + end + "ms");
		System.out.println("Ans = " + evaluator.getNumberOfReturnedAnswersCompletePart() + " in " + end + "ms");


//    	Evaluator ev = new Evaluator();
//    	ev.runAllRewQueriesAtOnce(result);
//    	ev.runAllRewQueriesOneByOne(result);

	}
	
	
	private static OWLimQueryEvaluator createDataSetsAndEvaluator(String dataFiles, String dbPath, String ontologyFile) {

		String[] datasetFiles = null;
		OWLimQueryEvaluator evaluator = new OWLimQueryEvaluator();
		
		System.out.println( "dataFiles = " + dataFiles  );
		if ( dataFiles.contains("uba1.7") ){
			System.out.println("LUBM" + dataFiles);
			datasetFiles = createDataSetFilesLUBM(dataFiles);
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
		evaluator.loadInputToSystem(ontologyFile, datasetFiles);
    	return evaluator;
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
			if( !dataFiles[i].toString().contains(".svn") && !dataFiles[i].toString().contains(".DS_Store") ){
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


}