import com.jogamp.opengl.GL2;


public class Lights
{
	public void prepareLights(GL2 gl) {		
		// prepare colors
	    float spot_ambient[] =  {0.2f, 0.2f, 0.2f, 1.0f};
		float spot_diffuse[] =  {1f, 1f, 1f, 1.0f};
		float spot_specular[] = {0.8f, 0.8f, 0.8f, 1.0f};
		
		gl.glEnable(GL2.GL_LIGHTING);
		
		// prepare first light
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT,  spot_ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE,  spot_diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, spot_specular, 0);
		
		// prepare second light
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT,  spot_ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE,  spot_diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, spot_specular, 0);		
	}
	
	public void setPointLightsPosition(GL2 gl, Vector3D lightPos1, Vector3D lightPos2) {
		gl.glPushMatrix();

	    // set first light position
	    float spot_position1[] =  {(float) lightPos1.getX(), (float) lightPos1.getY() - 200, (float) lightPos1.getZ(), 1};
	    float spot_direction1[] = {0, -1, 0};
	    float spot_angle1=180.0f;
	    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION,  spot_position1,1);
	    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION,spot_direction1,0);
	    gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF,(float)spot_angle1);
	    gl.glPopMatrix();
	    
		gl.glPushMatrix();

	    // set second light position
	    float spot_position2[] =  {(float) lightPos2.getX(), (float) lightPos2.getY() - 200, (float) lightPos2.getZ(), 1};
	    float spot_direction2[] = {0, -1, 0};
	    float spot_angle2=180.0f;
	    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION,  spot_position2,1);
	    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION,spot_direction2,0);
	    gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF,(float)spot_angle2);
	    gl.glPopMatrix();
	}
}
