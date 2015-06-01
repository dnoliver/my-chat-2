/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.ubp.das.actions;

import ar.edu.ubp.das.entities.ProfileEntity;
import ar.edu.ubp.das.entities.UserLoginEntity;
import ar.edu.ubp.das.mvc.actions.Action;
import ar.edu.ubp.das.mvc.actions.DynaActionForm;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class ValidateProfileLoginStatusAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("ValidateProfileLoginStatusAction:execute");
        
        DynaActionForm form = this.getForm();
        Client client = ClientBuilder.newClient();
        String profile = (String) form.getItem("profile");
        
        WebTarget profileTarget = client.target("http://localhost:8080/app/webresources/profiles/login/" + profile);
        Invocation profileInvocation = profileTarget.request().buildGet();
        Response res = profileInvocation.invoke();

        ProfileEntity profileEntity = res.readEntity(new GenericType<ProfileEntity>(){});
        
        WebTarget loginTarget = client.target("http://localhost:8080/app/webresources/userslogins/lastlogin/profile/" + profileEntity.getId());
        Invocation loginInvocation = loginTarget.request().buildGet();
        Response res1 = loginInvocation.invoke();

        if(res1.getStatus() == 404){
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            UserLoginEntity loginEntity = res1.readEntity(new GenericType<UserLoginEntity>(){});

            if(loginEntity != null){
                Calendar cal = GregorianCalendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, 1);
                Date limit = cal.getTime();

                if(loginEntity.getDatetimeOfAccessEnd().before(limit)){
                    response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
            else{
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}
