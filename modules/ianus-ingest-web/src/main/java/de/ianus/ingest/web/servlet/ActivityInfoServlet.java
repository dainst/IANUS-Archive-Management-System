package de.ianus.ingest.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;

import de.ianus.ingest.core.Services;

/**
 * This class was meant to prevent crashing running activities by premature redeployment of the application. 
 * Challenges are, to filter the user tasks and to retrieve a list of activities currently performed from camunda.
 * 
 * Yet, the query does NOT return a list of active tasks.
 * 
 * @author hendrik
 *
 */

public class ActivityInfoServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9018068884533727169L;
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Boolean active = false;
		Integer activities = 0;
		try {
			TaskQuery query = Services.getInstance().getProcessEngineService().getProcessEngine().getTaskService().createTaskQuery();
			List<Task> list = query.active().list();
			if(list != null && list.size() > 0) {
				for(Task task : list) {
					// ingest workflow tasks
					if(	!task.getTaskDefinitionKey().toLowerCase().startsWith("user")
					&&	!task.getTaskDefinitionKey().equals("collection_id_editor")
					&&	!task.getTaskDefinitionKey().equals("files_merge_transfer_packages")
					// transfer workflow
					&&	!task.getTaskDefinitionKey().equals("metadata_edition_initial")
					&&	!task.getTaskDefinitionKey().equals("metadata_edition_initial_evaluation")
					&&	!task.getTaskDefinitionKey().equals("data_upload")) {
						out.println(task.getTaskDefinitionKey());
						activities++;
					}
				}
				if(activities > 0) {
					out.println(activities + " ongoing activities");
				}else{
					out.print("no ongoing activities");
				}
			}else{
				out.print("no ongoing activities");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
	}
	
	
}
