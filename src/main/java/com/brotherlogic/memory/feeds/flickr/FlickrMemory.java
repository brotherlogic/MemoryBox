package com.brotherlogic.memory.feeds.flickr;

import com.brotherlogic.memory.core.ImageMemory;
import com.brotherlogic.memory.core.Memory;

public class FlickrMemory extends Memory implements ImageMemory
{
   private String filename;
   private String caption;

   public String getCaption()
   {
      return caption;
   }

   public void setCaption(String caption)
   {
      this.caption = caption;
   }

   @Override
   public String getImagePath()
   {
      return filename;
   }

   @Override
   public void setImagePath(String path)
   {
      filename = path;
   }
}
