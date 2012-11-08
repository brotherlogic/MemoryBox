package com.brotherlogic.memory.core;

/**
 * A git checkin
 * 
 * @author simon
 * 
 */
public class GitMemory extends Memory
{
   /** The branch to which we checked in */
   private String branch;

   /** The project we checked in on */
   private String project;

   public String getBranch()
   {
      return branch;
   }

   public String getProject()
   {
      return project;
   }

   public void setBranch(String branch)
   {
      this.branch = branch;
   }

   public void setProject(String project)
   {
      this.project = project;
   }
}
