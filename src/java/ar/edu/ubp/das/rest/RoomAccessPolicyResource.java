/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.ubp.das.rest;

import ar.edu.ubp.das.entities.RoomAccessPolicyEntity;
import ar.edu.ubp.das.entities.RoomEntity;
import ar.edu.ubp.das.mvc.actions.DynaActionForm;
import ar.edu.ubp.das.mvc.daos.Dao;
import ar.edu.ubp.das.mvc.daos.DaoFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author dnoliver
 */
@Path("roomsaccesspolicy")
public class RoomAccessPolicyResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RoomAccessPolicyResource
     */
    public RoomAccessPolicyResource() {
    }

        @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(RoomAccessPolicyEntity entity){
        try {
            Dao dao = DaoFactory.getDao("RoomAccessPolicy");
            DynaActionForm form = new DynaActionForm();
            
            form.setItems(entity.toMap());
            dao.insert(form);
            entity.setId((int) form.getItem("id"));
            return Response.ok(entity).build();
        } catch (Exception ex) {
            Logger.getLogger(ProfileResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public RoomAccessPolicyEntity findById(@PathParam("id") Integer id) {
        try {
            Dao dao = DaoFactory.getDao("RoomAccessPolicy");
            DynaActionForm form = new DynaActionForm();
            List<DynaActionForm> resultSet;
            List<RoomAccessPolicyEntity> entities = new LinkedList<>();
            
            form.setItem("selector", "byId");
            form.setItem("id", id);
            resultSet = dao.select(form);
            
            for(DynaActionForm temp : resultSet ){
                RoomAccessPolicyEntity e = new RoomAccessPolicyEntity();
                e.fromMap(temp.getItems());
                entities.add(e);
            }
            
            if(entities.size() != 1){
                return null;
            }
            else {
                return entities.get(0);
            }
        } catch (Exception ex) {
            Logger.getLogger(ProfileResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Retrieves representation of an instance of ar.edu.ubp.das.rest.RoomAccessPolicyResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public List<RoomAccessPolicyEntity> findAll() {
        try {
            Dao dao = DaoFactory.getDao("RoomAccessPolicy");
            DynaActionForm form = new DynaActionForm();
            List<DynaActionForm> resultSet;
            List<RoomAccessPolicyEntity> entities = new LinkedList<>();
            
            form.setItem("selector", "findAll");
            resultSet = dao.select(form);
            
            for(DynaActionForm temp : resultSet ){
                RoomAccessPolicyEntity e = new RoomAccessPolicyEntity();
                e.fromMap(temp.getItems());
                entities.add(e);
            }
            
            return entities;
        } catch (Exception ex) {
            Logger.getLogger(ProfileResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @GET
    @Path("room/{room}")
    @Produces("application/json")
    public List<RoomAccessPolicyEntity> findByRoom(@PathParam("room") Integer room) {
        try {
            Dao dao = DaoFactory.getDao("RoomAccessPolicy");
            DynaActionForm form = new DynaActionForm();
            List<DynaActionForm> resultSet;
            List<RoomAccessPolicyEntity> entities = new LinkedList<>();
            
            form.setItem("selector", "byRoom");
            form.setItem("room", room);
            resultSet = dao.select(form);
            
            for(DynaActionForm temp : resultSet ){
                RoomAccessPolicyEntity e = new RoomAccessPolicyEntity();
                e.fromMap(temp.getItems());
                entities.add(e);
            }
            
            return entities;
        } catch (Exception ex) {
            Logger.getLogger(ProfileResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
