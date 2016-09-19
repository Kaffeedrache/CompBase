// (c) Wiltrud Kessler
// 21.08.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.nlpbase.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;


/**
 * Wrapper around Stanford POS tagger.
 * See:  http://nlp.stanford.edu/software/corenlp.shtml#Usage
 *       http://nlp.stanford.edu/software/tokenizer.shtml
 * @author kesslewd
 */
public class POSTaggerStanford extends POSTagger {
   
   /**
    * TODO parametrize
    */
   private String modelTaggerFile = "../models/english-left3words-distsim.tagger";
   
   /**
    * The POSTagger.
    */
   private MaxentTagger stanfTagger;

   
   
   // ======= Initialization =======

   

   /**
    * Initialize tokenizer.
    * @throws IOException if the model tagger file is not found
    *    (supposed to be at location given in 'modelTaggerFile')
    */
   public POSTaggerStanford() throws IOException {
      stanfTagger = new MaxentTagger(modelTaggerFile);
   }
   
   
   // ======= Processing (return Strings) =======
   

   /**
    * Tags a list of tokens.
    * @return List of POS tags. 
    */
   public String[] getPOSTags (String[] tokenlist) {
      
      List<Thingy> stupid = new ArrayList<Thingy>();
      for (String word : tokenlist) {
         Thingy blubb = new Thingy();
         blubb.setWord(word);
         stupid.add(blubb);
      }
      
      ArrayList<TaggedWord> ptbt = stanfTagger.tagSentence(stupid);      
      String[] str = new String[ptbt.size()];
      for (int i=0; i<ptbt.size(); i++) {
         str[i] = ptbt.get(i).tag();
      }

      return str;
   }

   

   // ======= Cleanup =======
   
   
   /**
    * Implment Closeable.
    * Close all open resources.
    */
   @Override
   public void close() throws IOException {
   }
   
   
   /**
    * Stupid
    * @author kesslewd
    */
   class Thingy implements HasWord {
      private static final long serialVersionUID = 1L;
      String word;
      @Override
      public void setWord(String word) {
         this.word = word;
      }
      @Override
      public String word() {
         return word;
      }
   }
   
}
