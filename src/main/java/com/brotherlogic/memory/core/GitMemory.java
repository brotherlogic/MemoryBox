package com.brotherlogic.memory.core;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class GitMemory extends Memory implements JSONConstructable
{
   private String branch;

   private String project;

   @Override
   public void buildFromJSON(JSONObject obj) throws JSONException
   {
      project = obj.getJSONObject("repo").getString("name");
      branch = obj.getJSONObject("payload").getString("ref");
      try
      {
         setTimestamp(obj.getString("created_at"), "yyyy-MM-DD'T'HH:mm:ss'Z'");
      }
      catch (ParseException e)
      {
         e.printStackTrace();
      }
   }

   public String getBranch()
   {
      return branch;
   }

   public String getProject()
   {
      return project;
   }

   public void setBranch(String branch)
   {
      this.branch = branch;
   }

   public void setProject(String project)
   {
      this.project = project;
   }
}
