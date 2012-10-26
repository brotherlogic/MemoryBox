package com.brotherlogic.memory.feeds;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brotherlogic.memory.core.UntappdMemory;

public class UntappdFeedReader extends JSONFeedReader
{
   String baseURL = "http://api.untappd.com/v4";
   String username;
   boolean checkedVersion = false;

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
         urlText += "&offset=" + pagination;

      return new URL(urlText);
   }

   @Override
   protected long processFeedText(String text) throws JSONException
   {
      JSONObject obj = new JSONObject(text);
      long nextVal = obj.getJSONObject("response").getJSONObject("pagination").getLong("max_id");

      JSONArray arr = obj.getJSONObject("response").getJSONObject("checkins").getJSONArray("items");
      for (int i = 0; i < arr.length(); i++)
      {
         UntappdMemory mem = new UntappdMemory();
         int version = mem.buildFromJSON(arr.getJSONObject(i));

         if (!checkedVersion && updateNeeded(version))
            requireUpdate();

         addObjectToRead(mem);
      }
      return nextVal;
   }

   private boolean updateNeeded(int readVersion)
   {
      return false;
   }

   public static void main(String[] args) throws Exception
   {
      UntappdFeedReader reader = new UntappdFeedReader("brotherlogic");
      reader.gatherObjects(25);
   }

}
