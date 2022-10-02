#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float u_time;

varying vec4 v_color;
varying vec2 v_texCoord;


void main() {
    float dist = distance(vec2(0.5), v_texCoord) * 2.0;
    float alpha = smoothstep(.4, 1., dist);
    if (dist > 1.) alpha = 0.;
    gl_FragColor = vec4(.3, .3, .8, alpha);
}