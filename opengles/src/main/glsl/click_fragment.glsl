precision mediump float;

uniform ivec3 uColor;
varying vec3 vColor;

void main() {
	gl_FragColor = vec4(float(uColor.r) / 255.0, float(uColor.g) / 255.0, float(uColor.b) / 255.0, 1.0);
}
