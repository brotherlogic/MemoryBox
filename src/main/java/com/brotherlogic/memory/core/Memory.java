package com.brotherlogic.memory.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
   /** Used to log output */
   private final Logger logger = Logger.getLogger(this.getClass().getName());

   /** All memories can timestamp */
   private Long timestamp;

   /** A means of identifying the memory in some way */
   private String uniqueID;

   @Override
   public int compareTo(Memory o)
   {
      if (timestamp != null)
         return timestamp.compareTo(o.timestamp);
      else
         return uniqueID.compareTo(o.uniqueID);
   }

   @Override
   public boolean equals(Object obj)
   {
      if (!(obj instanceof Memory))
         return false;

      if (!obj.getClass().equals(this.getClass()))
         return false;

      System.out.println(uniqueID + " and " + ((Memory) obj).getUniqueID());
      return (uniqueID.equals(((Memory) obj).uniqueID));
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

   public Long getTimestamp()
   {
      return timestamp;
   }

   /**
    * Get method for the unique ID
    * 
    * @return An identifier for the memory
    */
   public String getUniqueID()
   {
      return uniqueID;
   }

   @Override
   public int hashCode()
   {
      return uniqueID.hashCode();
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
                     logger.log(Level.WARNING, meth.getName() + " is not filled for " + this);
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

   public void setTimestamp(Long timestamp)
   {
      this.timestamp = timestamp;
   }

   /**
    * Set method for the unique ID
    * 
    * @param id
    *           A String which defines the memory
    */
   public void setUniqueID(final String id)
   {
      this.uniqueID = id;
   }

   @Override
   public String toString()
   {
      return "Memory: " + uniqueID;
   }
}
