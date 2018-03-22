import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.Term;
import org.oxford.comlab.perfectref.rewriter.TermFactory;
import org.oxford.comlab.perfectref.rewriter.Variable;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLProperty;

import edu.aueb.queries.Evaluator;

/**
 * Create clauses that consist of one or two conjuncts and have no answers
 *
 * These will be later used to cut off queries from the UCQ rewriting
 *
 *
 * DEPRACETED
 *
 */

public class PreProcessingDBOptimizedForNPD {

	private static TermFactory m_termFactory = new TermFactory();

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub


//		String path = "/Users/avenet/Academia/Ntua/Ontologies/LUBM-ex/";
//		String ontologyFile = "file:" + path + "EUGen/ontologies/lubm-ex-20.owl";
//		String ontologyDataPath = "file:" + path;


//		String path = "/Users/avenet/Academia/Ntua/Ontologies/reactome/";
//		String ontologyFile = "file:" + path + "biopax-level3-processed.owl";
//		String ontologyDataPath = "file:" + path;

//		String path = "/Users/avenet/Academia/Ntua/Ontologies/Fly/";
//		String ontologyFile = "file:" + path + "fly_anatomy_XP_with_GJ_FC_individuals_owl-tBox-AssNorm.owl";
////		String ontologyFile = "file:" + path + "fly_anatomy_XP_with_GJ_FC_individuals_owl-tBox-AssNorm-new.owl";
////		String ontologyFile = "file:" + path + "fly_anatomy_XP_with_GJ_FC_individuals_owl-tBox-AssNorm-new-noTrans.owl";


//		String path = "/Users/avenet/Academia/Ntua/Ontologies/Vicodi/";
//		String ontologyFile = "file:" + path + "vicodi_all.owl";
////		String dataOntologyFile = "file:" + path + "vicodi_all.owl";

//		String path = "/Users/avenet/Academia/Ntua/Ontologies/Semintec/";
//		String ontologyFile = "file:" + path + "bigFile.owl";
//		String dataOntologyFile = "file:" + path + "bigFile.owl";

		String path = "/Users/avenet/Academia/Ntua/Ontologies/UOBM/";
		String ontologyFile = "file:" + path + "univ-bench-dl-Zhou.owl";
////		String dataOntologyFile = "file:" + path + "preload_generated_uobm/univ0.owl";

//		String path = "/Users/avenet/Academia/Ntua/Ontologies/TestOnto/";
//		String ontologyFile = "file:" + path + "testOnto.owl";
//
//		String path = "/Users/avenet/Academia/Ntua/Ontologies/LUBM/";
//		String ontologyFile = "file:" + path + "univ-bench_DL-Lite_owlapi.owl";
//		String ontologyFile = "file:" + path + "univ-bench-gstoil.owl";
//////		String ontologyDataPath = "file:" + path + "DataOntos/";

//		String path = "/Users/avenet/Academia/Ntua/Ontologies/npd-benchmark-master/";
//		String ontologyFile = "file:" + path + "ontology/npd-v2-ql.owl";
//		String ontologyDataPath = "file:" + path;
//

		HashMap<String,Set<String>> conceptsAndAnswers = new HashMap<String,Set<String>>();
		HashMap<String,Set<String>> rolesAndAnswers = new HashMap<String,Set<String>>();
		HashMap<String,Set<String>> invRolesAndAnswers = new HashMap<String,Set<String>>();

		String optimizationPath = path + "/OptimizationClauses/testing/optimizationClausesPreprocessingDBOptimized13.4.2016.txt";

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		URI physicalURI = URI.create(ontologyFile);
        OWLOntology ontology = manager.loadOntologyFromPhysicalURI(physicalURI);

        Evaluator ev = new Evaluator("jdbc:postgresql://127.0.0.1:5432/UOBM30");
//        Evaluator ev = new Evaluator("jdbc:mysql://127.0.0.1:3306/npdfactpages");
//		Evaluator ev = new Evaluator("jdbc:mysql://127.0.0.1:3306/npd",path + "mappings/mysql/npd-v2-ql-mysql.obda", true);

		System.out.println("PreprocessingDBOptimized13.4.2016");

		Set<OWLEntity> signature = ontology.getSignature();
		Set<OWLClass> concepts = new HashSet<OWLClass>();
		Set<OWLProperty> props = new HashSet<OWLProperty>();

		Set<Term> conceptTermsWithAnswers = new HashSet<Term>();
		Set<Term> roleTermsWithAnswers = new HashSet<Term>();
		Set<Term> invRoleTermsWithAnswers = new HashSet<Term>();

		Set<Clause> clausesWithNoAnswers = new HashSet<Clause>();

		Variable var1 = m_termFactory.getVariable(0);
		Variable var2 = m_termFactory.getVariable(1);

