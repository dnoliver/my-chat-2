/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.ubp.das.daos;

import ar.edu.ubp.das.mvc.actions.DynaActionForm;
import ar.edu.ubp.das.mvc.daos.MSSQLDao;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static java.sql.Types.INTEGER;
import java.util.List;

/**
 *
 * @author dnoliver
 */
public class MSSQLRoomDao extends MSSQLDao {

    @Override
    public DynaActionForm make(ResultSet result) throws Exception {
        DynaActionForm room = new DynaActionForm();
        
        room.setItem("id", result.getInt("id"));
        room.setItem("name", result.getString("name"));
        room.setItem("type", result.getString("type"));
        room.setItem("owner", result.getInt("owner"));
        
        return room;
    }

    @Override
    public void insert(DynaActionForm form) throws Exception {
        this.setStatement("InsertRoom(?,?,?,?)");
        CallableStatement statement = this.getStatement();
        
        statement.registerOutParameter("id",INTEGER);
        statement.setString("name", (String) form.getItem("name"));
        statement.setString("type", (String) form.getItem("type"));
        statement.setInt("owner", (Integer) form.getItem("owner"));
        
        this.executeUpdate();
        form.setItem("id", statement.getInt("id"));
    }

    @Override
    public void update(DynaActionForm form) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(DynaActionForm form) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DynaActionForm> select(DynaActionForm form) throws Exception {
        String selector = (String) form.getItem("selector");
        
        if(selector.equals("byId")){
            this.setStatement("SelectRoomById(?)");
            this.getStatement().setInt("id", (Integer) form.getItem("id"));
        }
         
        else if(selector.equals("byName")){
            this.setStatement("SelectRoomByName(?)");
            this.getStatement().setString("name", (String) form.getItem("name"));
        }
        
        else if(selector.equals("byOwner")){
            this.setStatement("SelectRoomByOwner(?)");
            this.getStatement().setInt("owner", (Integer) form.getItem("owner"));
        }
        
        else {
            this.setStatement("SelectRooms()");
        }
        
        return this.execute();
    }

    @Override
    public boolean valid(DynaActionForm form) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
