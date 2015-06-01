/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.ubp.das.entities;

import java.util.Map;

/**
 *
 * @author dnoliver
 */
public abstract class Entity {
    public abstract Map toMap();
    public abstract void fromMap(Map map);
    
    public abstract Integer getId();
    public abstract void setId(Integer Id);
}
