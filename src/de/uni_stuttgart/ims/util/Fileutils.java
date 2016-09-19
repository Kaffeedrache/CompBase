// (c) Wiltrud Kessler
// 21.08.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


/**
 * Collection of helper functions that have something to do with files
 * (opening, closing, writing, reading) 
 * 
 * @author kesslewd
 *
 */
public class Fileutils {


   /**
    * Close something without throwing an error,
    * just a gentle output.
    * 
    * @param resource The thing you want to close DUH.
    */
   public static void closeSilently(Closeable resource) {
      if (resource == null)
         return;
      try {
         resource.close();   
      }
      catch (IOException e) {
         System.err.println("Error in closing resource: " + resource.getClass());
      }   
   }
   

   /**
    * Do all the Java stuff necessary to open a file that you can write to.
    * @param filename The path to the file you want to open.
    * @return A handle for this file.
    * @throws IOException If the file is not found.
    */
   public static BufferedWriter getWriteFile (String filename) throws IOException {
      FileWriter fstream1 = new FileWriter(filename);
      return new BufferedWriter(fstream1);
   }

   /**
    * Do all the Java stuff necessary to open a file that you can read from.
    * @param filename The path to the file you want to open.
    * @return A handle for this file.
    * @throws IOException If the file is not found.
    */
   public static BufferedReader getReadFile (String filename) throws IOException {
      DataInputStream fstream = new DataInputStream(new FileInputStream(filename));
      BufferedReader br = new BufferedReader(new InputStreamReader(fstream, Charset.forName("UTF-8")));
      return br; 
   }

   /**
    * Do all the Java stuff necessary to open a file that you can read from.
    * @param file The file you want to open.
    * @return A handle for this file.
    * @throws IOException If the file is not found.
    */
   public static BufferedReader getReadFile (File file) throws IOException {
      DataInputStream fstream = new DataInputStream(new FileInputStream(file));
      BufferedReader br = new BufferedReader(new InputStreamReader(fstream, Charset.forName("UTF-8")));
      return br; 
   }

   /**
    * Read the complete contents of a file into a String.
    * @param filename The path to the file you want to open.
    * @return A handle for this file.
    * @throws IOException If the file is not found.
    */
   public static String readCompleteFile (String filename) throws IOException {
      BufferedReader br = null;
      try {
         DataInputStream in = new DataInputStream(new FileInputStream(filename));
         br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
      } catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      String strLine = "";

      while (strLine != null) {      
         try {
            strLine += br.readLine();
         } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
      }
      
      return strLine;
   }
   

   /** 
    * Read the complete contents of a file into a String.
    * @param file The file you want to read.
    * @param encoding The character encoding (UTF-8 or whatever)
    * @return The complete text of the file.
    * @throws IOException If the file is not found.
    */
   public static String readCompleteFile (File file, Charset encoding) throws IOException {
      BufferedReader br = null;
      try {

         DataInputStream in = new DataInputStream(new FileInputStream(file));
         br = new BufferedReader(new InputStreamReader(in, encoding));
      } catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      String strLine = "";

      while (strLine != null) {            

         try {
            strLine += br.readLine();
         } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
      }
      
      return strLine;

   }
   
   
   

   /**
    * Write a line to the given file, add newline, flush.
    * @param out file to write to
    * @param line String to write (linebreak will be added at the end)
    */
   public static void writeLine (BufferedWriter out, String line) {
      if (out == null)
         return;
      
      try {
         out.write(line);
         out.newLine();
         out.flush();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   
}
