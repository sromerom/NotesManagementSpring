package com.liceu.sromerom.utils;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Database {
    private static Connection connection;
    //@Autowired
    //static AnnotationConfigWebApplicationContext context;

    public static Connection getConnection() {
        //Properties properties = (Properties) context.getBean("properties");
        //System.out.println(properties.getProperty("DB_DRIVER"));
        try {
            //Class.forName(properties.getProperty("DB_DRIVER"));
            //String url = properties.getProperty("DB_URL_WINDOWS");

            Class.forName("org.sqlite.JDBC");
            //#### Windows ####//
            //String url = "jdbc:sqlite:E:\\Fp informatica\\CFGS 2n any REP\\PracticasEntornServidor\\NotesManagementSpring\\databaseManagement.db";
            //#### Linux ####//
            String url = "jdbc:sqlite:/home/superior/sromerom/Practiques Entorn Servidor/NotesManagementSpring/databaseManagement.db";


            if (connection == null) {
                connection = DriverManager.getConnection(url);
                connection.createStatement().execute("PRAGMA foreign_keys = ON");
                connection.createStatement().execute("PRAGMA encoding = 'UTF-16'");
            }
            return connection;
        } catch (Exception e) {
            return null;
        }
    }
}

