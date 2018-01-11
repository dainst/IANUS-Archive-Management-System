package de.ianus.ingest.core.processEngine.ms;

import java.util.List;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.processEngine.ms.utils.SpecialCharactersUtils;
import de.ianus.ingest.core.processEngine.ms.utils.SpecialCharactersUtils.SpecialCharacterResult;

/**
 * This MS will replace all special characters in the files stored in the
 * temporal folder (files names and folder names). Report into the
 * (ActivityOutput) output object. To implement this method, we can use the
 * method: Services.getInstance().getStorageService().
 * getRecursiveFilePathInTmpDataFolder(sip)
 *
 * @author jurzua
 */
public class MSSpecialCharacterProcessing extends AbstractTask implements ActivityBehavior {

	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution);

		try {

			List<SpecialCharacterResult> rs = SpecialCharactersUtils.recognize(wfip);
			for(SpecialCharacterResult item : rs){
				this.output.print(item.toString());
			}
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("characters_processing_ok");
			finishExecution();
		}
	}
	
	

}