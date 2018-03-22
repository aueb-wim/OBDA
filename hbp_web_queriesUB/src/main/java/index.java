/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sotiris
 */
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
 
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;


import java.util.ArrayList;

import edu.aueb.NPD.RunQueryWEB;
import edu.aueb.queries.Evaluator;
import org.oxford.comlab.perfectref.rewriter.PI;
import edu.aueb.queries.QueryOptimization;
import org.oxford.comlab.perfectref.parser.DLliteParser;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Set;
import java.util.Iterator;
import java.io.File;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

@WebServlet("/HelloServlet")
// Extend HttpServlet class
public class index extends HttpServlet {
 
   // Method to handle GET method request.
   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
   
      // Set response content type
      response.setContentType("text/html");
   
      PrintWriter out = response.getWriter();
            
       try {
           mvc_writer.header(out, request);
       } catch (SQLException ex) {
           Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
       }

       mvc_writer.print_query_form(out, "", false);
      
       mvc_writer.footer(out);
      
   }
   
   private boolean isMultipart;
   private String filePath;
   private int maxFileSize = 50 * 1024;
   private int maxMemSize = 4 * 1024;
   private File file ;
   
   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         
        HttpSession session = request.getSession();

        PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF8"), true);

        response.setContentType("text/html;  charset=UTF-8");
        
        //begin set path
        /*ServletContext context = getContext();
        String webPath = context.getRealPath("/WEB-INF");*/
        //String webPath = request.getContextPath();

        String webPath = getServletContext().getRealPath("/WEB-INF");
        //String webPath = application.getRealPath("/WEB-INF");
        System.out.println("webPath: '"+webPath+"'");

        String queryText = "";
        
        Set<String> tuples = null;
        
        String tmp_query = "";
           
        //begin
        // Check that we have a file upload request
        isMultipart = ServletFileUpload.isMultipartContent(request);

        DiskFileItemFactory factory = new DiskFileItemFactory();

        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);

        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File("/tmp"));

        filePath = "/tmp/";

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // maximum file size to be uploaded.
        //upload.setSizeMax( maxFileSize );

        boolean uploadViaFile = false;
        
        try { 
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);
            //List<FileItem> fileItems = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

            // Process the uploaded file items
            Iterator i = fileItems.iterator();

            while ( i.hasNext () ) {
                FileItem fi = (FileItem)i.next();
                if ( !fi.isFormField () ) {
                    
                    System.out.println("Got a non form field: " + fi.getFieldName()+ " " + fi.getString() );

                    // Get the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    String contentType = fi.getContentType();
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();

                    if ( fi.getFieldName().equals("file") ) {
                        queryText = fi.getString();
                    }else if ( fi.getFieldName().equals("ontologyfile") ) {
                        //Save to disk
                        
                        // Write the file
                        if( fileName.lastIndexOf("\\") >= 0 ) {
                            file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\")));
                            RunQueryWEB.ontologyFile = filePath + fileName.substring( fileName.lastIndexOf("\\") );
                        } else {
                            file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
                            RunQueryWEB.ontologyFile = filePath + fileName.substring(fileName.lastIndexOf("\\")+1);
                        }
                        fi.write( file ) ;
                        out.println("Uploaded Filename: " + fileName + " " + fi.getString() + "<br>");
                        
                    }else {
                        System.out.println( ":( " + fi.getFieldName() );
                    }
                }else {

                     System.out.println("Got a form field: " + fi.getFieldName()+ " " + fi.getString() );

                     if ( fi.getFieldName().equals("hiddenFlagValue") ) {
                         if ( fi.getString().equals("1") ) {
                             uploadViaFile = true;
                             System.out.println( "uploadViaFile" );
                         }else {
                             System.out.println( "Don't upload");
                         }
                     }else if ( fi.getFieldName().equals("queryText") ) {
                         tmp_query = fi.getString();
                     }

                }
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }

        //end
        
        if ( !uploadViaFile ) {
            queryText = tmp_query;
        }


        String tmp_ontology_file = "file:" + RunQueryWEB.ontologyFile;
        System.out.println( "ontology file points to " + RunQueryWEB.ontologyFile );
        
        //begin
        ///RunQueryWEB.setPaths();
	
        RunQueryWEB rqb = new RunQueryWEB();
        rqb.setPathsWEB(webPath);
        
        RunQueryWEB.ontologyFile = tmp_ontology_file;//location of uploaded ontology file
        
        Evaluator eval = null;
        ArrayList<PI> tBoxAxioms = null;
        
        try {
            System.out.println("*** 1 ***");
            eval = new Evaluator(RunQueryWEB.dbPath, RunQueryWEB.mappings, false);//false gia na MHN kanoume Mapping Optimization
            System.out.println("*** 2 ***");
            System.out.println("    *** 2.i ***");
            //eval.createConnection(); //<-- no need: it is done in Evaluator constructor
            System.out.println("    *** 2.ii ***");
            tBoxAxioms = RunQueryWEB.parser.getAxiomsWithURI(RunQueryWEB.ontologyFile);
            System.out.println("*** 3 ***");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Smthin went wrong in the beginnin...");
            System.exit(-1);
        }
        QueryOptimization qOpt = null;
        if (RunQueryWEB.optPath != null && RunQueryWEB.optPath != "") {
            qOpt = new QueryOptimization(RunQueryWEB.optPath);
        }

        System.out.println("***** Done with the preparation stuff... *****");
      //end set path
      
      //begin
          eval = null;
        tBoxAxioms = null;
            try {
                eval = new Evaluator(RunQueryWEB.dbPath, RunQueryWEB.mappings, true);
		eval.createConnection();
		tBoxAxioms = RunQueryWEB.parser.getAxiomsWithURI(RunQueryWEB.ontologyFile);
	} catch (Exception e) {
            e.printStackTrace();
            System.err.println("Smthin went wrong in the beginnin...");
            System.exit(-1);
	}
	
        qOpt = null;
        if (RunQueryWEB.optPath != null && RunQueryWEB.optPath != "") {
            qOpt = new QueryOptimization(RunQueryWEB.optPath);
        }

        //end
        
        try {
            System.out.println( "query2 has := " + queryText );
            tuples = RunQueryWEB.runQuery2(eval.conn, queryText, tBoxAxioms, qOpt);
            //tuples = RunQueryWEB.runQuery(eval.conn, RunQueryWEB.queryPath, tBoxAxioms, qOpt);
        } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Smthing went wrong while evaluating query....");
                System.exit(-1);
        }
        Iterator<String> it = tuples.iterator();
      
        try {
           mvc_writer.header(out, request);
       } catch (SQLException ex) {
           Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
       }

       mvc_writer.print_query_form(out,queryText, uploadViaFile );
       
       out.println( "<h2> You query </h2>" );
       
       out.println( queryText );
       
       out.println( "<hr>" );
       
       out.println( "<h2> Total results found </h2>" );
       
       out.println( tuples.size() );
       
       out.println( "<hr>" );
       
        out.println( "<table border='1'>");
        
        out.println( "<tr>" );
        
            out.println( "<th>whole</th>" );
            out.println( "<th>link</th>" );
            out.println( "<th>tag</th>" );
            out.println( "<th>value</th>" );
        
        out.println( "</tr>" );
        
        String tmp = "";
        
        while (it.hasNext()) {
        
            tmp = it.next();
            
            String tokens[] = tmp.split("----");
        
            out.println( "<tr>");
                out.println( "<td>");
                    out.println( tmp );
                out.println( "</td>" );
                
                for ( int i = 0; i < tokens.length; ++i ) {
                    out.println( "<td>");
                        out.print( tokens[i] );
                    out.println( "</td>" );
                }
            out.println( "</tr>" );
        }
        
        out.println( "</table>" );
       
        mvc_writer.footer(out);
       
   }
   
  
   
   
}
