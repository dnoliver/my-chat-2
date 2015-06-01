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
import java.util.Date;
import java.util.List;

/**
 *
 * @author dnoliver
 */
public class MSSQLUserLoginDao extends MSSQLDao {

    @Override
    public DynaActionForm make(ResultSet result) throws Exception {
        DynaActionForm userlogin = new DynaActionForm();
        
        userlogin.setItem("id", result.getInt("id"));
        userlogin.setItem("profile", result.getInt("profile"));
        userlogin.setItem("datetimeOfAccessStart", result.getTimestamp("datetime_of_access_start"));
        userlogin.setItem("datetimeOfAccessEnd", result.getTimestamp("datetime_of_access_end"));
        
        return userlogin;
    }

    @Override
    public void insert(DynaActionForm form) throws Exception {
        this.setStatement("InsertUserLogin(?,?,?,?)");
        CallableStatement statement = this.getStatement();
        Date start = (Date) form.getItem("datetimeOfAccessStart");
        Date end = (Date) form.getItem("datetimeOfAccessEnd");
        java.sql.Timestamp sql_start, sql_end;
        
        sql_start = start == null ? null : new java.sql.Timestamp( start.getTime() );
        sql_end = end == null ? null : new java.sql.Timestamp( end.getTime() );
        
        statement.registerOutParameter("id",INTEGER);
        statement.setInt("profile", (int) form.getItem("profile"));
        statement.setTimestamp("datetimeOfAccessStart", sql_start);
        statement.setTimestamp("datetimeOfAccessEnd", sql_end);
        
        this.executeUpdate();
        form.setItem("id", statement.getInt("id"));
    }

    @Override
    public void update(DynaActionForm form) throws Exception {
        this.setStatement("UpdateUserLogin(?,?,?,?)");
        Date start = (Date) form.getItem("datetimeOfAccessStart");
        Date end = (Date) form.getItem("datetimeOfAccessEnd");
        java.sql.Timestamp sql_start, sql_end;
        
        sql_start = start == null ? null : new java.sql.Timestamp( start.getTime() );
        sql_end = end == null ? null : new java.sql.Timestamp( end.getTime() );
        
        this.getStatement().setInt("id", (int) form.getItem("id"));
        this.getStatement().setInt("profile", (int) form.getItem("profile"));
        this.getStatement().setTimestamp("datetimeOfAccessStart", sql_start);
        this.getStatement().setTimestamp("datetimeOfAccessEnd", sql_end);
        
        this.executeUpdate();
    }

    @Override
    public void delete(DynaActionForm form) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DynaActionForm> select(DynaActionForm form) throws Exception {
        String selector = (String) form.getItem("selector");
        
        if(selector.equals("byId")){
            this.setStatement("SelectUserLoginById(?)");
            this.getStatement().setInt("id", (int) form.getItem("id"));
        }
        
        else if(selector.equals("byProfile")){
            this.setStatement("SelectUserLoginByProfile(?)");
            this.getStatement().setInt("profile", (int) form.getItem("profile"));
        }
        
        else if(selector.equals("byLastLogin")){
            this.setStatement("SelectLastUserLogin(?)");
            this.getStatement().setInt("profile", (int) form.getItem("profile"));
        }
        
        else {
            this.setStatement("SelectUsersLogins()");
        }
        
        return this.execute();
    }

    @Override
    public boolean valid(DynaActionForm form) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
