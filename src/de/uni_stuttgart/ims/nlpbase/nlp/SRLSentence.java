// (c) Wiltrud Kessler
// 28.03.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/

package de.uni_stuttgart.ims.nlpbase.nlp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import de.uni_stuttgart.ims.nlpbase.nlp.ArgumentType;
import de.uni_stuttgart.ims.nlpbase.nlp.PredicateType;
import de.uni_stuttgart.ims.nlpbase.nlp.PredicateDirection;



/**
 * Extends a sentence to contain Semantic Role Labeling information.
 * 
 * @author kesslewd
 */
public class SRLSentence extends Sentence {
   
   
   private ArrayList<Word> predicates;
   private HashMap<Word,HashMap<Word,ArgumentType>> arguments;
   
   
   /**
    * Get a new empty list of predicates and arguments.
    */
   private void initialize () {
      predicates = new ArrayList<Word>();
      arguments = new HashMap<Word,HashMap<Word,ArgumentType>>();
   }

   /**
    * A SRL sentence is a sentence plus potentially 
    * predicates and arguments.
    */
   public SRLSentence() {
      super();
      initialize();
   }

   /**
    * A SRL sentence is a sentence plus potentially 
    * predicates and arguments.
    * Add all words from String (assume the string is tokenized)
    * 
    * @param tokenizedString A sentence.
    */
   public SRLSentence(String tokenizedString) {
      super(tokenizedString);
      initialize();
   }


   /**
    * A SRL sentence is a sentence plus potentially 
    * predicates and arguments.
    * Add all words and dependency structure from other sentence.
    * @param otherSentence A different sentence.
    */
   public SRLSentence(Sentence otherSentence) {
      super(otherSentence);
      initialize();
   }
   

   /**
    * A SRL sentence is a sentence plus potentially 
    * predicates and arguments.
    * Add all words, dependency structure and SRL relations
    * from other sentence.
    * @param otherSentence A different sentence.
    */
   public SRLSentence(SRLSentence otherSentence) {
      this((Sentence)otherSentence);
      initialize();
      
      // Add predicate-argument structure
      for (Word pred : otherSentence.getPredicates()) {
         int predID = pred.getId();
         Word myPred = this.getWord(predID);
         this.addPredicate(myPred, pred.getType(), pred.getDirection()); 

         for (Word arg : otherSentence.getArguments(pred)) {
            int argID = arg.getId();
            this.addArgument(myPred, this.getWord(argID), otherSentence.getRelation(pred, arg));
         }
      }
      
   }
   
   

   // ======= SRL methods =======   
   
   
   /**
    * Add this word as a predicate.
    * Also marks the word to be a predicate and sets its type.
    * Predicates are sorted in order of appearance in sentence.
    * @param predicate The word from the sentence that should be added to predicates.
    *    No word can be added twice!
    * @param type Type of the comparison introduced by the predicate
    * @param direction Direction of the ranking introduced by the predicate
    */
   public void addPredicate(Word predicate, PredicateType type, PredicateDirection direction) {
      if (!words.contains(predicate)) {
         System.err.println("Error add pred, predicate " + predicate + " is not in this sentence!");
         return;
      }
      if (predicates.contains(predicate)) {
         System.err.println("Error add pred, predicate " + predicate + " is already a predicate!");
         return;
      }
      predicates.add(predicate);
      predicate.markAsPredicate(type, direction);
      Collections.sort(predicates, wordSequenceComparator);
   }

   
   /**
    * All predicates in the sentence.
    * @return predicates in the order of the sentence
    */
   public List<Word> getPredicates () {
      return predicates;
   }

   
   /**
    * Removes a word from the list of predicates and unmarks it.
    * @param predicate The word that shouldn't be a predicate anymore
    */
   public void removePredicate (Word predicate) {
      predicates.remove(predicate);
      predicate.unmarkAsPredicate();
   }

   
   /**
    * Add a word as argument of a given predicate with a given relation.
    * Will printout error and return if one of the words is not in the sentence.
    * Null is a valid relation type
    * 
    * @param predicate A word in the sentence.
    * @param argument A word in the sentence.
    * @param relation A type of relation.
    */
   public void addArgument(Word predicate, Word argument, ArgumentType relation) {     
      if (!words.contains(predicate)) {
         System.err.println("Error add arg, predicate " + predicate + " is not in this sentence!");
         return;
      }
      if (!words.contains(argument)) {
         System.err.println("Error add arg, argument " + argument + " is not in this sentence!");
         return;
      }
      
      // Everything ok, found both words
      HashMap<Word, ArgumentType> value = arguments.get(predicate);
      if (value == null) {
         value = new HashMap<Word, ArgumentType>();
         arguments.put(predicate, value);
      }
      value.put(argument, relation);
   }

   
   /**
    * Get all arguments of the given predicate.
    * No particular order.
    * 
    * @param predicate A word in the sentence.
    * @return List with all arguments of this predicate (empty list if there are none).
    */
   public List<Word> getArguments (Word predicate) {
      HashMap<Word, ArgumentType> result = arguments.get(predicate);
      if (result == null) {
         return new ArrayList<Word>();
      }
      ArrayList<Word> resultList = new ArrayList<Word>();
      for (Word key : result.keySet()) {
         resultList.add(key);
      }
      Collections.sort(resultList, wordSequenceComparator);
      return resultList;
   }


