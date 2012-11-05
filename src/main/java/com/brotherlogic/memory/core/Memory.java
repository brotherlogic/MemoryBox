package com.brotherlogic.memory.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

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

   private final Logger logger = Logger.getLogger(this.getClass().getName());

   /** The underlying timestamp for this memory (is unique) */
   private Long timestamp;

   @Override
   public int compareTo(final Memory o)
   {
      return -timestamp.compareTo(o.timestamp);
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
    * Get method for the memory class
    * 
    * @return The name of this class
    */
   public String getMemoryClass()
   {
      return this.getClass().getName();
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

   @Override
   public int hashCode()
   {
      return timestamp.hashCode();
   }

   /**
    * Determines if this object is fully constructed
    * 
    * @return true if the object is fully built, false otherwise
    */
   public boolean isFilled()
   {
      boolean allFilled = true;

      // Get all the get methods and check that they're filled
      try
      {
         for (Method meth : this.getClass().getMethods())
            if (meth.getName().startsWith("get") && meth.getParameterTypes().length == 0)
               // Check that this isn't an annotation method
               if (meth.getAnnotation(Annotation.class) == null)
               {
                  Object obj = meth.invoke(this, new Object[0]);
                  if (obj == null)
                  {
                     logger.log(Level.INFO, meth.getName() + " is not filled for " + this);
                     allFilled = false;
                  }
               }
      }
      catch (IllegalAccessException e)
      {
         e.printStackTrace();
         allFilled = false;
      }
      catch (InvocationTargetException e)
      {
         e.printStackTrace();
         allFilled = false;
      }

      return allFilled;
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

   /**
    * Set method for the timestamp
    * 
    * @param value
    *           The string to parse
    * @throws ParseException
    *            If we can't parse the date
    */
   public void setTimestamp(final String value, String format) throws ParseException
   {
      df = new SimpleDateFormat(format);
      timestamp = df.parse(value).getTime();
   }

   @Override
   public String toString()
   {
      return "Memory: " + timestamp;
   }
}
