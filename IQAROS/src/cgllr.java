import java.util.ArrayList;

import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;
import org.oxford.comlab.perfectref.rewriter.Saturator;
import org.oxford.comlab.perfectref.rewriter.TermFactory;


public class cgllr {
	private static final TermFactory m_termFactory = new TermFactory();
	private static final DLliteParser parser = new DLliteParser();

	public static void main(String[] args) throws Exception {
		String ontologyFile;
		String queryFile;
		ArrayList<PI> pis = new ArrayList<PI>();

		String path = System.getProperty("user.dir")+ "/dataset/Evaluation_ISWC'09/";

		queryFile = System.getProperty("user.dir")+ "/dataset/Tests/queries.txt";
		ontologyFile = "file:" + path + "Ontologies/P5.owl";
//		ontologyFile = "file:" + path + "Bug.owl";

		System.out.println( path );

		pis = parser.getAxioms(ontologyFile);
		Clause originalQuery = parser.getQuery(queryFile);
		
		System.out.println(originalQuery);
		System.out.println(originalQuery.m_canonicalRepresentation);

		Saturator cgllrRewriter = new Saturator(m_termFactory);
		long t = System.currentTimeMillis();
		ArrayList<Clause> rewritingTree = cgllrRewriter.saturate(pis, originalQuery);
		System.out.println( (System.currentTimeMillis()-t) + " ms");

		System.out.println( rewritingTree.size() );
//		System.out.println( rewritingTree );
	}
}
