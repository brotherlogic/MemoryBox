package com.brotherlogic.memory.db;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import com.brotherlogic.memory.core.Memory;

public abstract class DBInterface
{
   public abstract void storeMemory(Memory mem) throws IOException;

   public List<String> deriveProperties(Memory mem)
   {
      List<String> strs = new LinkedList<String>();

      for (Class inter : mem.getClass().getInterfaces())
         System.out.println("interface = " + inter);

      for (Method meth : mem.getClass().getMethods())
         System.out.println(meth.getName() + " => " + meth.getDeclaringClass() + " and ");

      return strs;
   }
}
