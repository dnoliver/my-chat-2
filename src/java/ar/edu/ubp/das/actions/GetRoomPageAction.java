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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dnoliver
 */
public class GetRoomPageAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("GetRoomPageAction:execute");
        
        DynaActionForm form = this.getForm();
        Client client = ClientBuilder.newClient();
        String room = (String) form.getItem("room");
        WebTarget roomTarget = client.target("http://localhost:8080/app/webresources/rooms/" + room);
        Invocation roomInvocation = roomTarget.request().buildGet();
        Response res = roomInvocation.invoke();
        
        RoomEntity roomEntity = res.readEntity(new GenericType<RoomEntity>(){});
        form.setItem("room", roomEntity);
        
        this.gotoPage("/template/user/room.jsp", request, response);
    }
    
}
