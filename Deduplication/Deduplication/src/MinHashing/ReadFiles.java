package MinHashing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadFiles {// reads file recursively

	
	public List<String> Files = new ArrayList<String>();
	public void listFilesForFolder(final File folder) {
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
//	            System.out.println(fileEntry.getName());
	            this.Files.add(fileEntry.getName());
	        }
	    }
	}

	
	/*public static void main(String[] args) {
		final File folder = new File("/Users/prsachde/Deduplication-using-minhashing/Deduplication/resource");
		listFilesForFolder(folder);	
	}*/
}
