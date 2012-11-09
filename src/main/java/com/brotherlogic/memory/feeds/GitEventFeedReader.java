package com.brotherlogic.memory.feeds;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

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
      GitMemory mem = new GitMemory();
      mem.setProject(obj.getJSONObject("repo").getString("name"));
      mem.setBranch(obj.getJSONObject("payload").getString("ref"));
      try
      {
         mem.setTimestamp(obj.getString("created_at"), "yyyy-MM-dd'T'HH:mm:ss'Z'");
      }
      catch (ParseException e)
      {
         e.printStackTrace();
      }
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

      for (int i = 0; i < arr.length(); i++)
      {
         Memory mem = buildMemory(arr.getJSONObject(i));
         addObjectToRead(mem, arr.getJSONObject(i).toString());
      }

      // We only get one page of git events
      return -1;
   }
}
