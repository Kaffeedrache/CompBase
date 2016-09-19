// (c) Wiltrud Kessler
// 04.04.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.nlpbase.nlp;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Wrapper around Parts of Speech as assigned by tagger or parser
 * to ensure easy adaptability to a new tagest or language.
 * @author kesslewd
 */
public class POSUtils {

   /** category according to universal tagset (Petrov et al. 2012)
    * Merge pre/post position and particle (
    **/
   public static enum POSCategory {
      NOUN, ADJECTIVE, ADVERB, VERB,
      PRONOUN, DETERMINER, PREPOSITION, CONJUNCTION,
      NUMBER, PUNCTUATION, 
      OTHER
   }
   

   /**
    * List of punctuation characters
    */
   private static List<Character> punctuationChar = Arrays.asList(
         '.',',',';',':','!','?', // sentence punctuation
         '(',')','[',']', // parenthesis
         '\'','`','"', // quotes
         '-','*','/','\\' // other
         );

   /**
    * List of punctuation POS
    */
   private static List<String> punctuationPOS = Arrays.asList(
         ".",",",";",":","!","?", // sentence punctuation
         "-lrb-","-rrb-","[","]", "(", ")", // parenthesis
         "\"","`","'", // quotes
         "-","--","*","/","\\","$" // other         
         );
   
   
   private static final Map<POSCategory, List<String>> posMapping = createMap();

   private static Map<POSCategory, List<String>> createMap() {
      HashMap<POSCategory, List<String>> result = new HashMap<POSCategory, List<String>>();
      result.put(POSCategory.NOUN, Arrays.asList("NN", "NNP", "NNPS", "NNS"));
      result.put(POSCategory.PRONOUN, Arrays.asList("PRP", "PRP$", "WP", "WP$"));
      result.put(POSCategory.ADJECTIVE, Arrays.asList("JJ", "JJR", "JJS"));
      result.put(POSCategory.ADVERB, Arrays.asList("RB", "RBR", "RBS", "WRB"));
      result.put(POSCategory.VERB, Arrays.asList("MD", "VB", "VBD", "VBG", "VBN", "VBP", "VBZ"));
      
      result.put(POSCategory.PREPOSITION, Arrays.asList("IN", "POS", "RP", "TO"));
      result.put(POSCategory.DETERMINER, Arrays.asList("DT", "WDT", "PDT", "EX"));
      result.put(POSCategory.CONJUNCTION, Arrays.asList("CC"));
      result.put(POSCategory.NUMBER, Arrays.asList("CD"));
      result.put(POSCategory.PUNCTUATION, punctuationPOS);
      
      return Collections.unmodifiableMap(result);
   }
   

   /**
    * Checks whether the given String POS is in the given category.
    * @param pos Penn Treebank part-of-speech tag
    * @param category Our categories.
    * @return TRUE if the pos is in the category, FALSE otherwise.
    */
   private static boolean checkMapping (POSCategory category, String pos) {      
      List<String> listy = posMapping.get(category);
      if (listy != null)
         return listy.contains(pos);
      else 
         return false;
   }
   

   /**
    * Checks if the given Part of Speech is from the category Noun (NN*).
    * @param pos Penn Treebank part-of-speech tag
    * @return TRUE if this is a noun (defined by POSCategory.NOUN).
    */
   public static boolean isNounPOS (String pos) {
      return checkMapping(POSCategory.NOUN, pos);
   }
   
   /**
    * Checks if the given Part of Speech is from the category Adjective (JJ*).
    * @param pos Penn Treebank part-of-speech tag
    * @return TRUE if this is an adjective (defined by POSCategory.ADJECTIVE).
    */
   public static boolean isAdjectivePOS (String pos) {
      return checkMapping(POSCategory.ADJECTIVE, pos);
   }

   /**
    * Checks if the given Part of Speech is from the category Adverb (RB*, WRB).
    * @param pos Penn Treebank part-of-speech tag
    * @return TRUE if this is an adverb (defined by POSCategory.ADVERB).
    */
   public static boolean isAdverbPOS (String pos) {
      return checkMapping(POSCategory.ADVERB, pos);
   }

   /**
    * Checks if the given Part of Speech is from the category Verb (VB*).
    * @param pos Penn Treebank part-of-speech tag
    * @return TRUE if this is a verb (defined by POSCategory.VERB).
    */
   public static boolean isVerbPOS (String pos) {
      return checkMapping(POSCategory.VERB, pos);
   }   

