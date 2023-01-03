package com.example.onlineexamination;

import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connection_class  {
    public Connection conn;
    public Statement st;
    public ResultSet rs;

    public Connection_class()
    {
        try {
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","root");
            st= conn.createStatement();
        } catch (Exception e) {
            System.out.print("Error in Connection");
        }

    }

    private boolean fire(String qry)
    {
        boolean x;
        try {
            x = st.execute(qry);

        } catch (Exception e) {
            x=false;
        }
        return x;
    }

    private ResultSet getfire(String qry)
    {
        try {
            this.rs= st.executeQuery(qry);
        } catch (Exception e) {
            System.out.print("error in fire query " + e.getMessage());
        }
        return this.rs;
    }

    public ResultSet getdata(String table)
    {
        String qry;
        qry="select * from " + table+";";
        this.rs= this.getfire(qry);
        return this.rs;
    }

    public ResultSet getdata(String field, String table, String value)
    {
        String qry;
        qry = "select "+field+" from "+table+" "+value+";";
        this.rs = this.getfire(qry);
        return this.rs;
    }

    public ResultSet countColumn(String field, String table){
        String qry;
        qry = "select count("+field+") from "+table+";";
        System.out.println(qry);
        this.rs = this.getfire(qry);
        return this.rs;

    }

}
