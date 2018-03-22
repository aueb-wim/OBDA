# OBDA module

The Ontology Based Data Access (OBDA) module reformulates queries so as to capture the knowledge of the ontologies when querying data. Using the module's interface the user writes a Query or chooses one or more from her local files. Queries have to be in Datalog form. Once the Query is given for execution, the module's main component, IQAROS, takes control and does Query Rewriting so as to generate a Query which takes into account the knowledge from the chosen Ontology, Access Control rules that may have been given as well and do all that within an efficient rewriting minimisation.

In this version, the module after the Query Rewriting is over, connects to the NPD database, which is an OBDA benchmark.

This repository contains 2 projects:
  - hbp_web_queriesUB which is the web interface
  - IQAROS which is the Query Rewriting engine

GlashFish Settings:

1) Navigate to glashfish admin panel, localhost:4848
2) Go to Configurations->server-config->JVM Settings
3) Then go to "JVM Options" tab
4) Increase the "-Xmx" parameter e.x. to -Xmx5012m
5) Save & Restart the server to apply changes

<h2> Servlet Example </h2>

Query: Type the query or upload the file containing it
Ontology file: upload the .owl file

![](servlet_images_example/type_query.png?raw=true)

<h2> After execution </h2>

![](servlet_images_example/type_query_result.png?raw=true)
