
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

public class WordCount {
	public static ArrayList<String> files = new ArrayList<String>();
	public static boolean debugMode = false;

	// The main entry point
	public static void main(String[] args) {
        // Populate file list with all files in tests directory
        try {
            File test = new File("../tests/");
            for (String f : test.list()) {
                files.add("../tests/" + f);
            }
        } catch (NullPointerException e) {
            System.err.println("Error: test files not found");
            System.exit(1);
        }
        
		for (String s : files) {
			System.out.println("Text: " + s);
			try {
				ArrayList<String> list = readWords(s);
				long start = System.nanoTime();
				int total = countUniqueWords(list);
				long stop = System.nanoTime();
				System.out.println("countUniqueWords took: " + (stop - start) + " nanoseconds");

				start = System.nanoTime();
				total = countUniqueWordsSorting(list);
				stop = System.nanoTime();
				System.out.println("countUniqueWordsSorting took: " + (stop - start) + " nanoseconds");

				start = System.nanoTime();
				total = countUniqueWordsTree(list);
				stop = System.nanoTime();
				System.out.println("countUniqueWordsTree took: " + (stop - start) + " nanoseconds");

				start = System.nanoTime();
				total = countUniqueWordsHash(list);
				stop = System.nanoTime();
				System.out.println("countUniqueWordsHash took: " + (stop - start) + " nanoseconds");

				System.out.println();
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

}
