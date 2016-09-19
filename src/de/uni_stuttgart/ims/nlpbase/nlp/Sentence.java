// (c) Wiltrud Kessler
// 27.01.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.nlpbase.nlp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Encapsulates a sentence as a sequence of words
 * and a dependency structure.
 * @author kesslewd
 */
public class Sentence implements Comparable<Sentence> {

   /**
    * The words of the sentence (in order)
    */
   protected ArrayList<Word> words;
   
   
   /**
    * Create empty word list (only ROOT).
    */
   public Sentence() {
      words = new ArrayList<Word>();
      addWord(new Word("ROOT")); // add root
   }


   /**
    * Create word list with all words from the string 
    * (assume tokenized).
    * 
    * @param tokenizedString Tokens separated by whitespace
    */
   public Sentence(String tokenizedString) {
      this(); // add root
      for (String word : tokenizedString.split(" ")) {
         addWord(new Word(word)); // add words
      }
   }
   

   /**
    * Create word list with copies of all words from the sentence.
    * (deep copy, new Word objects).
    * @param otherSentence A different sentence.
    */
   public Sentence (Sentence otherSentence) {
      this(); // add root
      
      // Add copies of words
      for (Word word : otherSentence.getWordList()) {
         addWord(new Word(word));
      }
      
      // Build syntactic tree structure
      this.buildDependencyTree();
      
   }

   
   // ======= Basic word manipulation =======   
   
   
   /**
    * Add word to the end of word list.
    * Give the word the id = index in the list.
    * @param word A word that is not in the sentence yet.
    */
   public void addWord(Word word) {
      words.add(word);
      if (word.getId() == 0)
         word.setID(words.indexOf(word));
      else if (word.getId() != words.indexOf(word))
         System.err.println("You tried to add a word with index " + word.getId() + " in location " + words.indexOf(word));
   }
   
   
   /**
    * Get words (excluding ROOT).
    * @return List of words in the sentence, ordered.
    */
   public List<Word> getWordList () {
      return words.subList(1, words.size());
   }
   
   
   /**
    * Checks if the sentence contains any words.
    * @return TRUE if there is only the ROOT, FALSE otherwise.
    */
   public boolean isEmpty () {
      return (words.size() <= 1);
   }

   
   /**
    * Returns the length of the sentence (in number of tokens).
    * @return number of words in the sentence (including punctuation etc).
    */
   public int getSentenceLength () {
      // minus one = ignore ROOT
      return words.size()-1;
   }

   
   /**
    * Get word number X from the sentence.
    * Counting starts with 1, 0 is the index of the ROOT. 
    * @param index index in the sentence.
    * @return the word at that place, else null
    */
   public Word getWord (int index) {
      if (index > 0 && index < words.size())
         return words.get(index);
      else
         return null;
   }

   /**
    * Get index of Word in sentence.
    * 0 is the index of the ROOT.
    * @param word A word in the sentence.
    * @return index of word or -1 if not found.
    */
   public int getIndex (Word word) {
      return words.indexOf(word);
   }
      
   
   // ======= Dependency structure =======   

   
   /**
    * Build dependency structure based on the head
    * indices given in the words.
    */
   public void buildDependencyTree(){
      for(int i=1;i<words.size();++i){
         Word curWord = words.get(i);
         Word head = words.get(curWord.getHeadId());
         curWord.setHead(head);
         head.addChild(curWord);
      }     
   }
   

   // ======= Path methods =======
   

   /**
    * Returns all words that are in the tree below the current word.
    * Does not include the word itself.
    * @param word A word in the sentence.
    * @return All children and all their children.
    */
   public Set<Word> getDescendants (Word word) {
      Set<Word> descendantsList = new HashSet<Word>();
      Set<Word> children = word.getDirectChildren();
      for (Word child : children) {
         descendantsList.add(child);
         descendantsList.addAll(this.getDescendants(child));
      }
      return descendantsList;
   }
   
   
   /**
    * Gives a list of words from the current word to the ROOT.
    * First word in the list is always ROOT.
    * Last word in the list is always the word itself.
    * @param word A word in the sentence
    * @return List of words from this word to the ROOT.
    */
   public List<Word> getPathToRoot (Word word) {
      ArrayList<Word> headsList = new ArrayList<Word>();
      headsList.add(word);
      Word currentHead = word.getHead();
      while (currentHead != null) {
         headsList.add(0,currentHead);
         currentHead = currentHead.getHead();       
      }
      return headsList;
   }
   

