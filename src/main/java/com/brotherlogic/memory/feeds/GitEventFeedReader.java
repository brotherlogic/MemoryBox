package com.brotherlogic.memory.feeds;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brotherlogic.memory.core.GitMemory;
import com.brotherlogic.memory.core.Memory;

public class GitEventFeedReader extends JSONFeedReader
{
   String username;

   public GitEventFeedReader(String user)
   {
      username = user;
   }

   @Override
   protected Memory buildMemory(String json) throws JSONException
   {
      GitMemory mem = new GitMemory();
      mem.buildFromJSON(new JSONObject(json));
      return mem;
   }

   @Override
   protected String getClassName()
   {
      return GitMemory.class.getName();
   }

   @Override
   protected URL getFeedURL(long pagination) throws MalformedURLException
   {
      return new URL("https://api.github.com/users/" + username + "/events");
   }

   @Override
   protected long processFeedText(String text) throws JSONException
   {
      JSONArray arr = new JSONArray(text);

      for (int i = 0; i < arr.length(); i++)
      {
         GitMemory mem = new GitMemory();
         mem.buildFromJSON(arr.getJSONObject(i));
         addObjectToRead(mem, arr.getJSONObject(i).toString());
      }

      // We only get one page of git events
      return -1;
   }

}
