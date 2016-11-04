import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


public class CameraMovement
{
	private boolean mUpPressed = false;
	private boolean mDownPressed = false;
	private boolean mLeftPressed = false;
	private boolean mRightPressed = false;
	private boolean mWPressed = false;
	private boolean mSPressed = false;
	private boolean mAPressed = false;
	private boolean mDPressed = false;
	private boolean oPressed = false;
	private boolean pPressed = false;

	private double mSpeed = 10;

	private double mPitchAngle = 0;
	private double mRollAngle = 0;
	
	private int mLastMouseX = 0;
	private int mLastMouseY = 0;
	
	private KeyListener mKeyListener = new KeyListener();
	private MouseListener mMouseListener = new MouseListener();
	
	public KeyListener getKeyListener() {
		return mKeyListener;
	}
	
	public MouseListener getMouseListener() {
		return mMouseListener;
	}
	
	public double getPitchAngle() {
		return mPitchAngle;
	}
	
	public double getRollAngle() {
		return mRollAngle;
	}
	
	public Vector3D performStep (Vector3D cameraPos) {
		if (mUpPressed) {
			double newPosX = mSpeed *  Math.sin(Math.toRadians(mRollAngle)) * Math.cos(Math.toRadians(mPitchAngle));
			double newPosY = mSpeed * -Math.sin(Math.toRadians(mPitchAngle));
			double newPosZ = mSpeed *  Math.cos(Math.toRadians(mRollAngle)) * Math.cos(Math.toRadians(mPitchAngle));

			cameraPos = cameraPos.add(new Vector3D(newPosX, newPosY, -newPosZ));
		}
		if (mDownPressed) {
			double newPosX = mSpeed *  Math.sin(Math.toRadians(mRollAngle)) * Math.cos(Math.toRadians(mPitchAngle));
			double newPosY = mSpeed * -Math.sin(Math.toRadians(mPitchAngle));
			double newPosZ = mSpeed *  Math.cos(Math.toRadians(mRollAngle)) * Math.cos(Math.toRadians(mPitchAngle));

			cameraPos = cameraPos.add(new Vector3D(-newPosX, -newPosY, newPosZ));			
		}

		while (mPitchAngle < 0) {
			mPitchAngle += 360;
		}
		while (mPitchAngle > 360) {
			mPitchAngle -= 360;
		}

		if (mLeftPressed) {
			if(mPitchAngle < 270 && mPitchAngle > 90) {
				mRollAngle+=1;
			} else {
				mRollAngle-=1;
			}
		}
		if (mRightPressed) {
			if(mPitchAngle < 270 && mPitchAngle > 90) {
				mRollAngle-=1;
			} else {
				mRollAngle+=1;
			}
		}
		
		if (mWPressed) {
			mPitchAngle+=1;
		}
		if (mSPressed) {
			mPitchAngle-=1;
		}
		
		if (mAPressed) {
			double newPosX = mSpeed * Math.cos(Math.toRadians(mRollAngle));
			double newPosY = 0;
			double newPosZ = mSpeed *  Math.sin(Math.toRadians(mRollAngle));

			cameraPos = cameraPos.add(new Vector3D(-newPosX, -newPosY, -newPosZ));			
		}
		
		if (mDPressed) {
			double newPosX = mSpeed * Math.cos(Math.toRadians(mRollAngle));
			double newPosY = 0;
			double newPosZ = mSpeed *  Math.sin(Math.toRadians(mRollAngle));

			cameraPos = cameraPos.add(new Vector3D(newPosX, newPosY, newPosZ));			
		}
		
		if (oPressed) {
			double newPosX = 0;
			double newPosY = mSpeed * Math.cos(Math.toRadians(mPitchAngle));
			double newPosZ = 0;

			cameraPos = cameraPos.add(new Vector3D(-newPosX, newPosY, newPosZ));			
		}
		
		if (pPressed) {
			double newPosX = 0;
			double newPosY = mSpeed;
			double newPosZ = 0;
			
			cameraPos = cameraPos.add(new Vector3D(-newPosX, -newPosY, -newPosZ));			
		}
		
		return cameraPos;
	}
	
	private class MouseListener implements MouseMotionListener {
		@Override
		public void mouseMoved(MouseEvent e)
		{
			mLastMouseX = e.getXOnScreen();
			mLastMouseY = e.getYOnScreen();
			
		}
		
		@Override
		public void mouseDragged(MouseEvent e)
		{
			mRollAngle -= ((mLastMouseX - e.getXOnScreen()) / 10.0 * mSpeed);
			
			
			mPitchAngle -= ((mLastMouseY - e.getYOnScreen()) / 10.0 * mSpeed);
			mLastMouseX = e.getXOnScreen();
			mLastMouseY = e.getYOnScreen();			
		}		
	}
	
	private class KeyListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {

			if(e.getKeyCode() == KeyEvent.VK_UP){
				mUpPressed = false;
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN){
				mDownPressed = false;
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT){
				mLeftPressed = false;
			} else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				mRightPressed = false;
			} else if(e.getKeyCode() == KeyEvent.VK_W){
				mWPressed = false;
			} else if(e.getKeyCode() == KeyEvent.VK_S){
				mSPressed = false;
			} else if (e.getKeyCode() == KeyEvent.VK_A) {
				mAPressed = false;
			} else if (e.getKeyCode() == KeyEvent.VK_D) {
				mDPressed = false;
			} else if (e.getKeyCode() == KeyEvent.VK_O) {
				oPressed = false;
			} else if (e.getKeyCode() == KeyEvent.VK_P) {
				pPressed = false;
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_UP){
				mUpPressed = true;
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN){
				mDownPressed = true;
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT){
				mLeftPressed = true;
			} else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				mRightPressed = true;
			} else if(e.getKeyCode() == KeyEvent.VK_W){
				mWPressed = true;
			} else if(e.getKeyCode() == KeyEvent.VK_S){
				mSPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_A) {
				mAPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_D) {
				mDPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_O) {
				oPressed = true;
			} else if (e.getKeyCode() == KeyEvent.VK_P) {
				pPressed = true;
			}
		}
	}

}
