package com.brotherlogic.memory.core;

import java.io.File;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Representation of an Untappd Memory
 * 
 * @author simon
 * 
 */
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
   public void setImagePath(final String filePath)
   {
      imageFile = new File(filePath);
   }

   @Override
   public int hashCode()
   {
      return super.hashCode() + imageFile.hashCode();
   }

   @Override
   public boolean equals(final Object o)
   {
      if (!(o instanceof UntappdMemory))
         return false;
      UntappdMemory other = (UntappdMemory) o;

      if (!other.imageFile.equals(imageFile))
         return false;

      return super.equals(o);
   }

   @Override
   public String toString()
   {
      return "Untappd: " + imageFile + ", " + super.toString();
   }

}
