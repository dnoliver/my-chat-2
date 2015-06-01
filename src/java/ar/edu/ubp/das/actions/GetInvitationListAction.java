/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.ubp.das.actions;

import ar.edu.ubp.das.entities.InvitationEntity;
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
public class GetInvitationListAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("GetInvitationListAction:execute");
        
        DynaActionForm form = this.getForm();
        String receiver = (String) form.getItem("receiver");
        Client client = ClientBuilder.newClient();
        WebTarget publicRoomsTarget = client.target("http://localhost:8080/app/webresources/invitations/receiver/" + receiver);
        Invocation publicRoomsInvocation = publicRoomsTarget.request().buildGet();
        Response res = publicRoomsInvocation.invoke();

        List<InvitationEntity> invitations = res.readEntity(new GenericType<List<InvitationEntity>>(){});
        form.setItem("invitations", invitations);
        
        this.gotoPage("/template/user/invitationList.jsp", request, response);
    }
    
}
