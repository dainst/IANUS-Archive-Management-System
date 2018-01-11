package de.ianus.access.web.servlet;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.WorkflowIP;;


/**
 * Servlet implementation class PermanentURLServlet
 * 
 * https://www.ianus-fdz.de/datenportal/collections/2017-04711/Tell-Fecheriye/B-0002/B-0002_TF-00002_pottery/Dateixyz.jpg
 * 
 * 
 * @author MR
 *
 */
public class PermanentURLServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(PermanentURLServlet.class);
	
	private static String contextPath = "/pages/collectionView.jsp?dipId=";
	
	//public Properties props = null; 
	
	private Properties properties = new Properties();
	
	private static final String PROPERTIES_FILES = "ianus-access.properties";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
//    public PermanentURLServlet() {
//        super();
//        
//        // load props
//        InputStream input = null;
//		try {
//			 input = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILES);
//			 if (input == null) {
//				logger.error("Unable to find properties file");
//				return;
//			}
//			props.load(input);
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		} finally {
//			if (input != null) {
//				try {
//					input.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//    }
	
	
	public PermanentURLServlet(){
		
		try {
			InputStream input = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILES);
			
			if(input != null){
				properties.load(input);
			}
			
			input.close();
			
		} catch (IOException e) {
			logger.error("Properties file \'" + PROPERTIES_FILES + " no found.", e);
			e.printStackTrace();
		} 
	}
	
	
    
    @Override
	public void init() throws ServletException {

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String fullPath = request.getPathInfo();
		
		if(fullPath != null && !fullPath.equals("/")){
			
			String [] paths = fullPath.split("/");
			
			List<String> pathList = Arrays.asList(paths);
			
			String relativepath = relativePath(fullPath); 
			
			System.out.println("Relative Path: " + relativepath);

			String collectionId = pathList.get(1);
				
			Long iId = getInternalId(collectionId);
				
			if(iId != null){
						
				String newURL = getDomainName() + getContextPath() + iId;
					
				response.setContentType("text/html");
					
				response.sendRedirect(newURL);
				
			}else {
					
				PrintWriter out = response.getWriter();
					
				out.print("<html><body><h1>Not found any Internal ID</h1><p>" + collectionId + "</p></body></html>");
				
				out.flush();
			}
	
		}else {
			response.sendRedirect(getDomainName());
		}
		
	}
	
	private Long getInternalId(String collectionId){
		
		Long iId = null;
		
		Set<WorkflowIP> dlist = Services.getInstance().getDaoService().getDipByIanusIdentifier(collectionId);
		
		if(!dlist.isEmpty()){
			for(WorkflowIP dip : dlist){
				 iId = dip.getId();
			}
		}
		
		return iId;
	}
	
	private String getDomainName(){
		
		return this.properties.getProperty("home.url");
		
	}
	
	private String getContextPath(){
		
		return contextPath;
	}
	
	private static String relativePath(String fullPath) {
		
		String[] paths = fullPath.split("/");
		
		List<String> pathList = Arrays.asList(paths);
		
		return fullPath.replace(pathList.get(1) + "/" , "");
	
	}
	
	
	@Override
	public void destroy() {
		// do nothing.
	}

}
