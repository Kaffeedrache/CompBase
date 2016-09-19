// (c) Wiltrud Kessler
// 21.08.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.nlpbase.tools;

import java.io.Closeable;
import java.io.IOException;

/**
 * Wrapper around different POS tagger.
 * @author kesslewd
 */
public abstract class POSTagger implements Closeable {
   

   /**
    * Tags a list of tokens.
    * @param tokenlist A list of tokens to be tagged.
    * @return List of POS tags assigned to the tokens. 
    */
   public abstract String[] getPOSTags (String[] tokenlist);

   
   /**
    * Implment Closeable.
    * Close all open resources.
    */
   @Override
   public abstract void close() throws IOException;
   
   
}
