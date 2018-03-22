package edu.aueb.queries;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.text.TabExpander;

import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.Constant;
import org.oxford.comlab.perfectref.rewriter.Term;
import org.oxford.comlab.perfectref.rewriter.Variable;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

import edu.aueb.NPD.OBDAParser;

public class Evaluator2 {
	
	String connStr;
	int resSize;
	long evalTime;
	Connection conn;
	
	public OBDAParser mappingManager;
	
	String database;
	
	public int getNumOfAns() {
		return resSize;
	}

	public long getEvalTime() {
		return evalTime;
	}

	public int getMaxAtoms() {
		return maxAtoms;
	}

	int maxAtoms;
	
	public void closeConn() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createConnection() throws SQLException {
    	if ( connStr.startsWith("jdbc:postgresql")) {
			try {
				DriverManager.registerDriver(new org.postgresql.Driver());
				conn = DriverManager.getConnection(connStr + "?user=postgres"+"&password=0000");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
    	}
		else if (connStr.startsWith("jdbc:mysql")) {
			try {
				DriverManager.registerDriver(new com.mysql.jdbc.Driver());
				conn = DriverManager.getConnection(connStr + "?user=root"+"&password=");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		conn.setAutoCommit(false);
		
		System.out.println("Connection Created");
	}
	
	
	public Evaluator2(String dbUsed) throws SQLException {
		maxAtoms = 0;
		connStr = dbUsed;
		createConnection();
	}
	
	public Evaluator2(String dbUsed,String mappings) throws SQLException {
		maxAtoms = 0;
		connStr = dbUsed;
		createConnection();
		System.out.println();

		mappingManager = new OBDAParser(mappings);
	}
	
	public Evaluator2() throws SQLException {
		maxAtoms = 0;
		
//		connStr = "jdbc:postgresql://127.0.0.1:5432/TestOnto";
//		connStr = "jdbc:postgresql://127.0.0.1:5432/reactome";
//		connStr = "jdbc:postgresql://127.0.0.1:5432/reactome2";
		connStr = "jdbc:postgresql://127.0.0.1:5432/Fly";
//		connStr = "jdbc:postgresql://127.0.0.1:5432/Vicodi";
//		connStr = "jdbc:postgresql://127.0.0.1:5432/Semintec";
//		connStr = "jdbc:postgresql://127.0.0.1:5432/UOBM";
//		connStr = "jdbc:postgresql://127.0.0.1:5432/UOBM5";
//		connStr = "jdbc:postgresql://127.0.0.1:5432/UOBM10";
//		connStr = "jdbc:postgresql://127.0.0.1:5432/UOBM30";
//		connStr = "jdbc:postgresql://127.0.0.1:5432/UOBM50";
//		connStr = "jdbc:postgresql://127.0.0.1:5432/LUBM";
//		connStr = "jdbc:postgresql://127.0.0.1:5432/LUBM-ex";
//		connStr = "jdbc:postgresql://127.0.0.1:5432/LUBM5"; //This database contains data from 15 universities
//		connStr = "jdbc:postgresql://127.0.0.1:5432/LUBM10"; //This database contains data from 15 universities
//		connStr = "jdbc:postgresql://127.0.0.1:5432/LUBMLarge"; //This database contains data from 15 universities
//		connStr = "jdbc:postgresql://127.0.0.1:5432/LUBM30"; //This database contains data from 30 universities
		
		createConnection();

	}


	public void runAllRewQueriesAtOnce(ArrayList<Clause> rewriting) {
		String sqlQuery;
		try {
		    if ( rewriting.size() > 0 ) {
				sqlQuery = getSQL(rewriting);
//				System.out.println(rewriting);
//				System.out.println(sqlQuery);
				System.out.println( "Evaluating rew size = " + rewriting.size() );
//		    	long begin = System.currentTimeMillis();
		    	evaluateSQL(null, sqlQuery);
//		    	long end = System.currentTimeMillis();
//		    	System.out.println("Eval Time = " + (end - begin) + "ms" );
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void runAllRewQueriesOneByOne(ArrayList<Clause> rewriting) {
		String sqlQuery;
		ArrayList<Clause> rewWithNonEmptyClauses = new ArrayList<Clause>();

		try {
			if (rewriting.size() > 0 ) {
				for ( Clause clause : rewriting ) {
					if ( clause.getHead().getName().toString().equals( "Q" ) ) {
						sqlQuery = getSQLavenet(clause).concat(";");
						int clauseAnsSize = evaluateSQLandReturnNumOfAnws(null,sqlQuery);
						//avenet - me ayto petame ta queries pou exoun to headOf epeidi oi apantiseiw tous einai redundant
						//LUBM
//						if ( clauseAnsSize > 0 && !( queryContainsBodyAtom(clause, "headOf") || queryContainsBodyAtom(clause, "ResearchAssistant") ) ) {
						//UOBM
//						if ( clauseAnsSize > 0 && !(queryContainsBodyAtom(clause, "ResearchAssistant") ) ) {
//						Semintec
//						if ( clauseAnsSize > 0 && !queryContainsBodyAtom(clause, "hasCreditCard") ) {
//						Vicodi
//						if ( clauseAnsSize > 0 && !queryContainsBodyAtom(clause, "") ) {
						if ( clauseAnsSize > 0 ) {
							System.out.println( "Clause = " + clause );
							System.out.println( "SQL = " + sqlQuery );
							System.out.println( clauseAnsSize );
							rewWithNonEmptyClauses.add(clause);
						}
					}
					else
						System.err.println("\t\t" + clause);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println( "Size of rew that all queries return answers = " + rewWithNonEmptyClauses.size() );

		runAllRewQueriesAtOnce(rewWithNonEmptyClauses);
	}

	private boolean queryContainsBodyAtom(Clause clause, String string) {

		for ( Term atom: clause.getBody())
			if (atom.getName().toString().equals(string)) {
//				System.out.println( "\t\t\t" + clause );
				return true;
			}

		return false;
	}

	private ArrayList<Clause> pruneEmptyPredicates(ArrayList<Clause> clauses, HashMap<String,String> mappings){

		ArrayList<Clause> result = new ArrayList<Clause>();

		boolean toBePruned;

		for(Clause c: clauses){
			toBePruned = false;
			for(Term bodyAtom : c.getBody()){
				if(!mappings.containsKey(bodyAtom.getName())){
					toBePruned = true;
					break;
				}
			}
			if(!toBePruned){
				result.add(c);
			}
		}

		return result;
	}

//	public String getSQL(ArrayList<Clause> rewriting, HashMap<String,String> mappings) throws Exception {
//
//		rewriting = this.pruneEmptyPredicates(rewriting, mappings);
//
//		if(rewriting.size() > 0){
//			String result = "";
//			Clause clause;
//
//			for(int i=0; i<rewriting.size(); i++){
//				clause = rewriting.get(i);
//
//				if(clause.getHead().getPredicate().equals("Q")){
//					if(i!=0)
//						result += " UNION \n";
//					result += getSQL(rewriting.get(i), mappings);
//				}
//				else{
//					return null;
//
//				}
//			}
//
//			result += ";";
//			return result;
//		}
//		return "";
//	}

	public String getSQL(ArrayList<Clause> rewriting) throws Exception {

//		rewriting = this.pruneEmptyPredicates(rewriting, mappings);

		if(rewriting.size() > 0){
			String result = "";
			Clause clause;

			int j = 0;

			for(int i=0; i<rewriting.size(); i++){
				clause = rewriting.get(i);

//				System.out.println(clause.getHead().getPredicate().toString().equals("Q"));

				if(clause.getHead().getName().toString().equals("Q")){
					if(j!=0)
						result += " UNION \n";
					result += getSQLavenet(rewriting.get(i));
					j++;
				}
//				else{
//					return null;
//				}
			}

			result += ";";
			System.out.println(result);
			return result;
		}
		return "";
	}
	
	public String getSQLWRTMappings(ArrayList<Clause> rewriting) throws Exception {

//		rewriting = this.pruneEmptyPredicates(rewriting, mappings);

		if(rewriting.size() > 0){
			String result = "";
			Clause clause;

			int j = 0;
			for(int i=0; i<rewriting.size(); i++){
				clause = rewriting.get(i);

//				System.out.println(clause.getHead().getPredicate().toString().equals("Q"));

				if(clause.getHead().getName().toString().equals("Q")){
					if(j!=0)
						result += " UNION \n";
					System.out.println("query as clause: " + rewriting.get(i));
					String cqInUCQ = getSQLavenet(rewriting.get(i));
//					System.out.println("SQL before mapping translation: " + cqInUCQ);
					result += mappingManager.replaceFrom(cqInUCQ);
					j++;
				}
//				else{
//					return null;
//				}
			}

			result += ";";
//			System.out.println("res: " + result);
			return result;
		}
		return "";
	}


//	private String getSQL(Clause clause, HashMap<String,String> mappings){
//		String result = "";
//
//		ArrayList<String> selectParts = new ArrayList<String>();
//		ArrayList<String> fromParts = new ArrayList<String>();
//		ArrayList<String> whereParts = new ArrayList<String>();
//
//
//		HashMap<String,String> mappingVariables = new HashMap<String,String>();
//
//		for(int i = 0; i < clause.getBody().size(); i ++){
//			Atom bodyAtom = clause.getBody().get(i);
//
////			fromParts.add("(" + mappings.get(bodyAtom.getPredicate()) + ") AS T" + i);
//
//			for(int j=0; j<bodyAtom.getTerms().size(); j++){
//				//variable
//				if(bodyAtom.getArgument(j) instanceof Variable){
//					Variable var = (Variable) bodyAtom.getArgument(j);
//					if(!mappingVariables.containsKey(var.getName())){
//						mappingVariables.put(var.getName(), "T" + i + ".C" + j);
//					}
//					//Shared variable
//					else{
//						whereParts.add(mappingVariables.get(var.getName()) + " = " + "T" + i + ".C" + j);
//						mappingVariables.put(var.getName(), "T" + i + ".C" + j);
//					}
//				}
//				//constant
//				if(bodyAtom.getArgument(j) instanceof FunctionalTerm){
//					FunctionalTerm cons = (FunctionalTerm) bodyAtom.getArgument(j);
//					whereParts.add("T" + i + ".C" + j + " = '" + cons.getName().substring(1, cons.getName().length()-1) + "'");
//				}
//			}
//		}
//
//		for(Term vc: clause.getHead().getArguments()){
//			//variable
//			if(vc instanceof Variable){
//				selectParts.add(mappingVariables.get(vc.getName()));
//			}
//			//constant
//			if(vc instanceof FunctionalTerm){
//				selectParts.add("'" + vc.getName() + "'");
//			}
//		}
//
//		result = "SELECT ";
//		for(int s=0; s<selectParts.size(); s++){
//			if(s!=0)
//				result += ", ";
//			result += selectParts.get(s);
//		}
//
//		result += " FROM ";
//
//		for(int f=0; f<fromParts.size(); f++){
//			if(f!=0)
//				result += ", ";
//			result += fromParts.get(f);
//		}
//
//		if(whereParts.size() > 0){
//			result += " WHERE ";
//
//			for(int w=0; w<whereParts.size(); w++){
//				if(w!=0)
//					result += " AND ";
//				result += whereParts.get(w);
//			}
//		}
//
//		return result;
//	}


//	public String getSQL(Clause clause){
//		String result = "";
//
//		ArrayList<String> selectParts = new ArrayList<String>();
//		ArrayList<String> fromParts = new ArrayList<String>();
//		ArrayList<String> whereParts = new ArrayList<String>();
//
//
//		HashMap<String,String> mappingVariables = new HashMap<String,String>();
//
//		for(int i = 0; i < clause.getBody().size(); i ++){
//			Atom bodyAtom = clause.getBody().get(i);
//
////			fromParts.add("(" + bodyAtom.getName() + ") AS T" + i);
//			fromParts.add(bodyAtom.getPredicate() + " AS T" + i);
//
//			for(int j=0; j<bodyAtom.getTerms().size(); j++){
//				//variable
//				if(bodyAtom.getArgument(j) instanceof Variable){
//					Variable var = (Variable) bodyAtom.getArgument(j);
//					if(!mappingVariables.containsKey(var.getName())){
//						mappingVariables.put(var.getName(), "T" + i + ".C" + j);
//					}
//					//Shared variable
//					else{
//						whereParts.add(mappingVariables.get(var.getName()) + " = " + "T" + i + ".C" + j);
//						mappingVariables.put(var.getName(), "T" + i + ".C" + j);
//					}
//				}
//				//constant
//				if(bodyAtom.getArgument(j) instanceof FunctionalTerm){
//					FunctionalTerm cons = (FunctionalTerm) bodyAtom.getArgument(j);
//					whereParts.add("T" + i + ".C" + j + " = '" + cons.getName().substring(1, cons.getName().length()-1) + "'");
//				}
//			}
//		}
//
//		for(Term vc: clause.getHead().getArguments()){
//			//variable
//			if(vc instanceof Variable){
//				selectParts.add(mappingVariables.get(vc.getName()));
//			}
//			//constant
//			if(vc instanceof FunctionalTerm){
//				selectParts.add("'" + vc.getName() + "'");
//			}
//		}
//
//		result = "SELECT ";
//		for(int s=0; s<selectParts.size(); s++){
//			if(s!=0)
//				result += ", ";
//			result += selectParts.get(s);
//		}
//
//		result += " FROM ";
//
//		for(int f=0; f<fromParts.size(); f++){
//			if(f!=0)
//				result += ", ";
//			result += fromParts.get(f);
//		}
//
//		if(whereParts.size() > 0){
//			result += " WHERE ";
//
//			for(int w=0; w<whereParts.size(); w++){
//				if(w!=0)
//					result += " AND ";
//				result += whereParts.get(w);
//			}
//		}
//
//		return result;
//	}


	public String getSQLavenet(Clause clause){
		String result = "";

		ArrayList<String> selectParts = new ArrayList<String>();
		ArrayList<String> fromParts = new ArrayList<String>();
		ArrayList<String> whereParts = new ArrayList<String>();

//		System.out.println("Clause = " + clause);

		HashMap<String,String> mappingVariables = new HashMap<String,String>();

		if ( maxAtoms < clause.getBody().length )
			maxAtoms = clause.getBody().length;
		
		for(int i = 0; i < clause.getBody().length; i ++){
			Term bodyAtom = clause.getBody()[i];

//			fromParts.add("(" + bodyAtom.getName() + ") AS T" + i);
			fromParts.add("\"" + bodyAtom.getName().replace("/", "") + "\" AS T" + i);

			for(int j=0; j<bodyAtom.getArguments().length; j++){
				//variable
				if(bodyAtom.getArgument(j) instanceof Variable){
					Variable var = (Variable) bodyAtom.getArgument(j);
					if(!mappingVariables.containsKey(var.getName())){
						if (bodyAtom.getArguments().length == 1)
							mappingVariables.put(var.getName(), "T" + i + ".individual");
						else
							if (j == 0)
								mappingVariables.put(var.getName(), "T" + i + ".subject");
							else
								mappingVariables.put(var.getName(), "T" + i + ".obj");
					}
					//Shared variable
					else{
						if (bodyAtom.getArguments().length == 1) {
							whereParts.add(mappingVariables.get(var.getName()) + " = " + "T" + i + ".individual");
							mappingVariables.put(var.getName(), "T" + i + ".individual");
						}else
							if (j == 0)
							{
								whereParts.add(mappingVariables.get(var.getName()) + " = " + "T" + i + ".subject");
								mappingVariables.put(var.getName(), "T" + i + ".subject");
							}
							else
							{
								whereParts.add(mappingVariables.get(var.getName()) + " = " + "T" + i + ".obj");
								mappingVariables.put(var.getName(), "T" + i + ".obj");
							}
					}
				}
				//constant
				if(bodyAtom.getArgument(j) instanceof Constant){
					Constant cons = (Constant) bodyAtom.getArgument(j);
					if (bodyAtom.getArguments().length == 1)
						whereParts.add("T" + i + ".individual = '" + cons.getName().substring(0, cons.getFunctionalPrefix().length()) + "'");
					else
						if (j == 0)
							whereParts.add("T" + i + ".subject = '" + cons.getName().substring(0, cons.getName().length()) + "'");
						else
							whereParts.add("T" + i + ".obj = '" + cons.getName().substring(0, cons.getName().length()) + "'");
				}
			}
		}

		Term headAtom = clause.getHead();

		for ( int j = 0; j < headAtom.getArguments().length ; j++) {
			if ( headAtom.getArgument(j) instanceof Variable ) {
				Variable var = (Variable) headAtom.getArgument(j);
				selectParts.add(mappingVariables.get(var.getName()));
			}
			//constant
			if(headAtom.getArgument(j) instanceof Constant){
				Constant con = (Constant) headAtom.getArgument(j);
				selectParts.add("'" + con.getName() + "'");
			}
		}
		//select count(1) 28, select no distinct 29, select count( ,,, ) 26, select distinct 43, select * 29, 
//		result = "SELECT * ";
		result = "SELECT ";
		for(int s=0; s<selectParts.size(); s++){
			if(s!=0)
				result += ", ";
			result += selectParts.get(s);
		}

		result += " FROM ";

		for(int f=0; f<fromParts.size(); f++){
			if(f!=0)
				result += ", ";
			result += fromParts.get(f);
		}

		if(whereParts.size() > 0){
			result += " WHERE ";

			for(int w=0; w<whereParts.size(); w++){
				if(w!=0)
					result += " AND ";
				result += whereParts.get(w);
			}
		}
//		result = "SELECT EXISTS( " + result + ")";
		
//		System.out.println("SQL Query" + result);
//		result=mappingManager.replaceFrom(result);
		return result;
	}

	public String getSQLFromAtom(Term bodyAtom){
		String result = "";

		ArrayList<String> selectParts = new ArrayList<String>();
		ArrayList<String> fromParts = new ArrayList<String>();
		ArrayList<String> whereParts = new ArrayList<String>();

//		System.out.println("Clause = " + clause);

		HashMap<String,String> mappingVariables = new HashMap<String,String>();

//		for(int i = 0; i < clause.getBody().size(); i ++){
		int i = 0;
//			Atom bodyAtom = clause.getBody().get(i);

//			fromParts.add("(" + bodyAtom.getName() + ") AS T" + i);
			fromParts.add("\"" + bodyAtom.getName() + "\" AS T" + i);

			for(int j=0; j<bodyAtom.getArguments().length; j++){
				//variable
				if(bodyAtom.getArgument(j) instanceof Variable){
					Variable var = (Variable) bodyAtom.getArgument(j);
					if(!mappingVariables.containsKey(var.getName())){
						if (bodyAtom.getArguments().length == 1)
							mappingVariables.put(var.getName(), "T" + i + ".individual");
						else
							if (j == 0)
								mappingVariables.put(var.getName(), "T" + i + ".subject");
							else
								mappingVariables.put(var.getName(), "T" + i + ".obj");
					}
					//Shared variable
					else{
						if (bodyAtom.getArguments().length == 1) {
							whereParts.add(mappingVariables.get(var.getName()) + " = " + "T" + i + ".individual");
							mappingVariables.put(var.getName(), "T" + i + ".individual");
						}else
							if (j == 0)
							{
								whereParts.add(mappingVariables.get(var.getName()) + " = " + "T" + i + ".subject");
								mappingVariables.put(var.getName(), "T" + i + ".subject");
							}
							else
							{
								whereParts.add(mappingVariables.get(var.getName()) + " = " + "T" + i + ".obj");
								mappingVariables.put(var.getName(), "T" + i + ".obj");
							}
					}
				}
				//constant
				if(bodyAtom.getArgument(j) instanceof Constant){
					Constant cons = (Constant) bodyAtom.getArgument(j);
					if (bodyAtom.getArguments().length == 1)
						whereParts.add("T" + i + ".individual = '" + cons.getName().substring(0, cons.getFunctionalPrefix().length()) + "'");
					else
						if (j == 0)
							whereParts.add("T" + i + ".subject = '" + cons.getName().substring(0, cons.getName().length()) + "'");
						else
							whereParts.add("T" + i + ".obj = '" + cons.getName().substring(0, cons.getName().length()) + "'");
				}
			}
//		}

//		Atom headAtom = clause.getHead();

		for ( int j = 0; j < bodyAtom.getArguments().length ; j++) {
			if ( bodyAtom.getArgument(j) instanceof Variable ) {
				Variable var = (Variable) bodyAtom.getArgument(j);
				selectParts.add(mappingVariables.get(var.getName()));
			}
			//constant
//			if(bodyAtom.getArgument(j) instanceof Constant){
//				Constant con = (Constant) bodyAtom.getArgument(j);
//				selectParts.add("'" + con.getName() + "'");
//			}
		}

		result = "SELECT distinct ";
		for(int s=0; s<selectParts.size(); s++){
			if(s!=0)
				result += ", ";
			result += selectParts.get(s);
		}

		result += " FROM ";

		for(int f=0; f<fromParts.size(); f++){
			if(f!=0)
				result += ", ";
			result += fromParts.get(f);
		}

		if(whereParts.size() > 0){
			result += " WHERE ";

			for(int w=0; w<whereParts.size(); w++){
				if(w!=0)
					result += " AND ";
				result += whereParts.get(w);
			}
		}

		return result;
	}

	
	//	public boolean evaluateSQL(String[] connectionData, String query, JTextArea textArea) throws Exception{
//        Statement stmt = null;
//	    ResultSet rs = null;
//	    ResultSetMetaData rsmd;
//	    try {
//	    	long begin = System.currentTimeMillis();
//			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
//			DriverManager.registerDriver(new org.postgresql.Driver());
//
//	        conn = DriverManager.getConnection(connectionData[0]+connectionData[1]+"?user="+connectionData[2]+"&password="+connectionData[3]);
//	        conn.setAutoCommit(false);
//	        stmt = conn.createStatement();//java.sql.ResultSet.TYPE_FORWARD_ONLY,java.sql.ResultSet.CONCUR_READ_ONLY);
//	        stmt.setFetchSize(50);
//
//	        rs = stmt.executeQuery(query);
//
//	        rsmd = rs.getMetaData();
//	        int numCols = rsmd.getColumnCount();
//	        int numRows = 0;
//	        while (rs.next()){
//		         for (int i = 1; i <= numCols; i++){
//		        	 //System.out.print(rs.getString(i)+"\t");
//		        	 textArea.append(rs.getString(i)+"\t");
//		         }
//		         //System.out.println();
//		         textArea.append("\n");
//		         numRows++;
//	         }
//	        long end = System.currentTimeMillis();
//	        textArea.append("-------------------------------\n");
//	        if(numRows == 1){
//	        	textArea.append("1 row retrieved in " + Long.toString(end - begin) + " millisec.");
//	        }
//	        else{
//	        	textArea.append(numRows + " rows retrieved in " + Long.toString(end - begin) + " millisec.");
//	        }
//
//		}
//		finally {
//		        if (rs != null) {
//		            try {
//		                rs.close();
//		            } catch (SQLException sqlEx) { } // ignore
//		            rs = null;
//		        }
//		        if (stmt != null) {
//		            try {
//		                stmt.close();
//		            } catch (SQLException sqlEx) { } // ignore
//		            stmt = null;
//		        }
//		    }
//		return true;
//	}

	public boolean evaluateSQL(String[] connectionData, String query) throws Exception{
		return evaluateSQL(connectionData, query, true);
	}
	
	public boolean evaluateSQL(String[] connectionData, String query, boolean print) throws SQLException {
        Statement stmt = null;
	    ResultSet rs = null;
	    ResultSetMetaData rsmd;
//	    try {
//	    	if ( connStr.startsWith("jdbc:postgresql")) {
//				DriverManager.registerDriver(new org.postgresql.Driver());
//				conn = DriverManager.getConnection(connStr + "?user=postgres"+"&password=0000");
//	    	}
//			else if (connStr.startsWith("jdbc:mysql")) {
//				DriverManager.registerDriver(new com.mysql.jdbc.Driver());
//				conn = DriverManager.getConnection(connStr + "?user=root"+"&password=");
//			}
//			
//			System.out.println(query);
//
//	        
//	        conn.setAutoCommit(false);
	    System.out.println("\n\nConn: " + conn.getCatalog());
	        stmt = conn.createStatement();//java.sql.ResultSet.TYPE_FORWARD_ONLY,java.sql.ResultSet.CONCUR_READ_ONLY);
//	        stmt.setFetchSize(50);
	    	long begin = System.currentTimeMillis();

	        rs = stmt.executeQuery(query);

//	        rsmd = rs.getMetaData();

	    	long end = System.currentTimeMillis();

	        resSize = 0;
	    	while (rs.next())
	    		resSize++;
//	        if (print) {
	    	
	    	evalTime = (end - begin);
	        	System.out.println("numTuples = " + resSize);
		    	System.out.println("Eval Time = " + (end - begin) + "ms" );	        
//	        }
//		}
//		finally {
//		        if (rs != null) {
//		            try {
//		                rs.close();
//		            } catch (SQLException sqlEx) { } // ignore
//		            rs = null;
//		        }
//		        if (stmt != null) {
//		            try {
//		                stmt.close();
//		            } catch (SQLException sqlEx) { } // ignore
//		            stmt = null;
//		        }
//		        conn.close();
//		    }
		return true;
	}

	
	public Set<String> evaluateSQLReturnResults(String[] connectionData, String query, int flag) {
        Statement stmt = null;
	    ResultSet rs = null;
	    ResultSetMetaData rsmd;
	    Set<String> result = new HashSet<String>();
//	    try {
//	    	if ( connStr.startsWith("jdbc:postgresql")) {
//				DriverManager.registerDriver(new org.postgresql.Driver());
//				conn = DriverManager.getConnection(connStr + "?user=postgres"+"&password=0000");
//	    	}
//			else if (connStr.startsWith("jdbc:mysql")) {
//				DriverManager.registerDriver(new com.mysql.jdbc.Driver());
//				conn = DriverManager.getConnection(connStr + "?user=root"+"&password=");
//			}
//
//
//			conn.setAutoCommit(false);
    	long begin = System.currentTimeMillis();

    	try {
			stmt = conn.createStatement();
	        rs = stmt.executeQuery(query);

	        resSize = 0;
//	        System.out.println("TTT: " + rs.getMetaData().getColumnCount());
	        while (rs.next()) {
	        	if (rs.getMetaData().getColumnCount() == 1)
	        		result.add(rs.getString(1));
	        	else
	        		result.add(rs.getString(1)+"----IQAROS---"+rs.getString(2));
//	    	while (rs.next()) {
//	    		if (flag==0)
//	    			result.add(rs.getString(1));
//	    		else if (flag==1)
//	    			result.add(rs.getString(1)+"----"+rs.getString(2));
//	    		else if (flag==2)
//	    			result.add(rs.getString(2));
//	    		else if (flag==3)
//	    			result.add(rs.getString(2)+"----"+rs.getString(1));
	    		resSize++;
	    	}
	    	
	    	System.out.println("numTuples = " + resSize);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//java.sql.ResultSet.TYPE_FORWARD_ONLY,java.sql.ResultSet.CONCUR_READ_ONLY);
//	        stmt.setFetchSize(50);


//	        rsmd = rs.getMetaData();


	        
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//		        if (rs != null) {
//		            try {
//		                rs.close();
//		            } catch (SQLException sqlEx) { } // ignore
//		            rs = null;
//		        }
//		        if (stmt != null) {
//		            try {
//		                stmt.close();
//		            } catch (SQLException sqlEx) { } // ignore
//		            stmt = null;
//		        }
//		        conn.close();
//		    }
		return result;
	}
	
	public int evaluateSQLandReturnNumOfAnws(String[] connectionData, String query) throws Exception{
        Statement stmt = null;
	    ResultSet rs = null;
	    ResultSetMetaData rsmd;
	    int resSize = 0;
//	    try 
	    {

	    	long begin = System.currentTimeMillis();

	        stmt = conn.createStatement();//java.sql.ResultSet.TYPE_FORWARD_ONLY,java.sql.ResultSet.CONCUR_READ_ONLY);
//	        stmt.setFetchSize(50);
//	        System.out.println(query);
	        try {
	        	rs = stmt.executeQuery(query);
	        } catch(MySQLSyntaxErrorException e){
	        	if (!e.getMessage().contains("Table") || !e.getMessage().contains("doesn't exist")) {
	        		e.printStackTrace();
	        		System.out.println(query);
	        	}
	        	
	        	return 0;
	        }

	        rsmd = rs.getMetaData();

	        resSize = 0;
	    	while (rs.next())
	    		resSize++;

//	        System.out.println("numOfTuples = " + resSize);


		}
//		finally {
//		        if (rs != null) {
//		            try {
//		                rs.close();
//		            } catch (SQLException sqlEx) { } // ignore
//		            rs = null;
//		        }
//		        if (stmt != null) {
//		            try {
//		                stmt.close();
//		            } catch (SQLException sqlEx) { } // ignore
//		            stmt = null;
//		        }
//		        conn.close();
//		    }
		return resSize;
	}
	
	public String getSQLConceptConceptgstoil(Clause clause) {
		String result=null;
		result = "SELECT individual FROM " + clause.getBody()[0].getName() + " INTERSECT SELECT individual FROM " + clause.getBody()[1].getName(); 
		result=mappingManager.replaceFrom(result);
		return result;
	}
	
	public String getSQLConceptRolegstoil(Clause clause) {
		String result=null;
		if (clause.getBody()[0].getArguments().length==1)
			result = "SELECT individual FROM " + clause.getBody()[0].getName() + " INTERSECT SELECT subject, obj FROM " + clause.getBody()[1].getName(); 
		else
			result = "SELECT subject, obj FROM " + clause.getBody()[0].getName() + " INTERSECT SELECT individual FROM " + clause.getBody()[1].getName();
		
		result=mappingManager.replaceFrom(result);
		return result;
	}

	public String getSQLgstoil(Clause clause) {
		
		String result = "SELECT subject, obj FROM " + clause.getBody()[0].getName() + " INTERSECT SELECT subject, obj FROM " + clause.getBody()[1].getName(); 
		result=mappingManager.replaceFrom(result);
		return result;
	}
	
	public void seeForeignKeys() throws SQLException {
		System.out.println("Got Connection.");
//	    Statement st = conn.createStatement();
//	    st.executeUpdate("drop table survey;");
//	    st.executeUpdate("create table survey (id int,name varchar(30));");
//	    st.executeUpdate("insert into survey (id,name ) values (1,'nameValue')");

	    DatabaseMetaData meta = conn.getMetaData();
	    ResultSet res = meta.getTables(conn.getCatalog(), null, null, new String[] {"TABLE"});

	    ArrayList<String> tableNames = new ArrayList<String>();
	    
	    while (res.next() ) {
//	        System.out.println(
//	                "   "+res.getString("TABLE_CAT") 
//	               + ", "+res.getString("TABLE_SCHEM")
//	               + ", "+res.getString("TABLE_NAME")
//	               + ", "+res.getString("TABLE_TYPE")
//	               + ", "+res.getString("REMARKS")); 
	        tableNames.add(res.getString("TABLE_NAME"));
	    }
	    
	    for (int i = 0; i < tableNames.size(); i++ ) {
	    	 System.out.println("\n\n\n\ntable = " + tableNames.get(i));
	    	 ResultSet rs = null;
	    	 
	    	 /**
	    	  * Get primary key
	    	  */
	    	 rs = meta.getPrimaryKeys(conn.getCatalog(), null, tableNames.get(i));
	    	 while (rs.next()) {
	    		 String columnName = rs.getString("COLUMN_NAME");
	    		 System.out.println("Primary Key: " + columnName);
	    	 }
	    	 
	    	 /**
	    	  * Get foreign keys
	    	  */
	    	 rs = meta.getExportedKeys(conn.getCatalog(), null, tableNames.get(i));
		     while (rs.next()) {
		       String fkTableName = rs.getString("FKTABLE_NAME");
		       String fkColumnName = rs.getString("FKCOLUMN_NAME");
		       int fkSequence = rs.getInt("KEY_SEQ");
		       System.out.println("getExportedKeys(): fkTableName="+fkTableName);
		       System.out.println("getExportedKeys(): fkColumnName="+fkColumnName);
		       System.out.println("getExportedKeys(): fkSequence="+fkSequence);
		     }
	    }
	}
}
