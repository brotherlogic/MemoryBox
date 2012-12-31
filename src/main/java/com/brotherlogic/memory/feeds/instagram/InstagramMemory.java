package com.brotherlogic.memory.feeds.instagram;

import com.brotherlogic.memory.core.ImageMemory;
import com.brotherlogic.memory.core.Memory;

public class InstagramMemory extends Memory implements ImageMemory
{
   long timeTaken;
   String imagePath;

   @Override
   public String getImagePath()
   {
      return imagePath;
   }

   @Override
   public void setImagePath(String path)
   {
      imagePath = path;
   }

   public long getTimeTaken()
   {
      return timeTaken;
   }

   public void setTimeTaken(long timeTaken)
   {
      this.timeTaken = timeTaken;
   }
}
