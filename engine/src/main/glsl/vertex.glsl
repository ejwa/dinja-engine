uniform mat4 uModelViewProjectionMatrix;
attribute vec4 vPosition;

void main() {
	gl_Position = uModelViewProjectionMatrix * vPosition;
}
