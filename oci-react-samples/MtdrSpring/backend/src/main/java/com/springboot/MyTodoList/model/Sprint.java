package com.springboot.MyTodoList.model;


import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
@Entity
@Table(name = "SPRINT")
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SprintId")
    int ID;
    @Column(name = "Name")
    String name;
    @Column(name = "Description")
    String description;
    @Column(name = "StartDate")
    OffsetDateTime startDate;
    @Column(name = "EndDate")
    OffsetDateTime endDate;
    @ManyToOne
    @JoinColumn(name = "ProjectId")
    Project project;
    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
    List<Task> tasks;
    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
    List<SprintUpdate> sprintUpdates;  
    public Sprint(){

    }
    public Sprint(int ID, String name, String description, OffsetDateTime startDate, OffsetDateTime endDate, Project project) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.project = project;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Task> getTasks(){
        return tasks;
    }
    
    public List<SprintUpdate> getSprintUpdates(){
        return sprintUpdates;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "ID=" + ID +
                ", Start date='" + startDate + '\'' +
                ", End date=" + endDate +
                ", Name=" + name +
                ", Description=" + description +
                ", Project Name=" + project.getName() +
                '}';
    }
}
