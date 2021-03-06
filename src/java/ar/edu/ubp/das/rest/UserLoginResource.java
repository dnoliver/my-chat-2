/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.ubp.das.rest;

import ar.edu.ubp.das.entities.UserLoginEntity;
import ar.edu.ubp.das.mvc.actions.DynaActionForm;
import ar.edu.ubp.das.mvc.daos.Dao;
import ar.edu.ubp.das.mvc.daos.DaoFactory;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
@Path("userslogins")
public class UserLoginResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UserLoginResource
     */
    public UserLoginResource() {
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response create(UserLoginEntity entity){
        try {
            Dao dao = DaoFactory.getDao("UserLogin");
            DynaActionForm form = new DynaActionForm();
            form.setItems(entity.toMap());
            dao.insert(form);
            entity.setId((Integer) form.getItem("id"));        
            return Response.ok(entity).build();
        } catch (Exception ex) {
            Logger.getLogger(UserLoginResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    @POST
    @Path("{id}/terminate")
    @Produces("application/json")
    @Consumes("application/json")
    public Response terminateSession(UserLoginEntity entity){
        try {
            Dao dao = DaoFactory.getDao("UserLogin");
            entity.setDatetimeOfAccessEnd(new Date());
            
            DynaActionForm form = new DynaActionForm();
            form.setItems(entity.toMap());
            dao.update(form);
            
            return Response.ok(entity).build();
        } catch (Exception ex) {
            Logger.getLogger(UserLoginResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
        
    }
    
    /**
     * Retrieves representation of an instance of ar.edu.ubp.das.rest.UserLoginResource
     * @param id
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response findById(@PathParam("id") int id) {
        try {
            Dao dao = DaoFactory.getDao("UserLogin");
            DynaActionForm form = new DynaActionForm();
            
            form.setItem("selector", "byId");
            form.setItem("id",id);
            List<DynaActionForm> select = dao.select(form);
            
            if(select.size() == 1){
                UserLoginEntity entity = new UserLoginEntity();
                entity.fromMap(select.get(0).getItems());
                return Response.ok(entity).build();
            }
            else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception ex) {
            Logger.getLogger(UserLoginResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    @GET
    @Path("lastlogin/profile/{id}")
    @Produces("application/json")
    public Response findLastLogin(@PathParam("id") int id) {
        try {
            Dao dao = DaoFactory.getDao("UserLogin");
            DynaActionForm form = new DynaActionForm();
            
            form.setItem("selector", "byLastLogin");
            form.setItem("profile",id);
            List<DynaActionForm> select = dao.select(form);
            
            if(select.size() == 1){
                UserLoginEntity entity = new UserLoginEntity();
                entity.fromMap(select.get(0).getItems());
                return Response.ok(entity).build();
            }
            else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception ex) {
            Logger.getLogger(UserLoginResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
    
    @GET
    @Produces("application/json")
    public List<UserLoginEntity> findAll() {
        try {
            Dao dao = DaoFactory.getDao("UserLogin");
            DynaActionForm form = new DynaActionForm();
            List<UserLoginEntity> entities = new LinkedList<>();
            
            form.setItem("selector", "findAll");
            List<DynaActionForm> select = dao.select(form);
            
            for(DynaActionForm temp : select){
                UserLoginEntity e = new UserLoginEntity();
                e.fromMap(temp.getItems());
                entities.add(e);
            }
            
            return entities;
        } catch (Exception ex) {
            Logger.getLogger(UserLoginResource.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @GET
    @Produces("application/json")
    @Path("auth/profile/{email}")
    public Response authProfile(@PathParam("email") String email){
        final String username = "<mail>";
        final String password = "<password>";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                }
          });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from-email@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Account Validation");
            message.setText("http://localhost:8080/app/index.jsp#/home?sessionValidation=true");

            Transport.send(message);
            return Response.ok().build();

        } catch (MessagingException ex) {
            Logger.getLogger(UserLoginResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.serverError().build();
        }
    }
}
