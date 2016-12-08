import com.jogamp.opengl.GL2;


public class ParticleEffect
{
	private static final int MAX_PARTICLES = 100;
	
    private Particle[] particles = new Particle[MAX_PARTICLES];
    
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
            particles[loop] = new Particle();
            particles[loop].active = true;  // Make All The Particles Active
            particles[loop].life = 1.0f;  // Give All The Particles Full Life
             
            // Random Fade Speed
            particles[loop].fade = (float) (100 * Math.random()) / 1000.0f + 0.003f;  
             
            // Select Red Rainbow Color
            particles[loop].r = colors[loop * (12 / MAX_PARTICLES)][0];  
             
            // Select Red Rainbow Color
            particles[loop].g = colors[loop * (12 / MAX_PARTICLES)][1];  
             
            // Select Red Rainbow Color
            particles[loop].b = colors[loop * (12 / MAX_PARTICLES)][2];  
             
            // Random Speed On X Axis
            particles[loop].xi = (float) ((50 * Math.random()) - 26.0f) * 10.0f;    
             
            // Random Speed On Y Axis
            particles[loop].yi = (float) ((50 * Math.random()) - 25.0f) * 10.0f;    
             
            // Random Speed On Z Axis
            particles[loop].zi = (float) ((50 * Math.random()) - 25.0f) * 10.0f;    
            particles[loop].xg = 0.0f;  // Set Horizontal Pull To Zero
            particles[loop].yg = 0.8f;  // Set Vertical Pull Downward
            particles[loop].zg = 0.0f;  // Set Pull On Z Axis To Zero
        }
    }
	
    public void display(GL2 gl, float rotate_x, float rotate_y) {        
        // Loop Through All The Particles
        for (int loop = 0; loop < MAX_PARTICLES; loop++)  
        {
            if (particles[loop].active)  // If The Particle Is Active
            {
                float x = particles[loop].x;  // Grab Our Particle X Position
                float y = particles[loop].y;  // Grab Our Particle Y Position
                float z = particles[loop].z + zoomRate;  // Particle Z Pos + Zoom
 
                // Draw The Particle Using Our RGB Values, Fade The 
                // Particle Based On It's Life
                gl.glColor4f(particles[loop].r, particles[loop].g, 
                        particles[loop].b, particles[loop].life);

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
                particles[loop].x += (particles[loop].xi * 10) / (slowdown * 1000);
                 
                // Move On The Y Axis By Y Speed
                particles[loop].y += (particles[loop].yi * 10) / (slowdown * 1000);
                 
                // Move On The Z Axis By Z Speed
                particles[loop].z += (particles[loop].zi * 10) / (slowdown * 1000);
 
                // Take Pull On X Axis Into Account
                particles[loop].xi += particles[loop].xg;
                 
                // Take Pull On Y Axis Into Account
                particles[loop].yi += particles[loop].yg;      
                 
                // Take Pull On Z Axis Into Account
                particles[loop].zi += particles[loop].zg;    
                 
                // Reduce Particles Life By 'Fade'
                particles[loop].life -= particles[loop].fade;    
 
                if (particles[loop].life < 0.0f)  // If Particle Is Burned Out
                {
                    particles[loop].life = 1.0f;  // Give It New Life
                     
                    // Random Fade Value
                    particles[loop].fade = (float) (100 * Math.random()) / 1000.0f + 0.003f;  
                    particles[loop].x = 0.0f;  // Center On X Axis
                    particles[loop].y = 0.0f;  // Center On Y Axis
                    particles[loop].z = 0.0f;  // Center On Z Axis
                     
                    // X Axis Speed And Direction
                    particles[loop].xi = xspeed + (float) ((60 * Math.random()) - 32.0f);  
                     
                    // Y Axis Speed And Direction
                    particles[loop].yi = yspeed + (float) ((60 * Math.random()) - 30.0f);  
                     
                    // Z Axis Speed And Direction
                    particles[loop].zi = (float) ((60 * Math.random()) - 30.0f);  
                    particles[loop].r = colors[col][0];   // Select Red From Color Table
                    particles[loop].g = colors[col][1];   // Select Green From Color Table
                    particles[loop].b = colors[col][2];   // Select Blue From Color Table
                }
            }
        }
 
        delay++;
        if (delay > 25) {
            cycleColor();
        }
    }
}
