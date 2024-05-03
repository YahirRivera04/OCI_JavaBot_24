package com.springboot.MyTodoList.model;

import javax.persistence.*;


@Entity
@Table(name = "TASKSTATUS")
public class TaskStatus{
    @Id
    int TaskId;
    @Column(name = "Name")
    String Name;
    @Column(name = "Description")
    String Description;
    @Column(name = "EstimatedHours")
    float EstimatedHours;
    @Column(name = "Priority")
    int Priority;

 public TaskStatus(int TaskId, String Name, String Description, float EstimatedHours, int Priority) {
     this.TaskId = TaskId;
     this.Name = Name;
     this.Description = Description;
     this.EstimatedHours = EstimatedHours;
 }
 
 public int getTaskId() {
     return TaskId;
 }
 
 public void setTaskId(int TaskId) {
     this.TaskId = TaskId;
 }
 
 public String getName() {
     return Name;
 }
 
 public void setName(String Name) {
     this.Name = Name;
 }
 
 public String getDescription() {
     return Description;
 }
 
 public void setDescription(String Description) {
     this.Description = Description;
 }
 
 public float getEstimatedHours() {
     return EstimatedHours;
 }
 
 public void setEstimatedHours(float EstimatedHours) {
     this.EstimatedHours = EstimatedHours;
 }
 
 public int getPriority() {
     return Priority;
 }
 
 public void setPriority(int Priority) {
     this.Priority = Priority;
 }
 
 @Override
 public String toString() {
     return "TASKSTATUS{" +
             "TaskId=" + TaskId +
             ", Name='" + Name + '\'' +
             ", Description='" + Description + '\'' +
             ", EstimatedHours=" + EstimatedHours +
             ", Priority=" + Priority +
             '}';
 }
}


