package com.brotherlogic.memory.core;

import java.io.File;

/**
 * Representation of an Untappd Memory
 * 
 * @author simon
 * 
 */
public class UntappdMemory extends Memory implements ImageMemory, TimedMemory
{
   /** The ABV of the beer */
   private Double abv;

   /** The amount of liquid */
   private Integer amount;

   /** The name of the beer */
   private String beerName = null;

   /** The brewery that makes the beer */
   private String breweryName = null;

   /** The path to the image */
   private File imageFile = new File("");

   /** The at which we drunk the thing */
   private Long timestamp;

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
    * Get method for the abv
    * 
    * @return The ABV
    */
   public final Double getAbv()
   {
      return abv;
   }

   /**
    * Get method for the amount drunk
    * 
    * @return The amount drunk
    */
   @Annotation
   public final Integer getAmount()
   {
      return amount;
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

   /**
    * Get method for the brewery name
    * 
    * @return The name of the brewery
    */
   public String getBreweryName()
   {
      return breweryName;
   }

   @Override
   public String getImagePath()
   {
      return imageFile.getAbsolutePath();
   }

   @Override
   public Long getTimestamp()
   {
      return timestamp;
   }

   @Override
   public int hashCode()
   {
      return super.hashCode() + imageFile.hashCode();
   }

   /**
    * Set method for the abv
    * 
    * @param abvLevel
    *           the abv level as a {@link Double}
    */
   public void setAbv(final Double abvLevel)
   {
      this.abv = abvLevel;
   }

   /**
    * Sets the amount drunk
    * 
    * @param am
    *           the amount drunk (in ml)
    */
   @Annotation
   public void setAmount(final Integer am)
   {
      this.amount = am;
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

   /**
    * Set method for the brewery name
    * 
    * @param brewery
    *           The name of the brewery
    */
   public void setBreweryName(final String brewery)
   {
      this.breweryName = brewery;
   }

   @Override
   public void setImagePath(final String filePath)
   {
      imageFile = new File(filePath);
   }

   @Override
   public void setTimestamp(Long time)
   {
      // TODO Auto-generated method stub
      timestamp = time;
   }

   @Override
   public String toString()
   {
      return "Untappd: " + breweryName + " - " + beerName + ", " + super.toString();
   }

}
