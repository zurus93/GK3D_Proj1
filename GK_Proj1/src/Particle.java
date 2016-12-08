
public class Particle implements Comparable<Particle>
{	
    boolean active;          // Active (Yes/No)
    float life;          // Particle Life
    float fade;          // Fade Speed
    float r;          // Red Value
    float g;          // Green Value
    float b;          // Blue Value
    float x;          // X Position
    float y;          // Y Position
    float z;          // Z Position
    float xi;          // X Direction
    float yi;          // Y Direction
    float zi;          // Z Direction
    float xg;          // X Gravity
    float yg;          // Y Gravity
    float zg;          // Z Gravity
    
    float cameraDistance;

	@Override
	public int compareTo(Particle p)
	{
		return (this.cameraDistance > p.cameraDistance) ? 1 : ((this.cameraDistance < p.cameraDistance) ? -1 : 0);
	}
}
