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

   public static void main(String[] args) throws IOException
   {
      UntappdMemory mem = new UntappdMemory();
      MongoInterface inter = new MongoInterface();
      inter.storeMemory(mem);
      DBProxy proxy = new DBProxy();
      proxy.storeMemory(mem);
   }

}
