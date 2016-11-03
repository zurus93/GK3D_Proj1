import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;

public class CameraTest implements GLEventListener {

	private final GLCanvas canvas = new GLCanvas();

	public static GL2 gl;

	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean wPressed = false;
	private boolean sPressed = false;
	private boolean aPressed = false;
	private boolean dPressed = false;

	double speed = 5;

	private double pitchAngle = 0;
	private double rollAngle = 0;
	
	private int lastMouseX = 0;
	private int lastMouseY = 0;
	
	private ObjLoader bench;
	
	private Scene scene;

	Vector3D cameraPos;

	Vector3D lightPos;

	public CameraTest() {		
		scene = new Scene();
		cameraPos = scene.initialCameraPosition();
		lightPos = scene.initialLightPosition();
		
		bench = new ObjLoader("res/Bench/Cgtuts_Wood_Bench_OBJ.obj");
		
		canvas.addGLEventListener(this);
		gl = (GL2) canvas.getGL();

		FPSAnimator animator = new FPSAnimator(canvas, 60);
		animator.start();

		canvas.addKeyListener(new KeyAdapter(){

			@Override
			public void keyReleased(KeyEvent e) {

				if(e.getKeyCode() == KeyEvent.VK_UP){
					upPressed = false;
				}
				else if(e.getKeyCode() == KeyEvent.VK_DOWN){
					downPressed = false;
				}
				else if(e.getKeyCode() == KeyEvent.VK_LEFT){
					leftPressed = false;
				}
				else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					rightPressed = false;
				}
				else if(e.getKeyCode() == KeyEvent.VK_W){
					wPressed = false;
				}
				else if(e.getKeyCode() == KeyEvent.VK_S){
					sPressed = false;
				} else if (e.getKeyCode() == KeyEvent.VK_A) {
					aPressed = false;
				} else if (e.getKeyCode() == KeyEvent.VK_D) {
					dPressed = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {


				if(e.getKeyCode() == KeyEvent.VK_UP){
					upPressed = true;
				}
				else if(e.getKeyCode() == KeyEvent.VK_DOWN){
					downPressed = true;
				}
				else if(e.getKeyCode() == KeyEvent.VK_LEFT){
					leftPressed = true;
				}
				else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					rightPressed = true;
				}
				else if(e.getKeyCode() == KeyEvent.VK_W){
					wPressed = true;
				}
				else if(e.getKeyCode() == KeyEvent.VK_S){
					sPressed = true;
				} else if (e.getKeyCode() == KeyEvent.VK_A) {
					aPressed = true;
				} else if (e.getKeyCode() == KeyEvent.VK_D) {
					dPressed = true;
				}
			}
		});

		canvas.addMouseMotionListener(new MouseMotionListener()
		{
			
			@Override
			public void mouseMoved(MouseEvent e)
			{
				lastMouseX = e.getXOnScreen();
				lastMouseY = e.getYOnScreen();
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e)
			{
				rollAngle -= ((lastMouseX - e.getXOnScreen()) / 10.0 * speed);
				
				
				pitchAngle -= ((lastMouseY - e.getYOnScreen()) / 10.0 * speed);
				lastMouseX = e.getXOnScreen();
				lastMouseY = e.getYOnScreen();
				
			}
		});

		JFrame frame = new JFrame("Camera Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas, BorderLayout.CENTER);

		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		canvas.requestFocusInWindow();
	}


	public void init(GLAutoDrawable drawable) {
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		gl = (GL2) canvas.getGL();

		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glCullFace(GL2.GL_BACK);

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glEnable(GL2.GL_BLEND);
		gl.glEnable(GL2.GL_NORMALIZE);

		gl.glBlendFunc(GL2.GL_SRC_ALPHA,  GL2.GL_ONE_MINUS_SRC_ALPHA);

		gl.glLoadIdentity();
		gl.glFrustum(-1, 1, -1, 1, 1, scene.getWorldDepth());
		gl.glViewport(0, 0, width, height);
         
		gl.glClearColor(.25f, .25f, .25f, 1f);
	
	}


	public void display(GLAutoDrawable drawable){
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		step();		
		displayFromCamera(drawable);
	}

	private void step() {		
		if(upPressed){
			double newPosX = speed *  Math.sin(Math.toRadians(rollAngle)) * Math.cos(Math.toRadians(pitchAngle));
			double newPosY = speed * -Math.sin(Math.toRadians(pitchAngle));
			double newPosZ = speed *  Math.cos(Math.toRadians(rollAngle)) * Math.cos(Math.toRadians(pitchAngle));

			cameraPos = cameraPos.add(new Vector3D(newPosX, newPosY, -newPosZ));
		}
		if(downPressed){
			double newPosX = speed *  Math.sin(Math.toRadians(rollAngle)) * Math.cos(Math.toRadians(pitchAngle));
			double newPosY = speed * -Math.sin(Math.toRadians(pitchAngle));
			double newPosZ = speed *  Math.cos(Math.toRadians(rollAngle)) * Math.cos(Math.toRadians(pitchAngle));

			cameraPos = cameraPos.add(new Vector3D(-newPosX, -newPosY, newPosZ));			
		}

		while(pitchAngle < 0){
			pitchAngle += 360;
		}
		while(pitchAngle > 360){
			pitchAngle -= 360;
		}

		if(leftPressed){
			if(pitchAngle < 270 && pitchAngle > 90){
				rollAngle+=1;
			}
			else{
				rollAngle-=1;
			}

		}
		if(rightPressed){
			if(pitchAngle < 270 && pitchAngle > 90){
				rollAngle-=1;
			}
			else{
				rollAngle+=1;
			}
		}
		
		if(wPressed){
			pitchAngle+=1;
		}

		if(sPressed){
			pitchAngle-=1;
		}
		
		if (aPressed) {
			double newPosX = speed * Math.cos(Math.toRadians(rollAngle));
			double newPosY = 0;
			double newPosZ = speed *  Math.sin(Math.toRadians(rollAngle));

			cameraPos = cameraPos.add(new Vector3D(-newPosX, -newPosY, -newPosZ));			
		}
		
		if (dPressed) {
			double newPosX = speed * Math.cos(Math.toRadians(rollAngle));
			double newPosY = 0;
			double newPosZ = speed *  Math.sin(Math.toRadians(rollAngle));

			cameraPos = cameraPos.add(new Vector3D(newPosX, newPosY, newPosZ));			
		}
	}

	public void displayFromCamera(GLAutoDrawable drawable){
		gl.glPushMatrix();


		gl.glViewport(0, 0, canvas.getWidth(), canvas.getHeight());

		gl.glLoadIdentity();

		gl.glFrustum(-1, 1, -1, 1, 1, 1000000);		

	       // prepare spotlight
  float spot_ambient[] =  {0.2f,0.2f,0.2f,1.0f };
  float spot_diffuse[] =  {0.8f,0.8f,0.8f,1.0f };
  float spot_specular[] =  {0.8f,0.8f,0.8f,1.0f };
  // set colors here and do the geometry in draw
  gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT,  spot_ambient,0);
  gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE,  spot_diffuse,0);
  gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, spot_specular,0);
  gl.glEnable(GL2.GL_LIGHTING);
  gl.glEnable(GL2.GL_LIGHT0);
  
	      // draw light-bulb as a sphere

	      // set light position
	      // since ligth follows the model when mousing
	      // spotlight as it moves with the scene
	      float spot_position[] =  {(float) lightPos.getX(), (float) lightPos.getY(), (float) lightPos.getZ(), 1.0f};
	      float spot_direction[] = {0, -1, 0};
	      float spot_angle=180.0f;
	      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION,  spot_position,1);
	      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION,spot_direction,1);
	      gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF,(float)spot_angle);
	      // "smoothing" the border of the lightcone
	      // change this for effect
	      //gl.glLighti(GL2.GL_LIGHT0, GL2.GL_SPOT_EXPONENT, 10);
	      
		gl.glRotated(pitchAngle, 1, 0, 0);
		gl.glRotated(rollAngle, 0, 1, 0);		

		gl.glTranslated(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());

		displayWholeScene(drawable);

		gl.glPopMatrix();
	}

	public void displayWholeScene(GLAutoDrawable drawable) {
		scene.drawStage(gl, drawable);
		scene.drawPeron(gl, drawable);
		scene.drawBench(gl, bench);
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
	}

	public static void main(String[] args) {		
		new CameraTest();
	}


	@Override
	public void dispose(GLAutoDrawable arg0)
	{
		// TODO Auto-generated method stub

	}
}