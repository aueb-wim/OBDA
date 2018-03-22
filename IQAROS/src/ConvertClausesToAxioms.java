import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.PI;
import org.semanticweb.HermiT.Reasoner.Configuration;
import org.semanticweb.HermiT.owlapi.structural.OwlNormalization;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLObjectPropertyInverse;
import org.semanticweb.owl.model.OWLObjectSomeRestriction;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLIndividual;

/**
 * Reduce expressivity of ontologies
 * @author avenet
 *
 */

public class ConvertClausesToAxioms {

	private static final DLliteParser parser = new DLliteParser();
	
	public static void main(String[] args) throws Exception{

		String ontologyFile,newOnto;
		String queryFile;
		ArrayList<PI> tBoxAxioms = new ArrayList<PI>();

//		String path = System.getProperty("user.dir")+ "/dataset/Evaluation_ISWC'09/";
		String path = "/Users/avenet/Academia/Ntua/Ontologies/";
		
		queryFile = path + "LUBM/QueriesNoConstants/Query_11.txt";
		
//		ontologyFile = "file:" + path + "LUBM/univ-bench.owl";
//		newOnto = "file:" + path + "LUBM/univ-bench_DL-Lite_mine.owl";

		ontologyFile = "file:" + path + "UOBM/univ-bench-dl-Zhou.owl";
		newOnto = "file:" + path + "UOBM/univ-bench-dl-Zhou_DL-Lite_mine---.owl";

//		ontologyFile = "file:" + path + "reactome/biopax-level3-processed.owl";
//		newOnto = "file:" + path + "reactome/biopax-level3-processed_DL-Lite_mine.owl";

//		ontologyFile = "file:" + path + "Vicodi/vicodi_all.owl";
//		newOnto = "file:" + path + "Vicodi/vicodi_all_DL-Lite_mine.owl";

//		ontologyFile = "file:" + path + "Semintec/bigFile.owl";
//		newOnto = "file:" + path + "Semintec/bigFile_DL-Lite_mine.owl";

		tBoxAxioms = parser.getAxioms(ontologyFile);
		
		URI physicalURI = URI.create(ontologyFile);
		URI newOntoURI = URI.create(newOnto);
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromPhysicalURI(physicalURI);
        OWLOntology newOntology = manager.createOntology(newOntoURI);
        OWLDataFactory factory = manager.getOWLDataFactory();
        URI ontoURI = ontology.getURI();
        
        /**
         * Copy concepts, object&data properties
         */
        for ( OWLEntity ent : ontology.getSignature()) {
        	System.out.println(ent + "\t\t" + ent.isOWLClass());
        }
        
        System.out.println(ontoURI);
        
        OWLClass thing = factory.getOWLThing();
			
		for ( PI pi: tBoxAxioms ) {
			URI uri1 = URI.create(ontoURI + "#" + pi.m_left);
			URI uri2 = URI.create(ontoURI + "#" + pi.m_right);

			switch (pi.m_type) {
				case 1: {
//					System.out.println( pi.m_left + "\t\t" + pi.m_right );
					OWLClass leftClass = factory.getOWLClass(uri1);
					OWLClass rightClass = factory.getOWLClass(uri2);
					OWLAxiom axiom = factory.getOWLSubClassAxiom(leftClass, rightClass);
					manager.addAxiom(newOntology, axiom);
//					System.out.println(axiom);
					break;
				}
				case 2: {
//					System.out.println( pi.m_left + "\t\t" + pi.m_right );
					OWLClass leftClass = factory.getOWLClass(uri1);
					OWLObjectSomeRestriction rightClass = factory.getOWLObjectSomeRestriction(factory.getOWLObjectProperty(uri2), thing);
					OWLAxiom axiom = factory.getOWLSubClassAxiom(leftClass, rightClass);
					manager.addAxiom(newOntology, axiom);
//					System.out.println(axiom);
					break;
				}
				case 3: {
//					System.out.println( pi.m_left + "\t\t" + pi.m_right );
					OWLClass leftClass = factory.getOWLClass(uri1);
					OWLObjectSomeRestriction rightClass = factory.getOWLObjectSomeRestriction(factory.getOWLObjectPropertyInverse(factory.getOWLObjectProperty(uri2)), thing);
					OWLAxiom axiom = factory.getOWLSubClassAxiom(leftClass, rightClass);
					manager.addAxiom(newOntology, axiom);
//					System.out.println(axiom);
					break;
				}
				case 4: {
//					System.out.println( pi.m_left + "\t\t" + pi.m_right );
					OWLObjectSomeRestriction leftClass = factory.getOWLObjectSomeRestriction(factory.getOWLObjectProperty(uri1), thing);
					OWLClass rightClass = factory.getOWLClass(uri2);
					OWLAxiom axiom = factory.getOWLSubClassAxiom(leftClass, rightClass);
					manager.addAxiom(newOntology, axiom);
//					System.out.println(axiom);
					break;
				}
				case 5: {
//					System.out.println( pi.m_left + "\t\t" + pi.m_right );
					OWLObjectSomeRestriction leftClass = factory.getOWLObjectSomeRestriction(factory.getOWLObjectProperty(uri1), thing);
					OWLObjectSomeRestriction rightClass = factory.getOWLObjectSomeRestriction(factory.getOWLObjectProperty(uri2), thing);
					OWLAxiom axiom = factory.getOWLSubClassAxiom(leftClass, rightClass);
					manager.addAxiom(newOntology, axiom);
					break;
				}
				case 6: {
//					System.out.println( pi.m_left + "\t\t" + pi.m_right );
					OWLObjectSomeRestriction leftClass = factory.getOWLObjectSomeRestriction(factory.getOWLObjectProperty(uri1), thing);
					OWLObjectSomeRestriction rightClass = factory.getOWLObjectSomeRestriction(factory.getOWLObjectPropertyInverse(factory.getOWLObjectProperty(uri2)), thing);
					OWLAxiom axiom = factory.getOWLSubClassAxiom(leftClass, rightClass);
					manager.addAxiom(newOntology, axiom);
					break;
				}
				case 7: {
//					System.out.println( pi.m_left + "\t\t" + pi.m_right );
					OWLObjectSomeRestriction leftClass = factory.getOWLObjectSomeRestriction(factory.getOWLObjectPropertyInverse(factory.getOWLObjectProperty(uri1)), thing);
					OWLClass rightClass = factory.getOWLClass(uri2);
					OWLAxiom axiom = factory.getOWLSubClassAxiom(leftClass, rightClass);
					manager.addAxiom(newOntology, axiom);
//					System.out.println(axiom);
					break;
				}
				case 8: {
//					System.out.println( pi.m_left + "\t\t" + pi.m_right );
					OWLObjectSomeRestriction leftClass = factory.getOWLObjectSomeRestriction(factory.getOWLObjectPropertyInverse(factory.getOWLObjectProperty(uri1)), thing);
					OWLObjectSomeRestriction rightClass = factory.getOWLObjectSomeRestriction(factory.getOWLObjectProperty(uri2), thing);
					OWLAxiom axiom = factory.getOWLSubClassAxiom(leftClass, rightClass);
					manager.addAxiom(newOntology, axiom);
//					System.out.println(axiom);
					break;
				}
				case 9: {
//					System.out.println( pi.m_left + "\t\t" + pi.m_right );
					OWLObjectSomeRestriction leftClass = factory.getOWLObjectSomeRestriction(factory.getOWLObjectPropertyInverse(factory.getOWLObjectProperty(uri1)), thing);
					OWLObjectSomeRestriction rightClass = factory.getOWLObjectSomeRestriction(factory.getOWLObjectPropertyInverse(factory.getOWLObjectProperty(uri2)), thing);
					OWLAxiom axiom = factory.getOWLSubClassAxiom(leftClass, rightClass);
//					System.out.println(axiom);
					manager.addAxiom(newOntology, axiom);
					break;
				}
				case 10: {
//					System.out.println( pi.m_left + "\t\t" + pi.m_right );
					OWLObjectProperty leftProp = factory.getOWLObjectProperty(uri1);
					OWLObjectProperty rightProp = factory.getOWLObjectProperty(uri2);
					OWLAxiom axiom = factory.getOWLSubObjectPropertyAxiom(leftProp, rightProp);
//					System.out.println(axiom);
					manager.addAxiom(newOntology, axiom);
					
//					OWLObjectPropertyInverse leftPropInv = factory.getOWLObjectPropertyInverse(factory.getOWLObjectProperty(uri1));
//					OWLObjectPropertyInverse rightPropInv = factory.getOWLObjectPropertyInverse(factory.getOWLObjectProperty(uri2));
//					axiom = factory.getOWLSubObjectPropertyAxiom(leftPropInv, rightPropInv);
//					manager.addAxiom(newOntology, axiom);
					
					break;
				}
				case 11: {
//					System.out.println( pi.m_left + "\t\t" + pi.m_right );
					OWLObjectPropertyInverse leftProp = factory.getOWLObjectPropertyInverse(factory.getOWLObjectProperty(uri1));
					OWLObjectProperty rightProp = factory.getOWLObjectProperty(uri2);
					OWLAxiom axiom = factory.getOWLSubObjectPropertyAxiom(leftProp, rightProp);
//					System.out.println(axiom);
					manager.addAxiom(newOntology, axiom);
					break;
				}
				default:
					System.out.println( pi.m_left + "\t\t" + pi.m_right );
					
			}
			manager.saveOntology(newOntology);
		}
	}
}
