/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.ubp.das.entities;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dnoliver
 */
public class RoomAccessPolicyEntity {

    private Integer id;
    private Integer room;
    private Integer profile;
    private String policy;
   
    public RoomAccessPolicyEntity(){}
    
    public RoomAccessPolicyEntity(Integer id, Integer room, Integer profile, String policy){
        this.id = id;
        this.room = room;
        this.profile = profile;
        this.policy = policy;
    }
    
    public Integer getId(){ return this.id; }
    public Integer getRoom(){ return this.room ;}
    public Integer getProfile(){ return this.profile ;}
    public String getPolicy(){ return this.policy ;}
    
    public void setId(int id){ this.id = id; }
    public void setRoom(Integer value) { this.room = value; }
    public void setProfile(Integer value) { this.profile = value; }
    public void setPolicy(String value) { this.policy = value; }
    
    public void fromMap(Map map){
        this.id = (Integer) map.get("id");
        this.room = (Integer) map.get("room");
        this.profile =(Integer) map.get("profile");
        this.policy = (String) map.get("policy");
    }
    
    public Map toMap(){
        Map map = new HashMap<>();
        
        map.put("id", this.id);
        map.put("room", this.room);
        map.put("profile", this.profile);
        map.put("policy", this.policy);
        
        return map;
    }
    
}
