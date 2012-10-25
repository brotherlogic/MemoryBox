package com.brotherlogic.memory.core;

/**
 * A memory which also contains an image
 * 
 * @author simon
 * 
 */
public interface ImageMemory
{
   /**
    * The path to the image file
    * 
    * @return The Path to the image
    */
   String getImagePath();

   /**
    * Set the image
    * 
    * @param path
    *           The path to the image
    */
   void setImagePath(String path);
}
