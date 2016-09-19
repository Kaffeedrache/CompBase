// (c) Wiltrud Kessler
// 06.05.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.util;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.ims.nlpbase.nlp.Sentence;
import de.uni_stuttgart.ims.nlpbase.nlp.Word;

/**
 * Map a string / list of tokens to words in a sentence / depndency tree.
 * @author kesslewd
 *
 */
public class StringWordMapping {

   
   /**
    * Check if this word form corresponds to this token.
    * 
    * @param token String.
    * @param toCompare Word.
    * @param caseSensitive Use case information.
    * @return True if same, else yes.
    */
   private static boolean compareTokenWord (String token, String toCompare, boolean caseSensitive) {
      if (caseSensitive) {
         return toCompare.equals(token);
      } else {
         return toCompare.toLowerCase().equals(token.toLowerCase());
      }
   }

   
   
   // ====== Map from one Token to one Word =====
   

   
   /**
    * Get the parameter that should be compared
    * type 0 -  form
    * type 1 - lemma
    * type 2 - pos
    * type 3 - deprel
    * other null
    * 
    * @param type of of 0, 1, 2, 3
    * @param word The word that we want the info from.
    * @return The form/lemma/pos/deprel of the word.
    */
   private static String getToCompare(int type, Word word) {
      switch (type) {
      case 0 : return word.getForm();
      case 1 : return word.getLemma();
      case 2 : return word.getPOS();
      case 3 : return word.getDeprel();      
      }
      return null;
   }


   
   /**
    * Find all ocurrences of a word that corresponds to a single token.
    * Compare the token form.
    * 
    * @param sentence Sentence that is supposed to contain the String.
    * @param tokenToBeFound String (treated as single token) to be found.
    * @param type what of the word should be compared (type 0 -  form, 
    *    type 1 - lemma, type 2 - pos, type 3 - deprel, other null).
    * @param ignorePredicates Ignore predicates in processing if set to true.
    * @param caseSensitive Use case information.
    * @return The word, null if not found.
    */
   private static List<Word> identifyOneTokenAll (Sentence sentence, String tokenToBeFound,
         int type, boolean ignorePredicates, boolean caseSensitive) {
      List<Word> words = sentence.getWordList();
      List<Word> found = new ArrayList<Word>();

      for (Word word : words) {
         String toCompare = getToCompare(type, word);
         if (compareTokenWord(tokenToBeFound, toCompare, caseSensitive)) {
            // Skip predicates if desired - reset
            if (ignorePredicates && word.isPredicate()) {
               continue;
            } else {
               found.add(word);
               break;
            }
         }
      }
      if (found.size() == 0) 
         return null;
      
      return found;
   }


   /**
    * Find all ocurrences of a word that corresponds to a single token.
    * Compare the token form.
    * 
    * @param sentence Sentence that is supposed to contain the String.
    * @param stringToBeFound String (treated as single token) to be found.
    * @param ignorePredicates Ignore predicates in processing if set to true.
    * @param caseSensitive Use case information.
    * @return The word, null if not found.
    */
   public static List<Word> identifyOneWordAll (Sentence sentence, String stringToBeFound, boolean ignorePredicates, boolean caseSensitive) {
      return identifyOneTokenAll(sentence, stringToBeFound, 0, ignorePredicates, caseSensitive);
   }

   /**
    * Find all ocurrences of a word that corresponds to a single token.
    * Compare the token lemma.
    * 
    * @param sentence Sentence that is supposed to contain the String.
    * @param stringToBeFound String (treated as single token) to be found.
    * @param ignorePredicates Ignore predicates in processing if set to true.
    * @param caseSensitive Use case information.
    * @return The word, null if not found.
    */
   public static List<Word> identifyOneLemmaAll (Sentence sentence, String stringToBeFound, boolean ignorePredicates, boolean caseSensitive) {
      return identifyOneTokenAll(sentence, stringToBeFound, 1, ignorePredicates, caseSensitive);
   }

