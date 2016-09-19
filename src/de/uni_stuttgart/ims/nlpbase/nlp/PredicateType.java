// (c) Wiltrud Kessler
// 26.03.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/



package de.uni_stuttgart.ims.nlpbase.nlp;



/**
 * Comparison type
      ranked,
      superlative,
      equative, 
      difference,
      undefined;
 * 
 * @author kesslewd
 *
 */
public enum PredicateType {
   
      ranked,
      superlative,
      equative, 
      difference,
      undefined;

  
   /**
    * Write the entry in the parsed file, e.g., "05"
    * @param type comparison type
    * @param predicateDirection direction of the comparison
    * @return
    * 01 = ranked superior,
    * 02 = equative,
    * 03 = superlative superior,
    * 04 = difference,
    * 05 = ranked inferior,
    * 06 = superlative inferior.
    */
   public static String getStringFromType (PredicateType type, PredicateDirection predicateDirection) {
      switch (type) {
      case ranked:
         if (predicateDirection == PredicateDirection.INFERIOR)
            return "05";
         else
            return "01";
      case equative: 
         return "02";
      case superlative:
         if (predicateDirection == PredicateDirection.INFERIOR)
            return "06";
         else
            return "03";
      case difference: 
         return "04";
      default: 
         return "00";
      }
      
   }
   
   
   /**
    * Comparison type
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
   public static PredicateType getTypeFromString (String name) {
      String[] parts = name.split("\\.");
      String type = parts[1];
      
      if (type.equals("01") || type.equals("05"))
         return ranked;
      if (type.equals("02"))
         return equative;
      if (type.equals("03") || type.equals("06"))
         return superlative;
      if(type.equals("04"))
         return difference;
      
      return undefined;
   }


}
