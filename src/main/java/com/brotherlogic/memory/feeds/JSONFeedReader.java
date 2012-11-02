package com.brotherlogic.memory.feeds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import com.brotherlogic.memory.core.JSONConstructable;
import com.brotherlogic.memory.core.Memory;
import com.brotherlogic.memory.db.DBFactory;

public abstract class JSONFeedReader extends FeedReader
{
   private static Logger logger = Logger.getLogger("com.brotherlogic.memory.feeds.JSONFeedReader");

   Map<Memory, String> baseRepCache = new TreeMap<Memory, String>();

   private final Stack<JSONConstructable> readObjects = new Stack<JSONConstructable>();

   boolean updateRequired = false;

   protected void addObjectToRead(JSONConstructable cons, String rep)
   {
      baseRepCache.put((Memory) cons, rep);
      readObjects.add(cons);
   }

   protected Collection<JSONConstructable> gatherObjects(int number) throws IOException,
         JSONException
   {
      List<JSONConstructable> objects = new LinkedList<JSONConstructable>();

      long pagination = -1;
      while (objects.size() < number || updateRequired)
      {
         // Pull the feed and build the objects
         String feedText = read(getFeedURL(pagination));
         long nextPage = processFeedText(feedText);
         objects.addAll(popReadObjects());

         if (nextPage < 0)
            break;
         else
            pagination = nextPage;
      }

      return objects;
   }

   /**
    * Gets the URL for the given feed - pagination if necessary (ignored if < 0)
    * 
    * @param pagination
    *           The page/value to paginate
    * @return The URL for the feed
    */
   protected abstract URL getFeedURL(long pagination) throws MalformedURLException;

   @Override
   public String getUnderlyingRepresentation(Memory mem) throws IOException
   {
      if (baseRepCache.containsKey(mem))
         return baseRepCache.get(mem);
      return null;
   }

   protected void noUpdate()
   {
      updateRequired = false;
   }

   private Collection<JSONConstructable> popReadObjects()
   {
      List<JSONConstructable> consList = new LinkedList<JSONConstructable>();
      while (readObjects.size() > 0)
         consList.add(readObjects.pop());
      return consList;
   }

   @Override
   public Memory probeFeed() throws IOException
   {
      try
      {
         Collection<JSONConstructable> cons = gatherObjects(1);
         List<Memory> memorys = new LinkedList<Memory>();
         for (JSONConstructable con : cons)
            memorys.add((Memory) con);
         Collections.sort(memorys);
         return memorys.get(0);
      }
      catch (JSONException e)
      {
         throw new IOException(e);
      }
   }

   protected abstract long processFeedText(String text) throws JSONException;

   private String read(URL urlToRead) throws IOException
   {
      logger.log(Level.INFO, "Reading " + urlToRead);
      StringBuffer readText = new StringBuffer();

      BufferedReader reader = new BufferedReader(new InputStreamReader(urlToRead.openStream()));
      for (String line = reader.readLine(); line != null; line = reader.readLine())
         readText.append(line);
      reader.close();

      logger.log(Level.INFO, "Got " + readText);
      return readText.toString();
   }

   @Override
   public void updateAllMemories() throws IOException
   {
      logger.log(Level.INFO, "Updating all memories");

      updateRequired = true;
      try
      {
         Collection<JSONConstructable> objects = gatherObjects(Integer.MAX_VALUE);
         for (JSONConstructable obj : objects)
         {
            // Store the memory and the underlying representation
            Memory mem = (Memory) obj;
            DBFactory.buildInterface().storeMemory(mem);
            DBFactory.buildBaseRepStore().storeBaseRep(mem, getUnderlyingRepresentation(mem));
         }
      }
      catch (JSONException e)
      {
         throw new IOException(e);
      }
   }

   @Override
   public void updateMemories(long timestamp) throws IOException
   {
      try
      {
         List<JSONConstructable> objects = new LinkedList<JSONConstructable>();

         long pagination = -1;
         boolean timestampOver = false;
         while (!timestampOver)
         {
            // Pull the feed and build the objects
            String feedText = read(getFeedURL(pagination));
            long nextPage = processFeedText(feedText);
            for (JSONConstructable obj : popReadObjects())
               if (((Memory) obj).getTimestamp() > timestamp)
                  objects.add(obj);
               else
                  timestampOver = true;

            if (nextPage < 0)
               break;
            else
               pagination = nextPage;
         }

         // Update all the objects
         for (JSONConstructable obj : objects)
         {
            Memory m = (Memory) obj;
            DBFactory.buildInterface().storeMemory(m);
         }
      }
      catch (JSONException e)
      {
         e.printStackTrace();
      }

   }
}
