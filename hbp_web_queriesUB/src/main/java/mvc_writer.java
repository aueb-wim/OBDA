
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sotiris
 */
public class mvc_writer {
 
    private static String getValue(Part part) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
        StringBuilder value = new StringBuilder();
        char[] buffer = new char[1024];
        for (int length = 0; (length = reader.read(buffer)) > 0;) {
            value.append(buffer, 0, length);
        }
        return value.toString();
    }
    
     public static void clear( PrintWriter out ) {
        out.println( "<div class='clear'></div>" );
    }
    
    public static void header( PrintWriter out, HttpServletRequest request ) throws SQLException {
         
        out.println("<!DOCTYPE html>"
                + "<html>"
                    + "<head>"
                        + "<title>HBP-MIP query</title>"
                        + "<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />"
                        + "<!--<meta content=\"text/html; charset=UTF-8\">-->\n"
                        + "<meta content=\"width=device-width, initial-scale=1.0\">\n"
                        + "<link rel=\"stylesheet\" type=\"text/css\" href=\"obda.css\" >"
                    + "</head>"
                + "<body>"
                + "<div id='container'>" );
        
        out.println( "<h2 style='margin-top:20px;'>MIP Query with Ontology Based Data Access</h2>" );
    
        out.println( "<div style='border-radius: 15px; margin-top:20px; padding: 20px; border: 1px solid black;'>");
         
        clear( out );
         
    }
     
    public static void footer( PrintWriter out ) {
         
        out.println( "</div>" );
        out.println(    "</div>"
                    + "</body>"
                + "</html>");
         
    }
    
    public static void print_query_form( PrintWriter out, String query, boolean uploadFile ) {
              
              out.println(
"        <form action='HelloServlet' id=\"queryForm\" method=\"post\"  enctype='multipart/form-data'> " );


              int fl = 1;
              if ( uploadFile == false ) {
                  fl = 0;
              }
              
              out.println( "<input type='hidden' id='hiddenFlagValue' name='hiddenFlagValue' value='" + fl + "'> " );
              
              out.println( "Choose method to write query" );
              out.println( "<select onchange='toggle_choice()'>");
                out.println( "<option> Type query </option>" );
                out.println( "<option> Upload file </option>" );
              out.println( "</select>");
              
              out.println( "<script>");
              
              out.println( "function toggle_choice() {"
                      + " var x = document.getElementById(\"divText\");\n" +
                      " var y = document.getElementById(\"divUpload\");\n" +
                    " var z = document.getElementById(\"hiddenFlagValue\");\n" +
                    "    if (x.style.display === \"none\") {\n" +
                    "        x.style.display = \"block\";\n" +
                    "        y.style.display = \"none\";\n" +
                    "        z.value = \"0\";\n" +
                      
                    "    } else {\n" +
                    "        x.style.display = \"none\";\n" +
                    "        y.style.display = \"block\";\n" +
                    "        z.value = \"1\";\n" +
                    "    }"
                      + "}");
              
              out.println( "</script>");
              
              out.println( "<hr>" );
              
              String what = "";
              
              if ( uploadFile == true ) {
                what = "style=\"display: none;\"";
              }
              
              out.println( "<div id=\"divText\" " + what + ">" );
              out.println(
"                Please type the whole query here: <br>" + 
"                <textarea rows='10' cols='72' id=\"queryText\" name=\"queryText\" >" + query + "</textarea>" );

              out.println( "</div>" );
              
              if ( uploadFile == false ) {
                what = "style=\"display: none;\"";
              }
              
              out.println( "<div id=\"divUpload\" " + what + ">" );
              
              out.println( "Please upload the query file:<br>" +
                      "<input type='file' id='file' name='file' />" );
              
              out.println( "</div>" );
              
              out.println( "<hr>" );
              
              out.println( "Please select ontology file" );
              out.println( "<input type='file' id='ontologyfile' name='ontologyfile' />" );
              out.println( "<hr>");
              
      out.println( "<div>" );
          out.println( "<input type='submit' class='greenbtn' value='Submit query'>");
      out.println( "</div>" );
      
      out.println( "</form>" );
        
    }
    
}
