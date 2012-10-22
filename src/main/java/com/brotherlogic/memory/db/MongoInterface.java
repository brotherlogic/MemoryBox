package com.brotherlogic.memory.db;

import java.io.IOException;
import java.util.Map;

import org.bson.types.ObjectId;

import com.brotherlogic.memory.core.Memory;

public class MongoInterface extends DBInterface
{
   @Override
   public void storeMemory(Memory mem) throws IOException
   {
      String className = mem.getClass().getCanonicalName();
      Map<String, Class> propertyMap = deriveProperties(mem);

      // Store the memory in the memory table
      ObjectId id = store(Memory.class, mem, propertyMap);
   }

   private void store(Class storeType, Object toStore, Map<String, Class> potProperties)
   {

   }
}
