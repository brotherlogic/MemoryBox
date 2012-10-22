package com.brotherlogic.memory.db;

public class DBFactory
{
   public static DBInterface buildInterface()
   {
      return new MongoInterface();
   }
}
