package ch.android;

public class Bridge {
	private String description;
	private String status;
	private String nextVessel;
	private String subsequentVessel;
	
	public Bridge() {
				
	}
	
	public Bridge(String description, String status, 
			String nextVessel, String subsequentVessel) {
		
		this.description = description;
		this.status = status;
		this.nextVessel = nextVessel;
		this.subsequentVessel = subsequentVessel;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}		

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}	

	public String getNextVessel() {
		return nextVessel;
	}

	public void setNextVessel(String nextVessel) {
		this.nextVessel = nextVessel;
	}

	public String getSubsequentVessel() {
		return subsequentVessel;
	}

	public void setSubsequentVessel(String subsequentVessel) {
		this.subsequentVessel = subsequentVessel;
	}
}
