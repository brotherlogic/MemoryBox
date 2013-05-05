package com.brotherlogic.memory.feeds;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Central place for handling configuration
 * 
 * @author simon
 * 
 */
public final class Config
{
   /** Singleton represetnation */
   private static Config singleton;

   /**
    * Static method for getting config
    * 
    * @param baseAddresss
    *           The base address for accessing the config server
    * @return A valid Config system
    */
   public static Config getConfig()
   {
      if (singleton == null)
         singleton = new Config();
      return singleton;
   }

   /**
    * Blocking constructor
    * 
    * @param base
    *           The base address of the config server
    */
   private Config()
   {
   }

   /**
    * Gets the byte array for a given key
    * 
    * @param key
    *           THe key to access
    * @return The byte[] array
    * @throws IOException
    *            if something goes wrong
    */
   private byte[] get(final String key)
   {
      return System.getenv(key).getBytes();
   }

   /**
    * Gets a string result from a given parameter
    * 
    * @param key
    *           The key to retrieve
    * @return The value represented by this key or null
    */
   public String getParameter(final String key)
   {
      return new String(get(key));
   }

   /**
    * Gets an object from the config system
    * 
    * @param key
    *           THe key to retrieve on
    * @return A valid object that the key represents
    * @throws IOException
    *            If something goes wrong reading
    */
   public Object retrieveObject(final String key) throws IOException
   {
      try
      {
         byte[] arr = get(key);
         if (arr.length == 0)
            return null;
         ByteArrayInputStream bais = new ByteArrayInputStream(arr);
         ObjectInputStream ois = new ObjectInputStream(bais);
         System.out.println("Reading object");
         Object o = ois.readObject();
         ois.close();
         return o;
      }
      catch (ClassNotFoundException e)
      {
         throw new IOException(e);
      }
   }
}
