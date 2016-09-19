// (c) Wiltrud Kessler
// 06.05.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.util;

import java.util.Set;
import java.util.TreeSet;

import de.uni_stuttgart.ims.nlpbase.nlp.ArgumentType;
import de.uni_stuttgart.ims.nlpbase.nlp.POSUtils;
import de.uni_stuttgart.ims.nlpbase.nlp.SRLSentence;
import de.uni_stuttgart.ims.nlpbase.nlp.Word;


/**
 * Find the subtree that starts at a word.
 * 
 * @author kesslewd
 *
 */
public class SubTreeFinder {
   
   private static boolean filterFromSubTree (Word word, ArgumentType argumentName) {

      // TODO maybe we want not to filter ths??
      if (word.isPredicate()) {
         //System.out.println("delete " + word + " is pred");
         return true;
      }

      String pos = word.getPOS();

      // TODO maybe we want not to filter ths??
      if (CommonComparatives.isComparativePOS(pos)) {
         return true;
      }
      
      
      if (ArgumentType.isEntityType(argumentName)) {

//         System.out.println("delete " + word + " " 
//               + POSUtils.isConjunctionPOS(pos)  + " " 
//               + POSUtils.isPrepositionPOS(pos)+ " "    
//               + POSUtils.isPunctuationPOS(pos)
//               );
         return POSUtils.isConjunctionPOS(pos) | POSUtils.isPrepositionPOS(pos) | POSUtils.isPunctuationPOS(pos);
      }

      if (argumentName == ArgumentType.aspect) {

//         System.out.println("delete " + word + " " 
//               + POSUtils.isConjunctionPOS(pos)  + " " 
//               + POSUtils.isPrepositionPOS(pos)+ " "    
//               + POSUtils.isPunctuationPOS(pos) + " "
//               
//               + POSUtils.isDeterminerPOS(pos)
//               );
         
         return POSUtils.isConjunctionPOS(pos) | 
               POSUtils.isPrepositionPOS(pos) | 
               POSUtils.isPunctuationPOS(pos)| 
               POSUtils.isDeterminerPOS(pos)
               ;
      }
      
      // the rest of arguments may be anything
      return true;
      
   }
   
   
   /**
    * Get the subtree of all words that are dependents of this word.
    * 
    * @param tree Sentence
    * @param argument The word that we want to get the subtree from
    * @param argumentName what type of argument the word is.
    * @return List of words included in the subtree.
    */
   public static Word[] getSubTree(SRLSentence tree, Word argument, ArgumentType argumentName) {

      TreeSet<Word> childrenList = new TreeSet<Word>(tree.wordSequenceComparator);

      // Put argument itself in the list
      childrenList.add(argument);
      int argumentID = argument.getId();

      // Add all direct children
      Set<Word> children = argument.getDirectChildren();
      childrenList.addAll(children);
      

      // Take all words between the first and the last token
      int min = childrenList.first().getId();
      int max = childrenList.last().getId();
      
      // Delete token and all previous/following if
      // - is CC or IN
      // - is a predicate
      // - is punctuation

      // TODO
      // here we move min/max 1 token towards argument,
      // but if this is a child of the just deleted token,
      // we should not include this, but use the next child

      for (Word child : childrenList) {
         if (filterFromSubTree(child, argumentName)) { // delete
            
            int childID = child.getId();            
            if (childID < argumentID) {// before
               min = childID+1;
            }
            if (childID > argumentID) {// after
               max = childID-1;
            }
               
         }
         
      }
      

      Word[] tokens = new Word[max-min+1];
      //System.out.println("array size " + (max-min+1));
      int i = 0;
      for (int j=min; j<=max; j++) {
         Word child = tree.getWord(j);
         //System.out.println(i + " is word " + j + " " + child);
         tokens[i] = child;
         i++;
      }
      //System.out.println(ComparisonAnnotationToken.getStringWithIDs(tokens));
      
      return tokens;

      
   }

   
}
