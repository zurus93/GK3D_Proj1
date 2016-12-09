import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

public class MetroStationModel implements GLEventListener {

	private final GLCanvas mCanvas = new GLCanvas();

	private static GL2 mGl;
	
	private ObjLoader mBench;
	private ObjLoader mPerson;
	private ObjLoader mTank;
	
	private Scene mScene;
	private CameraMovement mCamera;
	private Lights mLights;

	private Vector3D mCameraPos;
	private Vector3D mLightPos1;
	private Vector3D mLightPos2;
	
	private Texture mMarmurTexture;
	private Texture mWoodTexture;
	private Texture mYellowStraw;
	private Texture mWallsTexture;
	private Texture mBilboardTexture;
	private Texture mParticuleTexture;
	private Texture mTankTexture;
	//private Texture mOverlayTexture;
	
	
	private ParticleEffect mParticleEffect;

	public MetroStationModel() {		
		mScene = new Scene();
		mCamera = new CameraMovement();
		mLights = new Lights();
		mCameraPos = mScene.initialCameraPosition();
		mLightPos1 = mScene.initialLightPosition1();
		mLightPos2 = mScene.initialLightPosition2();
		mParticleEffect = new ParticleEffect();
		
		mBench = new ObjLoader("res/Bench/Cgtuts_Wood_Bench_OBJ.obj");
		mPerson = new ObjLoader("res/SYLT_Business_Wom-06_lowpoly_max.obj");
		mTank = new ObjLoader("res/Tank/TANK.obj");
		
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
		GLCapabilities caps;
		caps = new GLCapabilities(GLProfile.getDefault());
		caps.setSampleBuffers(true);
		caps.setNumSamples(4);
        
        // Load texture.
        try {
        	mWallsTexture = Cubemap.loadFromStreams(mGl, MetroStationModel.class, "walls", "jpg", false);
        	
            InputStream stream = MetroStationModel.class.getResourceAsStream("marmur.jpg");
            TextureData data = TextureIO.newTextureData(GLProfile.getDefault(), stream, false, "jpg");
            mMarmurTexture = TextureIO.newTexture(data);
            
            stream = MetroStationModel.class.getResourceAsStream("wood.jpg");
            data = TextureIO.newTextureData(GLProfile.getDefault(), stream, false, "jpg");
            mWoodTexture = TextureIO.newTexture(data);
            
            stream = MetroStationModel.class.getResourceAsStream("yellow_straw.jpg");
            data = TextureIO.newTextureData(GLProfile.getDefault(), stream, false, "jpg");
            mYellowStraw = TextureIO.newTexture(data);
            
            stream = MetroStationModel.class.getResourceAsStream("ball.png");
            data = TextureIO.newTextureData(GLProfile.getDefault(), stream, false, "png");
            mBilboardTexture = TextureIO.newTexture(data);
            
			stream = MetroStationModel.class.getResourceAsStream("smoke.png");
			data = TextureIO.newTextureData(GLProfile.getDefault(), stream, false, "png");
			mParticuleTexture = TextureIO.newTexture(data);
			
			stream = MetroStationModel.class.getResourceAsStream("moro.jpg");
			data = TextureIO.newTextureData(GLProfile.getDefault(), stream, false, "jpg");
			mTankTexture = TextureIO.newTexture(data);
			
//			stream = MetroStationModel.class.getResourceAsStream("rRectangle.png");
//			data = TextureIO.newTextureData(GLProfile.getDefault(), stream, false, "png");
//			mOverlayTexture = TextureIO.newTexture(data);
        }
        catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }
        
        mWallsTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR); 
        mWallsTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        mWallsTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        mWallsTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        mWallsTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_WRAP_R, GL2.GL_REPEAT);
        
        mMarmurTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR); 
        mMarmurTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        
        mWoodTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR); 
        mWoodTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
               
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
        
        mBilboardTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR); 
        mBilboardTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        mBilboardTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        mBilboardTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        
        mParticuleTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST); 
        mParticuleTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        
        mTankTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR); 
        mTankTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        mTankTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        mTankTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
               
        mParticleEffect.init();
        
