package com.brotherlogic.memory.core;

/**
 * A timed memory
 * 
 * @author simon
 * 
 */
public interface TimedMemory
{
   /**
    * Get the timestamp
    * 
    * @return The timestamp as {@link Long} i.e. timeInMillis()
    */
   Long getTimestamp();

   /**
    * Set the timestamp
    * 
    * @param timestamp
    *           The timestamp as Long (ms)
    */
   void setTimestamp(Long timestamp);
}
