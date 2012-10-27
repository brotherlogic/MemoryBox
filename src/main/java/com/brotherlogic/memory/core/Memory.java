package com.brotherlogic.memory.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public abstract class Memory
{
   int version = 1;
   Long timestamp;
   DateFormat df;

   public int getVersion()
   {
      return version;
   }

   public Long getTimestamp()
   {
      return timestamp;
   }

   public void setTimestamp(Long value)
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
