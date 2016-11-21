import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

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
	
	private Texture mMarmurTexture;
	private Texture mYellowStraw;

	public MetroStationModel() {		
		mScene = new Scene();
		mCamera = new CameraMovement();
		mLights = new Lights();
		mCameraPos = mScene.initialCameraPosition();
		mLightPos1 = mScene.initialLightPosition1();
		mLightPos2 = mScene.initialLightPosition2();
		
		mBench = new ObjLoader("res/Bench/Cgtuts_Wood_Bench_OBJ.obj");
		mPerson = new ObjLoader("res/SYLT_Business_Wom-06_lowpoly_max.obj");
		mBilboard = new ObjLoader("res/Tank/TANK.obj");
		
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
        
        // Load texture.
        try {
            InputStream stream = MetroStationModel.class.getResourceAsStream("marmur.jpg");
            TextureData data = TextureIO.newTextureData(GLProfile.getDefault(), stream, false, "jpg");
            mMarmurTexture = TextureIO.newTexture(data);
            
            stream = MetroStationModel.class.getResourceAsStream("yellow_straw.jpg");
            data = TextureIO.newTextureData(GLProfile.getDefault(), stream, false, "jpg");
            mYellowStraw = TextureIO.newTexture(data);
        }
        catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }
        
        mMarmurTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR); 
        mMarmurTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        
        
        mYellowStraw.setTexParameteri(mGl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_BORDER); 
        mYellowStraw.setTexParameteri(mGl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_BORDER);
        float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        ByteBuffer bb = ByteBuffer.allocateDirect(color.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(color);
        fb.position(0);
        mYellowStraw.setTexParameterfv(mGl, GL2.GL_TEXTURE_BORDER_COLOR, fb);
        mYellowStraw.setTexParameteri(mGl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR); 
        mYellowStraw.setTexParameteri(mGl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        
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
		
		//mLights.setPointLightsPosition(mGl, mLightPos1, mLightPos2);
		
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
		
        // Set material properties.
        float[] rgba = {1f, 1f, 1f};
        mGl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
        mGl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
        mGl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 0.5f);

        // Apply texture.
        mGl.glActiveTexture(GL2.GL_TEXTURE0);
        mMarmurTexture.enable(mGl);
        mMarmurTexture.bind(mGl);
        
        mGl.glActiveTexture(GL2.GL_TEXTURE1);
        mYellowStraw.enable(mGl);
        mYellowStraw.bind(mGl);
        
		mScene.drawPeron(mGl, drawable);
		
		mGl.glActiveTexture(GL2.GL_TEXTURE0);
		mMarmurTexture.disable(mGl);
		
		mGl.glActiveTexture(GL2.GL_TEXTURE1);
		mYellowStraw.disable(mGl);
		
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