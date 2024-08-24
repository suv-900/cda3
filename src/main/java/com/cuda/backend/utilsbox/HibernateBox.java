package com.cuda.backend.utilsbox;

import java.sql.Connection;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.cuda.backend.entities.User;

public class HibernateBox {
   
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory(){
        try{
            Properties prop = new Properties();
            prop.setProperty("jakarta.persistence.jdbc.url","");
            prop.setProperty("jakarta.persistence.jdbc.user",""); 
            prop.setProperty("jakarta.persistence.jdbc.password","");

            prop.setProperty("hibernate.connection.pool_size","5"); 
            prop.setProperty("hibernate.jdbc.batch_size","10");
            prop.setProperty("hibernate.show_sql","true");
            prop.setProperty("hibernate.format_sql","true");
            prop.setProperty("hibernate.connection.isolation",String.valueOf(Connection.TRANSACTION_SERIALIZABLE));

            SessionFactory sf = new Configuration()
                .addAnnotatedClass(User.class)
                .setProperties(prop)
                .buildSessionFactory();
            return sf;
        }catch(Throwable err){
            throw err;
        }
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
