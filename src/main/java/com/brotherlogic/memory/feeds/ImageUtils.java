package com.brotherlogic.memory.feeds;

import java.net.URL;

import com.brotherlogic.memory.db.DBFactory;

/**
 * Utils for dealing with images
 * 
 * @author simon
 * 
 */
public class ImageUtils
{
   /**
    * Store an image
    * 
    * @param url
    *           The URL to store
    * @return The path where the item is stored
    */
   public String storeImage(final URL url)
   {
      return DBFactory.buildInterface().getDownloadQueue().download(url);
   }
}
