package org.liubin.blog.tasktimeout;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

public class HashtableUtils {

    /**
     * get all new elements from oldHashtable to newHashtable
     * 
     * @param oldHashtable
     * @param newHashtable
     * @return
     */
    public static Hashtable<Long, Long> getNewElements(
            Hashtable<Long, Long> oldHashtable,
            Hashtable<Long, Long> newHashtable) {

        Hashtable<Long, Long> newElements = new Hashtable<Long, Long>();

        Set<Entry<Long, Long>> entries = (Set<Entry<Long, Long>>) newHashtable
                .entrySet();

        for (Entry<Long, Long> e : entries) {
            if (!oldHashtable.containsKey(e.getKey())) {
                newElements.put(e.getKey(), e.getValue());
            }
        }
        return newElements;
    }

    /**
     * merge fromHashtable to toHashtable. if element not exist in toHashtable,
     * then put it. if exist,replace the value from fromHashtable.
     * 
     * @param fromHashtable
     * @param toHashtable
     * @return
     */
    public static Hashtable<Long, Long> merge(
            Hashtable<Long, Long> fromHashtable,
            Hashtable<Long, Long> toHashtable) {

        Set<Entry<Long, Long>> entries = (Set<Entry<Long, Long>>) fromHashtable
                .entrySet();

        for (Entry<Long, Long> e : entries) {
            if (toHashtable.containsKey(e.getKey())) {
                toHashtable.put(e.getKey(), e.getValue());
            }
        }
        return toHashtable;
    }
}
