package edu.aueb.NPD;


import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.coode.owl.rdf.turtle.TurtleOntologyFormat;
import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.Term;
import org.oxford.comlab.perfectref.rewriter.TermFactory;
import org.oxford.comlab.perfectref.rewriter.Variable;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChangeException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;

import edu.aueb.queries.Evaluator;


public class FindContainmentAmongAnswersBetweenEntities {

	private static TermFactory m_termFactory = new TermFactory();
	static Workbook workbook = new XSSFWorkbook();
	private static final DLliteParser parser = new DLliteParser();

	static String originalPath = "/Users/avenet/Academia/Ntua/Ontologies/";
	static String excelFile = "/Users/avenet/Academia/Aueb/Research/IncrementalQueryAnswering/IQAROS_opt=true_DB_lubm_ex_noJoinOpt.xlsx";
	static String mappingFile;
	static String uri = "";

	static final String npdv = "http://sws.ifi.uio.no/vocab/npd-v2#";
	static final String ptl = "http://sws.ifi.uio.no/vocab/npd-v2-ptl#";

	static boolean print2Excel = false;
	static String addon = "";

	public static void main(String[] args) throws Exception{
		PropertyConfigurator.configure("./logger.properties");

		String ontologyFile, queryPath, optPath, path, dbPath;
		/**
		 * NPD Tests
		 */
		path = originalPath + "npd-benchmark-master/";
		ontologyFile = "file:" + path + "ontology/npd-v2-ql.owl";
		queryPath = path + "avenet_queries/";
		mappingFile = path + "mappings/mysql/npd-v2-ql-mysql_gstoil_avenet.obda";
		optPath = path + "OptimizationClauses/atomicConceptsRoles.txt";
		/*
		 * NPD DB
		 */
		dbPath = "jdbc:mysql://127.0.0.1:3306/npd";
		System.out.println("**************************");
		System.out.println("**\tNPD\t\t**");
		System.out.println("**************************");
		runTest(ontologyFile, queryPath, optPath, dbPath, true, 1, print2Excel);
	}

