
/*****************
 * WordCount
 * Author: Christian Duncan
 * Modified by: Joey Germain, Megan Forster, Alexandra Martin, and Mike Murphy
 *
 * Reads in a file of text and counts how many unique words are
 * in that text.
 *****************/
import java.util.Scanner;
import java.util.TreeSet;
import java.io.IOException;
import java.io.FileReader;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.io.File;
import java.nio.file.*;

public class WordCount {
	public static ArrayList<String> files = new ArrayList<String>();
	public static boolean debugMode = false;
    
	// The main entry point
	public static void main(String[] args) {
        
        /* We need to read an input file and create an output file.
         * Currently the relative paths that work for me don't work
         * for my teammates so I'm leaving this flag here for them.
         * If you are running this through the command line, then 
         * set eclipse to false. If you are running though eclipse,
         * set it to true. Not sure why this is needed.
         */
        boolean eclipse = false;
        
        String sep = System.getProperty("file.separator"); // "/" or "\"
        String testDir = eclipse ? "tests" + sep : ".." + sep + "tests" + sep;      // "tests/" or "../tests/"
        String outputDir = eclipse ? "results" + sep : ".." + sep + "results" + sep;// "results/" or "../results/"
        
        // Populate file list with all files in tests directory
        try {
            File test = new File(testDir);
            for (String f : test.list()) {
                files.add(testDir + f);
            }
        } catch (NullPointerException e) {
            System.err.println("Error: test files not found");
            System.exit(1);
        }
    
        // Use current time to create a unique filename
        long unixTime = System.currentTimeMillis() / 1000L;
        
        // Create output file
        File outputFile = new File(outputDir + "test " + unixTime + ".csv");
        
        //Try to write the file to disk
        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            System.err.println("Error creating output file");
            System.exit(2);
        }
        
        // Write headers to output file
        writeToFile(outputFile, "Filename, Array, Sorting, Tree, Hash \n");
        
		for (String s : files) {
			System.out.println("Text: " + s);
			try {
				ArrayList<String> list = readWords(s);
				long start = System.nanoTime();
				int total = countUniqueWords(list);
				long stop = System.nanoTime();
				long time1 = stop-start;
				System.out.println("countUniqueWords took: " + (stop - start) + " nanoseconds");

				start = System.nanoTime();
				total = countUniqueWordsSorting(list);
				stop = System.nanoTime();
				long time2 = stop-start;
				System.out.println("countUniqueWordsSorting took: " + (stop - start) + " nanoseconds");

				start = System.nanoTime();
				total = countUniqueWordsTree(list);
				stop = System.nanoTime();
				long time3 = stop-start;
				System.out.println("countUniqueWordsTree took: " + (stop - start) + " nanoseconds");

				start = System.nanoTime();
				total = countUniqueWordsHash(list);
				stop = System.nanoTime();
				long time4 = stop-start;
				System.out.println("countUniqueWordsHash took: " + (stop - start) + " nanoseconds");

				System.out.println();
				
				// Write data for this round to the output file
				writeToFile(outputFile, s + "," + time1 + "," + time2 + "," + time3 + "," + time4 + "\n");
				
			} catch (IOException e) {
				System.err.println("Error processing file: " + s);
				System.err.println(e.getMessage());
			}
		}
	}

	/****
	 * Process the given file returning a list of words The words are not unique.
	 * Apostrophes (') are part of a word in our context. --->There should be no
	 * reason to modify this code<---
	 ****/
	public static ArrayList<String> readWords(String file) throws IOException {
		// The pattern determines how to split words on a line
		Pattern pattern = Pattern.compile("[^a-zA-Z']+");
		Scanner in = new Scanner(new FileReader(file));
		ArrayList<String> wordList = new ArrayList<String>();

		while (in.hasNext()) {
			String line = in.nextLine(); // Read in the line
			String[] words = pattern.split(line); // Split line into words
			for (String w : words) {
				if (w.length() > 0)
					wordList.add(w); // Add all words to list
			}
		}
		in.close();
		return wordList;
	}

	/****
	 * Count the number of unique words in a given list of words
	 ****/

	// ArrayList one
	public static int countUniqueWords(ArrayList<String> words) {
		ArrayList<String> set = new ArrayList<String>();

		// For each word, check if it is in the set. If not, add it.
		// This removes duplicates from original list
		for (String w : words) {
			if (!set.contains(w))
				set.add(w);
		}

		// For debugging only (print out the list!)
		if (debugMode) {
			for (String w : set) {
				System.out.println(w);
			}
		}

		return set.size(); // Return the size of the set
	}

	public static int countUniqueWordsSorting(ArrayList<String> words) {
		// Create, fill, and sort an ArrayList with all words
		ArrayList<String> set = new ArrayList<String>();
		for (String w : words)
			set.add(w);
		Collections.sort(set);

		int uniqueWords = set.size();

		// Remove duplicates -- since set is sorted alphabetically, all are adjacent
		for (int i = 0; i < set.size() - 1; i++) {
			if (set.get(i).equals(set.get(i + 1)))
				uniqueWords--;
		}

		return uniqueWords;
	}

	public static int countUniqueWordsTree(ArrayList<String> words) {
		TreeSet<String> set = new TreeSet<>();

		for (String w : words) {
			if (!set.contains(w)) {
				set.add(w);
			}
		}

		return set.size();
	}

	// Hash Function to count Unique Words
	public static int countUniqueWordsHash(ArrayList<String> words) {
		HashSet<String> wordCountTxt = new HashSet<>();

		for (String w : words) {
			if (!wordCountTxt.contains(w)) {
				wordCountTxt.add(w);
			}
		}

		return wordCountTxt.size();
	}
	
	public static void writeToFile(File f, String s) {
        try {
            Files.write(Paths.get(f.getPath()), s.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error writing to output file");
            System.exit(3);
        }
	}

}
