uniform mat4 uModelViewProjectionMatrix;

attribute vec4 aColor;
attribute vec3 aNormal;
attribute vec4 aPosition;
attribute vec2 aTexCoord;

varying vec2 vTexCoord;
varying vec4 vColor;

void main() {
	gl_Position = uModelViewProjectionMatrix * aPosition;
	vColor = aColor;
	vTexCoord = aTexCoord;
}
