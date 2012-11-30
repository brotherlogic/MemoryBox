package com.brotherlogic.memory;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import com.brotherlogic.memory.core.DiscogsMemory;
import com.brotherlogic.memory.core.Memory;
import com.brotherlogic.memory.db.DBFactory;

public class RecordCollection
{
   public static void main(String[] args) throws IOException
   {
      Collection<Memory> records = DBFactory.buildInterface().retrieveMemories(DiscogsMemory.class);
      System.out.println(records.size());
      LinkedList<Memory> mems = new LinkedList<Memory>(records);
      Collections.sort(mems);
      for (Memory mem : mems)
      {
         DiscogsMemory dm = (DiscogsMemory) mem;
         System.out.println(dm.getArtist() + " - " + dm.getTitle());
      }
   }
}
