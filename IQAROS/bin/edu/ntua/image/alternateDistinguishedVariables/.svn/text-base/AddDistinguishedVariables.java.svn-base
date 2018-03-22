/*
 * #%L
 * IQAROS
 *
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 by the Image, Video and Multimedia Laboratory, NTUA
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package edu.ntua.image.alternateDistinguishedVariables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;
import org.oxford.comlab.perfectref.rewriter.Substitution;
import org.oxford.comlab.perfectref.rewriter.Term;
import org.oxford.comlab.perfectref.rewriter.TermFactory;
import org.oxford.comlab.perfectref.rewriter.Variable;

import edu.ntua.image.incremental.Incremental;
import edu.ntua.image.redundancy.SimpleRedundancyEliminator;


public class AddDistinguishedVariables {

	private static TermFactory m_termFactory = new TermFactory();
	public boolean substUsed = false;
	private ArrayList<Variable> varsNotContainedInClause = new ArrayList<Variable>();
	private Incremental incremental = new Incremental();
	private ArrayList<PI> tBoxAxioms;
	private Clause originalQuery;
	
	public ArrayList<Clause> computeAddDistVar( ArrayList<PI> tBoxAxioms, Clause originalQuery, int variable) {

		SimpleRedundancyEliminator redundancyEliminator = new SimpleRedundancyEliminator();
		
		/**
		 * Inc_1
		 * 
		 * Attention!!! Inc must not run subsumption checking!!!
		 */
		Incremental incremental = new Incremental();
		ArrayList<Clause> result = incremental.computeUCQRewriting(tBoxAxioms,originalQuery);
		HashMap<String, Substitution> resultSubMap = incremental.getsubstitutionsMap();
		
//		Configuration con = new Configuration();
//		con.redundancyElimination=RedundancyEliminationStrategy.Full_Subsumption;
//		Incremental incremental = new Incremental( con );
//		ArrayList<Clause> result = incremental.computeUCQRewriting(tBoxAxioms,originalQuery);
		
//		result = redundancyEliminator.removeRedundantClauses(result);
		
		long t1 = System.currentTimeMillis();

//		AddDistinguishedVariables addDVar = new AddDistinguishedVariables();
		ArrayList<Clause> res = addDistVariables(result, resultSubMap, Collections.singleton( m_termFactory.getVariable( variable ) ) , tBoxAxioms , originalQuery);
		