   /**
    * Get the argument(s) of this type for the predicate.
    * If there is none, the empty list is returned.
    * 
    * @param predicate A word in the sentence.
    * @param argumentType Type of relation.
    * @return argument A list of word in the sentence.
    */
   public List<Word> getArgument (Word predicate, ArgumentType argumentType) {
      HashMap<Word, ArgumentType> value = arguments.get(predicate);
      if (value == null) {
          //yes can happen if the predicate has no arguments
         return new ArrayList<Word>();
      }
      List<Word> result = new ArrayList<Word>();
      for (Entry<Word, ArgumentType> entry : value.entrySet()) {
         if (entry.getValue() == argumentType)
            result.add(entry.getKey()); // there may be several!
      }
      return result;
   }
   
   

   /**
    * Get the relation between the predicate and the argument.
    * If there is none, null is returned.
    * 
    * @param predicate A word in the sentence.
    * @param argument A word in the sentence.
    * @return The relation between these words (or null).
    */
   public ArgumentType getRelation (Word predicate, Word argument) {
      HashMap<Word, ArgumentType> value = arguments.get(predicate);
      if (value == null) {
          //yes can happen if the predicate has no arguments
         return null;
      }
      return value.get(argument);
   }
   
   
   
   
   
   // ======= Print =======
   

   /**
    * CoNLL representation of a word from this sentence.
    * Includes columns for predicate and argument annotations.
    * 
    * @param word A word in the sentence.
    * @return CoNLL representation (see Word.toCoNLLString) + predicate-argument info
    */
   public String wordToCoNLLString(Word word) {
      
      // Simple word information
      String str = word.toCoNLLString();
      
      // Is this a predicate?
      if (word.isPredicate()) {
         str = str +"\t"+"Y"+"\t"+word.getPredicateAnnotation();
      } else {
         str = str +"\t"+"_"+"\t"+"_";
      }
      
      // Is this an argument?
      for (Word predicate : this.getPredicates()) {
         ArgumentType relation = this.getRelation(predicate, word);
         if (relation != null) {
            str = str +"\t"+relation.getMappedString();
         } else {
            str = str +"\t"+"_";
         }
      }
      
      return str;
   }
   

   /**
    * Print each word from the sentence with id and
    * simple word representation plus short SRL information.
    */
   public void printSRLTree() {
      for (int i=1; i<words.size(); i++) {
         Word word = words.get(i);
         System.out.print(i + " " + word.toString());
         // Is this a predicate?
         if (word.isPredicate()) {
            System.out.print(" P="+word.getPredicateAnnotation());
         } 
         // Is this an argument?
         for (Word predicate : this.getPredicates()) {
            ArgumentType relation = this.getRelation(predicate,word);
            if (relation != null) {
               System.out.print(" A="+relation);
            }
         }
         System.out.println();
      }
      
   }
   

   /**
    * Sentence in one line, pred/args are marked.
    * @return one line with marked preds/args.
    */
   public String toSRLString() {
      String str="";
      for (int i=1; i<words.size(); i++) {
         Word word = words.get(i);
         str += " " + word.getForm();
         // Is this a predicate?
         if (word.isPredicate()) {
            str += "[P]";
         } 
         // Is this an argument?
         for (Word predicate : this.getPredicates()) {
            ArgumentType relation = this.getRelation(predicate,word);
            if (relation != null) {
               str += "[" + predicate.getForm() + "_" + relation + "]";
            }
         }
      }
      return str.trim();
   }
    
   
}
