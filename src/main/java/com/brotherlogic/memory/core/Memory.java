package com.brotherlogic.memory.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Abstract representation of a Memory
 * 
 * @author simon
 * 
 */
public abstract class Memory implements Comparable<Memory>
{
   DateFormat df;

   /** The underlying timestamp for this memory (is unique) */
   private Long timestamp;

   int version = 1;

   @Override
   public int compareTo(Memory o)
   {
      return timestamp.compareTo(o.timestamp);
   }

   @Override
   public boolean equals(final Object o)
   {
      if (!(o instanceof Memory))
         return false;
      Memory other = (Memory) o;

      return other.timestamp.equals(timestamp);
   }

   /**
    * Get method for the timestamp
    * 
    * @return The time at which this memory occured
    */
   public final Long getTimestamp()
   {
      return timestamp;
   }

   public int getVersion()
   {
      return version;
   }

   @Override
   public int hashCode()
   {
      return timestamp.hashCode();
   }

   /**
    * Set method for the timestamp
    * 
    * @param value
    *           The time at which this memory occured
    */
   public final void setTimestamp(final Long value)
   {
      timestamp = value;
   }

   public void setTimestamp(String value) throws ParseException
   {
      if (df == null)
         df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
      timestamp = df.parse(value).getTime();
   }

   @Override
   public String toString()
   {
      return "Memory: " + timestamp;
   }
}