   /**
    * Find lowest common ancestor in the dependency tree
    * of the two words given.
    * Assumes that easy cases are already dealt with.
    * @param word1 A word in the sentence
    * @param word2 A word in the sentence
    * @param word1Heads all words on the path from word1 to ROOT (first item is ROOT, last is word itself)
    * @param word2Heads all words on the path from word2 to ROOT (first item is ROOT, last is word itself)
    * @return Word LCA
    */
   private Word findLowestCommonAncestor (Word word1, Word word2, List<Word> word1Heads, List<Word> word2Heads) {

      // Simple cases
      if (word1 == word2) { // same word
         return word2;
      }
      if (word1.getHead() == word2) { // word2 direct head of word1
         return word2;
      }
      if (word2.getHead() == word1) { // word1 direct head of word2
         return word1;
      }
      if (word1.getHead() == word2.getHead()) { // direct head common
         return word1.getHead();
      }
      
      Iterator<Word> iter1 = word1Heads.iterator();
      Iterator<Word> iter2 = word2Heads.iterator();
      Word one = iter1.next(); // first item is ROOT
      Word two = iter2.next(); // first item is ROOT
      // loop over all words in the list that are the same
      while (one == two && one != null) {
         if (iter1.hasNext()) {
            one = iter1.next();
         } else {
            one = null; // if list ends, enforce exit
         }
         if (iter2.hasNext()) {
            two = iter2.next();
         } else {
            two = null; // if list ends, enforce exit
         }
      }
      Word head = null;
      if (one != null) {
         head = one;
      } else if (two != null) {
         head = two;
      } else {
         // they shouldn't both be null, that would mean
         // they are both the same -> should have returned earlier
         System.out.println("something strange happened here (LCA of " 
               + word1.getForm() + " and " + word2.getForm() + ")");
      }
      return head.getHead();
   }
   
   
   /**
    * Find lowest common ancestor in the dependency tree
    * of the two words given.
    * @param word1 A word in the sentence.
    * @param word2 A word in the sentence.
    * @return A word in the sentence.
    */
   public Word findLowestCommonAncestor (Word word1, Word word2) {      
      // Complicated cases...
      // Compare path to root
      List<Word> word1Heads = getPathToRoot(word1);
      List<Word> word2Heads = getPathToRoot(word2);
      return findLowestCommonAncestor(word1, word2, word1Heads, word2Heads);
      
   }
   

   /**
    * Path from word 1 to word 2.
    * List contains 2 lists: 1 to go "up" to LCA
    * 2nd to go "down"
    * @param word1 A word in the sentence.
    * @param word2 A word in the sentence.
    * @return Word LCA
    */
   public List<List<Word>> getWordsOnPath (Word word1, Word word2) {

      List<Word> word1Heads = getPathToRoot(word1); // first is ROOT, last is word1
      List<Word> word2Heads = getPathToRoot(word2); // first is ROOT, last is word2
      Word lca = this.findLowestCommonAncestor(word1, word2, word1Heads, word2Heads);
      List<List<Word>> path = new ArrayList<List<Word>>(); //word1.getPOS() + "_" + word1.getDeprel();

      // Go from word1 up to LCA
      // (iterate reversed direction, ignore last word (== word itself)
      List<Word> up = new ArrayList<Word>();
      for (int i=word1Heads.size()-1; i>=0; i--) {
         Word word1Head = word1Heads.get(i);
         up.add(word1Head);
         if (word1Head == lca) {
            break;
         }
      }
      path.add(up);

      // Go from LCA down to word2
      List<Word> down = new ArrayList<Word>();
      boolean flag = false;
      for (Word word2Head : word2Heads) {
         if (word2Head == lca) {
            flag = true;
            continue;
         }
         if (flag) {
            down.add(word2Head);
         }
      }
      path.add(down);
      
      return path;
   }
   

   // ======= Word comparisons =======
   

   /**
    * Comparator to compare two words with respect to their sequencial order.
    */
   public final Comparator<Word> wordSequenceComparator = new Comparator<Word>(){
      @Override
      public int compare(Word arg0, Word arg1) {
         return words.indexOf(arg0) - words.indexOf(arg1);
      }     
   };
   
