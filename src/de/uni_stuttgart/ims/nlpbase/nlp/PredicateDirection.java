// (c) Wiltrud Kessler
// 26.03.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/



package de.uni_stuttgart.ims.nlpbase.nlp;


/**
 * Direction of a comparison (E1 is inferior/superior to E2 + undefined). 
 * 
 * @author kesslewd
 */
public enum PredicateDirection {
   
   INFERIOR, SUPERIOR, UNDEFINED;
      
   /**
    * Print.
    */
   public String toString() {
      switch (this) {
      case INFERIOR: return "-";
      case SUPERIOR: return "+";
      case UNDEFINED: return "x";
      }
      return null; // shouldn't happen
   }

   /**
    * Direction of a comparison (E1 is inferior/superior to E2 + undefined).
    * 
    * The types are 
    * 01 = ranked superior,
    * 02 = equative,
    * 03 = superlative superior,
    * 04 = difference,
    * 05 = ranked inferior,
    * 06 = superlative inferior.
    *  
    * @param name The entry in the parsed file, e.g., "05"
    * @return Direction of the comparison as coded in the type.
    */
   public static PredicateDirection getDirectionFromString(String name) {
      String[] parts = name.split("\\.");
      String type = parts[1];
      
      if (type.equals("05") || type.equals("06"))
         return PredicateDirection.INFERIOR;
      if (type.equals("01") || type.equals("03"))
         return PredicateDirection.SUPERIOR;
      
      return PredicateDirection.UNDEFINED;
   }

}
