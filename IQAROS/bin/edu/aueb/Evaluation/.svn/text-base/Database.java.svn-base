package edu.aueb.Evaluation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;



public class Database {

	public static void main(String[] args) throws SQLException, OWLOntologyCreationException {
		// TODO Auto-generated method stub
//		String ontologyFile;
//		String dbName = "Test1";
////		createDB(dbName);
//		createTable("Test1", "table1");
//		System.out.println("Done");
		
		
		IRI iri = IRI.create("file:/Users/avenet/Academia/Ntua/Ontologies/LUBM/University0_0.owl");
		
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology onto = manager.loadOntologyFromOntologyDocument(iri);
		
		Set<OWLClass> classes = onto.getClassesInSignature();
		Set<OWLObjectProperty> objectProperties = onto.getObjectPropertiesInSignature();
		

		
	}
	
	private static void createTable(String dbName, String tableName) throws SQLException {
		Statement stm = null;

		Connection connection = DriverManager.getConnection(
				   "jdbc:postgresql://127.0.0.1:5432/test1","postgres", "0000");

		stm = connection.createStatement();
				
		stm = connection.createStatement();
		String query = "CREATE TABLE  table1111(PersonID int, LastName varchar(255),	FirstName varchar(255),	Address varchar(255), City varchar(255)	)";
		stm.execute(query);
		
		connection.close();
	}

	
	private static void createDB(String dbName) throws SQLException {
		Connection connection = null;
		Statement stm = null;

		String query = "CREATE DATABASE " + dbName;
		
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(
			   "jdbc:postgresql://127.0.0.1:5432/","postgres", "0000");

			stm = connection.createStatement();
			stm.executeQuery(query);

			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stm != null)
				stm.close();
		}
	}
}