   /**
    * Checks if the given Part of Speech is from the category Pronoun (PR*).
    * @param pos Penn Treebank part-of-speech tag
    * @return TRUE if this is a pronoun (defined by POSCategory.PRONOUN).
    */
   public static boolean isPronounPOS (String pos) {
      return checkMapping(POSCategory.PRONOUN, pos);
   }
   
   /**
    * Checks if the given Part of Speech is from the category Preposition (IN, TO).
    * @param pos Penn Treebank part-of-speech tag
    * @return TRUE if this is a preposition (defined by POSCategory.PREPOSITION).
    */
   public static boolean isPrepositionPOS (String pos) {
      return checkMapping(POSCategory.PREPOSITION, pos);
   }

   /**
    * Checks if the given Part of Speech is from the category Determiner (DT, CD).
    * @param pos Penn Treebank part-of-speech tag
    * @return TRUE if this is a determiner (defined by POSCategory.DETERMINER).
    */
   public static boolean isDeterminerPOS (String pos) {
      return checkMapping(POSCategory.DETERMINER, pos);
   }

   /**
    * Checks if the given Part of Speech is from the category Conjunction (CC).
    * @param pos Penn Treebank part-of-speech tag
    * @return TRUE if this is a conjunction (defined by POSCategory.CONJUNCTION).
    */
   public static boolean isConjunctionPOS (String pos) {
      return checkMapping(POSCategory.CONJUNCTION, pos);
   }

   /**
    * Checks if the given Part of Speech is from the category Cardinal Number (CD).
    * @param pos Penn Treebank part-of-speech tag
    * @return TRUE if this is a number (defined by POSCategory.NUMBER).
    */
   public static boolean isNumberPOS (String pos) {
      return checkMapping(POSCategory.NUMBER, pos);
   }

   /**
    * Checks if the given Part of Speech is from the category Punctuation (lots of stuff).
    * @param pos Penn Treebank part-of-speech tag
    * @return TRUE if this is punctuation (defined by POSCategory.PUNCTUATION).
    */
   public static boolean isPunctuationPOS (String pos) {
      return checkMapping(POSCategory.PUNCTUATION, pos);
   }
   

   /**
    * Returns the POS category of the given POS.
    * Null in case of nullstring or empty string.
    * @param pos Penn Treebank part-of-speech tag
    * @return POS Category ('OTHER' if none is found).
    */
   public static POSCategory getPOSCategory (String pos) {
      
      if (pos == null || pos.isEmpty())
         return null;
      
      if (isNounPOS(pos))
         return POSCategory.NOUN;
      if (isAdjectivePOS(pos))
         return POSCategory.ADJECTIVE;
      if (isAdverbPOS(pos))
         return POSCategory.ADVERB;
      if (isVerbPOS(pos))
         return POSCategory.VERB;

      if (isPronounPOS(pos))
         return POSCategory.PRONOUN;
      if (isPrepositionPOS(pos))
         return POSCategory.PREPOSITION;
      if (isDeterminerPOS(pos))
         return POSCategory.DETERMINER;
      if (isConjunctionPOS(pos))
         return POSCategory.CONJUNCTION;
      
      if (isNumberPOS(pos))
         return POSCategory.NUMBER;
      if (isPunctuationPOS(pos))
         return POSCategory.PUNCTUATION;

      
      return POSCategory.OTHER;

   }
   

   /**
    * Give two first letters of POS.
    * If the POS is not long enough, return POS itself.
    * @param pos Penn Treebank part-of-speech tag
    * @return two first letters of POS.
    */
   public static String getPOSPrefix (String pos) {
      if (pos.length() > 2) {
         return pos.substring(0, 2);
      }
      return pos;
   }


   /**
    * Checks if the two POS are from the same category
    * @param pos1 Penn Treebank part-of-speech tag
    * @param pos2 Penn Treebank part-of-speech tag
    * @return TRUE if cateogires are the same, FALSE otherwise.
    */
   public static boolean haveSamePOSCategory (String pos1, String pos2) {
       POSCategory cat1 = getPOSCategory(pos1);
       POSCategory cat2 = getPOSCategory(pos2);
      return cat1 == cat2;
   }
   
   

   /**
    * Checks if a word is punctuation (dot, comma, parenthesis, ...).
    * 
    * @param word The word to check (check surface form).
    * @return true if the word contains no non-punctuation char, 
    *    false in every other case (including null, empty String, ROOT)
    */
   public static boolean isPunctuationWord (String word) {
            
      if (word == null || word.equals("")) { 
         return false;
      }
      
      for (Character c : word.toCharArray()) {
         if (!punctuationChar.contains(c)) {
            return false;
         }
      }
      return true;
      
   }
   
}
