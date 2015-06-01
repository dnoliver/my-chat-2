/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.ubp.das.entities;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dnoliver
 */
@XmlRootElement
public class InvitationEntity {

    private Integer id;
    private Integer room;
    private Integer sender;
    private Integer receiver;
    private String state;
    
    public InvitationEntity(){}
    
    public InvitationEntity(Integer id, Integer room, Integer sender, Integer receiver, String state){
        this.id = id;
        this.room = room;
        this.sender = sender;
        this.receiver = receiver;
        this.state = state;
    }
    
    public void fromMap(Map map){
        this.id = (Integer) map.get("id");
        this.room = (Integer) map.get("room");
        this.sender = (Integer) map.get("sender");
        this.receiver = (Integer) map.get("receiver");
        this.state = (String) map.get("state");
    }
    
    public Map toMap() {
        Map map = new HashMap<>();
        
        map.put("id", this.id);
        map.put("room", this.room);
        map.put("sender", this.sender);
        map.put("receiver", this.receiver);
        map.put("state", this.state);
        
        return map;
    }
    
    public Integer getId(){ return this.id; }
    public Integer getRoom(){ return this.room; }
    public Integer getReceiver(){ return this.receiver; }
    public Integer getSender(){ return this.sender; }
    public String getState(){ return this.state; }
    
    public void setId(Integer value){ this.id = value; }
    public void setRoom(Integer value){ this.room = value; }
    public void setSender(Integer value){ this.sender = value; }
    public void setReceiver(Integer value){ this.receiver = value; }
    public void setState(String value){ this.state = value; }
    
}
