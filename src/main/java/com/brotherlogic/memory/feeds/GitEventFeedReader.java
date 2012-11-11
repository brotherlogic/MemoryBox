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

import com.brotherlogic.memory.core.GitMemory;
import com.brotherlogic.memory.core.Memory;

/**
 * Reads git events and converts them to memories
 * 
 * @author simon
 * 
 */
public class GitEventFeedReader extends JSONFeedReader
{
   DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

   Logger logger = Logger.getLogger(this.getClassName());

   /** The username to retrieve for */
   private final String username;

   /**
    * Constructor
    * 
    * @param user
    *           The name of the user to read
    */
   public GitEventFeedReader(final String user)
   {
      username = user;
   }

   @Override
   protected Memory buildMemory(final JSONObject obj) throws JSONException
   {
      logger.log(Level.INFO, "Building memory");
      GitMemory mem = new GitMemory();
      mem.setProject(obj.getJSONObject("repo").getString("name"));
      mem.setBranch(obj.getJSONObject("payload").getString("ref"));
      mem.setUniqueID(obj.getString("id"));
      try
      {
         mem.setTimestamp(df.parse(obj.getString("created_at")).getTime());
      }
      catch (ParseException e)
      {
         e.printStackTrace();
      }

      logger.log(Level.INFO, "Returning");

      return mem;
   }

   @Override
   protected String getClassName()
   {
      return GitMemory.class.getName();
   }

   @Override
   protected URL getFeedURL(final long pagination) throws MalformedURLException
   {
      return new URL("https://api.github.com/users/" + username + "/events");
   }

   @Override
   protected void login()
   {
      // TODO Auto-generated method stub

   }

   @Override
   protected long processFeedText(final String text) throws JSONException
   {
      JSONArray arr = new JSONArray(text);

      logger.log(Level.INFO, "To process " + arr.length());
      for (int i = 0; i < arr.length(); i++)
      {
         logger.log(Level.INFO, "Proc: " + arr.getJSONObject(i));
         Memory mem = buildMemory(arr.getJSONObject(i));
         logger.log(Level.INFO, "Adding: " + mem);
         addObjectToRead(mem, arr.getJSONObject(i).toString());
         logger.log(Level.INFO, "Added");
      }

      logger.log(Level.INFO, "Done");

      // We only get one page of git events
      return -1;
   }
}
