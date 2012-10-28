package com.brotherlogic.memory.core;

/**
 * Stores a discog entry
 * 
 * @author simon
 * 
 */
public class DiscogsMemory extends Memory implements ImageMemory
{
   String artist;

   String imagePath;

   public String getArtist()
   {
      return artist;
   }

   @Override
   public String getImagePath()
   {
      return imagePath;
   }

   public void setArtist(String artist)
   {
      this.artist = artist;
   }

   @Override
   public void setImagePath(String path)
   {
      imagePath = path;
   }

}