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

   private String sortArtist;

   @Annotation
   public String getSortArtist()
   {
      if (artist.equals("Various"))
         return title;
      return sortArtist;
   }

   public void setSortArtist(String sortArtist)
   {
      this.sortArtist = sortArtist;
   }

   /** The path to the given image file */
   private String imagePath;

   private Double releaseOrder;

   private Integer releaseYear;

   private String title;

   @Override
   public int compareTo(final Memory mem)
   {
      if (!(mem instanceof DiscogsMemory))
         return super.compareTo(mem);
      DiscogsMemory o = (DiscogsMemory) mem;

      if (getSortArtist() != null && o.getSortArtist() != null)
      {
         int artComp = getSortArtist().compareTo(o.getSortArtist());
         if (artComp != 0)
            return artComp;
      }

      if (releaseYear != null && o.releaseYear != null)
      {
         int yearComp = releaseYear.compareTo(o.releaseYear);
         if (yearComp != 0)
            return yearComp;
      }

      if (releaseOrder != null && o.releaseOrder != null)
      {
         int orderComp = releaseOrder.compareTo(o.releaseOrder);
         if (orderComp != 0)
            return orderComp;
      }

      return title.compareTo(o.title);
   }

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
   public Double getReleaseOrder()
   {
      return releaseOrder;
   }

   public Integer getReleaseYear()
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

   public void setReleaseOrder(Double releaseOrder)
   {
      this.releaseOrder = releaseOrder;
   }

   public void setReleaseYear(Integer releaseYear)
   {
      this.releaseYear = releaseYear;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }
}
