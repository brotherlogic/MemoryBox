package com.brotherlogic.memory.db;

import java.io.IOException;

import com.brotherlogic.memory.core.Memory;

public class MongoInterface extends DBInterface
{
   @Override
   public void storeMemory(Memory mem) throws IOException
   {
      String className = mem.getClass().getCanonicalName();
      String memoryName = className.substring("com.brotherlogic.memory.core.".length());
      deriveProperties(mem);
   }
}
