package edu.aueb.queries;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLOntology;

public class DBOperations {

	Connection conn;
	OWLOntology ontology;
	Set<OWLClass> classes = new HashSet<OWLClass>();
	Set<OWLObjectProperty> objProperties = new HashSet<OWLObjectProperty>();
	Set<OWLDataProperty> dataProperties = new HashSet<OWLDataProperty>();
	Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();


	public DBOperations(Connection conn, OWLOntology ontology) {
		this.conn = conn;
		this.ontology = ontology;
		for ( OWLEntity en : ontology.getSignature() ) {
//			System.out.println(en);
			if ( en instanceof OWLClass)
				classes.add((OWLClass) en);
			else if (en instanceof OWLObjectProperty)
				objProperties.add((OWLObjectProperty) en);
			else if (en instanceof OWLDataProperty)
				dataProperties.add((OWLDataProperty) en);
			else if (en instanceof OWLIndividual)
				individuals.add((OWLIndividual) en);
		}
	}

//	public void executeSQLQuery(String sqlQuery) {
	public void executeSQLQuery(PreparedStatement stmt) {

//        PreparedStatement stmt = null;
//		try {
//			stmt = (PreparedStatement) conn.prepareStatement( sqlQuery );
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
////			e1.printStackTrace();
//			System.err.println( "TASSS\t\t\t" + sqlQuery );
//		}

//		logger.info( schemaCreation );
//		System.out.println( sqlQuery );

		try {
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			if( e.getMessage().contains( "does not exist" ) ){
//				StringTokenizer strTok = new StringTokenizer( sqlQuery , " " );
//				strTok.nextToken(); strTok.nextToken();
//				strTok = new StringTokenizer( strTok.nextToken() , "(" );
//				System.err.println( "Table " + strTok.nextToken() + " does not exist" );
//			}
//			else if ( e.getMessage().contains( "already exists" ) ){
//				StringTokenizer strTok = new StringTokenizer( sqlQuery , " " );
//				strTok.nextToken(); strTok.nextToken();
//				strTok = new StringTokenizer( strTok.nextToken() , "(" );
//				System.err.println( "Table " + strTok.nextToken() + " already exists" );
//			}
		}
	}
	
	public void printClasses() {
		int i = 1;
		for (OWLClass cl: classes)
			System.out.println( i++ + ": " + cl + "\t\t" + cl.getURI().toString());
	}

	public void clearDB() throws SQLException {

		String statement = null;
		PreparedStatement pStatement = null;
		for ( OWLClass cl : classes ) {
			statement = "DELETE FROM \"" + cl + "\"";
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);
			statement = "DROP TABLE \"" + cl + "\"";
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);
			statement = "DELETE FROM " + cl;
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);
			statement = "DROP TABLE " + cl;
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);
		}

		for ( OWLObjectProperty objPr : objProperties ) {
			statement = "DELETE FROM " + objPr;
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);
			statement = "DROP TABLE " + objPr;
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);

			statement = "DELETE FROM \"" + objPr + "\"";
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);
			statement = "DROP TABLE \"" + objPr + "\"";
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);
		}

		for ( OWLDataProperty dataPr : dataProperties ) {
			statement = "DELETE FROM " + dataPr;
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);
			statement = "DROP TABLE " + dataPr;
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);

			statement = "DELETE FROM \"" + dataPr + "\"";
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);
			statement = "DROP TABLE \"" + dataPr + "\"";
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);
		}
	}

	public void createTables() throws SQLException {
		String statement = null;
		PreparedStatement pStatement = null;
		int tablesCreated = 0;
		for ( OWLClass cl : classes ) {
//			statement = "CREATE TABLE " + cl.getIRI().getFragment() + "(individual VARCHAR(200))";
			statement = "CREATE TABLE \"" + cl + "\"(individual VARCHAR(200))";
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);
			System.out.println(statement);
			tablesCreated++;
		}

		for ( OWLObjectProperty objPr : objProperties ) {
//			statement = "CREATE TABLE " + objPr.getIRI().getFragment() + "(subject VARCHAR(200), obj VARCHAR(200))";
			statement = "CREATE TABLE \"" + objPr + "\"(subject VARCHAR(200), obj VARCHAR(200))";
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);
//			System.out.println(statement);
			tablesCreated++;
		}

		for ( OWLDataProperty dataPr : dataProperties ) {
//			statement = "CREATE TABLE " + dataPr.getIRI().getFragment() + "(subject VARCHAR(200), obj VARCHAR(200))";
			statement = "CREATE TABLE \"" + dataPr + "\"(subject VARCHAR(200), obj VARCHAR(200))";
//			executeSQLQuery(statement);
			pStatement = conn.prepareStatement(statement);
			executeSQLQuery(pStatement);
//			System.out.println(statement);
			tablesCreated++;
		}
		System.out.println("Tables created = " + tablesCreated);
	}

	public void loadInstances() throws SQLException {

		System.out.println("ONTO LOADED");
		String statement = null;
		PreparedStatement pStatement = null;
		int instLoaded = 0;

		for (OWLAxiom ax: ontology.getAxioms()) {
			if (ax instanceof OWLClassAssertionAxiom) {
				OWLDescription description = ((OWLClassAssertionAxiom) ax).getDescription();
				OWLIndividual individual = ((OWLClassAssertionAxiom) ax).getIndividual();
				statement = "INSERT INTO \"" +  description.toString() + "\" VALUES(?)";
//				statement = "INSERT INTO \"" +  description.toString() + "\" VALUES('" + individual.getURI() + "')";
//				System.out.println(statement);
//				executeSQLQuery(statement);
				pStatement = conn.prepareStatement(statement);
				pStatement.setString(1, individual.getURI().toString());
//				System.out.println(pStatement.toString());
				executeSQLQuery(pStatement);
				instLoaded++;
			}
			if (ax instanceof OWLObjectPropertyAssertionAxiom) {
				OWLObjectPropertyAssertionAxiom objAx = ((OWLObjectPropertyAssertionAxiom) ax);
				OWLIndividual sub = objAx.getSubject();
				OWLIndividual obj = objAx.getObject();
				OWLObjectPropertyExpression property = objAx.getProperty();
				statement = "INSERT INTO \"" +  property + "\" VALUES(?,?)";
//				statement = "INSERT INTO \"" +  property + "\" VALUES('" + sub.getURI().toString().replace("'", "''") + "', '" + obj.getURI().toString().replace("'", "''") + "')";
//				executeSQLQuery(statement);
//				System.out.println( "\t\t" + statement);
				pStatement = conn.prepareStatement(statement);
				pStatement.setString(1, sub.getURI().toString());
				pStatement.setString(2, obj.getURI().toString());
//				System.out.println(pStatement.toString());
				executeSQLQuery(pStatement);
				instLoaded++;
			}
			if (ax instanceof OWLDataPropertyAssertionAxiom) {
				OWLDataPropertyAssertionAxiom objAx = ((OWLDataPropertyAssertionAxiom) ax);
				OWLIndividual sub = objAx.getSubject();
				OWLConstant obj = objAx.getObject();
				OWLDataPropertyExpression property = objAx.getProperty();
				statement = "INSERT INTO \"" +  property + "\" VALUES(?,?)";
//				statement = "INSERT INTO \"" +  property + "\" VALUES('" + sub.getURI().toString().replace("'", "''") + "', '" + obj.getLiteral().toString().replace("'", "''") + "')";
//				executeSQLQuery(statement);
//				System.out.println("\t" + statement);
				pStatement = conn.prepareStatement(statement);
				pStatement.setString(1, sub.getURI().toString());
				pStatement.setString(2, obj.getLiteral().toString());
//				System.out.println(pStatement.toString());
				executeSQLQuery(pStatement);
				instLoaded++;
			}
		}
//		System.out.println( "InstLoaded" );
		System.out.println( "InstLoaded = " + instLoaded );
	}


	public void loadInstancestoOnto(OWLOntology ontology) {

		this.ontology = ontology;
		try {
			loadInstances();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
