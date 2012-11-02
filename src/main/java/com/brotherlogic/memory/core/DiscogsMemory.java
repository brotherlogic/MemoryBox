package com.brotherlogic.memory.core;

/**
 * Stores a discog entry
 * 
 * @author simon
 * 
 */
public class DiscogsMemory extends Memory implements ImageMemory
{
   /** The name of the artist */
   private String artist;

   /** The path to the given image file */
   private String imagePath;

   /** The local version of the memory */
   private int localVersion = 1;

   /**
    * Get method for artist
    * 
    * @return The name of the artist
    */
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

   /**
    * Set method for artist
    * 
    * @param artistIn
    *           The name of the artist
    */
   public void setArtist(final String artistIn)
   {
      this.artist = artistIn;
   }

   @Override
   public void setImagePath(final String path)
   {
      imagePath = path;
   }

   @Override
   public void setLocalVersion(final Integer localVersionIn)
   {
      this.localVersion = localVersionIn;
   }

}
