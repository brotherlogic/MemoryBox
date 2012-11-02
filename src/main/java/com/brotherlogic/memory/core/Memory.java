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
   /** Used to process dates */
   private DateFormat df;

   /** The underlying timestamp for this memory (is unique) */
   private Long timestamp;

   /**
    * Version 2 added the ability to store the underlying representation
    */
   private final int version = 2;

   @Override
   public int compareTo(final Memory o)
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
    * Get the local version of this memory
    * 
    * @return the version number for the concrete memory
    */
   public abstract int getLocalVersion();

   /**
    * Get method for the timestamp
    * 
    * @return The time at which this memory occured
    */
   public final Long getTimestamp()
   {
      return timestamp;
   }

   /**
    * Get the version of this memory
    * 
    * @return The version number of this memmory
    */
   public int getVersion()
   {
      return version + getLocalVersion();
   }

   @Override
   public int hashCode()
   {
      return timestamp.hashCode();
   }

   /**
    * Set the local version number
    * 
    * @param localVersion
    *           The number to set
    */
   public abstract void setLocalVersion(Integer localVersion);

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

   /**
    * Set method for the timestamp
    * 
    * @param value
    *           The string to parse
    * @throws ParseException
    *            If we can't parse the date
    */
   public void setTimestamp(final String value) throws ParseException
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
