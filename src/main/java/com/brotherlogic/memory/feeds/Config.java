package com.brotherlogic.memory.feeds;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

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
   public static Config getConfig(final String baseAddresss)
   {
      if (singleton == null)
         singleton = new Config(baseAddresss);
      return singleton;
   }

   /** THe base address of the config server */
   private final String baseAddress;

   /** The http client used for accessing web content */
   private final DefaultHttpClient httpClient = new DefaultHttpClient();

   /**
    * Blocking constructor
    * 
    * @param base
    *           The base address of the config server
    */
   private Config(final String base)
   {
      baseAddress = base;
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
   private byte[] get(final String key) throws IOException
   {
      try
      {
         HttpGet getRequest = new HttpGet(baseAddress + "store?key=" + key);
         HttpResponse resp = httpClient.execute(getRequest);
         HttpEntity ent = resp.getEntity();
         InputStream is = ent.getContent();

         // Maximum 1Mb file
         byte[] buffer = new byte[1024 * 1024];
         int read = is.read(buffer);
         is.close();

         if (read > 0)
         {
            byte[] ret = new byte[read];
            for (int i = 0; i < ret.length; i++)
               ret[i] = buffer[i];
            return ret;
         }

         return new byte[0];
      }
      catch (URISyntaxException e)
      {
         throw new IOException(e);
      }
      catch (HttpException e)
      {
         throw new IOException(e);
      }
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
      try
      {
         return new String(get(key));
      }
      catch (IOException e)
      {
         return "";
      }
   }

   /**
    * Loads a config file into the server
    * 
    * @param f
    *           The file to load
    * @throws IOException
    *            If something goes wrong in accessing the server
    */
   public void loadConfig(final File f) throws IOException
   {
      BufferedReader reader = new BufferedReader(new FileReader(f));
      for (String line = reader.readLine(); line != null; line = reader.readLine())
      {
         String[] elems = line.trim().split("\\|");
         Config.getConfig("http://edip:8085/configstore/").setParameter(elems[0], elems[1]);
      }
      reader.close();
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

   /**
    * Sets a parameter within the config system
    * 
    * @param key
    *           The key to store the value under
    * @param value
    *           The value to be stored
    */
   public void setParameter(final String key, final String value)
   {
      try
      {
         store(key, value.getBytes());
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   /**
    * Stores a byte array in the config sytem
    * 
    * @param key
    *           The key to retrieve from
    * @param value
    *           The byte array value to store
    * @throws IOException
    *            If something goes wrong
    */
   private void store(final String key, final byte[] value) throws IOException
   {
      try
      {
         HttpPut putRequest = new HttpPut(baseAddress + "store?key=" + key);
         ByteArrayEntity input = new ByteArrayEntity(value);
         putRequest.setEntity(input);
         httpClient.execute(putRequest);
      }
      catch (URISyntaxException e)
      {
         throw new IOException(e);
      }
      catch (HttpException e)
      {
         throw new IOException(e);
      }
   }

   /**
    * Stores an object within the config system
    * 
    * @param key
    *           The key to store
    * @param o
    *           The object to store
    * @throws IOException
    *            If we can't reach the config serer
    */
   public void storeObject(final String key, final Object o) throws IOException
   {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(o);
      store(key, baos.toByteArray());
      oos.close();
   }
}
