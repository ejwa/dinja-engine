uniform mat4 uModelViewProjectionMatrix;

attribute vec4 aPosition;

void main() {
	gl_Position = uModelViewProjectionMatrix * aPosition;
}
