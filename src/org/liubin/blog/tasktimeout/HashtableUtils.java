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
}
