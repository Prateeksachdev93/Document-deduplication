package MinHashing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

public class MinHashWindowedShinglesMultiDoc<T> {
	
	public final static String PATH = "/Users/prsachde/Deduplication-using-minhashing/Deduplication/resource/";
	public final static File FolderName = new File(PATH);
	private final int NumOfHashes;
	private final int HashValues[][];
	private final int MinHashingMatrix[][];
	private Map<T, boolean[]> Characteristic_Matrix = new HashMap<T,boolean[]>();

	private final int INF = 9999999;
	private int NumSets;
	

	public MinHashWindowedShinglesMultiDoc(List<Set<T>> Sets)
	{
		this.NumOfHashes = 1000;
		NumSets = Sets.size();	
		MinHashingMatrix = new int[NumSets][NumOfHashes];
		HashValues = assignHashValues(NumOfHashes,Sets);


	}
	private void updateCharateristicMatrix(Set<T> s1, int setindex) {
		
		for(T shingle: s1)
		{
			
			if(Characteristic_Matrix.containsKey(shingle))
			{
				boolean[] temp = new boolean[NumSets];
				temp = Characteristic_Matrix.get(shingle);
				temp[setindex] = true;
				
				Characteristic_Matrix.put(shingle, temp);
			}
			else
			{
				boolean[] Contains = new boolean[NumSets];
				setfalse(Contains);				
				Contains[setindex] = true;
				Characteristic_Matrix.put(shingle, Contains);
			}
		}
		
		
		
	}
	private void initSets(List<Set<T>> Sets)
	{
		for(int i=0; i < NumSets ;i++)
		{
			
			updateCharateristicMatrix(Sets.get(i), i);
		}
		
			
	}
	private void setfalse(boolean[] contains) {
		for(int i=0;i<NumSets;i++)
		{
			contains[i] = false;
		}
	}

	private int[][] assignHashValues(int NumHashes,List<Set<T>> Sets)
	{

		Set<T> Union = new HashSet<T>(Sets.get(0));
		for(int i=0;i<NumSets;i++)
		{
			Union.addAll(Sets.get(i));//union of all sets to create hashvalues matrix
		}
		
		int Union_size = Union.size();
		List<Integer> permlist = new ArrayList<Integer>();// list to be permuted to get random ordering
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
				Hashvalues[i][j] = permlist.get(i);
			}
			
		}

		return Hashvalues;
	}

	private void create_minhashing_matrix(Set<T> CurrentSet,int SetIndex)
	{
		
		for(int i=0;i<NumOfHashes;i++)
		{
			MinHashingMatrix[SetIndex][i] = INF;//initial value s
		}

		int shingleindex = 0;
		for(T shingle:Characteristic_Matrix.keySet() )
		{
			for(int i=0;i<NumOfHashes;i++)
			{

				if(CurrentSet.contains(shingle))
				{
					MinHashingMatrix[SetIndex][i] = Math.min(MinHashingMatrix[SetIndex][i], HashValues[shingleindex][i]);//position of shingle in ith hashing
				}
			}
			shingleindex++;

		}


		
	}
	private void initMinhashingMatrix(List<Set<T>> Sets)
	{
		for(int i=0;i<NumSets;i++)
		{
			create_minhashing_matrix(Sets.get(i),i);
		}
	}
	private double findJaccardValue(int SetIndex1, int SetIndex2)
	{
		double JaccardCoeff;
		int intersectionSize = 0;
		int unionSize = NumOfHashes;

		for(int i=0;i<NumOfHashes;i++)
		{
		
			if(MinHashingMatrix[SetIndex1][i]==MinHashingMatrix[SetIndex2][i])
			{
				intersectionSize++;
			}

		}
		JaccardCoeff = (intersectionSize*1.0)/unionSize;

		return JaccardCoeff;

	}

	
	public static Set<String> Create_Shingles_optimized(String s, int shingle_length)
	{
		Set<String> shingle_set = new HashSet<String>();
		String shingle;
		shingle = s.substring(0, shingle_length);
		shingle_set.add(shingle);
		String tempstring;
		
		for(int i=shingle_length;i<s.length();i++)
		{
			tempstring = shingle.substring(1);
			tempstring += s.charAt(i);
			shingle = tempstring;
			shingle_set.add(shingle);
					
		}
		return shingle_set;
			
		
	}
	private void getSimilarity(List<Set<T>> Sets)
	{
		int len = NumSets;
		initSets(Sets);
		initMinhashingMatrix(Sets);
		for(int i=0;i<len;i++)
		{
			for(int j=i+1;j<len;j++)
			{
				System.out.println("Document " + i + " and " + j + " are " + findJaccardValue(i,j)*100 + "% similar" );
			}
		}
	}
	public static void main(String[] args) {

		int shingle_length = 5;
		List<Set<String>> CompleteSet= new ArrayList<Set<String>>();
		
		ReadFiles obj = new ReadFiles();
		obj.listFilesForFolder(FolderName);
		
		List ListOfFiles = new ArrayList(obj.Files);
		int NumFiles = ListOfFiles.size();
		String[] FileStrings = new String[NumFiles];
		for(int i=0;i<NumFiles;i++)
		{
			try {
				System.out.println("Reading file : " + PATH + ListOfFiles.get(i));
				FileStrings[i] = new Scanner(new File(PATH + ListOfFiles.get(i))).useDelimiter("\\Z").next();//reading the complete file as string
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		for(int i=0;i<NumFiles;i++)
		{
			CompleteSet.add(Create_Shingles_optimized(FileStrings[i], shingle_length));
		}
		MinHashWindowedShinglesMultiDoc<String> res = new MinHashWindowedShinglesMultiDoc<String>(CompleteSet);
		res.getSimilarity(CompleteSet);
		
		

	}





}