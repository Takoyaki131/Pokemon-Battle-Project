package pokemon;

public class WeatherMove extends Move{
	private String target_weather;

	public WeatherMove(int tm_no, String name, String type, int max_pp, double accuracy,
			String target_weather) {
		super(tm_no, name, type, max_pp, accuracy);
		this.target_weather = target_weather;
	}
	
	public String getTarget_weather()
	{
		return target_weather;
	}
}
