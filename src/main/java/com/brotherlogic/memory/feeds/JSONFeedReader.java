package com.brotherlogic.memory.feeds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import com.brotherlogic.memory.core.JSONConstructable;

public abstract class JSONFeedReader
{
   private static Logger logger = Logger.getLogger("com.brotherlogic.memory.feeds.JSONFeedReader");

   /**
    * Gets the URL for the given feed - pagination if necessary (ignored if < 0)
    * 
    * @param pagination
    *           The page/value to paginate
    * @return The URL for the feed
    */
   protected abstract URL getFeedURL(long pagination) throws MalformedURLException;

   protected abstract long processFeedText(String text) throws JSONException;

   private final Stack<JSONConstructable> readObjects = new Stack<JSONConstructable>();

   boolean updateRequired = true;

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

   private String read(URL urlToRead) throws IOException
   {
      logger.log(Level.INFO, "Reading " + urlToRead);
      StringBuffer readText = new StringBuffer();

      BufferedReader reader = new BufferedReader(new InputStreamReader(urlToRead.openStream()));
      for (String line = reader.readLine(); line != null; line = reader.readLine())
         readText.append(line);
      reader.close();

      return readText.toString();
   }

   protected void addObjectToRead(JSONConstructable cons)
   {
      readObjects.add(cons);
   }

   public Collection<JSONConstructable> gatherObjects(int number) throws IOException, JSONException
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
}