//		System.out.println( "Size of UCQ before subsumption = " + res.size() + " computed in " + (System.currentTimeMillis() - t1) + "ms" );
		
		long t = System.currentTimeMillis();
		res = redundancyEliminator.removeRedundantClauses(res);
		System.out.println( "Final  = " + res.size() + " Sub = " + (System.currentTimeMillis() - t) +"ms");
		
		System.out.println( "TOTAL = " + (System.currentTimeMillis() - t1) +"ms");
		
		return res;
		
	}


	public ArrayList<Clause> addDistVariables( ArrayList<Clause> u, HashMap<String,Substitution> subMap , Set<Variable> dv, ArrayList<PI> tBox, Clause originalClause) {

		long t1 = System.currentTimeMillis();
		if ( dv.isEmpty() ) {
			System.out.println( "\nWith Distinguished variable extension returns u.\ntook TOTAL " + (System.currentTimeMillis() - t1) +"ms\n\n");
			return u;
		}

		tBoxAxioms = tBox;
		originalQuery = originalClause;

		ArrayList<Clause> result = new ArrayList<Clause>();
		
		//copy all clauses from u to result
		for ( Clause c : u )
			result.add( c );

		for ( Variable v : dv ) {
			result = addDistVariable(result, subMap, v);
			if ( result.isEmpty() ) {
				System.err.println( "Variable " + v + " does not appear in any clause " );
				return new ArrayList<Clause>();
			}
		}

		System.out.println( "\nWith Distinguished variable extension  = " + result.size() + " took TOTAL " + (System.currentTimeMillis() - t1) +"ms");

		return result;
	}

	private ArrayList<Clause> addDistVariable ( ArrayList<Clause> u, HashMap<String,Substitution> subMap , Variable v) {

		ArrayList<Clause> result = new ArrayList<Clause>();

		for ( Clause c : u  ) {
			//To c periexei oles tis metavlites tou dv opote to prosthetoume sto result
			if ( containsVar(c, subMap.get( c.m_canonicalRepresentation ), v) ) {
				//Periexei tin v
//				if ( !substUsed )
					result.add( new Clause( c.getBody() , m_termFactory.getFunctionalTerm( c.getHead().getName() , newDistVars(c, v) ) ) );
				/*
				 * does not contain v but was coppied in inc_1 so it has to be extended
				 * We know that it has been extended if it contains a substitution
				 */
//				else {
//					result.addAll( joinClauseWithRewOfAtomsContainingVar( c , rewOfAtomsThatContainVar( originalQuery , v ) , v) );
//				}
			}
		}

		return result;
	}

	private ArrayList<Clause> joinClauseWithRewOfAtomsContainingVar( Clause c, ArrayList<Clause> clausesOfAtomsThatContainVar, Variable v) {

		ArrayList<Clause> result = new ArrayList<Clause>();

		for ( Clause clauseOfAt : clausesOfAtomsThatContainVar ) {
			Term[] cBody = c.getBody();
			int cBodyLength = cBody.length;
			Term[] clauseOfAtBody = clauseOfAt.getBody();
			int clauseOfAtBodyLength = clauseOfAtBody.length;
			
			Term [] newBody = new Term[ cBodyLength + clauseOfAtBodyLength ];
			
			for ( int i = 0 ; i < cBodyLength ; i++ )
				newBody[i] = cBody[i];
			for ( int j = 0 ; j < clauseOfAtBodyLength ; j++ )
				newBody[cBodyLength + j] = clauseOfAtBody[j];
			
			result.add( new Clause( newBody , m_termFactory.getFunctionalTerm( c.getHead().getName() , newDistVars(c, v) ) ) );
		}

		return result;
	}

	private ArrayList<Clause> rewOfAtomsThatContainVar( Clause originalQuery , Variable v ) {

		return incremental.computeUCQRewriting(tBoxAxioms, findAtomsWithVarInOriginalQuery( v ) );
	}

	private Clause findAtomsWithVarInOriginalQuery( Variable v ) {
		
		ArrayList<Term> newBodyList = new ArrayList<Term>();
		
		for (Term t : originalQuery.getBody() )
			if ( t.getArgument( 0 ).contains( v ) || 
					t.getArity() == 2 && t.getArgument( 1 ).contains( v ) )
				newBodyList.add( t );
		
		Set<Variable> newHeadVars = new HashSet<Variable>();
		for ( Term t : newBodyList ) {
			Term[] tArgs = t.getArguments();
			for ( Term arg : tArgs )
				newHeadVars.add( (Variable) arg );
		}
			
		Term newHead = m_termFactory.getFunctionalTerm(originalQuery.getHead().getName(), newHeadVars.toArray( new Term[0] ) );

		return new Clause( newBodyList.toArray(new Term[0]) , newHead);
	}

	private Term[] newDistVars (Clause c , Variable v) {

		int headArity = c.getHead().getArity();
		ArrayList<Variable> cDist = new ArrayList<Variable>();

		for ( int i = 0 ; i < headArity ; i++ )
			cDist.add( (Variable) c.getHead().getArgument( i ) );

//		for ( Variable v : dv )
		cDist.add(v);

		return cDist.toArray( new Term[0] );
	}

	private boolean containsVar(Clause clause, Substitution sub, Variable newDistingVar ) {

		ArrayList<Variable> clauseVariables = clause.getVariables();

		if ( clauseVariables.contains( newDistingVar ) || findMappedVariableInClause( clause , sub , newDistingVar ) )
			return true;
		else
			return false;
	}

	private boolean findMappedVariableInClause( Clause c, Substitution s, Term var ) {

		Term currentVar = var;
		Set<Term> checkedVars = new HashSet<Term>();
		ArrayList<Variable> clauseVariables = c.getVariables();

		if ( clauseVariables.contains( currentVar ) )
			return true;

		if ( s == null )
			return false;

		while ( !checkedVars.contains( currentVar ) && s.get( currentVar ) != null) {
			Term tempTerm = s.get( currentVar );
			if ( tempTerm != null )
				if ( clauseVariables.contains( tempTerm ) )
					return true;
				else {
					checkedVars.add( currentVar );
					currentVar = tempTerm;
				}
		}
		return false;
	}

	private Substitution settoSubst( Set<Substitution> subs ) {
		Substitution s = new Substitution();

		for ( Substitution ss : subs ) {
			for ( Entry<Variable, Term> se : ss.entrySet() ) {
				s.put( se.getKey(), se.getValue() );
			}
		}
		return s;
	}

	/**
	 * Creates a new query by applying the mgu of two atoms to a given query
	 * @param mgu
	 * @param clause
	 * @return
	 */
	public Clause getNewQuery(Substitution mgu, Clause clause){

		if ( mgu == null )
			return clause;

		Set<Term> newBody = new LinkedHashSet<Term>();
        //Copy the atoms from the main premise
        for (int index=0; index < clause.getBody().length; index++)
			newBody.add(clause.getBody()[index].apply(mgu, m_termFactory));

        //New body and head
        Term[] body = new Term[newBody.size()];
        newBody.toArray(body);
        Term head = clause.getHead().apply(mgu, m_termFactory);

        return new Clause(body, head);
	}
}
