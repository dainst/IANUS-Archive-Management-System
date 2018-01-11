package de.ianus.ingest.core.processEngine.ms;

import org.camunda.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import de.ianus.ingest.core.processEngine.ms.utils.SpecialCharactersUtils;

public class MSReplaceNotAllowedCharacters extends AbstractTask implements ActivityBehavior{

	
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		super.execute(execution); 
		
		try {
			
			SpecialCharactersUtils util = new SpecialCharactersUtils(this.output);
			util.replaceForbidden(this.wfip, null);
			
			
		} catch (Exception e) {
			setException(e);
		} finally {
			transition = execution.getActivity().findOutgoingTransition("sf_replace_not_allowed_characters_ok");
			finishExecution();
		}
	}
}
