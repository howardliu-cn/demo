package cn.howardliu.solr.exercise.pojo;

import org.apache.solr.client.solrj.beans.Field;

import java.util.Date;

public class DemoModel {
    @Field("id")
    private String id;
    @Field("type")
    private String type;
    @Field("name")
    private String name;
    @Field("createtime")
    private String createtime;
    @Field("currenttime")
    private Date currenttime;

    public DemoModel() {
    }

    public DemoModel(String id, String type, String name, String createtime, Date currenttime) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.createtime = createtime;
        this.currenttime = currenttime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Date getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(Date currenttime) {
        this.currenttime = currenttime;
    }
}