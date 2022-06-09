package pokemon;

public class StatusMove extends Move {
	
	private String status_type;
	// Note: accuracy is defined as chance to inflict status
	public StatusMove(int tm_no, String name, String type, int max_pp, double accuracy, String status_type) {
		super(tm_no, name, type, max_pp, accuracy);
		this.status_type = status_type;
	}

	public String getStatus_type()
	{
		return status_type;
	}
}
