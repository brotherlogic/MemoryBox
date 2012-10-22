package com.brotherlogic.memory.db;

import com.brotherlogic.memory.core.Memory;

public class DBProxy
{
   public void storeMemory(Memory o)
   {
      System.out.println(o.getClass().getCanonicalName());
   }
}
