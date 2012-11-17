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

public final class Config
{
   private static Config singleton;

   public static Config getConfig(String baseAddresss)
   {
      if (singleton == null)
         singleton = new Config(baseAddresss);
      return singleton;
   }

   public static void main(String[] args) throws IOException
   {
      File f = new File("etc/config.txt");
      Config.getConfig("http://edip:8085/configstore/").loadConfig(f);
   }

   String baseAddress;

   DefaultHttpClient httpClient = new DefaultHttpClient();

   private Config(String base)
   {
      baseAddress = base;
   }

   private byte[] get(String key) throws IOException
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

   public String getParameter(String key)
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

   public void loadConfig(File f) throws IOException
   {
      BufferedReader reader = new BufferedReader(new FileReader(f));
      for (String line = reader.readLine(); line != null; line = reader.readLine())
      {
         String[] elems = line.trim().split("\\|");
         Config.getConfig("http://edip:8085/configstore/").setParameter(elems[0], elems[1]);
      }
      reader.close();
   }

   public Object retrieveObject(String key) throws IOException
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

   public void setParameter(String key, String value)
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

   private void store(String key, byte[] value) throws IOException
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

   public void storeObject(String key, Object o) throws IOException
   {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(o);
      store(key, baos.toByteArray());
      oos.close();
   }
}
