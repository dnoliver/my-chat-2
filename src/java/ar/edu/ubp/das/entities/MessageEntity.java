/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.ubp.das.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dnoliver
 */
@XmlRootElement
public class MessageEntity {

    private Integer id;
    private Integer room;
    private Integer owner;
    private Date datetimeOfCreation;
    private String body;
    private String state;
    
    public MessageEntity(){
        this.datetimeOfCreation = new Date();
    }
    
    public MessageEntity(Integer id, Integer room, Integer owner, Date datetimeOfCreation, String body, String state){
        this.id = id;
        this.room = room;
        this.owner = owner;
        this.datetimeOfCreation = datetimeOfCreation;
        this.body = body;
        this.state = state;
    }
    
    public void fromMap(Map map){
        this.id = (Integer) map.get("id");
        this.room = (Integer) map.get("room");
        this.owner = (Integer) map.get("owner");
        this.datetimeOfCreation = (Date) map.get("datetimeOfCreation");
        this.body = (String) map.get("body");
        this.state = (String) map.get("state");
    }
    
    public Map toMap() {
        Map map = new HashMap<>();
        
        map.put("id", this.id);
        map.put("room", this.room);
        map.put("owner", this.owner);
        map.put("datetimeOfCreation", this.datetimeOfCreation);
        map.put("body", this.body);
        map.put("state", this.state);
        
        return map;
    }
    
    public Integer getId(){ return this.id; }
    public Integer getRoom(){ return this.room; }
    public Integer getOwner(){ return this.owner; }
    public Date getDatetimeOfCreation(){ return this.datetimeOfCreation; }
    public String getBody(){ return this.body; }
    public String getState(){ return this.state; }
    
    public void setId(Integer value){ this.id = value; }
    public void setRoom(Integer value){ this.room = value; }
    public void setOwner(Integer value){ this.owner = value; }
    public void setDatetimeOfCreation(Date value){ this.datetimeOfCreation = value; }
    public void setBody(String value){ this.body = value; }
    public void setState(String value){ this.state = value; }
    
}
