import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

public class MetroStationModel implements GLEventListener {

	private final GLCanvas mCanvas = new GLCanvas();

	private static GL2 mGl;
	
	private ObjLoader mBench;
	private ObjLoader mPerson;
	private ObjLoader mBilboard;
	
	private Scene mScene;
	private CameraMovement mCamera;
	private Lights mLights;

	private Vector3D mCameraPos;
	private Vector3D mLightPos1;
	private Vector3D mLightPos2;

	public MetroStationModel() {		
		mScene = new Scene();
		mCamera = new CameraMovement();
		mLights = new Lights();
		mCameraPos = mScene.initialCameraPosition();
		mLightPos1 = mScene.initialLightPosition1();
		mLightPos2 = mScene.initialLightPosition2();
		
		mBench = new ObjLoader("res/Bench/Cgtuts_Wood_Bench_OBJ.obj");
		mPerson = new ObjLoader("res/SYLT_Business_Wom-06_lowpoly_max.obj");
		mBilboard = new ObjLoader("res//Tank/TANK.obj");
		
		mCanvas.addGLEventListener(this);
		
		FPSAnimator animator = new FPSAnimator(mCanvas, 60);
		animator.start();

		mCanvas.addKeyListener(mCamera.getKeyListener());

		mCanvas.addMouseMotionListener(mCamera.getMouseListener());

		JFrame frame = new JFrame("Metro Station");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(mCanvas, BorderLayout.CENTER);

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		mCanvas.requestFocusInWindow();
	}


	public void init(GLAutoDrawable drawable) {
		mGl = (GL2) mCanvas.getGL();
		
		mLights.prepareLights(mGl);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		mGl.glMatrixMode(GL2.GL_PROJECTION);
		
		mGl.glEnable(GL2.GL_CULL_FACE);
		mGl.glCullFace(GL2.GL_BACK);
		
		mGl.glEnable(GL2.GL_DEPTH_TEST);
		
		mGl.glMatrixMode(GL2.GL_MODELVIEW);
		
		mGl.glEnable(GL2.GL_NORMALIZE);		

		mGl.glLoadIdentity();
		mGl.glFrustum(-1, 1, -1, 1, 1, mScene.getWorldDepth());
		mGl.glViewport(0, 0, width, height);
         
		mGl.glClearColor(.25f, .25f, .25f, 1f);	
	}


	public void display(GLAutoDrawable drawable){
		mGl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		mGl.glMatrixMode(GL2.GL_MODELVIEW);
		mGl.glLoadIdentity();
		
		mLights.setPointLightsPosition(mGl, mLightPos1, mLightPos2);
		
		mCameraPos = mCamera.performStep(mCameraPos);
		displayFromCamera(drawable);
	}

	public void displayFromCamera(GLAutoDrawable drawable) {
		mGl.glPushMatrix();

		mGl.glLoadIdentity();
		mGl.glFrustum(-1, 1, -1, 1, 1, 1000000);		
	      
		mGl.glRotated(mCamera.getPitchAngle(), 1, 0, 0);
		mGl.glRotated(mCamera.getRollAngle(), 0, 1, 0);		

		mGl.glTranslated(-mCameraPos.getX(), -mCameraPos.getY(), -mCameraPos.getZ());

		displayWholeScene(drawable);

		mGl.glPopMatrix();
	}

	public void displayWholeScene(GLAutoDrawable drawable) {
		mScene.drawStage(mGl, drawable);
		mScene.drawPeron(mGl, drawable);
		mScene.drawBench(mGl, mBench);
		mScene.drawBulb(mGl);
		mScene.drawPerson(mGl, mPerson);
		mScene.drawBilboard(mGl, mBilboard);
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}
}