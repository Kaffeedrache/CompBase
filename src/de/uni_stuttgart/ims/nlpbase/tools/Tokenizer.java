// (c) Wiltrud Kessler
// 11.03.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.nlpbase.tools;

import java.io.Closeable;


/**
 * Wrapper around different tokenizers.
 * @author kesslewd
 */
public abstract class Tokenizer implements Closeable {
   

   /**
    * Split one sentence into tokens.
    * @param sentence The sentence.
    * @return List of tokens. 
    */
   public abstract String[] tokenize (String sentence);


   /**
    * Split one sentence into tokens, return TextSpans.
    * @param sentence The sentence.
    * @return List of spans with the start/end positions of each token.  
    */
   public abstract TextSpan[] getTokenizationSpans (String sentence);

}
