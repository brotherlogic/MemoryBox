package com.brotherlogic.memory.feeds;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brotherlogic.memory.core.UntappdMemory;

/**
 * Feed Reader for Untappd
 * 
 * @author simon
 * 
 */
public class UntappdFeedReader extends JSONFeedReader
{
   /** Local logger */
   private static Logger logger = Logger
         .getLogger("com.brotherlogic.memory.feeds.UntappdFeedReader");

   /** The base URL for accessing the feed */
   private final String baseURL = "http://api.untappd.com/v4";

   /** The user name to pull the feed for */
   private final String username;

   /**
    * Constructor
    * 
    * @param user
    *           The username to build the feed for
    */
   public UntappdFeedReader(final String user)
   {
      username = user;
   }

   @Override
   protected URL getFeedURL(final long pagination) throws MalformedURLException
   {
      String urlText = baseURL + "/user/checkins/" + username;

      // Add in the client and secret stuff
      urlText += "?client_secret=" + Config.getParameter("untappd.secret") + "&client_id="
            + Config.getParameter("untappd.id");

      if (pagination > 0)
         urlText += "&max_id=" + pagination;

      return new URL(urlText);
   }

   @Override
   protected long processFeedText(final String text) throws JSONException
   {
      JSONObject obj = new JSONObject(text);

      // Read the next value - max_id is blank when we're done
      long nextVal = -1;
      if (obj.getJSONObject("response").getJSONObject("pagination").getString("max_id").length() > 0)
         nextVal = obj.getJSONObject("response").getJSONObject("pagination").getLong("max_id");

      JSONArray arr = obj.getJSONObject("response").getJSONObject("checkins").getJSONArray("items");
      logger.log(Level.INFO, "Read " + arr.length() + " objects");

      for (int i = 0; i < arr.length(); i++)
      {
         UntappdMemory mem = new UntappdMemory();
         mem.buildFromJSON(arr.getJSONObject(i));
         addObjectToRead(mem);
      }
      return nextVal;
   }
}
