import java.util.Random;


public class Utils
{
	public static float randomInRange(float min, float max) {
		Random random = new Random();
		  
		float range = max - min;
		float scaled = random.nextFloat() * range;
		float shifted = scaled + min;
		return shifted; // == (rand.nextDouble() * (max-min)) + min;
	}
	
	public static Vector3D randUnitVector() {
		double x = randomInRange(0, 100);
		double y = randomInRange(0, 100);
		double z = randomInRange(0, 100);
		
		return new Vector3D(x, y, z);		
	}
	
	public static float mix(float x, float y, float a) {
		return x * (1.0f - a) + y * a;
	}

}