		int clausesWithAnswers = 0;
		long start = System.currentTimeMillis();

		/*
		 * Identify all entities of the ontology
		 * Entities are:	classes
		 * 					properties (object + data properties)
		 */
		for (OWLEntity entity: signature)
		{
			if ( entity instanceof OWLClass )
				concepts.add((OWLClass) entity);
			if ( entity instanceof OWLObjectProperty || entity instanceof OWLDataProperty )
				props.add((OWLProperty) entity);
		}

		System.out.println("IDENTIFIEED");
		System.out.println("Concepts = " + concepts.size() );
		System.out.println("Properties = " + props.size() );
		System.out.println("in " + (System.currentTimeMillis() - start) + " ms");

		/*
		 * Evaluate all classes on the DB and store
		 */

		System.out.println("\nEvaluating all concepts: there are " + concepts.size());
		long startConcepts = System.currentTimeMillis();
		int processed=0;
		double progress=0;
		double lastprogress=0;
		int allWork=concepts.size();
		for ( OWLClass cl: concepts )
		{
			processed++;
			progress=processed;
			progress=progress/allWork;
//			System.out.println(progress);
			if (progress>lastprogress+0.05) {
				System.out.print((int)(progress*100) + "%, ");
				lastprogress=progress;
			}
//			String clName = cl.getURI().toString();
			String clName = cl.getURI().getFragment();
//			if (clName==null)
//				clName=cl.getURI().toString().substring(cl.getURI().toString().lastIndexOf("/")+1);
//			System.out.println(cl + " " + cl.getURI() + " " + clName + " " + var1);
			Term clTerm = m_termFactory.getFunctionalTerm(clName, var1);
			Term[] body = new Term[1];
			body[0] = clTerm;
			Clause clause = new Clause(body, m_termFactory.getFunctionalTerm("Q", var1));
//			System.out.println("\n" + clause + "\t" + ev.getSQLWRTMappings(clause));
			Set<String> ans;
			if (path.contains("npd"))
				ans = ev.evaluateSQLReturnResults(null, ev.getSQLWRTMappings(clause));
			else
				ans = ev.evaluateSQLReturnResults(null, ev.getSQLavenet(clause));
			if (ans.size() == 0) {
//				entitiesWithNoAnswersMap.put(clName, ans);
//				conceptTermsWithNoAnswers.add(clTerm);
				clausesWithNoAnswers.add(clause);
			}
			else {
				conceptTermsWithAnswers.add(clTerm);
				conceptsAndAnswers.put(clName, ans);
			}
		}
		System.out.println("\nClauses With Answers " + conceptTermsWithAnswers.size());
		System.out.println("ClausesWithNoAnswersSize = " + clausesWithNoAnswers.size() + " in " + (System.currentTimeMillis() - startConcepts) + "ms");



		/*
		 * Evaluate all properties on the DB and store
		 * 		entitiesWithNoAnswer
		 * 		roleTermsTermWithNoAnswer
		 */
		allWork=props.size();
		System.out.println("\nEvaluating all properties. there are: " + allWork);
		long startProperties = System.currentTimeMillis();
		processed=0;
		progress=0;
		lastprogress=0;
		for ( OWLProperty prop: props )
		{
			processed++;
			progress=processed;
			progress=progress/allWork;
//			System.out.println(progress);
			if (progress>lastprogress+0.05) {
				System.out.print((int)(progress*100) + "%, ");
				lastprogress=progress;
			}
//			String propName = prop.getURI().toString();
			String propName = prop.getURI().getFragment();
//			if (propName==null)
//				propName=prop.getURI().toString().substring(prop.getURI().toString().lastIndexOf("/")+1);
			Term propTerm = m_termFactory.getFunctionalTerm(propName, var1, var2);
			Term propTermInv = m_termFactory.getFunctionalTerm(propName, var2, var1);
			Term[] body = new Term[1];
			body[0] = propTerm;
			Clause clause = new Clause(body, m_termFactory.getFunctionalTerm("Q", var1, var2));
			Set<String> ans;
			if (path.contains("npd"))
				ans = ev.evaluateSQLReturnResults(null, ev.getSQLWRTMappings(clause));
			else
				ans = ev.evaluateSQLReturnResults(null, ev.getSQLavenet(clause));
			if (ans.size() == 0) {
				clausesWithNoAnswers.add(clause);
				//Create the clause with the inverse role and add it also to the list
				body = new Term[1];
				body[0] = propTermInv;
				clause = new Clause(body, m_termFactory.getFunctionalTerm("Q", var1));
				clausesWithNoAnswers.add(clause);
			}
			else {
				roleTermsWithAnswers.add(propTerm);
				invRoleTermsWithAnswers.add(propTermInv);

				rolesAndAnswers.put(propName, ans);
				invRolesAndAnswers.put(propName, ans);
			}
		}
		System.out.println("\nClauses With Answers " + roleTermsWithAnswers.size());
		System.out.println("ClausesWithNoAnswersSize = " + clausesWithNoAnswers.size() + " in " + (System.currentTimeMillis() - startProperties) + "ms");


