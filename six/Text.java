/* Text.java 
 * Created on Dec 1, 2006 
 * by ilteris
 */
package six;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;

public class Text {

	PApplet	p;


	int		prevArr[][]		= new int[5][];
	int		curArray[][]	= new int[5][];


	
	
	
	
	Text(PApplet p_) {
		
		p = p_;
	}

	public void parsePHP(String url) {
		 prevArr = curArray;
		String lines[]		= p.loadStrings(url);
		// load the url and put line by line in an array
		
		for (int i = 0; i < lines.length; i++) {
			// traverse through the line array 
			 /*
			  * php structure
			 totalnumber [0]
			 totalA + totalB [1]
			 Total 6 List [2]
			 TotalA 6 List [3]
			 TotalB 6 List [4]
			  */
			
			String strs[] = lines[i].split(" ");
		//	String strs[]	= PApplet.split(lines[i], PConstants.WHITESPACE);
			//splits uses space as a default delimeter and we put them
			// generic strs[] array for one
			// to its own array. 
			curArray[i]		= new int[strs.length];
			// we are declaring  length of  arrays.
			for (int j = 0; j < curArray[i].length; j++) 
			{
				
				
				 // get the number and convert it into string.
				 curArray[i][j] = Integer.parseInt(strs[j]);
					//PApplet.println(curArray[i][j]); 
	
			}
		//	 p.println(prevArr[i].length);
			 
		}
		
    
		
		
		 
		 
	}

	/*
	 * String[] strs = line.split(...); int[] ints = new int[strs.length]; for
	 * each entry in strs ints[i] = make an int from strs[i] // and finally
	 * curArray[i] = ints
	 * 
	 */

	
public void getDifference(int diff) {
	
	// this gets the difference in the arrays.
	// new value 28, old value 26. it returns 2
	// and the id of the value index. 
	// for example this is the difference in the loved ones.

	
	for(int i = 0; i < curArray[diff].length; i++) {
		p.println("p.println(prevArr[diff][i]): " + prevArr[diff][i]);
		p.println("p.println(curArr[diff][i]): " + curArray[diff][i]);
		if(prevArr[diff][i] != curArray[diff][i]) {
			p.println(prevArr[diff][i]);
			
		} else {
			p.println("none");
		}
	}
	
	
}

		
	
public int getTotal() {
		
		return curArray[0][0];
	
	}
	
public int[] getOverall(int i){
		
		return curArray[i];
	}

public int getAreaOneTotal(){
	
	return curArray[1][0];
}

public int getAreaTwoTotal(){
	
	return curArray[1][1];
}
	
	
	public int getAreaOne(int title, int i){
		
		return curArray[title][i];
	}

	/*
	(for n=0; n < size; ++n) { 
	if (array[n] > nGreatest) { 
		nGreatest = array[n]; 
		} }

*/
	public int getMostIntense(int i) {
		int nGreatest = 0;
		int indexer	  = 0;
		for (int k=0; k < curArray[i].length; ++k) {
			int cur = curArray[i][k];
			if(cur>nGreatest) {
				nGreatest = cur;
				indexer   = k;
			}
			
		}
		return indexer;
	}
	
	public int getIntenseItself(int i) {
		int nGreatest = 0;
		int indexer	  = 0;
		for (int k=0; k < curArray[i].length; ++k) {
			int cur = curArray[i][k];
			if(cur>nGreatest) {
				nGreatest = cur;
				
			}
			
		}
		return nGreatest;
	}

}