   /**
    * Compares two words with respect to their sequencial order.
    * Both words need to be in the sentence.
    * @param word1 A word in the sentence.
    * @param word2 A word in the sentence.
    * @return 0 if the words are the same
    *    &gt;1 if word1 is after word2 in the sentence
    *    &lt;1 if word1 is before word2 in the sentence
    */
   public int compareSequence (Word word1, Word word2) {
      return wordSequenceComparator.compare(word1, word2); //words.indexOf(word1) - words.indexOf(word2);
   }

   
   public enum TreePosition { SAME, CHILD, PARENT, ANCESTOR, DESCENDANT, SIBLING, SIBLINGDESC, OTHER };
   
   
   /**
    * Compares two words with respect to their position in the tree.
    * @param word1 A word from the sentence.
    * @param word2 A word from the sentence.
    * @return 
    *    same (same word)
    *    child (word2 is direct child of word1)
    *    descendent (word2 is somewhere below word1)
    *    parent (word2 is direct parent of word1)
    *    ancestor (word2 is somewhere on the path from word1 to ROOT)
    *    sibling (word1 and word2 have the same head)
    *    sibling_desc (word2 is a descendent of a direct sibling of word1)
    *    other (everything else)
    */
   public TreePosition compareTreePosition (Word word1, Word word2) {
      
      // Same word
      if (word1 == word2) {
         return TreePosition.SAME;
      }
      
      // Direct child/parent, sibling
      if (word2.getHead() == word1) {
         return TreePosition.CHILD;
      }
      if (word1.getHead() == word2) {
         return TreePosition.PARENT;
      }
      if (word1.getHead() == word2.getHead()) {
         return TreePosition.SIBLING;
      }
      
      // Ancestor
      List<Word> word1Heads = getPathToRoot(word1);
      if (word1Heads.contains(word2)) {
         return TreePosition.ANCESTOR;
      }

      // Descendent
      List<Word> word2Heads = getPathToRoot(word2);
      if (word2Heads.contains(word1)) {
         return TreePosition.DESCENDANT;
      }
      
      // TODO more fine-grained?
      if (word2Heads.contains(word1.getHead())) {
         return TreePosition.SIBLINGDESC;
      }
      
      return TreePosition.OTHER;
   }



   // ======= Comparing sentences =======
   
   /**
    * Check if this sentence is the same as another.
    * Only the word forms are compared, not the analysis
    * (not lemma, POS, depencency, ...).
    *  
    * @param otherSentence A different sentnece.
    * @return TRUE if the two sentences contain the same word forms, FALSE otherwise.
    */
   public boolean isSameSentence(Sentence otherSentence) {
      
      if (otherSentence == null)
         return false;

      List<Word> thisWords = this.getWordList();
      List<Word> otherWords = otherSentence.getWordList();

      // If sizes are different, return false.
      if (thisWords.size() != otherWords.size()) {
         return false;
      }
      
      // For all words, check form
      for (int i=0; i<thisWords.size(); i++) {
         if (!thisWords.get(i).getForm().equals(otherWords.get(i).getForm())) {
            return false;
         }
      }
      
      // Nothing bad found, cool :)
      return true;
      
   }
   

   /**
    * 
    * Compares two the token Strings of the two sentences lexicographically. 
    * DOES NOT COMPARE THE SYNTAX!!
    * 
    * @param o A different sentence.
    * @return 
    *    the value 0 if the argument sentence is equal in tokens to this string ;
    *    a value less than 0 if this string is lexicographically less than the string argument;
    *    and a value greater than 0 if this string is lexicographically greater;
    */
   @Override
   public int compareTo(Sentence o) {
      return this.toString().compareTo(o.toString());
   }
   
   
   

   // ======= Print =======
   

   /**
    * Simple representation by tokens separated by whitespace.
    */
   public String toString () {
      String str = "";
      for(int i=1;i<words.size();i++){
         str = str + " " + words.get(i).getForm();
      }
      return str.trim();
   }
   

   /**
    * CoNLL representation of a word from this sentence.
    * 
    * @param word A word in the sentence.
    * @return CoNLL representation (see Word.toCoNLLString)
    */
   public String wordToCoNLLString(Word word) {
      return word.toCoNLLString();
   }

   
   /**
    * Print each word from the sentence with id and
    * simple word representation.
    */
   public void printTree() {
      for (int i=1; i<words.size(); i++) {
         System.out.println(i + " " + words.get(i).toString());
      }
   }



   
}
