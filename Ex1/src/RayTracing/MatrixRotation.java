package RayTracing;

public class MatrixRotation {

	
	public static Vector rotateX(Vector v, double theta) {
		theta = theta * Math.PI / 180;
		// Multiplying by rotation matrix.
		return new Vector(
				v.getX(), // Omitted zeroes.
				Math.cos(theta) * v.getY() + (-1) * Math.sin(theta) * v.getZ(), // Omitted zero.
				Math.sin(theta) * v.getY() + Math.cos(theta) * v.getZ() // Omitted zero.
				);
	}
	
	
	public static Vector rotateY(Vector v, double theta) {
		theta = theta * Math.PI / 180;
		// Multiplying by rotation matrix.
		return new Vector(
				Math.cos(theta) * v.getX() + Math.sin(theta) * v.getZ(), // Omitted zero.
				v.getY(), // Omitted zeroes.
				(-1) * Math.sin(theta) * v.getX() + Math.cos(theta) * v.getZ() // Omitted zero.
				);
	}
	
	
	public static Vector rotateZ(Vector v, double theta) {
		theta = theta * Math.PI / 180;
		// Multiplying by rotation matrix.
		return new Vector(
				Math.cos(theta) * v.getX() + (-1) * Math.sin(theta) * v.getY(), // Omitted zero.
				Math.sin(theta) * v.getX() + Math.cos(theta) * v.getY(), // Omitted zero.
				v.getZ() // Omitted zeroes.
				);
	}
}
