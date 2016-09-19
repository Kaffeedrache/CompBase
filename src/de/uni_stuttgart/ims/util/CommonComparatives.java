// (c) Wiltrud Kessler
// 06.05.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.util;

import java.util.Arrays;
import java.util.List;

/**
 * Words/POS that indicate comparatives.
 * @author kesslewd
 *
 */
public class CommonComparatives {

   
   public static final List<String> comparativePOS = Arrays.asList("JJR", "JJS", "RBR", "RBS");

   
   public static final List<String> commonComparativeWords = Arrays.asList("as", "more", "less", "most", "least");

   
   public static boolean isComparativePOS (String pos) {
      return comparativePOS.contains(pos);
   }
   

   public static boolean isCommonComparativeWord (String word) {
      return commonComparativeWords.contains(word);
   }

   
   
}
