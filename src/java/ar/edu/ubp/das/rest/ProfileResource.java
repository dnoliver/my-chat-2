/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.ubp.das.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import ar.edu.ubp.das.entities.ProfileEntity;
import ar.edu.ubp.das.mvc.actions.DynaActionForm;
import ar.edu.ubp.das.mvc.daos.Dao;
import ar.edu.ubp.das.mvc.daos.DaoFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author dnoliver
 */
@Path("profiles")
public class ProfileResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ProfileResource
     */
    public ProfileResource() {
    }

    /**
     * Retrieves representation of an instance of ar.edu.ubp.das.rest.ProfileResource
     * @param id
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response findById(@PathParam("id") Integer id) {
        try {
            Dao dao;
            DynaActionForm form;
            List<DynaActionForm> resultSet;
            
            dao = DaoFactory.getDao("Profile");
            form = new DynaActionForm();
            form.setItem("selector", "byId");
            form.setItem("id", id);
            resultSet = dao.select(form);
            
            if(resultSet.size() != 1){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            else {
                ProfileEntity profile = new ProfileEntity();
                profile.fromMap(resultSet.get(0).getItems());
                return Response.ok(profile).build();
            }
        } catch (Exception ex) {
            Logger.getLogger(ProfileResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Produces("application/json")
    public List<ProfileEntity> findAll() {
        try {
            Dao dao;
            DynaActionForm form;
            List<DynaActionForm> resultSet;
            List<ProfileEntity> profiles = new LinkedList<>();
            
            dao = DaoFactory.getDao("Profile");
            form = new DynaActionForm();
            form.setItem("selector", "findAll");
            resultSet = dao.select(form);
            
            for(DynaActionForm temp : resultSet ){
                ProfileEntity profile = new ProfileEntity();
                profile.fromMap(temp.getItems());
                profiles.add(profile);
            }
            
            return profiles;
        } catch (Exception ex) {
            Logger.getLogger(ProfileResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @GET
    @Path("login/{login}")
    @Produces("application/json")
    public Response findByLogin(@PathParam("login") String login) {
        try {
            Dao dao;
            DynaActionForm form;
            List<DynaActionForm> resultSet;
            
            dao = DaoFactory.getDao("Profile");
            form = new DynaActionForm();
            form.setItem("selector", "byLogin");
            form.setItem("login", login);
            resultSet = dao.select(form);
            
            if(resultSet.size() != 1){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            else {
                ProfileEntity profile = new ProfileEntity();
                profile.fromMap(resultSet.get(0).getItems());
                return Response.ok(profile).build();
            }
        } catch (Exception ex) {
            Logger.getLogger(ProfileResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("room/{room}/actives")
    @Produces("application/json")
    public List<ProfileEntity> findActivesByRoom(@PathParam("room") Integer room) {
        try {
            Dao dao;
            DynaActionForm form;
            List<DynaActionForm> resultSet;
            List<ProfileEntity> profiles = new LinkedList<>();
            
            dao = DaoFactory.getDao("Profile");
            form = new DynaActionForm();
            form.setItem("selector", "byRoom");
            form.setItem("room", room);
            resultSet = dao.select(form);
            
            for(DynaActionForm temp : resultSet){
                ProfileEntity profile = new ProfileEntity();
                profile.fromMap(temp.getItems());
                profiles.add(profile);
            }

            return profiles;
        } catch (Exception ex) {
            Logger.getLogger(ProfileResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(ProfileEntity entity) {
        try {
            Dao dao = DaoFactory.getDao("Profile");
            DynaActionForm form = new DynaActionForm();
            
            form.setItems(entity.toMap());
            dao.insert(form);
            entity.setId((Integer) form.getItem("id"));
            
            return Response.ok(entity).build();
        } catch (Exception ex) {
            Logger.getLogger(ProfileResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.PRECONDITION_FAILED).build();
        }
    }
    
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response update(ProfileEntity entity) {
        try {
            Dao dao = DaoFactory.getDao("Profile");
            DynaActionForm form = new DynaActionForm(); 
            
            form.setItems(entity.toMap());
            dao.update(form);
            
            return Response.ok(entity).build();
        } catch (Exception ex) {
            Logger.getLogger(ProfileResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.PRECONDITION_FAILED).build();
        }
    }
}
