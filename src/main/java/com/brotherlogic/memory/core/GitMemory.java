package com.brotherlogic.memory.core;

/**
 * A git checkin
 * 
 * @author simon
 * 
 */
public class GitMemory extends Memory implements TimedMemory
{
   /** The branch to which we checked in */
   private String branch;

   /** The project we checked in on */
   private String project;

   /** The time of creation of this thingy */
   private Long time;

   /**
    * Get method for branch
    * 
    * @return The name of th branch
    */
   public String getBranch()
   {
      return branch;
   }

   /**
    * Get method for the project
    * 
    * @return The name of the project
    */
   public String getProject()
   {
      return project;
   }

   @Override
   public Long getTimestamp()
   {
      return time;
   }

   /**
    * Set method for branch
    * 
    * @param brnch
    *           The name of the branch
    */
   public void setBranch(final String brnch)
   {
      this.branch = brnch;
   }

   /**
    * Set method for project
    * 
    * @param prject
    *           THe name of the project
    */
   public void setProject(final String prject)
   {
      this.project = prject;
   }

   @Override
   public void setTimestamp(final Long timestamp)
   {
      time = timestamp;

      // Convert the time into the unique id
      setUniqueID(timestamp.hashCode() + "");
   }

   @Override
   public String toString()
   {
      String retString = super.toString();
      retString += ", Branch = " + branch + ", Project = " + project;
      return retString;
   }
}
