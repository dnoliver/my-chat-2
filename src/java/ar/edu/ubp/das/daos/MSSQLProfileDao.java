/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.ubp.das.daos;

import ar.edu.ubp.das.mvc.actions.DynaActionForm;
import ar.edu.ubp.das.mvc.daos.MSSQLDao;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import static java.sql.Types.INTEGER;
import java.util.List;

/**
 *
 * @author dnoliver
 */
public class MSSQLProfileDao extends MSSQLDao {

    @Override
    public DynaActionForm make(ResultSet result) throws Exception {
        DynaActionForm profile = new DynaActionForm();
        
        profile.setItem("id", result.getInt("id"));
        profile.setItem("login", result.getString("login"));
        profile.setItem("password", result.getString("password"));
        profile.setItem("type", result.getString("type"));
        
        return profile;
    }

    @Override
    public void insert(DynaActionForm form) throws Exception {
        this.setStatement("InsertProfile(?,?,?,?)");
        CallableStatement statement = this.getStatement();
        
        statement.registerOutParameter("id",INTEGER);
        statement.setString("login", (String) form.getItem("login"));
        statement.setString("password", (String) form.getItem("password"));
        statement.setString("type", (String) form.getItem("type"));
        
        this.executeUpdate();
        form.setItem("id", statement.getInt("id"));
    }

    @Override
    public void update(DynaActionForm form) throws Exception {
        this.setStatement("UpdateProfile(?,?,?,?)");
        CallableStatement statement = this.getStatement();
        
        statement.setInt("id", (int) form.getItem("id"));
        statement.setString("login", (String) form.getItem("login"));
        statement.setString("password", (String) form.getItem("password"));
        statement.setString("type", (String) form.getItem("type"));
        
        this.executeUpdate();
    }

    @Override
    public void delete(DynaActionForm form) throws Exception {
        this.setStatement("DeleteProfile(?)");
        CallableStatement statement = this.getStatement();
        statement.setInt("id", (int) form.getItem("id"));
        this.executeUpdate();
    }

    @Override
    public List<DynaActionForm> select(DynaActionForm form) throws Exception {
        String selector = (String) form.getItem("selector");
        
        if(selector.equals("byId")){
            this.setStatement("SelectProfileById(?)");
            this.getStatement().setInt("id", (int) form.getItem("id"));
        }
        
        else if(selector.equals("byLogin")){
            this.setStatement("SelectProfileByLogin(?)");
            this.getStatement().setString("login", (String) form.getItem("login"));
        }
        
        else if(selector.equals("byRoom")){
            this.setStatement("SelectParticipantsForRoom(?)");
            this.getStatement().setInt("room", (Integer) form.getItem("room"));
        }
        
        else {
            this.setStatement("SelectProfiles()");
        }
        
        return this.execute();
    }

    @Override
    public boolean valid(DynaActionForm form) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
