package com.brotherlogic.memory.core;

import java.io.File;

/**
 * Representation of an Untappd Memory
 * 
 * @author simon
 * 
 */
public class UntappdMemory extends Memory implements ImageMemory
{
   /** The abv of the beer */
   private double abv;

   /** The amount of liquid */
   private Integer amount;

   /** The name of the beer */
   private String beerName = "";

   /** The path to the image */
   private File imageFile = new File("");

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

   public double getAbv()
   {
      return abv;
   }

   @Annotation
   public Integer getAmount()
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

   public void setAbv(double abv)
   {
      this.abv = abv;
   }

   @Annotation
   public void setAmount(Integer amount)
   {
      this.amount = amount;
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
