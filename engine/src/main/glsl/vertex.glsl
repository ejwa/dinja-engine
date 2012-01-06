uniform mat4 uModelViewProjectionMatrix;

attribute vec2 aTexCoord;
attribute vec4 aPosition;
attribute vec3 aNormal;

varying vec2 vTexCoord;

void main() {
	gl_Position = uModelViewProjectionMatrix * aPosition;
	vTexCoord = aTexCoord;
}
