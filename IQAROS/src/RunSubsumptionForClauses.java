import java.util.ArrayList;

import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;

import edu.ntua.image.redundancy.RedundancyElimination;
import edu.ntua.image.redundancy.SimpleRedundancyEliminator;


public class RunSubsumptionForClauses {

	private static final DLliteParser parser = new DLliteParser();
	private static RedundancyElimination redundancyEliminator = new SimpleRedundancyEliminator();

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String path = "/Users/avenet/Ntua/JavaWorkspace/Nyaya/examples/rewriting_test/output/AX/LTGDREW_Q4_opt.dtg";

//		String ontologyFile = "file:" + System.getProperty("user.dir")+ "/dataset/Evaluation_ISWC'09/Ontologies/V.owl";
//
//		ArrayList<PI> tBoxAxioms = parser.getAxioms(ontologyFile);

		ArrayList<Clause> clauses = parser.getQueryArrayList(path);
		
		long t = System.currentTimeMillis();
		clauses = redundancyEliminator.standardSubsumptionCheck_req(clauses);
		
		System.out.println( clauses.size() + " in " + ( System.currentTimeMillis() - t) ); 
	}

}
