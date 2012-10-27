package com.brotherlogic.memory.core;

import java.io.File;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class UntappdMemory extends Memory implements ImageMemory, JSONConstructable
{
   private static final int VERSION = 1;

   @Override
   public int buildFromJSON(JSONObject obj) throws JSONException
   {
      int version = super.getVersion();

      System.out.println("HERE: " + obj);
      try
      {
         setTimestamp(obj.getString("created_at"));
      }
      catch (ParseException e)
      {
         System.err.println("Cannot parse: " + obj.getString("created_at"));
      }
      return version + VERSION;
   }

   File imageFile = new File("");

   @Override
   public String getImagePath()
   {
      return imageFile.getAbsolutePath();
   }

   @Override
   public void setImagePath(String filePath)
   {
      imageFile = new File(filePath);
   }

   @Override
   public boolean equals(Object o)
   {
      if (!(o instanceof UntappdMemory))
         return false;
      UntappdMemory other = (UntappdMemory) o;

      if (!other.imageFile.equals(imageFile))
         return false;

      return super.equals(o);
   }

}
