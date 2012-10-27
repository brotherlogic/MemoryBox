package com.brotherlogic.memory.db;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import com.brotherlogic.memory.core.Memory;

/**
 * Abstract class for dealing with the database
 * 
 * @author simon
 * 
 */
public abstract class DBInterface
{
   /**
    * Wipes the database
    * 
    * @throws IOException
    *            if the database can't be wiped
    */
   public abstract void clear() throws IOException;

   /**
    * Maps from the property name (accessible via get methods) to the
    * corresponding responsible class
    * 
    * @param mem
    *           The memory to derive
    * @return A {@link Map} from property name to the responsible class
    */
   protected final Map<String, Class<?>> deriveProperties(final Memory mem)
   {
      Map<String, Class<?>> strs = new TreeMap<String, Class<?>>();

      // Add all the interface methods
      for (Class<?> inter : mem.getClass().getInterfaces())
         for (Method meth : inter.getMethods())
            if (meth.getName().startsWith("get"))
               strs.put(getProperty(meth), inter);

      // Add all the class methods (excluding object methods)
      for (Method meth : mem.getClass().getMethods())
         if (meth.getName().startsWith("get") && !strs.containsKey(getProperty(meth)))
            if (meth.getDeclaringClass() != Object.class)
               strs.put(getProperty(meth), meth.getDeclaringClass());

      return strs;
   }

   /**
    * Gets a download queue associated with this database
    * 
    * @return a valid Download Queue
    */
   public abstract DownloadQueue getDownloadQueue();

   /**
    * Gets a value for a given property
    * 
    * @param property
    *           The property name to retrieve for
    * @param obj
    *           The object to get the property from
    * @return The value of the property
    * @throws IOException
    *            If something goes wrong
    */
   protected final Object getObject(final String property, final Object obj) throws IOException
   {
      try
      {
         Method meth = obj.getClass()
               .getMethod("get" + property.substring(0, 1).toUpperCase() + property.substring(1),
                     new Class[0]);
         Object retValue = meth.invoke(obj, new Object[0]);
         return retValue;
      }
      catch (NoSuchMethodException e)
      {
         throw new IOException(e);
      }
      catch (InvocationTargetException e)
      {
         throw new IOException(e);
      }
      catch (IllegalAccessException e)
      {
         throw new IOException(e);
      }
   }

   /**
    * Helper method to get a property from the Method
    * 
    * @param meth
    *           The Method to turn into a property
    * @return The property so getBlah -> blah
    */
   private String getProperty(final Method meth)
   {
      String mName = meth.getName();
      return mName.substring("get".length(), "get".length() + 1).toLowerCase()
            + mName.substring("get".length() + 1);
   }

   /**
    * Method to retreive the latest memory from the database
    * 
    * @param cls
    *           The class type of memory to retrieve from
    * @return The latest memory we have stored
    */
   public abstract Memory retrieveLatestMemory(Class<?> cls);

   /**
    * Retrieve a memory from the database
    * 
    * @param timestamp
    *           The timestamp of the memory
    * @param className
    *           The type of memory to retrieve
    * @return A built memory object
    * @throws IOException
    *            If something goes wrong with the db
    */
   public abstract Memory retrieveMemory(long timestamp, String className) throws IOException;

   /**
    * Sets a property on a given memory
    * 
    * @param obj
    *           The object to run on
    * @param propName
    *           The property to set
    * @param value
    *           The value to set
    * @throws IOException
    *            If something goes wrong with DB access
    */
   protected final void setProperty(final Memory obj, final String propName, final Object value)
         throws IOException
   {
      try
      {
         Method setMethod = obj.getClass().getMethod(
               "set" + propName.substring(0, 1).toUpperCase() + propName.substring(1), new Class[]
               { value.getClass() });
         setMethod.invoke(obj, new Object[]
         { value });
      }
      catch (NoSuchMethodException e)
      {
         throw new IOException(e);
      }
      catch (InvocationTargetException e)
      {
         throw new IOException(e);
      }
      catch (IllegalAccessException e)
      {
         throw new IOException(e);
      }
   }

   /**
    * Stores a memory in the DB
    * 
    * @param mem
    *           The memory to be store
    * @throws IOException
    *            If there's a db error
    */
   public abstract void storeMemory(final Memory mem) throws IOException;
}
