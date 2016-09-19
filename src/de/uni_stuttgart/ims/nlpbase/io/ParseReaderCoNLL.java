// (c) Wiltrud Kessler
// 27.01.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.nlpbase.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import de.uni_stuttgart.ims.nlpbase.nlp.ArgumentType;
import de.uni_stuttgart.ims.nlpbase.nlp.PredicateType;
import de.uni_stuttgart.ims.nlpbase.nlp.PredicateDirection;
import de.uni_stuttgart.ims.nlpbase.nlp.SRLSentence;
import de.uni_stuttgart.ims.nlpbase.nlp.Word;


/**
 * Reads parses from an input file in CoNLL format.
 * @author kesslewd
 */
public class ParseReaderCoNLL implements Closeable {

   /**
    * Location of input file in CoNLL format.
    */
   private String inputFileName;

   /**
    * Handle on open input file.
    */
   private BufferedReader inputFile;

   /**
    * Indicator whether a file is currently opened.
    */
   private boolean fileOpen = false;
   
   /**
    * Line number of last read line in current file.
    */
   private int lineno=0;
   
   
   
   /**
    * Create a reader for a file in CoNLL format. 
    * 
    * @param inputFileName Location of input file in CoNLL format.
    */
   public ParseReaderCoNLL (String inputFileName) {
      this.inputFileName = inputFileName;
   }

   
   /**
    * Opens the file set in the constructor.
    * 
    * @throws FileNotFoundException If the file is not where it's supposed to be.
    */
   public void openFile() throws FileNotFoundException {
      if (!this.fileOpen) {
         DataInputStream in = new DataInputStream(new FileInputStream(this.inputFileName));
         this.inputFile = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
         this.fileOpen = true;
      }
   }

   /**
    * Closes the currently open file.
    */
   public void close() throws IOException {
      this.fileOpen = false;
      if (this.inputFile != null)
         this.inputFile.close();
   }
   
   

   /** 
    * Reads a parse for a sentence from the input file.
    * 
    * @param readPred set to True if you want SRL predicate information to be read
    * @param readArgs set to True if you want SRL argument information to be read
    * @return Sentence with parse tree and SRL information (if wanted).
    */
   private SRLSentence readParseSRL(boolean readPred, boolean readArgs) {
      
      SRLSentence tree = new SRLSentence();
      String line = null;
      try {
      
         // Opens the file if not yet open.
         this.openFile();
         
         HashMap<Integer,HashMap<Word,String>> srlInformation = new HashMap<Integer,HashMap<Word,String>>();
         
         while ((line = inputFile.readLine()) != null) {
            lineno++;
            line = line.trim();
            
            // Sentences are separated by an empty line
            // -> if there is an empty line we have the whole sentence
            if (line.equals("")) {
               break;
            }
            
            // Split line in parts, format is
            // (x: is the number of the part, all parts are separated by tabs)
            // 0:ID 1:word 2:lemma 3:lemma 4:POS 5:POS 6:morph 7:morph 8:headID 9:headID 10:deprel 11:deprel [... SRL ...]
            // empty parts have a "_"
            String[] parts = line.split("\t");
            if (parts.length < 10)
               throw new Exception("Error, this line does not have enough parts");
            
            // Catch error in format if lemma/POS is in second slot instead of first
            String lemma =  parts[2];
            if (lemma.equals("_"))
               lemma =  parts[3];
            String pos =  parts[4];
            if (pos.equals("_"))
               pos =  parts[5];
            
            // Create a word with that info
            Word word = new Word(Integer.parseInt(parts[0]), parts[1], lemma, pos, Integer.parseInt(parts[8]), parts[10]);
            
            // Add to tree
            tree.addWord(word);
            
            // Check for predicate
            // 12 -> PRED Y/N
            // 13 -> pred name
            if (readPred && parts.length>=14) {
               if (parts[12].equals("Y")) {
                  String predicate = parts[13];
                  tree.addPredicate(word, PredicateType.getTypeFromString(predicate), PredicateDirection.getDirectionFromString(predicate));
               }
            }
            
            // Check for arguments
            // 14 -> arguments for pred 1
            // 15 -> arguments for pred 2
            // and so on ...
            if (readArgs) {
               for (int i=14;i<parts.length; i++) {
                  if (!parts[i].equals("_")) {
                     int index = i-14;
                     HashMap<Word, String> value = srlInformation.get(index);
                     if (value == null) {
                        value = new HashMap<Word, String>();
                     }
                     value.put(word, parts[i]);
                     srlInformation.put(index, value);
                  }
               }
            }
            
         }
         
         // Build syntactic tree structure (link heads, etc.)
         tree.buildDependencyTree();
         
         // Build SRL structure (add collected args)
         if (readArgs) {
            List<Word> predicates = tree.getPredicates();
            for (Integer predicateIndex : srlInformation.keySet()) {
               HashMap<Word, String> args = srlInformation.get(predicateIndex);
               for (Entry<Word, String> arg : args.entrySet()) {
                  tree.addArgument(predicates.get(predicateIndex), arg.getKey(), ArgumentType.getTypeFromString(arg.getValue()));
               }
            }
         }
         
         
      } catch (Exception e) {
         System.err.println("ERROR in reading parse from file " + this.inputFileName + " in line " + lineno + ":\n" + line);
         e.printStackTrace();
         return null;
      }

      return tree;
   }


   /** 
    * Reads a parse for a sentence from the input file.
    * @return Sentence with depencency information, but not SRL information.
    */
   public SRLSentence readParseOnlyDeps() {
      return readParseSRL(false, false);
   }

   /** 
    * Reads a parse for a sentence from the input file.
    * @return Sentence with depencency information and predicate information, but no arguments.
    */
   public SRLSentence readParseSRLOnlyPreds() {
      return readParseSRL(true, false);
   }
   

   /** 
    * Reads a parse for a sentence from the input file.
    * @return Sentence with depencency information and all SRL information.
    */
   public SRLSentence readParseSRL() {
      return readParseSRL(true, true);
   }
   
}
