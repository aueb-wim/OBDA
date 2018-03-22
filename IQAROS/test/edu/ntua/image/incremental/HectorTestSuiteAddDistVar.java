package edu.ntua.image.incremental;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;

import edu.ntua.image.alternateDistinguishedVariables.AddDistinguishedVariables;

public class HectorTestSuiteAddDistVar extends TestCase {
	
	String path = System.getProperty("user.dir")+ "/dataset/Evaluation_ISWC'09AddDistVar/";
	private static final DLliteParser parser = new DLliteParser();
	
	AddDistinguishedVariables addV;
	
	public HectorTestSuiteAddDistVar(String name) {
		super( name );
//		Configuration c = new Configuration();
//		c.redundancyElimination=RedundancyEliminationStrategy.Full_Subsumption;
////		inc = new Incremental( c);
//		inc = new Incremental( );
		addV = new AddDistinguishedVariables();
	}

    public void testVOntology() throws Exception {
    	ArrayList<Clause> result;

    	ArrayList<PI> tBoxAxioms = parser.getAxioms("file:" + path + "Ontologies/V.owl");

//    	/**		Q1	**/
//        result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/V/Q1.txt"), variable);
//        assertEquals(15, result.size());
        
        /**		Q2	*/
        result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/V/Q2.txt"), 1);
        assertEquals(10, result.size());
        
        /**		Q3	*/
        result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/V/Q3.txt"), 1);
        assertEquals(72, result.size());
        
        /**		Q4	*/
        result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/V/Q4.txt"), 1);
        assertEquals(185, result.size());
        
//        /**		Q5	*/
//        result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/V/Q5.txt"), variable);
//        assertEquals(30, result.size());
    }
    
////    public void testP1Ontology() throws Exception {
////    	System.out.println( "doing P1");
////    	ArrayList<Clause> result;
////
////    	ArrayList<PI> tBoxAxioms = parser.getAxioms("file:" + path + "Ontologies/P1.owl");
////    	
////    	/**		Q1	*/
////        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q1.txt"));
////        assertEquals(2, result.size());
////        
////        /**		Q2	*/
////        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q2.txt"));
////        assertEquals(2, result.size());
////        
////        /**		Q3	*/
////        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q3.txt"));
////        assertEquals(2, result.size());
////        
////        /**		Q4	*/
////        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q4.txt"));
////        assertEquals(2, result.size());
////        
////        /**		Q5	*/
////        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q5.txt"));
////        assertEquals(2, result.size());
////    }
//    
//    public void testP5Ontology() throws Exception {
//    	System.out.println( "doing P5");
//    	ArrayList<Clause> result;
//
//    	ArrayList<PI> tBoxAxioms = parser.getAxioms("file:" + path + "Ontologies/P5.owl");
//    	
//    	/**		Q1	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q1.txt"));
//        System.out.println( result );
//        assertEquals(6, result.size());
//        
//        /**		Q2	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q2.txt"));
//        assertEquals(10, result.size());
//        
//        /**		Q3	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q3.txt"));
//        assertEquals(13, result.size());
//        
//        /**		Q4	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q4.txt"));
//        assertEquals(15, result.size());
//        
//        /**		Q5	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q5.txt"));
//        assertEquals(16, result.size());
//    }
//    
//    public void testP5XOntology() throws Exception {
//    	System.out.println( "doing P5X");
//    	ArrayList<Clause> result;
//
//    	ArrayList<PI> tBoxAxioms = parser.getAxioms("file:" + path + "Ontologies/P5X.owl");
//    	
//    	/**		Q1	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q1.txt"));
//        assertEquals(14, result.size());
//        
//        /**		Q2	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q2.txt"));
//        assertEquals(25, result.size());
//        
//        /**		Q3	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q3.txt"));
//        assertEquals(58, result.size());
//        
//        /**		Q4	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q4.txt"));
//        assertEquals(179, result.size());
//        
//        /**		Q5	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/P1/Q5.txt"));
//        assertEquals(718, result.size());
//    }
    
