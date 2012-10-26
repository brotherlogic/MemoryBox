package com.brotherlogic.memory.core;

/**
 * Abstract representation of a Memory
 * 
 * @author simon
 * 
 */
public abstract class Memory
{
   /** The underlying timestamp for this memory (is unique) */
   private Long timestamp;

   /** The version of this memory - should iterate on changes */
   private final int version = 1;

   /**
    * Get method for the version number
    * 
    * @return The version number of this memory
    */
   public int getVersion()
   {
      return version;
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

   @Override
   public int hashCode()
   {
      return timestamp.hashCode();
   }

   @Override
   public boolean equals(final Object o)
   {
      if (!(o instanceof Memory))
         return false;
      Memory other = (Memory) o;

      return other.timestamp.equals(timestamp);
   }

   @Override
   public String toString()
   {
      return "Memory: " + timestamp;
   }
}
