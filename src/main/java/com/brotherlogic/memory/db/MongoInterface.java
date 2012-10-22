package com.brotherlogic.memory.db;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.types.ObjectId;

import com.brotherlogic.memory.core.Memory;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;

public class MongoInterface extends DBInterface
{
   private DB mongo;

   private final String DB_NAME = "memorybox";

   private void connect() throws IOException
   {
      if (mongo == null)
      {
         Mongo m = new Mongo();
         mongo = m.getDB(DB_NAME);
      }
   }

   @Override
   public Memory retrieveMemory(long timestamp, String className) throws IOException
   {
      try
      {
         // Create the given object
         Class objType = Class.forName(className);
         Constructor objCons = objType.getConstructor(new Class[0]);
         Object memory = objCons.newInstance(new Object[0]);

         return (Memory) memory;
      }
      catch (ClassNotFoundException e)
      {
         throw new IOException(e);
      }
      catch (NoSuchMethodException e)
      {
         throw new IOException(e);
      }
      catch (InvocationTargetException e)
      {
         throw new IOException(e);
      }
      catch (IllegalAccessException e)
      {
         throw new IOException(e);
      }
      catch (InstantiationException e)
      {
         throw new IOException(e);
      }
   }

   @Override
   public void storeMemory(Memory mem) throws IOException
   {
      String className = mem.getClass().getCanonicalName();
      Map<String, Class> propertyMap = deriveProperties(mem);

      // Store the memory in the memory table
      ObjectId id = store(Memory.class, mem, propertyMap);
   }

   private ObjectId store(Class storeType, Object toStore, Map<String, Class> potProperties)
         throws IOException
   {
      connect();

      String collection = storeType.getCanonicalName();
      BasicDBObject obj = new BasicDBObject();

      for (Entry<String, Class> prop : potProperties.entrySet())
         if (prop.getValue().equals(storeType))
            obj.put(prop.getKey(), "donkey");

      WriteResult res = mongo.getCollection(collection).insert(obj);
      return obj.getObjectId("_id");
   }
}
