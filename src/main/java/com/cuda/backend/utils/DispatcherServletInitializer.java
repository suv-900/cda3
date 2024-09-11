// package com.cuda.backend.utils;

// import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

// import jakarta.servlet.Filter;

// public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

//     public DispatcherServletInitializer(){
//         System.out.println("Creating a new Dispatcher Servlet");
//     }

//     @Override
//     protected Class<?>[] getRootConfigClasses(){
//         return new Class<?>[]{AppConfig.class};
//     }   
    
//     @Override
//     protected Class<?>[] getServletConfigClasses(){
//         return Class<?>[]{ServletConfig.class};
//     }

//     @Override
//     protected String[] getServletMappings(){
//         return new String[] {"/"};
//     }

//     @Override
//     protected Filter[] getServletFilters(){
//         return new Filter[]{};
//     }
// }
