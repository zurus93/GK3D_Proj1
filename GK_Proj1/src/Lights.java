import com.jogamp.opengl.GL2;


public class Lights
{
	public void prepareLights(GL2 gl) {		
		// prepare first spotlight
	    float spot_ambient[] =  {0.2f, 0.2f, 0.2f, 1.0f};
		float spot_diffuse[] =  {1f, 1f, 1f, 1.0f};
		float spot_specular[] = {0.8f, 0.8f, 0.8f, 1.0f};
		
		// set colors
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT,  spot_ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE,  spot_diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, spot_specular, 0);
	}
	
	public void setLightPosition(GL2 gl, Vector3D lightPos) {
		gl.glPushMatrix();
		gl.glLoadIdentity();
		
	    // set light position
	    float spot_position[] =  {(float) lightPos.getX(), (float) lightPos.getY(), (float) lightPos.getZ(), 1.0f};
	    float spot_direction[] = {0, 1, 0};
	    float spot_angle=180.0f;
	    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION,  spot_position,1);
	    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION,spot_direction,0);
	    //gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF,(float)spot_angle);
	    gl.glPopMatrix();
	}

}
