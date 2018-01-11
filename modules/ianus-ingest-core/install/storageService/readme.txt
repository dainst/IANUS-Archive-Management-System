== Initialize Storage Service ==

Go to: http://localhost:8080/ianus-ingest-web

On the top right corner click on "Process Engine".
Push the first Button "Submit" next to "Deploy transfer workflow".
Click on "Working Storage" (on the left/ next to "Process Engine").
Push the second Button "Init Core".


Check if the following folders are created. 
The user of tomcat should have wro on these folders (if the folders are not created automatically, there are probably right problems):

/data/ianus/aip_storage
/data/ianus/dip_storage
/data/ianus/sip_storage
/data/ianus/tip_storage

TODO: This folder is not created automatically -> we need to check, if we need it any longer
/data/ianus/working_storage 


