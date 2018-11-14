/*****************
 * Author: Christian Duncan
 * Modified by: ...
 *
 * Reds in a file of text and counts how many unique words are
 * in that text.
 *****************/
import java.util.Scanner;
import java.io.IOException;
import java.io.FileReader;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class WordCount {
    public static String fileName = "words.txt";   // The file to process
    public static boolean debugMode = false;

    // The main entry point
    public static void main(String[] args) {
        try {
            ArrayList<String> list = readWords(fileName);
            int total = countUniqueWords(list);
            System.out.println("Original length: " + list.size());
            System.out.println("Total unique words: " + total);
        } catch (IOException e) {
            System.err.println("Error processing file: " + fileName);
            System.err.println(e.getMessage());
        }
    }

    /****
     * Process the given file returning a list of words
     * The words are not unique.  Apostrophes (') are part of a word
     * in our context.
     * --->There should be no reason to modify this code<---
     ****/ 
    public static ArrayList<String> readWords(String file) throws IOException {
        // The pattern determines how to split words on a line
        Pattern pattern = Pattern.compile("[^a-zA-Z']+");
        Scanner in = new Scanner(new FileReader(file));
        ArrayList<String> wordList = new ArrayList<String>();
        
        while (in.hasNext()) {
            String line = in.nextLine();             // Read in the line
            String[] words = pattern.split(line);    // Split line into words
            for (String w: words) {                
                if (w.length() > 0) wordList.add(w); // Add all words to list
            }
        }
        in.close();
        return wordList;
    }
    
    /****
     * Count the number of unique words in a given list of words
     ****/ 
    public static int countUniqueWords(ArrayList<String> words) {
        ArrayList<String> set = new ArrayList<String>();

        // For each word, check if it is in the set.  If not, add it.
        //   This removes duplicates from original list
        for (String w: words) {
            if (!set.contains(w)) set.add(w);
        }
        
        // For debugging only (print out the list!)
        if (debugMode) {
            for (String w: set) {
                System.out.println(w);
            }
        }
        
        return set.size();  // Return the size of the set
    }
}
