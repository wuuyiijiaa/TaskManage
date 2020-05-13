package com.my.taskmanage.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "TaskEntity")
public class TaskEntity {
    @Column(name = "id",isId = true)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "des")
    private  String des;
    @Column(name = "pic")
    private String pic;
    @Column(name = "isUrgent")
    private boolean isUrgent;
    @Column(name = "isImportant")
    private boolean isImportant;
    @Column(name = "category")
    private String category;
    
    @Override
    public String toString() {
        return "TaskEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", des='" + des + '\'' +
            ", pic='" + pic + '\'' +
            ", isUrgent=" + isUrgent +
            ", isImportant=" + isImportant +
            ", category='" + category + '\'' +
            '}';
    }
    
    public TaskEntity() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDes() {
        return des;
    }
    
    public void setDes(String des) {
        this.des = des;
    }
    
    public String getPic() {
        return pic;
    }
    
    public void setPic(String pic) {
        this.pic = pic;
    }
    
    public boolean isUrgent() {
        return isUrgent;
    }
    
    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }
    
    public boolean isImportant() {
        return isImportant;
    }
    
    public void setImportant(boolean important) {
        isImportant = important;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }

}
