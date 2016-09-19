// (c) Wiltrud Kessler
// 27.01.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/


package de.uni_stuttgart.ims.nlpbase.nlp;

import java.util.HashSet;
import java.util.Set;


/**
 * Represents all information about one word in a sentence:
 * id, form, lemma, POS, deprel, head id, children, ...
 * 
 * @author kesslewd
 */
public class Word {
   


   private int id;
   private String form;
   private String lemma;
   private String pos;
   private String deprel;
   
   private int headID;
   private Word head;
   private Set<Word> children = new HashSet<Word>();
   
   private boolean isPredicate = false;
   private PredicateType type;
   private PredicateDirection direction;


   /**
    * Create a word with just a word form.
    * 
    * @param form surface word form.
    */
   public Word(String form){
      this(0, form, "", "", 0, "");
   }   

   /**
    * Create a word with all info.
    * 
    * @param id The ID of the word in the sentence (starting with 1).
    * @param form Surface word form.
    * @param lemma Lemma of the word.
    * @param POS Part-of-speech of the word.
    * @param headID ID of the head/parent, the word this is dependent of.
    * @param Deprel Dependency relation btw word and head.
    */
   public Word(int id, String form, String lemma, String POS, int headID, String Deprel){
      this.id = id;
      this.form=form;
      this.lemma=lemma;
      this.pos=POS;
      this.headID=headID;
      this.deprel = Deprel;
   }
   
   /**
    * Creates a word with the exact same informaion as the otherWord.
    * @param otherWord A different word.
    */
   public Word (Word otherWord) {
      this.id = otherWord.id;
      this.form=otherWord.form;
      this.lemma=otherWord.lemma;
      this.pos=otherWord.pos;
      this.deprel = otherWord.deprel;
      this.headID=otherWord.headID;
   }
   

   /*
    * Flat word attributes
    * Getter / setter
    */

   public String getForm() {
      return form;
   }
   public String getLemma() {
      return lemma;
   }
   public String getPOS() {
      return pos;
   }
   public int getId() {
      return id;
   }
   public String getDeprel() {
      return deprel;
   }
   public void setID (int id) {
      this.id = id;
   }
   

   /*
    * Dependency structure attributes
    * Getter / setter
    */


   public void setHead (Word head) {
      if (head != null) {
         this.head = head;
         this.headID = head.getId();
      }
   }

   public int getHeadId() {
      return headID;
   }
   public Word getHead() {
      return head;
   }

   public void addChild (Word child) {
      if (child != null)
         this.children.add(child);
   }
   
   public Set<Word> getDirectChildren(){
      return children;
   }

   
   /*
    * Marker
    */
   

   public void unmarkAsPredicate(){
      this.isPredicate=false;      
   }
   public void markAsPredicate(PredicateType type, PredicateDirection direction){
      this.isPredicate=true;
      this.type = type;      
      this.direction = direction;      
   }
   public boolean isPredicate(){
      return this.isPredicate;
   }
   public PredicateType getType() {
      return type;
   }
   public PredicateDirection getDirection() {
      return direction;
   }
   public String getPredicateAnnotation() {
      return "comparative.01"; // TODO!!!
   }
   
   

   /*
    * Compare
    */
   
   /**
    * Compare id and form.
    * @param otherWord A different word.
    * @return TRUE if they have the same ID and form, FALSE otherwise.
    */
   public boolean equals(Word otherWord) {
      if (otherWord == null)
         return false;
      return ((this.id == otherWord.id) && (this.form.equals(otherWord.form)));
   }
   
   /*
    * Output
    */
   
   /**
    * Converts this Word object one line with only basic information.
    * Form+" ("+id+","+Lemma+","+POS+")";
    * @return one line.
    */
   public String toString() {
      return form+" ("+id+","+lemma+","+pos+","+deprel+","+headID+")";
   }

   /**
    * Converts this Word to one line following the CoNLL 2009 format.
    * However, it does not include all columns, only dependencies.
    * Information is repeated, no real gold/predicted data.
    * @return one line.
    */
   public String toCoNLLString() {
      if (this.isPredicate)
         return form+"\t"+lemma+"\t"+lemma+"\t"+pos+"\t"+pos+"\t" + this.direction + "\t" + this.direction + "\t"+headID+"\t"+headID+"\t"+deprel+"\t"+deprel;
      else
         return form+"\t"+lemma+"\t"+lemma+"\t"+pos+"\t"+pos+"\t_\t_\t"+headID+"\t"+headID+"\t"+deprel+"\t"+deprel;
   }


   
}
