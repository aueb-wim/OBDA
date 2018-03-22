<%-- 
    Document   : index
    Created on : Nov 6, 2017, 4:51:55 PM
    Author     : kostis
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="edu.aueb.NPD.RunQueryWEB"%>
<%@page import="edu.aueb.queries.Evaluator"%>
<%@page import="org.oxford.comlab.perfectref.rewriter.PI"%>
<%@page import="edu.aueb.queries.QueryOptimization"%>
<%@page import="org.oxford.comlab.perfectref.parser.DLliteParser"%>
<%@page import="javax.servlet.ServletContext"%>
<%@page import="javax.servlet.ServletException"%>
<%@page import="javax.servlet.http.HttpServlet"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.io.File"%>
<%@page import="java.io.PrintWriter"%>

<%
            /*ServletContext context = getContext();
            String webPath = context.getRealPath("/WEB-INF");*/
            //String webPath = request.getContextPath();
            String webPath = application.getRealPath("/WEB-INF");
            System.out.println("webPath: '"+webPath+"'");
            
            RunQueryWEB rqb = new RunQueryWEB();
            rqb.setPathsWEB(webPath);
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
        %>

<!DOCTYPE html>
<html>
    <head>
        <title>HBP-MIP query</title>
        <meta http-equiv="Content-Type">
        <!--<meta content="text/html; charset=UTF-8">-->
        <meta content="width=device-width, initial-scale=1.0">        
    </head>
    <body>
        <div id="container">
            <h2>MIP Query with Ontology Based Data Access</h2>
        </div>
        
        <form id="queryForm" method="get">
            <div>
                Please type the whole query here:
            </div>
            <div>
            
                <input type="text" id="queryText" name="queryText" size="50">
            </div>
            
                <script language="javascript" type="text/javascript">
    function submitQuery()
    {
        //document.write('**** Lets submit the Query ..... ****');
        //var query = document.getElementById("queryText").innerHTML;
        var query = document.getElementById("queryText").value;
       // document.write('**** Query given: ' + query + ' ****');
       
        
       <%
        //Set<String> tuples = RunQueryWEB.runQuery2();//<--- 8elei ajax: jQuery... afou einai server-side
        
        //for ( String i : tuples ) {
             //document.write('lalalal');
             
        //}
        
        //RunQueryWEB.setPaths();
        
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

        Set<String> tuples = null;
        try {
                tuples = RunQueryWEB.runQuery(eval.conn, RunQueryWEB.queryPath, tBoxAxioms, qOpt);
        } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Smthing went wrong while evaluating query....");
                System.exit(-1);
        }
        Iterator<String> it = tuples.iterator();
        
        String tmp = "ffaf";
        
        while (it.hasNext()) {
            
            tmp = it.next();
                   
            System.out.println(">>> inside loop"+tmp+"<<<");
        }
   response.setContentType("text/html");
   
   try {
//PrintWriter out = response.getWriter();
System.out.print("<div>There is some text here</span>" + tmp);
   }catch( Exception e ) {
       e.printStackTrace();
   }
        %>
          
        
        alert('ok');// + x);
        //document.write('**** Query has been evaluated !!!  ****');
    }
</script>
 
            <div>
                <input type="button" onclick="submitQuery()" value="Submit Query">
                <!--<input type="button" onclick="submitQuery()" value="Submit Query">-->
            </div>
        </form>

        

 <%!
            String someOutput() {
                return "Some output";
            }
        %>
        <% someOutput(); %>
           
        
    </body>
</html>
