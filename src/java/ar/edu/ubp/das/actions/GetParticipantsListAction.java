/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.ubp.das.actions;

import ar.edu.ubp.das.entities.MessageEntity;
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

/**
 *
 * @author dnoliver
 */
public class GetParticipantsListAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("GetParticipantsListAction:execute");
        
        DynaActionForm form = this.getForm();
        String room = (String) form.getItem("room");
        String profile = (String) form.getItem("profile");
        
        Client client = ClientBuilder.newClient();
        WebTarget publicRoomsTarget = client.target("http://localhost:8080/app/webresources/profiles/room/" + room + "/actives" );
        Invocation publicRoomsInvocation = publicRoomsTarget.request().buildGet();
        Response res = publicRoomsInvocation.invoke();

        List<ProfileEntity> participants = res.readEntity(new GenericType<List<ProfileEntity>>(){});
        form.setItem("participants", participants);
        
        WebTarget profileTarget = client.target("http://localhost:8080/app/webresources/profiles/" + profile );
        Invocation profileInvocation = profileTarget.request().buildGet();
        Response res1 = profileInvocation.invoke();

        ProfileEntity profileEntity = res1.readEntity(new GenericType<ProfileEntity>(){});
        form.setItem("user", profileEntity);
        
        WebTarget roomTarget = client.target("http://localhost:8080/app/webresources/rooms/" + room );
        Invocation roomInvocation = roomTarget.request().buildGet();
        Response res2 = roomInvocation.invoke();

        RoomEntity roomEntity = res2.readEntity(new GenericType<RoomEntity>(){});
        form.setItem("room", roomEntity);
        
        this.gotoPage("/template/user/participantList.jsp", request, response);
    }
    
}
