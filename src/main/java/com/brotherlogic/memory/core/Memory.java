package com.brotherlogic.memory.core;

public abstract class Memory
{
   Long timestamp;

   public Long getTimestamp()
   {
      return timestamp;
   }

   public void setTimestamp(Long value)
   {
      timestamp = value;
   }

   @Override
   public boolean equals(Object o)
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
