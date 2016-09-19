// (c) Wiltrud Kessler
// 11.03.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/

package de.uni_stuttgart.ims.nlpbase.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;


/**
 * Wrapper around OpenNLP sentence splitter.
 * @author kesslewd
 */
public class SentenceSplitterOpenNLP extends SentenceSplitter {
   
   private FileInputStream modelSentenceSplitterFile;
   private SentenceDetectorME sentenceDetector;

  
   /**
    * Initialize sentence splitter.
    * @throws IOException if something goes wrong in the initialization of the model
    * @throws FileNotFoundException If model file not found (supposed to be in 'models/en-sent.bin')
    */
   public void initializeOpenNLP () throws FileNotFoundException, IOException {
      modelSentenceSplitterFile = new FileInputStream("models/en-sent.bin");
      SentenceModel modelSentenceSplitter = new SentenceModel(modelSentenceSplitterFile);
      sentenceDetector = new SentenceDetectorME(modelSentenceSplitter);
   }
   

   /**
    * Split the string into sentences with OpenNLP.
    * @param document The test of the whole document.
    * @return List of spans with the start/end positions of each sentence and covered text. 
    * TODO: add covered text
    */
   public TextSpan[] split(String document) {
      opennlp.tools.util.Span[] sentencesOpenNLPSpans = sentenceDetector.sentPosDetect(document);
      TextSpan[] sentences = new TextSpan[sentencesOpenNLPSpans.length];
      
      for (int i=0; i<sentencesOpenNLPSpans.length; i++) {
         opennlp.tools.util.Span sentenceOpenNLPSpan = sentencesOpenNLPSpans[i];
         sentences[i] = new TextSpan(sentenceOpenNLPSpan.getStart(), sentenceOpenNLPSpan.getEnd());
      }
      return sentences;
   }
   

   /**
    * Implment Closeable.
    * Close all open resources.
    */
   @Override
   public void close() throws IOException { 
         modelSentenceSplitterFile.close();
   }
   

}
