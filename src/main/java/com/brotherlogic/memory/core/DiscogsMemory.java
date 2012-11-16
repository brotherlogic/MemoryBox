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
   @Annotation
   public Long getTimestamp()
   {
      // TODO Auto-generated method stub
      return super.getTimestamp();
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
}
