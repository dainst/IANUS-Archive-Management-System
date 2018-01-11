package de.ianus.ingest.core.processEngine.ms;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.bo.SubmissionIP;
import de.ianus.ingest.core.processEngine.ms.utils.SpecialCharactersUtils;
import de.ianus.ingest.core.processEngine.ms.utils.SpecialCharactersUtils.SpecialCharacterResult;

/**
 * This MS will detect special characters and will report writing the cases into
 * the (ActivityOutput) output object. The list of special characters is store
 * in a file in the class path.
 * <p>
 * To implement this method, we can use the method:
 * Services.getInstance().getStorageService().
 * getRecursiveFilePathInTmpDataFolder(sip)
 *
 * @author jurzua
 */

public class MSIdentifySpecialCharacters extends AbstractTask implements ActivityBehavior {

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution);

		try {
			List<SpecialCharacterResult> rs = SpecialCharactersUtils.recognize(this.wfip);
			for(SpecialCharacterResult item : rs){
				this.output.print(item.toHtml());
			}
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("identify_characters_ok");
			finishExecution();
		}

	}

	private void oldImplementation() throws Exception {
		// Parse special characters file
		Map<String, String> charactersMap = new HashMap<String, String>();
		InputStream is = this.wfip.getClass().getClassLoader().getResourceAsStream("SpecialCharactersProcessing.txt");
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = "";

			while ((line = reader.readLine()) != null) {

				String parts[] = line.split("\\s+", 2);
				charactersMap.put(parts[0], parts[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Throwable ignore) {
			}
		}

		// Create object to get information about special characters in file and
		// folder names
		SpecialCharactersUtils scu = new SpecialCharactersUtils((SubmissionIP) this.wfip, charactersMap);

		// Check the information about the names (files and folders)
		if (scu.isContainsSC() == true) {
			for (Map.Entry e : scu.getSpecialCharacterInFileNames().entrySet()) {
				output.print("This special character -> " + e.getValue() + " is  used in filename: " + e.getKey()
						+ " - please rename!");
			}
			for (Map.Entry e : scu.getSpecialCharacterInFolderNames().entrySet()) {
				output.print("This special character -> " + e.getValue() + " is used in foldername: " + e.getKey()
						+ " - please rename!");
			}

		} else {
			output.print("Names checked - everything seems to be fine!");

		}
	}

}