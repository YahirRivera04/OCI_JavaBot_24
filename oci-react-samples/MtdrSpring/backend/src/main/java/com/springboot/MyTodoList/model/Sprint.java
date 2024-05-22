package com.springboot.MyTodoList.model;


import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
@Entity
@Table(name = "SPRINT")
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SPRINTID")
    Long sprintId;
    @Column(name = "NAME")
    String name;
    @Column(name = "DESCRIPTION")
    String description;
    @Column(name = "STARTDATE")
    OffsetDateTime startDate;
    @Column(name = "ENDDATE")
    OffsetDateTime endDate;
    @ManyToOne
    @JoinColumn(name = "PROJECTID")
    Project project;
    @OneToMany(mappedBy = "taskId", cascade = CascadeType.ALL)
    List<Task> tasks;
    @OneToMany(mappedBy = "sprintUpdateId", cascade = CascadeType.ALL)
    List<SprintUpdate> sprintUpdates;  
    public Sprint(){

    }
    public Sprint(Long sprintId, String name, String description, OffsetDateTime startDate, OffsetDateTime endDate, Project project) {
        this.sprintId = sprintId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.project = project;
    }

    public Long getID() {
        return sprintId;
    }

    public void setID(Long ID) {
        this.sprintId = ID;
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
                "ID=" + sprintId +
                ", Start date='" + startDate + '\'' +
                ", End date=" + endDate +
                ", Name=" + name +
                ", Description=" + description +
                ", Project Name=" + project.getName() +
                '}';
    }
}