		/**
		 * Evaluate COMBINATIONS of CONCEPTS
		 * For example for concepts C, D create clause
		 *
		 * Q(x) <- C(x), D(x)
		 *
		 * and identify if there exist answers
		 */
		allWork=conceptTermsWithAnswers.size()*conceptTermsWithAnswers.size();
		System.out.println("\n\nEvaluating combination of concepts");
		System.out.println("Possible combinations: " + allWork);
		long startConceptCombination = System.currentTimeMillis();
		processed=0;
		progress=0;
		lastprogress=0;
		HashSet<String> checkedConcepts = new HashSet<String>();
		for (Term conceptTerm1 : conceptTermsWithAnswers ) {
			for (Term conceptTerm2 : conceptTermsWithAnswers ) {
				if ( checkedConcepts.contains(conceptTerm2.toString()))
					continue;
				processed++;
				progress=processed;
				progress=progress/allWork;
//				System.out.println(progress);
				if (progress>lastprogress+0.05) {
					System.out.print((int)(progress*100) + "%, ");
					lastprogress=progress;
				}
				if ( !conceptTerm1.equals(conceptTerm2) ) {

					Set<String> term1Ans = conceptsAndAnswers.get(conceptTerm1.getName());
					Set<String> term2Ans = conceptsAndAnswers.get(conceptTerm2.getName());

					Term[] body = new Term[2];
					body[0] = conceptTerm1;
					body[1] = conceptTerm2;
					Clause clause = new Clause(body, m_termFactory.getFunctionalTerm("Q", var1));
					if ( !haveCommonAnswers(term1Ans,term2Ans) ) {
						clausesWithNoAnswers.add(clause);
					}
					else {
//						System.out.print("|");
						clausesWithAnswers++;
					}
				}
			}
			checkedConcepts.add(conceptTerm1.toString());
		}
		System.out.println("\nCombinations of concepts with answers " + clausesWithAnswers);
		System.out.println("ClausesWithNoAnswersSize = " + clausesWithNoAnswers.size() + " in " + (System.currentTimeMillis() - startConceptCombination) + "ms");

		clausesWithAnswers=0;

//		System.exit(0);

		/**
		 * Evaluate COMBINATIONS of CONCEPTS and ROLES
		 * For example for concept C and role R create clause
		 *
		 * Q(x) <- C(x), R(x,y)
		 *
		 * and identify if there exist answers
		 */
		allWork=conceptTermsWithAnswers.size()*roleTermsWithAnswers.size();
		System.out.println("\n\nEvaluating combinations of concepts and properties");
		System.out.println("Possible Combinations: " + allWork);
		long startConceptRoleCombination = System.currentTimeMillis();
		processed=0;
		progress=0;
		lastprogress=0;
		for (Term conceptTerm : conceptTermsWithAnswers ) {
			for (Term roleTerm : roleTermsWithAnswers ) {
				processed++;
				progress=processed;
				progress=progress/allWork;
//				System.out.println(progress);
				if (progress>lastprogress+0.05) {
					System.out.print((int)(progress*100) + "%, ");
					lastprogress=progress;
				}
				Term[] body = new Term[2];
				body[0] = conceptTerm;
				body[1] = roleTerm;
				Clause clause = new Clause(body, m_termFactory.getFunctionalTerm("Q", var1));

				Set<String> conceptAns = conceptsAndAnswers.get(conceptTerm.getName());
				Set<String> propsAns = getFirstVariablesAns(rolesAndAnswers.get(roleTerm.getName()));

				if ( !haveCommonAnswers(conceptAns,propsAns) ) {
					clausesWithNoAnswers.add(clause);
				}
				else {
//					System.out.print("|");
					clausesWithAnswers++;
				}
			}
		}
		System.out.println("\nCombinations of concepts with roles with answers " + clausesWithAnswers);
		System.out.println("ClausesWithNoAnswersSize = " + clausesWithNoAnswers.size() + " in " + (System.currentTimeMillis() - startConceptRoleCombination) + "ms");

		clausesWithAnswers = 0;


