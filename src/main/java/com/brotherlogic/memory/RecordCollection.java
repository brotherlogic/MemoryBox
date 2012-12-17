package com.brotherlogic.memory;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import com.brotherlogic.memory.core.DiscogsMemory;
import com.brotherlogic.memory.core.Memory;
import com.brotherlogic.memory.db.DBFactory;
import com.brotherlogic.memory.feeds.discogs.MusicbrainzArtistMatch;

public class RecordCollection
{
   public static void main(String[] args) throws IOException
   {
      Collection<Memory> records = DBFactory.buildInterface().retrieveMemories(DiscogsMemory.class);

      // Make sure we have sort artists for each record
      for (Memory rec : records)
      {
         DiscogsMemory dMem = (DiscogsMemory) rec;
         if (dMem.getSortArtist() == null)
            try
            {
               dMem.setSortArtist(MusicbrainzArtistMatch.getMatcher().getSortName(dMem.getArtist()));
               DBFactory.buildInterface().storeMemory(dMem);
            }
            catch (IOException e)
            {
               e.printStackTrace();
            }

         if (dMem.getSortArtist() == null)
         {
            System.err.println("No Sort Name for: " + dMem.getArtist());
            System.exit(1);
         }
      }

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
