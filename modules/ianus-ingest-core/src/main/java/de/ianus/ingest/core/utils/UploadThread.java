package de.ianus.ingest.core.utils;

import de.ianus.ingest.core.bo.BagitFile;

public class UploadThread  implements Runnable {

	
	private Long tpId;
	private Long bagitFileId;
	
	public UploadThread(Long tpId, Long bagitFileId){
		this.tpId = tpId;
		this.bagitFileId = bagitFileId;
	}
	
	public void run() {
		try {
			BagitFile file = null;//Services.getInstance().getDaoService().getBagitFile(fileId);
			
			file.setVirusScan(BagitFile.State.in_progress);
			this.virusScan();
			file.setVirusScan(BagitFile.State.finished_ok);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void virusScan(){
		
	}
}
