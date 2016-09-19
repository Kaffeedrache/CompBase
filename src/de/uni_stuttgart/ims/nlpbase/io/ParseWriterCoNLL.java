// (c) Wiltrud Kessler
// 27.01.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.nlpbase.io;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import de.uni_stuttgart.ims.nlpbase.nlp.Sentence;
import de.uni_stuttgart.ims.nlpbase.nlp.Word;


/**
 * Writes parses to an output file in CoNLL format.
 * @author kesslewd
 */
public class ParseWriterCoNLL implements Closeable {


   /**
    * Location of output file in CoNLL format.
    */
   private String outputFileName;

   /**
    * Handle on output file.
    */
   private BufferedWriter outputFile;
   
   /**
    * Indicator whether a file is currently opened.
    */
   private boolean fileOpen;
   

   /**
    * Create a writer for a file in CoNLL format. 
    * 
    * @param outputFileName Location of output file in CoNLL format.
    */
   public ParseWriterCoNLL (String outputFileName) {
      this.outputFileName = outputFileName;
      this.fileOpen = false;
   }


   /**
    * Closes the currently open file.
    */
   public void close() throws IOException {
      if (this.outputFile != null)
         this.outputFile.close(); 
      this.fileOpen = false;
   }

   /**
    * Opens the file set in the constructor.
    * 
    * @throws FileNotFoundException If the file is not where it's supposed to be.
    */
   public void openFile() throws IOException {
      if (!this.fileOpen) {
         FileWriter fstream2 = new FileWriter(this.outputFileName);
         this.outputFile = new BufferedWriter(fstream2);
         this.fileOpen = true;
      }
   }

   /**
    * Writes the parse of the sentence to the file in CoNLL format.
    * 
    * @param tree Parse of the sentence
    */
   public void writeParse(Sentence tree) {
      
      try {
         this.openFile();
      
         List<Word> wordlist = tree.getWordList();
         
         int i = 1;
         for (Word word: wordlist) {            
            this.outputFile.write(i + "\t" + tree.wordToCoNLLString(word));
            this.outputFile.newLine();
            i++;
         }
         
         // End parse with an empty line
         this.outputFile.newLine();
         this.outputFile.flush();
         
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   

}
