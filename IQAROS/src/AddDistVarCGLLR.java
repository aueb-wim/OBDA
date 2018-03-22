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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;
import org.oxford.comlab.perfectref.rewriter.PI;
import org.oxford.comlab.perfectref.rewriter.Saturator;
import org.oxford.comlab.perfectref.rewriter.TermFactory;

import edu.ntua.image.alternateDistinguishedVariables.AddDistinguishedVariablesCGLLR;
import edu.ntua.image.datastructures.Tree;
import edu.ntua.image.datastructures.TreeNode;
import edu.ntua.image.redundancy.SimpleRedundancyEliminator;

public class AddDistVarCGLLR {

	private static TermFactory m_termFactory = new TermFactory();

	private static final DLliteParser parser = new DLliteParser();

	private int orderedQueryIndex=0;

	public static void main(String[] args) throws Exception{

		String ontologyFile;
		String queryFile;
		
		String path = System.getProperty("user.dir")+ "/dataset/Evaluation_ISWC'09AddDistVar/";

		queryFile = System.getProperty("user.dir")+ "/dataset/Tests/queries.txt";
		ontologyFile = "file:" + path + "Ontologies/V.owl";

		ArrayList<PI> tBoxAxioms = parser.getAxioms(ontologyFile);

		Clause originalQuery = parser.getQuery(queryFile);
		System.out.println("Original Query = " + originalQuery);
		
		ArrayList<Clause> res = new AddDistinguishedVariablesCGLLR().computeAddDistVar(tBoxAxioms, originalQuery, 1);
		
	}
	
	
	public static void printTree( Tree<Clause> tree) {

		System.out.println("START");
//		Queue<Node<T>> queue = new LinkedList<Node<T>>();
		Set<TreeNode<Clause>> queue = new HashSet<TreeNode<Clause>>();
		queue.add( tree.getRootElement() );
		Set<TreeNode<Clause>> alreadyAddedToQueue = new HashSet<TreeNode<Clause>>();
		Set<TreeNode<Clause>> alreadyPrinted = new HashSet<TreeNode<Clause>>();
		alreadyAddedToQueue.add( tree.getRootElement() );

		while ( !queue.isEmpty() ) {
//			Node<T> cn = queue.poll();
			TreeNode<Clause> cn = queue.iterator().next();
			queue.remove(cn);
			if ( alreadyPrinted.contains(cn) )
				continue;
//			if ( !cn.getNodeLabel().containsAUXPredicates() )
				System.out.println( cn.getNodeLabel() + "\t\t" + cn.getNodeLabel().getUnifications() );
			alreadyPrinted.add( cn );
			Set<TreeNode<Clause>> cnChl = cn.getChildren();
			for ( TreeNode<Clause> nc : cnChl )
			{
//				if ( !nc.getNodeLabel().containsAUXPredicates() )
					System.out.println( "\t\t\t\t" + nc.getNodeLabel() + "\t\t" + nc.getNodeLabel().getUnifications() );
				if ( !alreadyAddedToQueue.contains( nc ))
				{
					queue.add( nc );
					alreadyAddedToQueue.add( nc );
				}
			}
		}
		System.out.println("END Printing tree \n");
	}

	
	private static ArrayList<Clause> differentClauses (ArrayList<Clause> more , ArrayList<Clause> less ) {
		ArrayList<Clause> result = new ArrayList<Clause>();
		
		for ( Clause c1 : more ) {
			boolean foundc1 = false;
			for ( Clause c2 : less ) {
				Clause tempc2 = new Clause( c2.getBody(), c1.getHead() );
				if ( tempc2.isEquivalentUpToVariableRenaming(c1) ) {
					foundc1 = true;
					break;
				}
			}
			if ( !foundc1 )
				result.add( c1 );
		}
		return result;
	}
	
}