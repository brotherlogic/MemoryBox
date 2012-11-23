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

   private double releaseOrder;

   private int releaseYear;

   private String title;

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

   @Annotation
   public double getReleaseOrder()
   {
      return releaseOrder;
   }

   public int getReleaseYear()
   {
      return releaseYear;
   }

   @Override
   @Annotation
   public Long getTimestamp()
   {
      // TODO Auto-generated method stub
      return super.getTimestamp();
   }

   public String getTitle()
   {
      return title;
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

   public void setReleaseOrder(double releaseOrder)
   {
      this.releaseOrder = releaseOrder;
   }

   public void setReleaseYear(int releaseYear)
   {
      this.releaseYear = releaseYear;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }
}
