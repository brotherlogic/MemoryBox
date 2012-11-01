package com.brotherlogic.memory.db;

import java.io.IOException;

import com.brotherlogic.memory.core.Memory;

/**
 * The base rep store stores the underlying representation of each object in a
 * neat store
 * 
 * @author simon
 * 
 */
public abstract class BaseRepStore
{
   public abstract String getBaseRep(Memory me) throws IOException;

   public abstract void storeBaseRep(Memory mem, String baseRep) throws IOException;
}
