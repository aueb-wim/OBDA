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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;
import org.oxford.comlab.perfectref.rewriter.Saturator;
import org.oxford.comlab.perfectref.rewriter.Substitution;
import org.oxford.comlab.perfectref.rewriter.Term;
import org.oxford.comlab.perfectref.rewriter.TermFactory;
import org.oxford.comlab.perfectref.rewriter.Variable;

import edu.ntua.image.redundancy.SimpleRedundancyEliminator;


public class AddDistinguishedVariablesCGLLR {

	private static TermFactory m_termFactory = new TermFactory();
	public boolean substUsed = false;
	
	
	public ArrayList<Clause> computeAddDistVar(ArrayList<PI> tBoxAxioms, Clause originalQuery, int variable) {
		
		SimpleRedundancyEliminator redundancyEliminator = new SimpleRedundancyEliminator();

//		/**
//		 * CGLLR
//		 */
		Saturator cgllrRewriter = new Saturator(m_termFactory);
		ArrayList<Clause> result = cgllrRewriter.saturate(tBoxAxioms, originalQuery);
//		result = cgllrRewriter.saturate(tBoxAxioms, originalQuery);

//		int j = 1;
//		for ( Clause c : result )
//			System.out.println(j++ + ": " + c);
		
		System.out.println("ADDING VAR");

		long t1 = System.currentTimeMillis();
		
		ArrayList<Clause> res = addDistVariable(result, Collections.singleton( m_termFactory.getVariable( variable ) ) );
		
//		System.out.println( "Size of UCQ before subsumption = " + res.size() + " computed in " + (System.currentTimeMillis() - t1) + "ms" );
		
		long t = System.currentTimeMillis();
		res = redundancyEliminator.removeRedundantClauses(res);
		System.out.println( "Final  = " + res.size() + " Sub = " + (System.currentTimeMillis() - t) +"ms");
		
		System.out.println( "TOTAL = " + (System.currentTimeMillis() - t1) +"ms");

//		int i = 1;
//		for ( Clause c : res ) 
//			System.out.println( i++ + " : " + c );
//			System.out.println( i++ + " : " + c + "\t\t" + c.getUnifications() );
		
		return res;
	}

	public ArrayList<Clause> addDistVariable( ArrayList<Clause> u, Set<Variable> dv) {

		long t1 = System.currentTimeMillis();

		if ( dv.isEmpty() ) {
			System.out.println( "\nWith Distinguished variable extension returns u.\ntook TOTAL " + (System.currentTimeMillis() - t1) +"ms\n\n");
			return u;
		}
		
		ArrayList<Clause> result = new ArrayList<Clause>();

		for ( Clause c : u  ) {
			if ( containsVar(c, dv) ) {
				result.add( new Clause( c.getBody() , m_termFactory.getFunctionalTerm( c.getHead().getName() , newDistVars(c, dv) ) ) );
			}
		}
		System.out.println( "\nWith Distinguished variable extension  = " + result.size() + " took TOTAL " + (System.currentTimeMillis() - t1) +"ms");
		return result;
	}
	
	private Term[] newDistVars (Clause c , Set<Variable> dv) {
		
		int headArity = c.getHead().getArity();
		ArrayList<Variable> cDist = new ArrayList<Variable>();
		
		for ( int i = 0 ; i < headArity ; i++ )
			cDist.add( (Variable) c.getHead().getArgument( i ) );
		
		for ( Variable v : dv )
			cDist.add(v);
		
		return cDist.toArray( new Term[0] );
	}

	private boolean containsVar(Clause clause, Set<Variable> newDistingVar ) {

		ArrayList<Variable> clauseVariables = clause.getVariables();
		
		if ( clauseVariables.containsAll( newDistingVar ) )
			return true;
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
