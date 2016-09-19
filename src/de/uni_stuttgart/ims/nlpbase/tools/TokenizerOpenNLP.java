// (c) Wiltrud Kessler
// 11.03.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.nlpbase.tools;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


/**
 * Wrapper around OpenNLP tokenizer.
 * @author kesslewd
 */
public class TokenizerOpenNLP extends Tokenizer implements Closeable {
   
   
   // OpenNLP
   private InputStream modelTokenizerFile = null;
   private TokenizerME tokenizerOpenNLP;
   
   
   // ======= Initialization =======

   
   /**
    * Initialize tokenizer.
    * @throws IOException If something goes wrong in the tokenizer initialization.
    * @throws FileNotFoundException If model file not found (supposed to be in 'models/en-sent.bin')
    */
   public TokenizerOpenNLP () throws FileNotFoundException, IOException  {
      modelTokenizerFile = new FileInputStream("models/en-token.bin");
      TokenizerModel modelTokenizer = new TokenizerModel(modelTokenizerFile);
      tokenizerOpenNLP = new TokenizerME(modelTokenizer);
   }
      

   /**
    * Split one sentences into tokens with OpenNLP tokenizer.
    * @param sentence The sentence.
    * @return List of tokens. 
    */
   public String[] tokenize(String sentence) {
      return tokenizerOpenNLP.tokenize(sentence);
   }
   
 
   /**
    * Split one sentences into tokens with OpenNLP tokenizer.
    * @param sentence The sentence.
    * @return List of spans with the start/end positions of each token. 
    * TODO: add covered text
    */
   public TextSpan[] getTokenizationSpans(String sentence) {

      opennlp.tools.util.Span[] tokensOpenNLPSpans = tokenizerOpenNLP.tokenizePos(sentence);
      TextSpan[] tokens = new TextSpan[tokensOpenNLPSpans.length];
      
      for (int i=0; i<tokensOpenNLPSpans.length; i++) {
         opennlp.tools.util.Span sentenceOpenNLPSpan = tokensOpenNLPSpans[i];
         tokens[i] = new TextSpan(sentenceOpenNLPSpan.getStart(), sentenceOpenNLPSpan.getEnd());
      }
      return tokens;
   }
   
   
   /**
    * Implment Closeable.
    * Close all open resources.
    */
   @Override
   public void close() throws IOException {
         modelTokenizerFile.close();
   }
   
}