    public void testSOntology() throws Exception {
    	System.out.println( "doing S");
    	ArrayList<Clause> result;

    	ArrayList<PI> tBoxAxioms = parser.getAxioms("file:" + path + "Ontologies/S.owl");
    	
//    	/**		Q1	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/S/Q1.txt"));
//        assertEquals(6, result.size());
        
        /**		Q2	*/
        result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/S/Q2.txt"), 1);
        assertEquals(2, result.size());
        
        /**		Q3	*/
        result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/S/Q3.txt"), 2);
        assertEquals(4, result.size());
        
        /**		Q4	*/
        result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/S/Q4.txt"), 2);
        assertEquals(4, result.size());
        
        /**		Q5	*/
        result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/S/Q5.txt"), 3);
        assertEquals(8, result.size());
    }
    
    public void testUOntology() throws Exception {
    	System.out.println( "doing U");
    	ArrayList<Clause> result;

    	ArrayList<PI> tBoxAxioms = parser.getAxioms("file:" + path + "Ontologies/U.owl");
    	
//    	/**		Q1	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/U/Q1.txt"));
//        assertEquals(2, result.size());
        
        /**		Q2	*/
    	result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/U/Q2.txt"), 1);
        assertEquals(1, result.size());
        
        /**		Q3	*/
        result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/U/Q3.txt"), 2);
        assertEquals(4, result.size());
        
        /**		Q4	*/
        result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/U/Q4.txt"), 1);
        assertEquals(2, result.size());
        
//        /**		Q5	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/U/Q5.txt"));
//        assertEquals(10, result.size());
    }
    
    public void testUXOntology() throws Exception {
    	System.out.println( "doing UX");
    	ArrayList<Clause> result;

    	ArrayList<PI> tBoxAxioms = parser.getAxioms("file:" + path + "Ontologies/UX.owl");
    	
//    	/**		Q1	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/U/Q1.txt"));
//        assertEquals(5, result.size());
        
        /**		Q2	*/
    	result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/U/Q2.txt"), 1);
        assertEquals(1, result.size());
        
        /**		Q3	*/
        result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/U/Q3.txt"), 2);
        assertEquals(12, result.size());
        
        /**		Q4	*/
        result = addV.computeAddDistVar(tBoxAxioms, parser.getQuery(path + "Queries/U/Q4.txt"), 1);
        assertEquals(5, result.size());
        
//        /**		Q5	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/U/Q5.txt"));
//        assertEquals(25, result.size());
    }
    
//    public void testAOntology() throws Exception {
//    	System.out.println( "doing A");
//    	ArrayList<Clause> result;
//
//    	ArrayList<PI> tBoxAxioms = parser.getAxioms("file:" + path + "Ontologies/A.owl");
//    	
//    	/**		Q1	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/A/Q1.txt"));
//        assertEquals(27, result.size());
//        
//        /**		Q2	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/A/Q2.txt"));
//        assertEquals(50, result.size());
//        
//        /**		Q3	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/A/Q3.txt"));
//        assertEquals(104, result.size());
//        
//        /**		Q4	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/A/Q4.txt"));
//        assertEquals(224, result.size());
//        
//        /**		Q5	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/A/Q5.txt"));
//        assertEquals(624, result.size());
//    }
//    
//    public void testAXOntology() throws Exception {
//    	System.out.println( "doing AX");
//    	ArrayList<Clause> result;
//
//    	ArrayList<PI> tBoxAxioms = parser.getAxioms("file:" + path + "Ontologies/AX.owl");
//    	
//    	/**		Q1	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/A/Q1.txt"));
//        assertEquals(41, result.size());
//        
//        /**		Q2	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/A/Q2.txt"));
//        assertEquals(1431, result.size());
//        
//        /**		Q3	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/A/Q3.txt"));
//        assertEquals(4466, result.size());
//        
//        /**		Q4	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/A/Q4.txt"));
//        assertEquals(3159, result.size());
//        
//        /**		Q5	*/
//        result = inc.computeUCQRewriting(tBoxAxioms,parser.getQuery(path + "Queries/A/Q5.txt"));
//        assertEquals(32921, result.size());
//    }
}
