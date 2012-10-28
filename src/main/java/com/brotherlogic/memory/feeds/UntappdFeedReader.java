package com.brotherlogic.memory.feeds;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brotherlogic.memory.core.UntappdMemory;

public class UntappdFeedReader extends JSONFeedReader
{
   private static Logger logger = Logger
         .getLogger("com.brotherlogic.memory.feeds.UntappdFeedReader");

   public static void main(String[] args) throws Exception
   {
      UntappdFeedReader reader = new UntappdFeedReader("brotherlogic");
      System.out.println(reader.gatherObjects(25));
   }

   String baseURL = "http://api.untappd.com/v4";

   boolean checkedVersion = false;

   String username;

   public UntappdFeedReader(String user)
   {
      username = user;
   }

   @Override
   protected URL getFeedURL(long pagination) throws MalformedURLException
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
   protected long processFeedText(String text) throws JSONException
   {
      JSONObject obj = new JSONObject(text);
      long nextVal = obj.getJSONObject("response").getJSONObject("pagination").getLong("max_id");

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
