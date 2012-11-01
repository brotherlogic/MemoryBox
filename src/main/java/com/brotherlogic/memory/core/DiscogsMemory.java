package com.brotherlogic.memory.core;

/**
 * Stores a discog entry
 * 
 * @author simon
 * 
 */
public class DiscogsMemory extends Memory implements ImageMemory
{
   private static int localVersion = 1;

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

   @Override
   public int getLocalVersion()
   {
      return localVersion;
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

   @Override
   public void setLocalVersion(Integer localVersion)
   {
      this.localVersion = localVersion;
   }

}
