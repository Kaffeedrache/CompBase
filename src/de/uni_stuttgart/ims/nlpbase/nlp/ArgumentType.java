// (c) Wiltrud Kessler
// 26.03.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.nlpbase.nlp;



/**
 * SRL Arguments
 * 
 * entity1, 
 * entity2, 
 * aspect,
 * sentiment
 * argument, // catch-all default
 * 
 * @author kesslewd
 *
 */
public enum ArgumentType {

   entity1, 
   entity2, 
   aspect,
   sentiment,
   
   /** catch all default */
   argument,
   ;
   
   
   /**
    * Different groupings / annotations for arguments.
    * There are four possibilities, those in () are grouped.
    * 
    * ONE,  // just 'argument'
    * TWO, // (entity1, entity2), (aspect, sentiment)
    * THRA, //  (entity1, entity2), aspect, sentiment
    * THRE, // entity1, entity2, (aspect, sentiment)
    * ALL  // entity1, entity2, aspect, sentiment
    * 
    * @author kesslewd
    *
    */
   public static enum argumentMarkerType {
         /**
          * just 'argument'
          */
         ONE, 
         
         /**
          * (entity1, entity2), (aspect, sentiment)
          */
         TWO,  
         
         /**
          *  (entity1, entity2), aspect, sentiment
          */
         THRA, 
         /**
          * entity1, entity2, (aspect, sentiment)
          */
         THRE,
         /**
          * entity1, entity2, aspect, sentiment
          */
         ALL 
         };
         
   public static argumentMarkerType useArgumentMarker = argumentMarkerType.ALL;

   public static void setArgumentMarker(String marker) {
      useArgumentMarker = argumentMarkerType.valueOf(marker.toUpperCase());
   }
   
   public static void setArgumentMarker(argumentMarkerType marker) {
      useArgumentMarker = marker;
   }
   
   
   
   
   /**
    * Entity types = entity1, entity2, entity
    * @param at the argument type
    * @return True if the type is entity1 or entity2, False otherwise
    */
   public static boolean isEntityType (ArgumentType at) {
      if ( at == ArgumentType.entity1 | at == ArgumentType.entity2 )
         return true;
      else
         return false;
   }

   /**
    * 
    * Target types = aspect, sentiment
    * @param at the argument type
    * @return True if the type is aspect or sentiment, False otherwise
    */
   public static boolean isTargetType (ArgumentType at) {
      if (at == ArgumentType.aspect | at == ArgumentType.sentiment)
         return true;
      else
         return false;
      
   }
   
   
   
   
   /**
    * Get arguments from String independent of the argument marker setting.
    * 
    * A0 : Aspect
    * A1 : Entity 1
    * A2 : Entity 2
    * A3 : Sentiment
    * 
    * @param name The entry in the parsed file, e.g., "A0"
    * @return The corresponding type, see above.
    */
   public static ArgumentType getUnmappedTypeFromString (String name) {
      if (name.equals("A0")) {
         return aspect;
      } else if (name.equals("A1")) {
         return entity1;
      } else if (name.equals("A2")) {
         return entity2;
      } else if (name.equals("A3")) {
         return sentiment;
      }
      return argument;
   }
   

   /**
    * Get Strings for arguments independent of the argument marker setting.
    * 
    * A0 : Aspect
    * A1 : Entity 1
    * A2 : Entity 2
    * A3 : Sentiment
    * 
    * @return The corresponding String, see above.
    */
   public String getUnmappedString() {
      switch (this) {
      case aspect: return "A0";
      case entity1: return "A1";
      case entity2: return "A2";
      case sentiment: return "A3";
      case argument: return "A";
      }
      return null;
   }
   
   
   

   /**
    * Go from String to ArgumentType according to the argument marker setting.
    * @param name The entry in the parsed file, e.g., "A0"
    * @return The corresponding type depending on the mapping.
    */
   public static ArgumentType getTypeFromString (String name) {

      switch (useArgumentMarker) {
      case ONE: return ArgumentType.argument;
      case TWO:
         if (name.equals("A0") || name.equals("A3")) { // aspect, sentiment -> map to aspect
            return ArgumentType.aspect;
         } else { // entity 1 and 2 -> map to one entity
            return ArgumentType.entity1;
         }
      case THRA: 
         if (name.equals("A0")) {
            return ArgumentType.aspect;
         } else if (name.equals("A3")) {
            return ArgumentType.sentiment;
         } else { // entity 1 and 2 -> map to one entity
            return ArgumentType.entity1;
         }
       case THRE: 
         if (name.equals("A1")) {
            return ArgumentType.entity1;
         } else if (name.equals("A2")) {
            return ArgumentType.entity2;
         } else { // aspect, sentiment -> map to aspect
            return ArgumentType.aspect;
         }
      case ALL: return getUnmappedTypeFromString(name);
      }
      return null;
   }
      
      

   /**
    * Get Strings for arguments according to the argument marker setting.
    * @return The corresponding Strings depending on the mapping.
    */
   public String getMappedString() {
      switch (useArgumentMarker) {
      case ONE: return "A0";
      case TWO:
         if (this == ArgumentType.aspect || this == ArgumentType.sentiment) {
            return "A0";
         } else { // entity 1 and 2
            return "A1";
         }
      case THRA: 
         if (this == ArgumentType.aspect) {
            return "A0";
         } else if (this == ArgumentType.sentiment) {
            return "A3";
         } else { // entity 1 and 2
            return "A1";
         }
       case THRE: 
         if (this == ArgumentType.aspect || this == ArgumentType.sentiment) {
            return "A0";
         } else if (this == ArgumentType.entity1) {
            return "A1";
         } else { // entity2
            return "A2";
         }
      case ALL: return this.getUnmappedString();
      }
      return null;
   }
   
   
   /**
    * Get all possible argument types according to the argument marker setting.
    * @return List of types that may be assigned.
    */
   public static ArgumentType[] getAllArgumentTypes () {
      
      switch (useArgumentMarker) {
      case ONE: return new ArgumentType[] {argument};
      case TWO: return new ArgumentType[] {entity1, aspect};
      case THRA: return new ArgumentType[] {entity1, aspect, sentiment};
      case THRE: return new ArgumentType[] {entity1, entity2, aspect};
      case ALL: return new ArgumentType[] {entity1, entity2, aspect, sentiment};
      }
      return null;
      
   }
       
     

   /**
    * List the mappings of all argument strings in an array.
    * This lists all values for what you get with 'getMappedString()'.
    * Mappings depend on the marker type set (default is all)
    * 
    * @return [mapping for aspect, mapping for entity1, mapping for entity2, mapping for sentiment]
    */
   public static String[] getMappedStrings() {
      ArgumentType[] myset = getAllArgumentTypes();
      String[] str = new String[myset.length];
      int i=0;
      for (ArgumentType a: myset) {
         str[i] = a.getMappedString();
         i++;
      }
      return str;
   }
   
   
   
};
