package com.brotherlogic.memory.core;

import java.io.File;

import org.json.JSONObject;

/**
 * Representation of an Untappd Memory
 * 
 * @author simon
 * 
 */
public class UntappdMemory extends Memory implements ImageMemory, JSONConstructable
{
   /** The location of the file */
   private File imageFile = new File("");

   @Override
   public int buildFromJSON(final JSONObject obj)
   {
      // TODO Auto-generated method stub
      return 0;
   }

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