//        mOverlayTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR); 
//        mOverlayTexture.setTexParameteri(mGl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		mGl.glMatrixMode(GL2.GL_PROJECTION);
		
		mGl.glEnable(GL2.GL_CULL_FACE);
		mGl.glCullFace(GL2.GL_BACK);
		
		mGl.glEnable(GL2.GL_DEPTH_TEST);
		
		mGl.glMatrixMode(GL2.GL_MODELVIEW);

		mGl.glEnable(GL2.GL_NORMALIZE);		

	    mGl.glEnable(GL2.GL_LINE_SMOOTH);      
	    mGl.glEnable(GL2.GL_POLYGON_SMOOTH);
	    mGl.glHint(GL2.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST);
	    mGl.glEnable(GL2.GL_MULTISAMPLE);
	    
	    mGl.glEnable(GL2.GL_BLEND);
	    mGl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
	    
		mGl.glLoadIdentity();
		mGl.glFrustum(-1, 1, -1, 1, 1, mScene.getWorldDepth());
		mGl.glViewport(0, 0, width, height);
						
        mGl.glShadeModel(GL2.GL_SMOOTH);               // Enables Smooth Color Shading

        mGl.glEnable(GL2.GL_FOG);
        {
          float fogColor[] =
          { 0.5f, 0.5f, 0.5f, 0.1f };
     
          mGl.glFogi(GL2.GL_FOG_MODE, GL2.GL_EXP);
          mGl.glFogfv(GL2.GL_FOG_COLOR, fogColor, 0);
          mGl.glFogf(GL2.GL_FOG_DENSITY, 0.0005f);
          mGl.glHint(GL2.GL_FOG_HINT, GL2.GL_NICEST);
        }
        
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
		mGl.glEnable(GL2.GL_MULTISAMPLE);
		mGl.glPushMatrix();

		mGl.glLoadIdentity();
		mGl.glFrustum(-1, 1, -1, 1, 1, 1000000);		
	      
		mGl.glRotated(mCamera.getPitchAngle(), 1, 0, 0);
		mGl.glRotated(mCamera.getRollAngle(), 0, 1, 0);		

		mGl.glTranslated(-mCameraPos.getX(), -mCameraPos.getY(), -mCameraPos.getZ());

		displayWholeScene(drawable);

		mGl.glPopMatrix();
		mGl.glDisable(GL2.GL_MULTISAMPLE);
	}

	public void displayWholeScene(GLAutoDrawable drawable) {
        // Set material properties.
        mGl.glActiveTexture(GL2.GL_TEXTURE0);
        mWallsTexture.bind(mGl);
        mWallsTexture.enable(mGl);
		mScene.drawStage(mGl, drawable);
		mGl.glActiveTexture(GL2.GL_TEXTURE0);
		mWallsTexture.disable(mGl);

		Texture texture = mCamera.onePressed() ? mMarmurTexture : mWoodTexture;
		
        // Apply texture.
        mGl.glActiveTexture(GL2.GL_TEXTURE1);
        texture.enable(mGl);
        texture.bind(mGl);
        
        mGl.glActiveTexture(GL2.GL_TEXTURE2);
        mYellowStraw.enable(mGl);
        mYellowStraw.bind(mGl);
        
		mScene.drawPeron(mGl, drawable);

		mGl.glActiveTexture(GL2.GL_TEXTURE1);
		texture.disable(mGl);
		
		mGl.glActiveTexture(GL2.GL_TEXTURE2);
		mYellowStraw.disable(mGl);

		mScene.drawBench(mGl, mBench);
		mScene.drawBulb(mGl);
		mScene.drawPerson(mGl, mPerson);
		
		mGl.glActiveTexture(GL2.GL_TEXTURE0);
        mTankTexture.enable(mGl);
        mTankTexture.bind(mGl);
		mScene.drawTank(mGl, mTank);
		mGl.glActiveTexture(GL2.GL_TEXTURE0);
        mTankTexture.disable(mGl);
        
        mGl.glActiveTexture(GL2.GL_TEXTURE0);
        mBilboardTexture.enable(mGl);
        mBilboardTexture.bind(mGl);
		mScene.drawBilboard(mGl, (float) mCamera.getRollAngle(), (float) mCamera.getPitchAngle());
		
		mGl.glActiveTexture(GL2.GL_TEXTURE0);
		mBilboardTexture.disable(mGl);

		mGl.glDisable(GL.GL_DEPTH_TEST);
		mGl.glActiveTexture(GL2.GL_TEXTURE0);
        mParticuleTexture.enable(mGl);
        mParticuleTexture.bind(mGl);
		mParticleEffect.display(mGl, (float) mCamera.getRollAngle(), (float) mCamera.getPitchAngle(), mCameraPos);
		
		mGl.glActiveTexture(GL2.GL_TEXTURE0);
		mParticuleTexture.disable(mGl);
		mGl.glEnable(GL.GL_DEPTH_TEST);

//		mGl.glDisable(GL2.GL_FOG);
//		mGl.glActiveTexture(GL2.GL_TEXTURE0);
//        mOverlayTexture.enable(mGl);
//        mOverlayTexture.bind(mGl);
//		drawOverlay();
//		mGl.glActiveTexture(GL2.GL_TEXTURE0);
//		mOverlayTexture.disable(mGl);
//		mGl.glEnable(GL2.GL_FOG);
	}
	
	public void drawOverlay() {
		mGl.glMatrixMode(GL2.GL_PROJECTION);
		mGl.glPushMatrix();
		mGl.glLoadIdentity();
		mGl.glOrtho(0.0, mCanvas.getWidth(), mCanvas.getHeight(), 0.0, -1.0, 10.0);
		mGl.glMatrixMode(GL2.GL_MODELVIEW);
		mGl.glLoadIdentity();
		mGl.glDisable(GL2.GL_CULL_FACE);

		mGl.glClear(GL2.GL_DEPTH_BUFFER_BIT);

		mGl.glBegin(GL2.GL_POLYGON);
		    mGl.glColor3f(1.0f, 1.0f, 0.0f);
		    mGl.glTexCoord2f(0, 0);
		    mGl.glVertex2f(0.0f, 0.0f);
		    mGl.glTexCoord2f(1, 0);
		    mGl.glVertex2f(500.0f, 0.0f);
		    mGl.glTexCoord2f(1, 1);
		    mGl.glVertex2f(500.0f, 350.0f);
		    mGl.glTexCoord2f(0, 1);
		    mGl.glVertex2f(0.0f, 350.0f);
		mGl.glEnd();

		// Making sure we can render 3d again
		mGl.glMatrixMode(GL2.GL_PROJECTION);
		mGl.glPopMatrix();
		mGl.glMatrixMode(GL2.GL_MODELVIEW);
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}
}