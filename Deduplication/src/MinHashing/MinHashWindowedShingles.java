package MinHashing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MinHashWindowedShingles<T> {

	private final int NumOfHashes;
	private final int HashValues[][];
	private Map<T, boolean[]> Characteristic_Matrix;

	private final int INF = 9999999;
	private final Set<T> Set1;
	private final Set<T> Set2;

	public MinHashWindowedShingles(Set<T> S1, Set<T> S2)
	{
		this.NumOfHashes = 1000;
		this.Set1 = S1;
		this.Set2 = S2;

		Characteristic_Matrix = createCharateristicMatrix(S1,S2);		
		HashValues = assignHashValues(NumOfHashes,S1,S2);


	}
	private Map<T, boolean[]> createCharateristicMatrix(Set<T> s1, Set<T> s2) {

		Map<T,boolean[]> Char_matrix = new HashMap<T,boolean[]>();
		for(T shingle: s1)// assign true to shingles in set s1 and default false assuming this shingle is not present in set s2
		{
			Char_matrix.put(shingle, new boolean[] {true,false} );
		}
		for(T shingle: s2)
		{
			if(s1.contains(shingle))
			{
				Char_matrix.put(shingle, new boolean[] {true,true} );
			}
			else
			{
				Char_matrix.put(shingle, new boolean[] {false,true} );
			}
		}
		return Char_matrix;
	}
	private int[][] assignHashValues(int NumHashes,Set<T> S1,Set<T> S2)
	{

		Set<T> Union = new HashSet<T>(S1);
		Union.addAll(S2);//union of both sets to create hashvalues matrix
		int Union_size = Union.size();
		List<Integer> permlist = new ArrayList<Integer>();
		for(int i=0;i<Union_size;i++)
		{
			permlist.add(i);
		}

		
		int Hashvalues[][] = new int[Union_size][NumHashes];//[set union size][number of hashes]
		for(int j=0;j<NumHashes;j++)
		{
			Collections.shuffle(permlist);
			for(int i=0;i<Union_size;i++)
			{
//				System.out.println("index = " + permlist.get(i));
				Hashvalues[i][j] = permlist.get(i);
			}
			
		}

		return Hashvalues;
	}

	private int[] create_minhashing_matrix(Set<T> CurrentSet)
	{
//		System.out.println("---------------------------");
		int Minhashing_matrix[] = new int[NumOfHashes];
		for(int i=0;i<NumOfHashes;i++)
		{
			Minhashing_matrix[i] = INF;//initial value s
		}

		int shingleindex = 0;
		for(T shingle:Characteristic_Matrix.keySet() )
		{
//			System.out.println("__DEBUG: shingle =  " + shingle);
			for(int i=0;i<NumOfHashes;i++)
			{

				if(CurrentSet.contains(shingle))
				{
//					System.out.println("__DEBUG : " + HashValues[shingleindex][i]);
					Minhashing_matrix[i] = Math.min(Minhashing_matrix[i], HashValues[shingleindex][i]);//position of shingle in ith hashing
				}
			}
			shingleindex++;

		}


		return Minhashing_matrix;
	}
	private double findJaccardValue()
	{
		double JaccardCoeff;
		int intersectionSize = 0;
		int unionSize = NumOfHashes;
		int minHashingMatrix1[] = create_minhashing_matrix(Set1);
		int minhashingMatrix2[] = create_minhashing_matrix(Set2);

		for(int i=0;i<NumOfHashes;i++)
		{
//			System.out.println("__DEBUG " + minHashingMatrix1[i] +  " "  + minhashingMatrix2[i]);
		
			if(minHashingMatrix1[i]==minhashingMatrix2[i])
			{
				intersectionSize++;
			}

		}
		JaccardCoeff = (intersectionSize*1.0)/unionSize;

		return JaccardCoeff;

	}
	public static Set<String> Create_Shingles(String s,int shingle_length)
	{
		Set<String> shingle_set = new HashSet<String>();
		String shingle;
		for(int i=0;i<s.length() - shingle_length + 1 ; i++)
		{
//			System.out.println(s.substring(i, i+shingle_length));
			shingle = s.substring(i, i+shingle_length);
			shingle_set.add(shingle);
		}
		return shingle_set;
	}
	public static void main(String[] args) {

		
		String s1 = "this is a new file";
		String s2 = "this is a nre file";
		
		int shingle_length = 5;
		
		
		Set setA = Create_Shingles(s1, shingle_length);
		Set setB = Create_Shingles(s2, shingle_length);
	
		MinHashWindowedShingles<String> obj = new MinHashWindowedShingles<String>(setA, setB);
		System.out.println("Similarity = " + obj.findJaccardValue());
		
		

	}





}