	private static void runTest(String ontologyFile, String queryPath, String optPath, String dbPath, boolean print) throws Exception {
		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0);
	}

	private static void runTest(String ontologyFile, String queryPath, String optPath, String dbPath, boolean print, int limit) throws Exception {
		runTest(ontologyFile, queryPath, optPath, dbPath, true, 0, false);
	}

	/**
	 * The functionality of this class has been transfered to the Evaluator.java class
	 * 
	 * @param ontologyFile
	 * @param queryPath
	 * @param optPath
	 * @param dbPath
	 * @param print
	 * @param limit
	 * @param printToExcel
	 * @throws Exception
	 */
	private static void runTest(String ontologyFile, String queryPath, String optPath, String dbPath, boolean print, int limit, boolean printToExcel) throws Exception {

		/**
		 * Connect to DB to get instances of concepts and roles
		 */

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		IRI physicalURI = IRI.create(ontologyFile);
        OWLOntology ontology = manager.loadOntology(physicalURI);

		Set<OWLEntity> signature = ontology.getSignature();
		Set<OWLClass> concepts = new HashSet<OWLClass>();
		Set<OWLProperty> props = new HashSet<OWLProperty>();
		
		HashMap<String,Set<String>> conceptsAndAnswers = new HashMap<String,Set<String>>();
		HashMap<String,Set<String>> rolesAndAnswers = new HashMap<String,Set<String>>();
		HashMap<String,Set<String>> invRolesAndAnswers = new HashMap<String,Set<String>>();

		
		for (OWLEntity entity: signature)
		{
			if ( entity instanceof OWLClass )
				concepts.add((OWLClass) entity);
			if ( entity instanceof OWLObjectProperty || entity instanceof OWLDataProperty )
				props.add((OWLProperty) entity);
		}

		

		long start = System.currentTimeMillis();
       	Evaluator ev = new Evaluator(dbPath,mappingFile);
//       	ev.seeForeignKeys();
       	
		Variable var1 = m_termFactory.getVariable(0);
		Variable var2 = m_termFactory.getVariable(1);


		System.out.println("\nEvaluating all concepts: there are " + concepts.size());
		long startConcepts = System.currentTimeMillis();
		int processed=0;
		double progress=0;
		double lastprogress=0;
		int allWork=concepts.size();
		Set<Clause> clausesWithNoAnswers = new HashSet<>();
		Set<Term> conceptTermsWithAnswers = new HashSet<>();
		Set<Term> roleTermsWithAnswers = new HashSet<>();
		for ( OWLClass cl: concepts )
		{
			if ( cl.toString().contains("owl:Thing") )
				continue;
			processed++;
			progress=processed;
			progress=progress/allWork;
//			System.out.println(progress);
			if (progress>lastprogress+0.05) {
				System.out.print((int)(progress*100) + "%, ");
				lastprogress=progress;
			}
//			String clName = cl.getIRI().toString();
			//getFragment does not work well when the string starts with a number
			String clName = cl.getIRI().getFragment();
//			if (clName==null)
//				String clName=cl.getIRI().toString().substring(cl.getIRI().toString().lastIndexOf("/")+1);
//			System.out.println(clName + " " + var1);
			Term clTerm = m_termFactory.getFunctionalTerm(clName, var1);
			Term[] body = new Term[1];
			body[0] = clTerm;
			Clause clause = new Clause(body, m_termFactory.getFunctionalTerm("Q", var1));
//			System.out.println("\n" + clause + "\t" + ev.getSQLWRTMappings(clause));
			Set<String> ans = ev.evaluateSQLReturnResults(null, ev.getSQLWRTMappings(clause));
			if (ans.size() == 0) {
//				entitiesWithNoAnswersMap.put(clName, ans);
//				conceptTermsWithNoAnswers.add(clTerm);
				clausesWithNoAnswers .add(clause);
			}
			else {
				conceptTermsWithAnswers.add(clTerm);
				conceptsAndAnswers.put(clName, ans);
			}
		}
		System.out.println("\nClauses With Answers " + conceptTermsWithAnswers.size());
		System.out.println("ClausesWithNoAnswersSize = " + clausesWithNoAnswers.size() + " in " + (System.currentTimeMillis() - startConcepts) + "ms");
       	
		int containedConcepts = 0;

		for (Term ct1: conceptTermsWithAnswers ) {
			for (Term ct2: conceptTermsWithAnswers) {
				if (ct1.equals(ct2))
					continue;
//				System.out.println("dsdsd");
//				System.out.println(conceptsAndAnswers.get(ct1.getName()).size());
				if ( containsAnswers( conceptsAndAnswers.get(ct1.getName()), conceptsAndAnswers.get(ct2.getName()) ) )
				{
					containedConcepts++;
//					System.out.println(ct1.getName() + " contains all the answers of " + ct2.getName());
				}
			}
		}
		
		System.out.println("#of concepts that are contained " + containedConcepts);
		
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
//			String propName = prop.getIRI().toString();
			String propName = prop.getIRI().getFragment();
			if (propName==null)
				propName=prop.getIRI().toString().substring(prop.getIRI().toString().lastIndexOf("/")+1);
			Term propTerm = m_termFactory.getFunctionalTerm(propName, var1, var2);
			Term propTermInv = m_termFactory.getFunctionalTerm(propName, var2, var1);
			Term[] body = new Term[1];
			body[0] = propTerm;
			Clause clause = new Clause(body, m_termFactory.getFunctionalTerm("Q", var1, var2));
			Set<String> ans;
			String queries = ev.getSQLWRTMappings(clause);
			ans = ev.evaluateSQLReturnResults(null, queries);
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
//				invRoleTermsWithAnswers.add(propTermInv);

				rolesAndAnswers.put(propName, ans);
//				invRolesAndAnswers.put(propName, ev.evaluateSQLReturnResultsInverse(null, queries));
			}
		}
		System.out.println("\nClauses With Answers " + (roleTermsWithAnswers.size() + invRolesAndAnswers.size() ) );
		System.out.println("ClausesWithNoAnswersSize = " + clausesWithNoAnswers.size() + " in " + (System.currentTimeMillis() - startProperties) + "ms");
		
		int containedRoles = 0;
		
		for (Term ct1: roleTermsWithAnswers ) {
			for (Term ct2: roleTermsWithAnswers) {
				if (ct1.equals(ct2))
					continue;
//				System.out.println("dsdsd");
//				System.out.println(conceptsAndAnswers.get(ct1.getName()).size());
				if ( containsAnswers( rolesAndAnswers.get(ct1.getName()), rolesAndAnswers.get(ct2.getName()) ) )
				{
					containedRoles++;
					System.out.println(ct1.getName() + " contains all the answers of " + ct2.getName());
				}
			}
		}
		System.out.println("#of containedRoles: " + containedRoles);

		
       	System.out.println("\nPerformed in " + ((System.currentTimeMillis() - start)) + "ms");
		ev.closeConn();
	}

	private static boolean containsAnswers(Set<String> set1, Set<String> set2) {

		if (set1 ==null || set2 == null)
			System.out.println("null");
		if ( set2.size() == 0 )
			return true;

		for (String set2Ans: set2) {
			if ( !set1.contains(set2Ans) )
			{
				return false;
			}
		}

		return true;
	}


}