/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.ubp.das.actions;

import ar.edu.ubp.das.entities.ProfileEntity;
import ar.edu.ubp.das.entities.RoomEntity;
import ar.edu.ubp.das.mvc.actions.Action;
import ar.edu.ubp.das.mvc.actions.DynaActionForm;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;


/**
 *
 * @author dnoliver
 */
public class GetUserHomePageAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("GetUserHomePageAction:execute");
        
        DynaActionForm form = this.getForm();
        Client client = ClientBuilder.newClient();
        String profile = (String) form.getItem("profile");
        WebTarget profileRoomsTarget = client.target("http://localhost:8080/app/webresources/profiles/" + profile);
        Invocation profileRoomsInvocation = profileRoomsTarget.request().buildGet();
        Response res = profileRoomsInvocation.invoke();

        ProfileEntity profileEntity = res.readEntity(new GenericType<ProfileEntity>(){});
        form.setItem("profile", profileEntity);
        this.gotoPage("/template/user/home.jsp", request, response);
    }
    
}
