package edu.aueb.OWL2TTL;

import java.io.File;

import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;


public class OWLtoTurtle {
	
	public static void main(String[] args) throws OWLOntologyCreationException {
		
		String path = "/Users/avenet/Academia/Ntua/Ontologies/";
		
		OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
		
//		String ontologyPath = "/Users/avenet/Academia/Ntua/JavaWorkspace/uba1.7/Ontos30/";
//		String ontologyPath = "/Users/avenet/Academia/Ntua/JavaWorkspace/uba1.7/lubmex20-1Univ/";
//		String ontologyPath = "/Users/avenet/Academia/Ntua/Ontologies/UOBM/UOBMGenerator/preload_generated_uobm30_dbcreation/";
//		String ontologyPath = "/Users/avenet/Academia/Ntua/Ontologies/reactome";
//		String ontologyPath = "/Users/avenet/Academia/Ntua/Ontologies/Fly";
		String ontologyPath = "/Users/avenet/Academia/Ntua/Ontologies/npd-benchmark-master/ontology";

		OWLOntology sourceOntology = null;
//		for( String str : getDataset(ontologyPath))
		String str = ontologyPath + "/npd-v2-ql-abox.owl";
		{
//			if ( str.contains("DS") )
//				continue;
			System.out.println("transforming ontology: " + str);
			sourceOntology = manager.loadOntology(IRI.create("file:" + str));
			String newPathName = "file:" + str;
			newPathName = newPathName.replace(".owl", "new.ttl");
			try {
				manager.saveOntology( sourceOntology, new TurtleOntologyFormat(), IRI.create(newPathName));
				manager.removeOntology(sourceOntology);
			} catch (OWLOntologyStorageException e) {
				System.err.println("Was trying to save at: " + newPathName);
				e.printStackTrace();
			}
		}
		
	}
	
	public static String[] getDataset(String datasetPath) {
//		datasetPath=datasetPath.replace("%20", " ");
	    File dir = new File(datasetPath);
	    File[] aBoxes = dir.listFiles();
	    String[] aBoxesAsStrings = new String[aBoxes.length];
		for (int i=0; i<aBoxes.length; i++)
			aBoxesAsStrings[i] = aBoxes[i].toString();
		return aBoxesAsStrings;
	}
}
