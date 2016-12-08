public class Vector3D {
	private final double mX;
	private final double mY;
	private final double mZ;

	public Vector3D(double x, double y, double z){
		this.mX = x;
		this.mY = y;
		this.mZ = z;
	}

	public float distance(Vector3D other) {
		return (float) Math.sqrt((mX-other.mX)*(mX-other.mX) + (mY-other.mY)*(mY-other.mY) + (mZ-other.mZ)*(mZ-other.mZ));
	}

	public Vector3D add(Vector3D delta) {
		return new Vector3D(mX+delta.mX, mY+delta.mY, mZ+delta.mZ);
	}
	
	public Vector3D substract(Vector3D vector) {
		return new Vector3D(mX - vector.getX(), mY - vector.getY(), mZ - vector.mZ);
	}
	
	public Vector3D multiply(float number) {
		return new Vector3D(mX*number, mY*number, mZ*number);
	}
	
	public Vector3D multiply(Vector3D vector) {
		return new Vector3D(mX * vector.getX(), mY * vector.getY(), mZ * vector.getZ());
	}
	
	public float distance() {
		return (float) Math.sqrt(mX * mX + mY * mY + mZ * mZ);
		
	}
	
	public double getX() {
		return mX;
	}
	
	public double getY() {
		return mY;
	}
	
	public double getZ() {
		return mZ;
	}

	public String toString(){
		return mX + ", " + mY + ", " + mZ;
	}
}
