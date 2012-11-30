package com.brotherlogic.memory.db;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.brotherlogic.memory.core.Annotation;
import com.brotherlogic.memory.core.DiscogsMemory;
import com.brotherlogic.memory.core.Memory;
import com.brotherlogic.memory.feeds.FeedReader;

/**
 * Abstract class for dealing with the database
 * 
 * @author simon
 * 
 */
public abstract class DBInterface
{
   /** The logger to display output */
   private static Logger logger = Logger.getLogger("com.brotherlogic.memory.db.DBInterface");

   public static void main(String[] args)
   {
      DiscogsMemory mem = new DiscogsMemory();
      System.out.println(mem.getReleaseOrder());
   }

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
    * Registers that we are following a given memory
    * 
    * @param memToFollow
    *           The type of memory to follow
    * @param memoryReader
    *           The reader that can update and process the memeory
    * @param param
    *           A paramter passed to the constructor of the reader class
    * @throws IOException
    *            If something goes wrong
    */
   public abstract void followMemory(Class<?> memToFollow, Class<?> memoryReader, String param)
         throws IOException;

   /**
    * Get which of the specified properties are annotated properties
    * 
    * @param properties
    *           A {@link Map} between the property name and the underlying class
    * @return A {@link Collection} of Properties which are annotated rather than
    *         inherent
    */
   protected final Collection<String> getAnnotatedProps(final Map<String, Class<?>> properties)
   {
      Collection<String> props = new LinkedList<String>();

      for (Entry<String, Class<?>> prop : properties.entrySet())
         try
         {
            Method getMethod = prop.getValue().getMethod(
                  "get" + prop.getKey().substring(0, 1).toUpperCase() + prop.getKey().substring(1),
                  new Class[0]);
            if (getMethod.getAnnotation(Annotation.class) != null)
               props.add(prop.getKey());
         }
         catch (NoSuchMethodException e)
         {
            e.printStackTrace();
         }

      return props;
   }

   /**
    * Gets a download queue associated with this database
    * 
    * @return a valid Download Queue
    */
   public abstract DownloadQueue getDownloadQueue();

   /**
    * Gets all the memory readers present in the system
    * 
    * @return A {@link Collection} of {@link FeedReader}s
    * @throws IOException
    *            If we can't access the database
    */
   public abstract Collection<FeedReader> getMemoryReaders() throws IOException;

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
         logger.log(Level.INFO, "Invoking " + meth + " on " + obj.getClass());
         Object retValue = meth.invoke(obj, new Object[0]);
         logger.log(Level.INFO, "Got: " + retValue);
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
    * @throws IOException
    *            if something goes wrong with retrieval
    */
   public abstract Memory retrieveLatestMemory(Class<?> cls) throws IOException;

   /**
    * Retrieve the memories for a given day
    * 
    * @param day
    *           The Calendar representation for the day (time is ignored)
    * @param className
    *           The name of Memory classes to retrieve
    * @return A Collection of memorys of the given class that were made on the
    *         given day
    * @throws IOException
    *            If something goes wrong when we read
    */
   public abstract Collection<Memory> retrieveMemories(Calendar day, String className)
         throws IOException;

   /**
    * Retrieve all the memories for the given class
    * 
    * @param className
    *           The Class of memories to retrieve
    * @return A collection of classes for the given memory
    * @throws IOException
    *            If something goes wrong
    */
   public abstract Collection<Memory> retrieveMemories(Class<?> className) throws IOException;

   /**
    * Retrieve a memory from the database
    * 
    * @param uid
    *           The unique ID of the memory
    * @param className
    *           The type of memory to retrieve
    * @return A built memory object
    * @throws IOException
    *            If something goes wrong with the db
    */
   public abstract Memory retrieveMemory(String uid, String className) throws IOException;

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
      logger.log(Level.INFO, "Logging " + obj + " given " + propName + " and " + value);

      // Do nothing if we don't have a valid value to work with
      if (value == null)
         return;

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
    * Stores a memory in the DB - will update if memory already exists!
    * 
    * @param mem
    *           The memory to be store
    * @throws IOException
    *            If there's a db error
    */
   public abstract void storeMemory(final Memory mem) throws IOException;
}
