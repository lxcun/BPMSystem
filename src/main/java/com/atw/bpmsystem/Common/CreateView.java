package com.atw.bpmsystem.Common;


import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;




public class CreateView {
    public void createView(String sql) {
        Connection con=getConnection();
        Statement sm = null;
        try {
            sm = con.createStatement();
            sm.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭Statement
            if (sm != null) {
                try {
                    sm.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public static String getValue(String key){
        Properties prop = new Properties();
        InputStream in = new CreateView().getClass().getResourceAsStream("/application.properties");
        try {
            prop.load(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(key);
    }

    public static Connection getConnection() {
        Connection con = null;
        String url=getValue("spring.datasource.url");
        String username=getValue("spring.datasource.username");
        String password=getValue("spring.datasource.password");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
