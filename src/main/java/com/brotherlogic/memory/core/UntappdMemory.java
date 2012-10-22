package com.brotherlogic.memory.core;

import java.io.File;
import java.io.IOException;

import com.brotherlogic.memory.db.DBProxy;
import com.brotherlogic.memory.db.MongoInterface;

public class UntappdMemory extends Memory implements ImageMemory
{
   File imageFile;

   @Override
   public String getImagePath()
   {
      return imageFile.getAbsolutePath();
   }

   @Override
   public void setImagePath(String filePath)
   {
      imageFile = new File(filePath);
   }

   @Override
   public boolean equals(Object o)
   {
      if (!(o instanceof UntappdMemory))
         return false;
      UntappdMemory other = (UntappdMemory) o;

      if (!other.imageFile.equals(imageFile))
         return false;

      return super.equals(o);
   }

   public static void main(String[] args) throws IOException
   {
      UntappdMemory mem = new UntappdMemory();
      MongoInterface inter = new MongoInterface();
      inter.storeMemory(mem);
      DBProxy proxy = new DBProxy();
      proxy.storeMemory(mem);
   }

}
