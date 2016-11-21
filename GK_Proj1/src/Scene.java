import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class Scene {
	private final int mWorldX = -400;
	private final int mWorldY = -400;
	private final int mWorldWidth = 3500;
	private final int mWorldHeight = 2500;
	private final int mWorldZ = 100;
	private final int mWorldDepth = 4800;
	
	private final int mPeronHeight = 800;
	private final int mPeronWidth = 2500;
	
	private GLU mGlu = new GLU();
	
	public void drawStage(GL2 gl, GLAutoDrawable drawable) {		
		double w = mWorldWidth / 10;
		double h = mWorldHeight / 10;
		double d = mWorldDepth / 10;
		
		for (double x = mWorldX; x < mWorldX + mWorldWidth; x += w) {
			for (double y = mWorldY; y < mWorldY + mWorldHeight; y += h) {
				gl.glBegin(GL2.GL_POLYGON);
	
				material(gl, 0, 1f, 0);
				gl.glNormal3d(0, 0, -1);
				gl.glVertex3d(x, (y + h), (mWorldZ + mWorldDepth));
				gl.glVertex3d((x + w), (y + h), (mWorldZ + mWorldDepth));
				gl.glVertex3d((x + w), y, (mWorldZ + mWorldDepth));				
				gl.glVertex3d(x, y, (mWorldZ + mWorldDepth));
				gl.glEnd();						
				
				gl.glBegin(GL2.GL_POLYGON);
	
				material(gl, 0, 1f, 0);
				gl.glNormal3d(0, 0, 1);
				gl.glVertex3d(x + w, y + h, mWorldZ);
				gl.glVertex3d(x, y+h, mWorldZ);
				gl.glVertex3d(x, y, mWorldZ);				
				gl.glVertex3d(x+w, y, mWorldZ);
				gl.glEnd();					
			}
		}								
		
		for (double y = mWorldY; y < mWorldY + mWorldHeight; y += h) {
			for (double z = mWorldZ; z < mWorldZ + mWorldDepth; z += d) {
				gl.glBegin(GL2.GL_POLYGON);
				material(gl, 0, 0, 1f);
				gl.glNormal3d(1, 0, 0);				
				gl.glVertex3d(mWorldX, y, z+d);
				gl.glVertex3d(mWorldX, y, z);
				gl.glVertex3d(mWorldX, y+h, z);
				gl.glVertex3d(mWorldX, y+h, z+d);
	
				gl.glEnd();
				
				gl.glBegin(GL2.GL_POLYGON);
				material(gl, 0, 0, 1f);
				gl.glNormal3d(-1, 0, 0);				
				gl.glVertex3d(mWorldX+mWorldWidth, y, z);
				gl.glVertex3d(mWorldX+mWorldWidth, y, z+d);
				gl.glVertex3d(mWorldX+mWorldWidth, y+h, z+d);
				gl.glVertex3d(mWorldX+mWorldWidth, y+h, z);
	
				gl.glEnd();				
			}
		}
		
		for (double x = mWorldX; x < mWorldX + mWorldWidth; x += w) {
			for (double z = mWorldZ; z < mWorldZ + mWorldDepth; z += d) {
				gl.glBegin(GL2.GL_POLYGON);
				material(gl, 1f, 0, 0);
				gl.glNormal3d(0, -1, 0);				
				gl.glVertex3d(x, mWorldY+mWorldHeight, z+d);
				gl.glVertex3d(x, mWorldY+mWorldHeight, z);
				gl.glVertex3d(x+w, mWorldY+mWorldHeight, z);
				gl.glVertex3d(x+w, mWorldY+mWorldHeight, z+d);
	
				gl.glEnd();
				
				gl.glBegin(GL2.GL_POLYGON);
				material(gl, 1f, 0, 0);
				gl.glNormal3d(0, 1, 0);				
				gl.glVertex3d(x, mWorldY, z);
				gl.glVertex3d(x, mWorldY, z+d);
				gl.glVertex3d(x+w, mWorldY, z+d);
				gl.glVertex3d(x+w, mWorldY, z);
	
				gl.glEnd();					
			}
		}		
	}
	
	public void drawPeron(GL2 gl, GLAutoDrawable drawable) {
		double w = mPeronWidth / 10;
		double h = mPeronHeight / 10;
		double d = mWorldHeight / 10;

		for (double x = mWorldX; x < mWorldX + mPeronWidth; x += w) {
			for (double z = mWorldZ; z < mWorldZ + mWorldDepth; z += d) {
				gl.glBegin(GL2.GL_POLYGON);

				gl.glNormal3d(0, 1, 0);
				
				gl.glMultiTexCoord2d(GL2.GL_TEXTURE0, 0, 0);
				if (x == mWorldX + mPeronWidth - w) 
					gl.glMultiTexCoord2d(GL2.GL_TEXTURE1, 0, 0);
				gl.glVertex3d(x, mWorldY+mPeronHeight, z);
				gl.glMultiTexCoord2d(GL2.GL_TEXTURE0, 1, 0);
				if (x == mWorldX + mPeronWidth - w) 
					gl.glMultiTexCoord2d(GL2.GL_TEXTURE1, 1, 0);
				gl.glVertex3d(x, mWorldY+mPeronHeight, z+d);
				gl.glMultiTexCoord2d(GL2.GL_TEXTURE0, 1, 1);
				if (x == mWorldX + mPeronWidth - w) 
					gl.glMultiTexCoord2d(GL2.GL_TEXTURE1, 1, 1);
				gl.glVertex3d(x+w, mWorldY+mPeronHeight, z+d);
				gl.glMultiTexCoord2d(GL2.GL_TEXTURE0, 0, 1);
				if (x == mWorldX + mPeronWidth - w) 
					gl.glMultiTexCoord2d(GL2.GL_TEXTURE1, 0, 1);
				gl.glVertex3d(x+w, mWorldY+mPeronHeight, z);
				gl.glEnd();
				
				gl.glBegin(GL2.GL_POLYGON);

				gl.glNormal3d(0, -1, 0);
				gl.glTexCoord2f(0.0f, 1.0f);
				gl.glVertex3d(x, mWorldY+mPeronHeight, z+d);
				gl.glTexCoord2f(0.0f, 0.0f);
				gl.glVertex3d(x, mWorldY+mPeronHeight, z);
				gl.glTexCoord2f(1.0f, 0.0f);
				gl.glVertex3d(x+w, mWorldY+mPeronHeight, z);
				gl.glTexCoord2f(1.0f, 1.0f);
				gl.glVertex3d(x+w, mWorldY+mPeronHeight, z+d);
				gl.glEnd();														
			}
		}
		
		for (double y = mWorldY; y < mWorldY + mPeronHeight; y += h) {
			for (double z = mWorldZ; z < mWorldZ + mWorldDepth; z += d) {
				gl.glBegin(GL2.GL_POLYGON);

				material(gl, 0f, 0f, 0f);
				
				gl.glNormal3d(1, 0, 0);
				gl.glVertex3d(mWorldX + mPeronWidth, y, z+d);
				gl.glVertex3d(mWorldX+ mPeronWidth, y, z);
				gl.glVertex3d(mWorldX+ mPeronWidth, y+h, z);
				gl.glVertex3d(mWorldX+ mPeronWidth, y+h, z+d);
										
				gl.glEnd();					
			}
		}
	}
	
	public void drawBench(GL2 gl, ObjLoader bench) {
		//ObjLoader mBench = new ObjLoader("res/Bench/Cgtuts_Wood_Bench_OBJ.obj");
		gl.glPushMatrix();

		material(gl, 0.6f, 0.3f, 0);
		gl.glTranslated((mWorldX + mWorldWidth) / 2, mPeronHeight / 2, (mWorldZ + mWorldDepth)/2);
		gl.glRotated(90, 0, 1, 0);	
		bench.drawModel(gl);
		
		gl.glTranslated(-1000, 0, 0);
		
		bench.drawModel(gl);;
		gl.glPopMatrix();
	}
	
	public void drawPerson(GL2 gl, ObjLoader person) {
		gl.glPushMatrix();

		material(gl, 0.8f, 0f, 0.6f);
		
		gl.glTranslated((mWorldX + mWorldWidth) / 2 - 100, mPeronHeight / 2, (mWorldZ + mWorldDepth)/2 -200);
		gl.glScalef(2, 2, 2);
		person.drawModel(gl);

		gl.glPopMatrix();
	}
	
	public void drawBilboard(GL2 gl, ObjLoader board) {
		gl.glPushMatrix();

		material(gl, 1f, 1f, 1f);
		
		gl.glTranslated(mWorldX + 500, mPeronHeight / 2, (mWorldZ + mWorldDepth)/2-200);

		board.drawModel(gl);

		gl.glPopMatrix();	
	}
	
	public void drawBulb(GL2 gl) {
	    gl.glPushMatrix();
        material(gl, 1f, 1f, 0f);
        gl.glTranslatef((mWorldX + mWorldWidth) / 2, mWorldY + mWorldHeight - 100, (mWorldZ + mWorldDepth)/2 - 1000);
        GLUquadric q=mGlu.gluNewQuadric();
        mGlu.gluSphere(q, 200f,20,20);
        mGlu.gluDeleteQuadric(q);
	    gl.glPopMatrix(); 
	    
	    gl.glPushMatrix();
        material(gl, 1f, 1f, 0f);
        gl.glTranslatef((mWorldX + mWorldWidth) / 2, mWorldY + mWorldHeight - 100, (mWorldZ + mWorldDepth)/2 + 1000);
        GLUquadric q2=mGlu.gluNewQuadric();
        mGlu.gluSphere(q2, 200f,20,20);
        mGlu.gluDeleteQuadric(q2);
	    gl.glPopMatrix(); 
	}
	
	public int getWorldDepth() {
		return mWorldZ + mWorldDepth + 10;
	}
	
	public Vector3D initialCameraPosition() {
		return new Vector3D((mWorldX + mWorldWidth)/2, (mWorldY+mWorldHeight)/2, (mWorldZ + mWorldDepth)/2);
	}
	
	public Vector3D initialLightPosition1() {
		return new Vector3D((mWorldX + mWorldWidth) / 2, mWorldY + mWorldHeight - 100, (mWorldZ + mWorldDepth)/2 - 1000);
	}
	
	public Vector3D initialLightPosition2() {
		return new Vector3D((mWorldX + mWorldWidth) / 2, mWorldY + mWorldHeight - 100, (mWorldZ + mWorldDepth)/2 + 1000);
	}
	
	private void material(GL2 gl, float r, float g, float b){
		float[] m = {r, g, b, 1};

		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, m,0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, m, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, m, 0);
	}
}
