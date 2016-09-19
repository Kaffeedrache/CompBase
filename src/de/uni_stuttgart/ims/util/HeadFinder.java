// (c) Wiltrud Kessler
// 06.05.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.util;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.ims.nlpbase.nlp.POSUtils;
import de.uni_stuttgart.ims.nlpbase.nlp.Sentence;
import de.uni_stuttgart.ims.nlpbase.nlp.Word;

/**
 * From a list of words, find the head.
 * @author kesslewd
 *
 */
public class HeadFinder {



   // ====== Predicates =====
   

   /**
    * From the list of words, select the head that should be annotated
    * as predicate.
    * Series of complex rules TODO describe.
    * 
    * @param sentence Sentence that is supposed to contain the String.
    * @param words Words that make up the predicate
    * @return The head. Null in case the list is empty.
    */
   public static Word getPredicateHead (Sentence sentence, List<Word> words) {

      // Ignore null
      if (words == null)
         return null;

      // Only one word - return it
      // (this shouldn't really happen here, but just in case)
      if (words.size() == 1) {
         return words.get(0);
      }
      
      // Two or more words
      
      // 1. Search for common comparative word
      for (Word word : words) {
         if (CommonComparatives.isCommonComparativeWord(word.getForm().toLowerCase())) {
            return word;
         }
      }

      // 2. Search for comparative POS
      for (Word word : words) {
         if (CommonComparatives.isComparativePOS(word.getPOS())) {
            return word;
         }
      }
      
      // 3. Delete prepositions and articles (includes numbers)
      List<Word> toConsider= new ArrayList<Word>();
      List<Word> toIgnorePrep= new ArrayList<Word>();
      List<Word> toIgnoreDet= new ArrayList<Word>();

      for (Word word : words) {
         if (POSUtils.isPrepositionPOS(word.getPOS())) {
            //System.out.println("delete " + word);
            toIgnorePrep.add(word);
         } else if (POSUtils.isDeterminerPOS(word.getPOS())) {
            toIgnoreDet.add(word);
         } else {
            toConsider.add(word);
         }
      }
      
      // 3a. If we have only prepositions, deal with that
      if (toConsider.size() == 0) {
         if (toIgnoreDet.size() > 0) {
            return toIgnoreDet.get(0);
         }         
         //System.out.println("have only prepositions");
         // only preps - TODO
         return toIgnorePrep.get(0);
      }
      
      // 3b. If we only have one word left - return it
      if (toConsider.size() == 1) {
         return toConsider.get(0);
      }
      
      // 3b. More than one word left - TODO
      return toConsider.get(0);
   }
   
   


   // ====== Arguments =====
   

   /**
    * From the list of words, select the head that should be annotated
    * as argument. This is the lowest common ancestor of all words.
    * 
    * @param sentence Sentence that is supposed to contain the String.
    * @param words Words that make up the argument.
    * @return The head. Null in case the list is empty.
    */
   public static Word getArgumentHead (Sentence sentence, List<Word> words) {
      
      if (words == null)
         return null;
      
      if (words.size() == 1)
         return words.get(0);
      

      
      // Delete prepositions and articles (includes numbers)
      List<Word> toConsider= new ArrayList<Word>();
      List<Word> toIgnorePunct= new ArrayList<Word>();
      List<Word> toIgnorePrep= new ArrayList<Word>();
      //List<Word> toIgnoreDet= new ArrayList<Word>();

      for (Word word : words) {
         if (POSUtils.isPrepositionPOS(word.getPOS())) {
            //System.out.println("delete " + word);
            toIgnorePrep.add(word);
         //} else if (POSUtils.isDeterminerPOS(word.getPOS())) { // don't do this because this includes model numbers as CD
         //   toIgnoreDet.add(word);
         } else if (POSUtils.isPunctuationPOS(word.getPOS())) {
            toIgnorePunct.add(word);
         } else {
            toConsider.add(word);
         }
      }
      
      // only one "real world" left -> return
      if (toConsider.size() == 1) 
         return toConsider.get(0);
         
      
      // Nothing left to consider -> take worse thing
      if (toConsider.size() == 0) {
         //System.out.println("nothing left to consider: " + words);
         //if (toIgnoreDet.size() > 0)
            //return toIgnoreDet.get(0);

         if (toIgnorePrep.size() > 0)
            return toIgnorePrep.get(0);

         else // this should never happen
            return toIgnorePunct.get(0);
         
      } else { // toConsider > 1

      ///System.out.println("find lca: " + toConsider);
      // Find lca of all
      List<Word> lcas = new ArrayList<Word>();
      Word currentHead = words.get(0);
      lcas.add(currentHead);
      for (int j=1; j<words.size(); j++) {
         currentHead = sentence.findLowestCommonAncestor(currentHead, words.get(j));

         // If lca is not in the list of words, replace by one of them
         // TODO check toConsider or all words??
         if (!words.contains(currentHead)) {
            //System.out.println("is not in list " + currentHead);
            currentHead = lcas.get(lcas.size()-1); // TODO check this
            //currentHead = toConsider.get(j);
         }
         lcas.add(currentHead);
      }
      //System.out.println("lca: " + currentHead);
      
      return currentHead;
      }
   }



   
   
}
