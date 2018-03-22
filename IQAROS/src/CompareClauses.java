import java.util.ArrayList;

import org.oxford.comlab.perfectref.parser.DLliteParser;
import org.oxford.comlab.perfectref.rewriter.Clause;

public class CompareClauses {
	
	private static final DLliteParser parser = new DLliteParser();

	public static void main(String[] args) throws Exception {
		
		String path = System.getProperty("user.dir");
		
		String path1 = path + "/cgllr+.txt";
		String path2 = path + "/inc1.txt";
		
		ArrayList<Clause> clauses1 = parser.getQueryArrayList(path1);
		ArrayList<Clause> clauses2 = parser.getQueryArrayList(path2);
		
//		System.out.println( clauses1.size() );
//		System.out.println( clauses2.size() );
	
		compareContents(clauses1, clauses2);
//		System.out.println( compareClauses(clauses1, clauses2).size() );
		
//		for ( Clause c : compareClauses(clauses1, clauses2) )
//			System.out.println( c );
	}
	
	public static ArrayList<Clause> compareClauses( ArrayList<Clause> c1 , ArrayList<Clause> c2 ) {
		ArrayList<Clause> result = new ArrayList<Clause>();
		
		for ( Clause cf1 : c1 ) {
			boolean found = false;
			for ( Clause cf2 : c2 ) {
				if ( cf2.isEquivalentUpToVariableRenaming(cf1) )
//				if ( cf2.m_canonicalRepresentation.toString().equals(cf1.m_canonicalRepresentation.toString()) )
					found = true;
			}
			if ( !found )
				result.add(cf1);
		}
		return result;
	}
	
	private static void compareContents( ArrayList<Clause> clauses1 , ArrayList<Clause> clauses2 ) {

		ArrayList<Clause> moreInCl1 = new ArrayList<Clause>();
		ArrayList<Clause> moreInCl2 = new ArrayList<Clause>();

		for ( Clause cl1 : clauses1 ) {
			boolean found = false;
			for ( Clause cl2 : clauses2 )
				if ( cl1.isEquivalentUpToVariableRenaming(cl2) )
					found = true;
			if ( !found )
				moreInCl1.add(cl1);
		}

		for ( Clause cl2 : clauses2 ) {
			boolean found = false;
			for ( Clause cl1 : clauses1 )
				if ( cl1.isEquivalentUpToVariableRenaming(cl2) )
					found = true;
			if ( !found )
				moreInCl2.add(cl2);
		}

		System.out.println("\n\nMoreClauses in 1 - " + moreInCl1.size());
		for ( Clause more1 : moreInCl1 )
			System.out.println( more1 );
		System.out.println("\n\n");


		System.out.println("\n\nMoreClauses in 2 - " + moreInCl2.size());
		for ( Clause more1 : moreInCl2 )
			System.out.println( more1 );
		System.out.println("\n\n");

	}

}
