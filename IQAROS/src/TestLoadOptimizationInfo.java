import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;
import org.oxford.comlab.perfectref.rewriter.Substitution;
import org.oxford.comlab.perfectref.rewriter.Term;
import org.oxford.comlab.perfectref.rewriter.Variable;

import edu.ntua.image.incremental.Incremental;


public class TestLoadOptimizationInfo {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String path = "/Users/avenet/Academia/Ntua/Ontologies/Fly/OptimizationClauses/optimizationClauses.txt";
		String ontoPath = "file:/Users/avenet/Academia/Ntua/Ontologies/Fly/fly_anatomy_XP_with_GJ_FC_individuals_owl-tBox-AssNorm.owl";
//		Incremental incremental = new Incremental(path,false);
		
		DLliteParser parser = new DLliteParser();
//		ArrayList<Clause> clauses = parser.getQueryArrayList(path);
		ArrayList<PI> pis = parser.getAxiomsWithURI(ontoPath);
		
//		int atoms = 0, joins = 0;
//		for (Clause cl : clauses) {
//			if (cl.getBody().length==1)
//				atoms++;
//			else if (cl.getBody().length==2)
//				joins++;
//		}
//		System.out.println( "Atoms: " + atoms + ", Joins: " + joins );
		
//		int j = 0;
//		for (PI p: pis) {
//			System.out.println(p.m_left + " " + p.m_type + " " + p.m_right );
//			j++;
//		}
//		System.out.println("Axioms size = " + j );
		
		
//		Substitution sub = new Substitution();
//
//		for ( Term[] t: joins )
//		{
//			System.out.println(t[0]);
//			for ( Clause cl: clauses ) {
//				Term[] body = cl.getBody();
//				for (Term term: body) {
//					if (term.getFunctionalPrefix().equals(t[1].getFunctionalPrefix()))
//					{
//						sub.put((Variable) term.getVariableOrConstant(), t[0].getVariableOrConstant());
//					}
//				}
//			}
//		}
//		for (Entry<Variable,Term> entry: sub.entrySet()) {
//			System.out.println(entry);
//		}
		
//
//		System.out.println( incremental.atomicEntitiesWithNoAnswers.size() + "\t\t" + incremental.binaryEntitiesWithNoAnswers.size() );

//		String a = "a", b = "b", c = "c";
//
//		HashMap<String,String> map = new HashMap<String,String>();
//		map.put(a, b);
//		map.put(a, c);
//		map.replace(a, a);
//
//		System.out.println( map.get(a) );

	}

	private static void changeClauses(ArrayList<Clause> clauses) {
		// TODO Auto-generated method stub
		for (Clause cl: clauses)
			cl.setToBeEvaluated(false);
	}

}
