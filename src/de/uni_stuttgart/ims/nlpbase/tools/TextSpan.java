// (c) Wiltrud Kessler
// 24.07.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.nlpbase.tools;

/**
 * Text span
 * from start of span.
 * to end end of span, which is +1 more than the last element in the span.
 * 
 * @author kesslewd
 *
 */
public class TextSpan {

   /**
    * Start index of span.
    */
   public int begin;

   /**
    * End index of span.
    */
   public int end;
   
   /**
    * Text covered by span.
    */
   public String coveredText;
   
   /**
    * Text span.
    * @param begin start of span.
    * @param end end of span, which is +1 more than the last element in the span.
    */
   public TextSpan (int begin, int end) {
      this.begin = begin;
      this.end = end;
   }

   /**
    * Text span.
    * @param begin start of span.
    * @param end end of span, which is +1 more than the last element in the span.
    * @param coveredText The text that this span covers.
    */
   public TextSpan (int begin, int end, String coveredText) {
      this.begin = begin;
      this.end = end;
      this.coveredText = coveredText; 
   }

   /**
    * Is this index inside the span?
    * @param index Element index.
    * @return True if index in [begin,end)
    */
   public boolean contains(int index) {
      return (index >= begin) && (index < end);
   }

   /**
    * Is this index inside the span?
    * @param begin  Element index.
    * @param end  Element index.
    * @return True if the span contains both begin and end.
    */
   public boolean contains (
         int begin, int end) {
      return (this.contains(begin) & this.contains(end));
   }
   
   /**
    * Get text in the String given as parameter that is covered by this span.
    * @param text Large text.
    * @return The substring from begin to end from the given text.
    */
   public String getCoveredText (String text) {
      return text.substring(begin, end);
   }
   
   /**
    * @return [begin, end)
    */
   public String toString() {
      return "[" + begin + ", " + end + ")";
   }
      
}