		/**
		 * Evaluate COMBINATIONS of CONCEPTS and ROLES
		 * For example for concept C and role R create clause
		 *
		 * Q(x) <- C(x), R(y,x)
		 *
		 * and identify if there exist answers
		 */
		allWork=conceptTermsWithAnswers.size()*roleTermsWithAnswers.size();
		System.out.println("\n\nEvaluating combinations of concepts and inverse properties");
		System.out.println("Possible Combinations: " + allWork);
		long startConceptInvRoleCombination = System.currentTimeMillis();
		processed=0;
		progress=0;
		lastprogress=0;
		for (Term conceptTerm : conceptTermsWithAnswers ) {
			for (Term roleTerm : invRoleTermsWithAnswers ) {
				processed++;
				progress=processed;
				progress=progress/allWork;
//				System.out.println(progress);
				if (progress>lastprogress+0.05) {
					System.out.print((int)(progress*100) + "%, ");
					lastprogress=progress;
				}

				Set<String> conceptAns = conceptsAndAnswers.get(conceptTerm.getName());
				Set<String> propsAns = getSecondVariablesAns(rolesAndAnswers.get(roleTerm.getName()));

				if ( !haveCommonAnswers(conceptAns,propsAns) ) {
					Term[] body = new Term[2];
					body[0] = conceptTerm;
					body[1] = roleTerm;
					Clause clause = new Clause(body, m_termFactory.getFunctionalTerm("Q", var1));
					clausesWithNoAnswers.add(clause);
				}
				else {
//					System.out.print("|");
					clausesWithAnswers++;
				}
			}
		}
		System.out.println("\nCombinations of concepts with inverse roles with answers " + clausesWithAnswers);
		System.out.println("ClausesWithNoAnswersSize = " + clausesWithNoAnswers.size() + " in " + (System.currentTimeMillis() - startConceptInvRoleCombination) + "ms");

		clausesWithAnswers = 0;

		/**
		 * Evaluate COMBINATIONS of ROLES
		 * For example for roles S and role R create clause
		 *
		 * Q(x) <- S(x,y), R(x,y)
		 *
		 * and identify if there exist answers
		 */
		allWork=roleTermsWithAnswers.size()*roleTermsWithAnswers.size();
		System.out.println("\n\nEvaluating combinations of properties");
		System.out.println("Possible Combinations: " + allWork);
		long startRoleRoleCombination = System.currentTimeMillis();
		processed=0;
		progress=0;
		lastprogress=0;
		for (Term roleTerm1 : roleTermsWithAnswers ) {
			for (Term roleTerm2 : roleTermsWithAnswers ) {
				processed++;
				progress=processed;
				progress=progress/allWork;
//				System.out.println(progress);
				if (progress>lastprogress+0.05) {
					System.out.print((int)(progress*100) + "%, ");
					lastprogress=progress;
				}
				if (!roleTerm1.equals(roleTerm2))
				{
					Term[] body = new Term[2];
					body[0] = roleTerm1;
					body[1] = roleTerm2;
					Clause clause = new Clause(body, m_termFactory.getFunctionalTerm("Q", var1));

					Set<String> role1Ans = rolesAndAnswers.get(roleTerm1.getName());
					Set<String> role2Ans = rolesAndAnswers.get(roleTerm2.getName());

					if ( !haveCommonAnswers(role1Ans,role2Ans) ) {
						clausesWithNoAnswers.add(clause);
					}
					else
						clausesWithAnswers++;
				}
			}
		}
		System.out.println("\nCombinations of roles with answers " + clausesWithAnswers);
		System.out.println("ClausesWithNoAnswersSize = " + clausesWithNoAnswers.size() + " in " + (System.currentTimeMillis() - startRoleRoleCombination) + "ms");


		System.out.println("\n\nOverall Time = " + (System.currentTimeMillis() - start) + "ms");
		/**
		 * Store this info in a file
		 */
		printResultToFile(optimizationPath, clausesWithNoAnswers);
		ev.closeConn();
	}

	private static Set<String> getFirstVariablesAns(Set<String> set) {

		Set<String> result = new HashSet<String>();

		for ( String str: set ) {
//			System.out.println("1: " + str);
//			System.out.println("2: " + str.substring(0, str.indexOf("----IQAROS---")));
			result.add(str.substring(0, str.indexOf("----IQAROS---")));
		}
		return result;
	}

	private static Set<String> getSecondVariablesAns(Set<String> set) {

		Set<String> result = new HashSet<String>();

		for ( String str: set ) {
//			System.out.println("\n1: " + str);
//			System.out.println("2: " + str.substring((str.indexOf("----IQAROS---")+14), str.length()));
			result.add(str.substring((str.indexOf("----IQAROS---")+14), str.length()));
		}
		return result;
	}


	private static boolean haveCommonAnswers(Set<String> term1Ans, Set<String> term2Ans) {

		for ( String s1: term1Ans )
			if ( term2Ans.contains(s1) )
				return true;

		return false;
	}

	private static void printResultToFile(String outputFilestr, Set<Clause> clauses) throws Exception{
		File outputFile = new File(outputFilestr);
        FileWriter out = new FileWriter(outputFile);

        for(Clause c: clauses){
			out.write( c.m_canonicalRepresentation + "\n");
		}

        out.close();
	}

}
