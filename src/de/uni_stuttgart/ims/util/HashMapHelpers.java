// (c) Wiltrud Kessler
// 21.08.2013
// This code is distributed under a Creative Commons
// Attribution-NonCommercial-ShareAlike 3.0 Unported license 
// http://creativecommons.org/licenses/by-nc-sa/3.0/



package de.uni_stuttgart.ims.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;



/**
 * Collection of helper functions that have something to do with hash-maps
 * (sorting them, inserting stuff) 
 * 
 * @author kesslewd
 *
 */
public class HashMapHelpers {

      
   /**
    * Sort a HashMap with KEY-VALUE pairs by VALUE (ascending).
    * @param theMap What you want to sort.
    * @param <K> The data type of the key.
    * @param <V> The data type of the value (something that you can sort by).
    * @return The same HashMap, just sorted.
    */
   public static <K, V extends Comparable<V>> List<Entry<K, V>> sortHashMapByValueAscending (HashMap<K, V> theMap) {
      List<Entry<K, V>> convertedToList = new ArrayList<Entry<K, V>>(theMap.entrySet());
      Collections.sort(convertedToList,
             new Comparator<Entry<K, V>>() {
               @Override
               public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                  return o1.getValue().compareTo(o2.getValue());
               }
          });
      return convertedToList;
   }
   

   /**
    * Sort a HashMap KEY-VALUE pairs by VALUE (descending).
    * @param theMap What you want to sort.
    * @param <K> The data type of the key.
    * @param <V> The data type of the value (something that you can sort by).
    * @return The same HashMap, just sorted.
    */
   public static <K, V extends Comparable<V>> List<Entry<K, V>> sortHashMapByValueDescending (HashMap<K, V> theMap) {
      List<Entry<K, V>> convertedToList = new ArrayList<Entry<K, V>>(theMap.entrySet());
      Collections.sort(convertedToList,
             new Comparator<Entry<K, V>>() {
               @Override
               public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                  return o2.getValue().compareTo(o1.getValue());
               }
          });
      return convertedToList;
   }
   


   /**
    * Sort a HashMap KEY-LIST pairs by length of LIST (ascending).
    * @param theMap What you want to sort.
    * @param <K> The data type of the key.
    * @param <V> The data type of the value.
    * @return The same HashMap, just sorted.
    */
   public static <K, V> List<Entry<K, List<V>>> sortHashMapByListSizeAscending (HashMap<K, List<V>> theMap) {
      List<Entry<K, List<V>>> convertedToList = new ArrayList<Entry<K, List<V>>>(theMap.entrySet());
      Collections.sort(convertedToList,
             new Comparator<Entry<K, List<V>>>() {
               @Override
               public int compare(Entry<K, List<V>> o1, Entry<K, List<V>> o2) {
                  return o1.getValue().size() - o2.getValue().size();
               }
          });
      return convertedToList;
   }


   /**
    * Sort a HashMap KEY-LIST pairs by length of LIST (descending).
    * @param theMap What you want to sort.
    * @param <K> The data type of the key.
    * @param <V> The data type of the value.
    * @return The same HashMap, just sorted.
    */
   public static <K, V> List<Entry<K, List<V>>> sortHashMapByListSizeDescending (HashMap<K, List<V>> theMap) {
      List<Entry<K, List<V>>> convertedToList = new ArrayList<Entry<K, List<V>>>(theMap.entrySet());
      Collections.sort(convertedToList,
             new Comparator<Entry<K, List<V>>>() {
               @Override
               public int compare(Entry<K, List<V>> o1, Entry<K, List<V>> o2) {
                  return o2.getValue().size() - o1.getValue().size();
               }
          });
      return convertedToList;
   }
   
   
   public static class Pair<X, Y> { 
      public X x; 
      public Y y; 
      public Pair(X x, Y y) { 
          this.x = x; 
          this.y = y; 
      }

      @Override
      public String toString() {
          return "(" + x + "," + y + ")";
      }
   }
   

   public static class Triple<X, Y, Z> { 
      public X x; 
      public Y y; 
      public Z z; 
      public Triple(X x, Y y, Z z) { 
          this.x = x; 
          this.y = y; 
          this.z = z; 
      }

      @Override
      public String toString() {
          return "(" + x + "," + y + "," + z + ")";
      }
   }
   
   
   

   /**
    * If the key is in the map, add the addValue to the value already in there,
    * if it is not, add a new element with the given key and the addValue as value.
    * 
    * @param map The map we want to add the stuff to.
    * @param key The key for the thing to be added.
    * @param addValue Value to be added to the existing value.
    */
   public static void addOrCreate (HashMap<String, Integer> map, String key, int addValue) {
      Integer value = map.get(key);
      if (value == null)
         map.put(key, addValue);
      else
         map.put(key, value+addValue);      
   }
   

   /**
    * If the key is in the map, add the addValue to the value already in there,
    * if it is not, add a new element with the given key and the addValue as value.
    * 
    * @param map The map we want to add the stuff to.
    * @param key The key for the thing to be added.
    * @param addValue Value to be added to the existing value.
    */
   public static void addOrCreate (HashMap<String, Double> map, String key, double addValue) {
      Double value = map.get(key);
      if (value == null)
         map.put(key, addValue);
      else
         map.put(key, value+addValue);      
   }
   

   /**
    * If the key is in the map, add the addValue to the list value already in there,
    * if it is not, add a new element with the given key and the addValue as new entry in the list.
    * 
    * @param map The map we want to add the stuff to.
    * @param key The key for the thing to be added.
    * @param <T> The data type of the value.
    * @param entry Value to be added to the existing value.
    */
   public static <T> void addOrCreate (HashMap<String, TreeSet<T>> map, String key, T entry) {
      TreeSet<T> value = map.get(key);
      if (value == null) {
         value = new TreeSet<T>();
         value.add(entry);
         map.put(key, value);
      }
      else
         value.add(entry);
   }
   
   
}
