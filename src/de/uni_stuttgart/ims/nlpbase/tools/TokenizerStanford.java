// (c) Wiltrud Kessler
// 11.03.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.nlpbase.tools;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;


/**
 * Wrapper around Stanford tokenizer.
 * @author kesslewd
 */
public class TokenizerStanford extends Tokenizer implements Closeable {
   

   // Stanford
   // to force 1 sentence: ssplit.isOneSentence
   private static String options = "normalizeParentheses=false,normalizeOtherBrackets=false,untokenizable=noneDelete,escapeForwardSlashAsterisk=false";
   
   // untokenizable: What to do with untokenizable characters (ones not known to the tokenizer). 
   //Six options combining whether to log a warning for none, the first, or all, and 
   //whether to delete them or to include them as single character tokens in the output: 
   //noneDelete, firstDelete, allDelete, noneKeep, firstKeep, allKeep. The default is "firstDelete". 

   // see 
   // http://nlp.stanford.edu/software/corenlp.shtml#Usage
   // http://nlp.stanford.edu/software/tokenizer.shtml

   
   /**
    * Split one sentences into tokens with Stanford tokenizer.
    * @param sentence The sentence.
    * @return List of tokens. 
    */
   public String[] tokenize (String sentence) {
      StringReader reader=new StringReader(sentence);
      String[] str = new String[0];
      PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<CoreLabel>(reader,
            new CoreLabelTokenFactory(), options);
      ArrayList<String> strList = new ArrayList<String>();
      for (CoreLabel label; ptbt.hasNext(); ) {
         label = ptbt.next();
         strList.add(label.word());
      }
      
      str = new String[strList.size()];
      for (int i=0; i<strList.size(); i++) {
         str[i] = strList.get(i);
      }

      return str;
   }
   

   /**
    * Split one sentences into tokens with Stanford tokenizer.
    * @param sentence The sentence.
    * @return List of spans with the start/end positions of each token. 
    */
   public TextSpan[] getTokenizationSpans (String sentence) {
      
      StringReader reader=new StringReader(sentence);
      PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<CoreLabel>(reader,
            new CoreLabelTokenFactory(), options);
      ArrayList<TextSpan> spansList = new ArrayList<TextSpan>();
      for (CoreLabel label; ptbt.hasNext(); ) {
         label = ptbt.next();
         spansList.add(new TextSpan(label.beginPosition(), label.endPosition(), label.word()));
      }
      
      TextSpan[] spans = new TextSpan[spansList.size()];
      for (int i=0; i<spansList.size(); i++) {
         spans[i] = spansList.get(i);
      }

      return spans;
   }


   @Override
   public void close() throws IOException {
   }
   

   
}
