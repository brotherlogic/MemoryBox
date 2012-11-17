package com.brotherlogic.memory.feeds;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brotherlogic.memory.core.Memory;
import com.brotherlogic.memory.core.UntappdMemory;
import com.brotherlogic.memory.db.DBFactory;

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

   /** Date format for the untappd feed reader */
   private final DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

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
   protected Memory buildMemory(final JSONObject json) throws JSONException
   {
      UntappdMemory mem = new UntappdMemory();
      try
      {
         mem.setTimestamp(df.parse(json.getString("created_at")).getTime());
         mem.setBeerName(json.getJSONObject("beer").getString("beer_name"));
         mem.setAbv(json.getJSONObject("beer").getDouble("beer_abv"));
         mem.setBreweryName(json.getJSONObject("brewery").getString("brewery_name"));
         mem.setUniqueID(json.getString("checkin_id"));

         // Get the largest image file that this references
         if (json.getJSONObject("media").getJSONArray("items").length() > 0)
         {
            String largeURL = json.getJSONObject("media").getJSONArray("items").getJSONObject(0)
                  .getJSONObject("photo").getString("photo_img_og");
            mem.setImagePath(DBFactory.buildInterface().getDownloadQueue()
                  .download(new URL(largeURL)));
         }
         else
            mem.setImagePath("NONE");
      }
      catch (ParseException e)
      {
         System.err.println("Cannot parse: " + json.getString("created_at"));
      }
      catch (MalformedURLException e)
      {
         System.err.println("Cannot urlise: "
               + json.getJSONObject("media").getJSONObject("photo").getString("photo_img_og"));
      }

      return mem;
   }

   @Override
   protected final String getClassName()
   {
      return UntappdMemory.class.getName();
   }

   @Override
   protected URL getFeedURL(final long pagination) throws MalformedURLException
   {
      String urlText = baseURL + "/user/checkins/" + username;

      // Add in the client and secret stuff
      urlText += "?client_secret="
            + Config.getConfig("http://edip:8085/configstore/").getParameter("untappd.secret")
            + "&client_id="
            + Config.getConfig("http://edip:8085/configstore/").getParameter("untappd.id");

      if (pagination > 0)
         urlText += "&max_id=" + pagination;

      return new URL(urlText);
   }

   @Override
   protected void login()
   {
      // TODO Auto-generated method stub

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
         Memory mem = buildMemory(arr.getJSONObject(i));
         addObjectToRead(mem, arr.getJSONObject(i).toString());
      }
      return nextVal;
   }
}
