package MithiDemo;

import java.io.*;
import java.util.*;

public class MithiAssignment {

    public static void main(String[] args) {
    	
       String[] pageFiles = {"C:/Users/User/Desktop/MithiPages/Page1.txt",
    		   "C:/Users/User/Desktop/MithiPages/Page2.txt", 
    		   "C:/Users/User/Desktop/MithiPages/Page3.txt"};
        Set<String> excludeWords = readExcludeWords("C:/Users/User/Desktop/MithiPages/exclude-words.txt");
        Map<String, Set<Integer>> wordIndex = buildIndex(pageFiles, excludeWords);
        writeIndex(wordIndex, "index.txt");
       
        System.out.println(wordIndex);
    }

    // Reads the common words from an exclude-words.txt file and returns them as a set
    private static Set<String> readExcludeWords(String excludeWordsFile) {
        Set<String> excludeWords = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(excludeWordsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                excludeWords.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading exclude words file: " + e.getMessage());
        }
        return excludeWords;
    }

    // Builds the word index by reading the pages from the specified files and excluding the common words
    private static Map<String, Set<Integer>> buildIndex(String[] pageFiles, Set<String> excludeWords) {
        Map<String, Set<Integer>> wordIndex = new HashMap<>();
        for (int i = 0; i < pageFiles.length; i++) {
            try (BufferedReader br = new BufferedReader(new FileReader(pageFiles[i]))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] words = line.split("\\W+"); // split on non-word characters
                    for (String word : words) {
                        word = word.toLowerCase();
                        if (!excludeWords.contains(word)) {
                            Set<Integer> pages = wordIndex.getOrDefault(word, new HashSet<>());
                            pages.add(i+1); // pages are 1-indexed
                            wordIndex.put(word, pages);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading page file " + pageFiles[i] + ": " + e.getMessage());
            }
        }
        return wordIndex;
    }

    // Writes the word index to a file in the specified format
    private static void writeIndex(Map<String, Set<Integer>> wordIndex, String indexFile) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(indexFile))) {
            List<String> sortedWords = new ArrayList<>(wordIndex.keySet());
            Collections.sort(sortedWords);
            for (String word : sortedWords) {
                Set<Integer> pages = wordIndex.get(word);
                pw.println(word + " : " + pages.toString().replaceAll("[\\[\\],]", ""));
            }
        } catch (IOException e) {
            System.err.println("Error writing index file: " + e.getMessage());
        }
    }

}