   /**
    * Find all ocurrences of a word that corresponds to a single token.
    * Compare the token Part of Speech.
    * 
    * @param sentence Sentence that is supposed to contain the String.
    * @param stringToBeFound String (treated as single token) to be found.
    * @param ignorePredicates Ignore predicates in processing if set to true.
    * @param caseSensitive Use case information.
    * @return The word, null if not found.
    */
   public static List<Word> identifyOnePOSAll (Sentence sentence, String stringToBeFound, boolean ignorePredicates, boolean caseSensitive) {
      return identifyOneTokenAll(sentence, stringToBeFound, 2, ignorePredicates, caseSensitive);
   }


   // ====== Map from sequence of Strings to sequence of Words =====

   

   /**
    * Find the words that correspond to a sequence of tokens.
    * 
    * @param sentence Sentence that is supposed to contain the String.
    * @param tokens String tokens to be found (each corresponding to one word).
    * @param ignorePredicates Ignore predicates in processing if set to true.
    * @param caseSensitive Use case information.
    * @return List of words in order of sequence, null if not found.
    */
   public static List<Word> identifyMWU (Sentence sentence, String[] tokens, boolean ignorePredicates, boolean caseSensitive) {
      
      List<Word> words = sentence.getWordList();
      
      //System.out.println("predicate: " + Arrays.asList(tokens).toString());
      String top = tokens[0];
      //System.out.println("top: " + top);
      int i = 0;
      int start = -1;
      boolean foundAll = false;
      for (Word word : words) {
         //System.out.println("check " + word.getForm() + " > "+ i);
         String toCompare = word.getForm();
         if (compareTokenWord(top, toCompare, caseSensitive)) {
            // Skip predicates if desired - reset
            if (ignorePredicates && word.isPredicate()) {
               i = 0;
               start = -1;
               top = tokens[0];
               continue;
            }
            //System.out.println("find top " + top + " > "+ i);
            if (i == 0) {
               start = words.indexOf(word);
            }
            if (i == tokens.length-1) {
               //System.out.println("found all");
               foundAll = true;
               break; // found it all
            }
            i++;
            top = tokens[i];
         } else if (i != 0) {
            //System.out.println("reset");
            i = 0;
            start = -1;
            top = tokens[0];
         }
      }

      if (foundAll) {
         List<Word> foundWords = new ArrayList<Word>();
         for (int j=start; j<start+tokens.length; j++) {
            foundWords.add(words.get(j));
         }
         return foundWords;
      }
      
      return null;
   }
   

   /**
    * Find all ocurrences of the words that correspond to a sequence of tokens.
    * 
    * @param sentence Sentence that is supposed to contain the String.
    * @param tokens String tokens to be found (each corresponding to one word).
    * @param ignorePredicates Ignore predicates in processing if set to true.
    * @param caseSensitive Use case information.
    * @return List of words in order of sequence, null if not found.
    */
   public static List<List<Word>> identifyMWUAll (Sentence sentence, String[] tokens, boolean ignorePredicates, boolean caseSensitive) {
      
      List<Word> words = sentence.getWordList();
      List<List<Word>> found = new ArrayList<List<Word>>();

      
      //System.out.println("predicate: " + Arrays.asList(tokens).toString());
      String top = tokens[0];
      //System.out.println("top: " + top);
      int i = 0;
      int start = -1;
      for (Word word : words) {
         //System.out.println("check " + word.getForm() + " > "+ i);
         String toCompare = word.getForm();
         if (compareTokenWord(top, toCompare, caseSensitive)) {
            // Skip predicates if desired - reset
            if (ignorePredicates && word.isPredicate()) {
               i = 0;
               start = -1;
               top = tokens[0];
               continue;
            }
            //System.out.println("find top " + top + " > "+ i);
            if (i == 0) {
               start = words.indexOf(word);
            }
            if (i == tokens.length-1) {
               //System.out.println("found all");
               List<Word> foundWords = new ArrayList<Word>();
               for (int j=start; j<start+tokens.length; j++) {
                  foundWords.add(words.get(j));
               }
               found.add(foundWords);

               //System.out.println("reset");
               i = 0;
               start = -1;
               top = tokens[0];
            }
            i++;
            top = tokens[i];
         } else if (i != 0) {
            //System.out.println("reset");
            i = 0;
            start = -1;
            top = tokens[0];
         }
      }


      if (found.size() == 0) 
         return null;
      
      return found;
   }
   
   
   
}
