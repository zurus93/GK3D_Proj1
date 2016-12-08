import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jogamp.opengl.GL2;


public class ParticleEffect
{
	private static final int MAX_PARTICLES = 100;
	
    private List<Particle> particles = new ArrayList<Particle>(MAX_PARTICLES);
    
    private float colors[][] =
            {
                {1.0f, 0.5f, 0.5f}, {1.0f, 0.75f, 0.5f}, {1.0f, 1.0f, 0.5f}, {0.75f, 1.0f, 0.5f},
                {0.5f, 1.0f, 0.5f}, {0.5f, 1.0f, 0.75f}, {0.5f, 1.0f, 1.0f}, {0.5f, 0.75f, 1.0f},
                {0.5f, 0.5f, 1.0f}, {0.75f, 0.5f, 1.0f}, {1.0f, 0.5f, 1.0f}, {1.0f, 0.5f, 0.75f}
            };

    private float slowdown = 2.0f;  // Slow Down Particles
    private float xspeed;  // Base X Speed (To Allow Keyboard Direction Of Tail)
 
    private float yspeed;   // Base Y Speed (To Allow Keyboard Direction Of Tail)
 
    private float zoomRate = 0.0f;  // Used To Zoom Out
    
    private int col;  // Current Color Selection
    private int delay;  // Rainbow Effect Delay
    
    public void cycleColor() {
        delay = 0;
        col = (col + 1) % colors.length;
    }
    
    public void init() { 
        for (int loop = 0; loop < MAX_PARTICLES; loop++)  // Initials All The Textures
        {
        	Particle particle = new Particle();
            particle.active = true;  // Make All The Particles Active
            particle.life = 1.0f;  // Give All The Particles Full Life
             
            // Random Fade Speed
            particle.fade = (float) (100 * Math.random()) / 1000.0f + 0.003f;  
             
            // Select Red Rainbow Color
            particle.r = colors[loop * (12 / MAX_PARTICLES)][0];  
             
            // Select Red Rainbow Color
            particle.g = colors[loop * (12 / MAX_PARTICLES)][1];  
             
            // Select Red Rainbow Color
            particle.b = colors[loop * (12 / MAX_PARTICLES)][2];  
             
            // Random Speed On X Axis
            particle.xi = (float) ((50 * Math.random()) - 26.0f) * 10.0f;    
             
            // Random Speed On Y Axis
            particle.yi = (float) ((50 * Math.random()) - 25.0f) * 10.0f;    
             
            // Random Speed On Z Axis
            particle.zi = (float) ((50 * Math.random()) - 25.0f) * 10.0f;    
            particle.xg = 0.0f;  // Set Horizontal Pull To Zero
            particle.yg = 0.8f;  // Set Vertical Pull Downward
            particle.zg = 0.0f;  // Set Pull On Z Axis To Zero
            
            particle.cameraDistance = -1;
            
            particles.add(particle);
        }
    }
	
    public void display(GL2 gl, float rotate_x, float rotate_y, Vector3D cameraPosition) {        
        // Loop Through All The Particles
        for (Particle particle : particles)  
        {
            if (particle.active)  // If The Particle Is Active
            {
                float x = particle.x;  // Grab Our Particle X Position
                float y = particle.y;  // Grab Our Particle Y Position
                float z = particle.z + zoomRate;  // Particle Z Pos + Zoom
 
                // Draw The Particle Using Our RGB Values, Fade The 
                // Particle Based On It's Life
                gl.glColor4f(particle.r, particle.g, 
                        particle.b, particle.life);

				gl.glPushMatrix();
				gl.glTranslated(Scene.getWorldX() + 1150, Scene.getPeronHeight() / 2 + 330, (Scene.getWorldZ() + Scene.getWorldDepth())/2-400);
				gl.glRotatef (rotate_x, 0, -1, 0);
				gl.glRotatef (rotate_y, -1, 0, 0);
                gl.glBegin(GL2.GL_TRIANGLE_STRIP); // Build Quad From A Triangle Strip
                gl.glTexCoord2d(1, 1);
                gl.glVertex3f(x + 25f, y + 25f, z); // Top Right
                gl.glTexCoord2d(0, 1);
                gl.glVertex3f(x - 25f, y + 25f, z); // Top Left
                gl.glTexCoord2d(1, 0);
                gl.glVertex3f(x + 25f, y - 25f, z); // Bottom Right
                gl.glTexCoord2d(0, 0);
                gl.glVertex3f(x - 25f, y - 25f, z); // Bottom Left
                gl.glEnd();  // Done Building Triangle Strip
                gl.glPopMatrix();
 
                // Move On The X Axis By X Speed
                particle.x += (particle.xi * 10) / (slowdown * 1000);
                 
                // Move On The Y Axis By Y Speed
                particle.y += (particle.yi * 10) / (slowdown * 1000);
                 
                // Move On The Z Axis By Z Speed
                particle.z += (particle.zi * 10) / (slowdown * 1000);
 
                // Take Pull On X Axis Into Account
                particle.xi += particle.xg;
                 
                // Take Pull On Y Axis Into Account
                particle.yi += particle.yg;      
                 
                // Take Pull On Z Axis Into Account
                particle.zi += particle.zg;    
                 
                // Reduce Particles Life By 'Fade'
                particle.life -= particle.fade;

                Vector3D position = new Vector3D(particle.x, particle.y, particle.z);
                particle.cameraDistance = (position.substract(cameraPosition)).distance();
                
                if (particle.life < 0.0f)  // If Particle Is Burned Out
                {
                    particle.life = 1.0f;  // Give It New Life
                    particle.cameraDistance = -1;
                     
                    // Random Fade Value
                    particle.fade = (float) (100 * Math.random()) / 1000.0f + 0.003f;  
                    particle.x = 0.0f;  // Center On X Axis
                    particle.y = 0.0f;  // Center On Y Axis
                    particle.z = 0.0f;  // Center On Z Axis
                     
                    // X Axis Speed And Direction
                    particle.xi = xspeed + (float) ((60 * Math.random()) - 32.0f);  
                     
                    // Y Axis Speed And Direction
                    particle.yi = yspeed + (float) ((60 * Math.random()) - 30.0f);  
                     
                    // Z Axis Speed And Direction
                    particle.zi = (float) ((60 * Math.random()) - 30.0f);  
                    particle.r = colors[col][0];   // Select Red From Color Table
                    particle.g = colors[col][1];   // Select Green From Color Table
                    particle.b = colors[col][2];   // Select Blue From Color Table
                }
            }
        }
 
        delay++;
        if (delay > 25) {
            cycleColor();
        }
        
        Collections.sort(particles);  
    }
}
