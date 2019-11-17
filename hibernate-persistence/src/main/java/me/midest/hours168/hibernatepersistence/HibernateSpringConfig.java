package me.midest.hours168.hibernatepersistence;

import me.midest.hours168.core.model.Category;
import me.midest.hours168.core.model.Day;
import me.midest.hours168.core.model.TimeSegment;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan( "me.midest.hours168" )
public class HibernateSpringConfig {

    @Bean
    public SessionFactory sessionFactory(){
        return new org.hibernate.cfg.Configuration()
                .configure( "hibernate.cfg.xml" )
                .addAnnotatedClass( Category.class )
                .addAnnotatedClass( TimeSegment.class )
                .addAnnotatedClass( Day.class )
                .buildSessionFactory();
    }

}
