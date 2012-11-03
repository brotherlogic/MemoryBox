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
   /** The name of the beer */
   private String beerName = "";

   /** The path to the image */
   private File imageFile = new File("");

   @Override
   public void buildFromJSON(final JSONObject obj) throws JSONException
   {
      try
      {
         setTimestamp(obj.getString("created_at"));
      }
      catch (ParseException e)
      {
         System.err.println("Cannot parse: " + obj.getString("created_at"));
      }
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

   /**
    * Get method for beer name
    * 
    * @return the name of the beer
    */
   public String getBeerName()
   {
      return beerName;
   }

   @Override
   public String getImagePath()
   {
      return imageFile.getAbsolutePath();
   }

   @Override
   public int hashCode()
   {
      return super.hashCode() + imageFile.hashCode();
   }

   /**
    * Set method for the beer name
    * 
    * @param name
    *           The name of the beer
    */
   public void setBeerName(final String name)
   {
      this.beerName = name;
   }

   @Override
   public void setImagePath(final String filePath)
   {
      imageFile = new File(filePath);
   }

   @Override
   public String toString()
   {
      return "Untappd: " + imageFile + ", " + super.toString();
   }

}
