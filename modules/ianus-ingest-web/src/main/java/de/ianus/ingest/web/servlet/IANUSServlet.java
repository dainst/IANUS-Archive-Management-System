package de.ianus.ingest.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

import de.ianus.ingest.core.Services;
import de.ianus.ingest.core.bo.InformationPackage;
import de.ianus.metadata.json.JSONUtils;

/**
 * 
 * #### Retrieve
 * http://localhost:8080/ianus-ingest-web/rest/retrieve/tp/2023/data/bagit_2017-01-26-03-20-35/D-DAI-Z-TAG08-0002.JPG
 * 
 * home = http://localhost:8080/ianus-ingest-web
 * servlet name= rest
 * method = retrieve
 * context = tp (information package type)
 * IP id = 2023
 * path = data/bagit_2017-01-26-03-20-35/D-DAI-Z-TAG08-0002.JPG
 * 
 * 
 * @author jurzua
 *
 */
public class IANUSServlet extends HttpServlet {
	private static final long serialVersionUID = 1378131095808241672L;
	private static final Logger logger = LogManager.getLogger(IANUSServlet.class);

	public static String METHOD_JSON = "json";
	public static String METHOD_RETRIEVE = "retrieve";
	
	@Override
	public void init() throws ServletException {

	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doIt(request, response);
	}

	public void doIt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Set response content type
		// response.setContentType("text/html");

		String pathInfo = request.getPathInfo();

		String[] paths = pathInfo.split("/");
		List<String> pathList = Arrays.asList(paths);
		System.out.println("pathInfo: " + pathInfo);

		String method = pathList.get(1);
		String context = pathList.get(2);
		String ipType = pathList.get(2); //tp, sip, aip, dip

		System.out.println(method);
		System.out.println(context);
		System.out.println(pathList.get(3));
		 
		if (StringUtils.equals("json", method)) {
			String objectClass = pathList.get(3);
			response.setContentType("application/json");
			JsonObject json = new JsonObject();

			try {
				json = new JsonObject();
				if (StringUtils.equals("metadata", context)) {
					if (StringUtils.equals("dataCollection", objectClass)) {
						json = getDataCollection(request, pathList);
					} else if (StringUtils.equals("collectionFile", objectClass)) {
						json = getCollectionFile(request, pathList);
					}
				}
				json.toString();
			} catch (Exception e) {
				e.printStackTrace();
				json = new JsonObject();
				json.addProperty("exception", e.toString());
			}
			
			PrintWriter out = response.getWriter();
			out.print(json.toString());
			out.flush();
		} else if (StringUtils.equals(METHOD_RETRIEVE, method)) {
			try {
				String param3 = pathList.get(3);
				Long ipId = Long.parseLong(param3);
				InformationPackage ip = null;
				if(StringUtils.contains(ipType, "tp")){
					ip = Services.getInstance().getDaoService().getTransfer(ipId);
				}else if(StringUtils.contains(ipType, "sip")){
					ip = Services.getInstance().getDaoService().getSip(ipId);
				}else if(StringUtils.contains(ipType, "aip")) {
					ip = Services.getInstance().getDaoService().getAip(ipId);
				}else {
					throw new Exception("Servlet method not implemented for Information Package type = " + ipType);
				}
				
				String absoluteFilePath = ip.getAbsolutePath() + getRelativePath(pathInfo);
				logger.info("downloading " + absoluteFilePath);
				File file = new File(absoluteFilePath);

				if (file.exists()) {
					// This should send the file to browser
					OutputStream out = response.getOutputStream();
					FileInputStream in = new FileInputStream(file);
					byte[] buffer = new byte[4096];
					int length;
					while ((length = in.read(buffer)) > 0) {
						out.write(buffer, 0, length);
					}
					in.close();
					out.flush();
					
					response.setContentType("text/html");
					response.setHeader("Content-disposition", "attachment; filename=error.html");
					
				} else {
					PrintWriter out = response.getWriter();
					out.print("<html><body><h1>Error: File no found</h1><p>" + absoluteFilePath + "</p></body></html>");
					out.flush();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String getRelativePath(String pathInfo) {
		String[] paths = pathInfo.split("/");
		List<String> pathList = Arrays.asList(paths);
		return pathInfo.replace("/" + pathList.get(1) + "/" + pathList.get(2) + "/" + pathList.get(3), "");
	}

	public static String getFileName(String pathInfo) {
		String[] paths = pathInfo.split("/");
		return paths[paths.length - 1];
	}

	public static String getExtensionFile(String fileName) {
		String[] parts = fileName.split("\\.");
		return parts[parts.length - 1];
	}

	public static String getContentType(String fileExtension) {
		fileExtension = fileExtension.toLowerCase();
		if (StringUtils.equals("pdf", fileExtension)) {
			return "application/pdf";
		} else if (StringUtils.equals("jpg", fileExtension) || StringUtils.equals("git", fileExtension)) {
			return "image/jpg";
		} else if (StringUtils.equals("png", fileExtension)) {
			return "image/png";
		} else if (StringUtils.equals("tif", fileExtension) || StringUtils.equals("tiff", fileExtension)) {
			return "image/tiff";
		} else if (StringUtils.equals("", fileExtension)) {
			return "text/html";
		}
		return "text/plain";
	}

	/**
	 * 
	 * Examples: rest/json/metadata/dataCollection/?id=41
	 * rest/json/metadata/dataCollection/collectionFiles/?id=41
	 * 
	 * @param request
	 * @param json
	 * @param pathList
	 * @throws Exception
	 */
	public JsonObject getDataCollection(HttpServletRequest request, List<String> pathList) throws Exception {
		JsonObject json = new JsonObject();
		String component = (pathList.size() >= 5) ? pathList.get(4) : null;
		Long dcId = getLong(request, "id");
		if (dcId != null) {
			if (StringUtils.isEmpty(component)) {
				json = Services.getInstance().getMDService().getDataCollection(dcId).toJsonObject(new JsonObject());
			} else if (StringUtils.equals("collectionFiles", component)) {
				json = JSONUtils.collectionFileListToJsonObject(Services.getInstance().getMDService(), dcId);
			}
		} else {
			mandatoryError(json, "id");
		}
		return json;
	}

	private void mandatoryError(JsonObject json, String... varList) {
		json.addProperty("error", "Parameter id is mandatory");
	}

	public JsonObject getCollectionFile(HttpServletRequest request, List<String> pathList) throws Exception {
		JsonObject json = new JsonObject();
		String component = (pathList.size() >= 5) ? pathList.get(4) : null;
		if (StringUtils.isEmpty(component)) {
			Long fileId = getLong(request, "id");
			if (fileId != null) {
				json = JSONUtils.collectionFileToJsonObject(Services.getInstance().getMDService(), fileId);
			} else {
				mandatoryError(json, "id");
			}
		}
		return json;
	}

	private Long getLong(HttpServletRequest request, String parameterName) {
		Object obj = request.getParameter(parameterName);
		if (obj != null) {
			return Long.parseLong((String) obj);
		}
		return null;
	}

	@Override
	public void destroy() {
		// do nothing.
	}
}
