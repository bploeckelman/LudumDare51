#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float u_time;

varying vec4 v_color;
varying vec2 v_texCoord;


void main() {
    vec2 noiseCoord1 = v_texCoord;
    vec2 noiseCoord2 = v_texCoord;

    float sin_factor1 = sin(u_time * .732);
    float cos_factor1 = cos(u_time * .732);

    float sin_factor2 = sin(-u_time * .852);
    float cos_factor2 = cos(-u_time * .852);

    noiseCoord1 = (noiseCoord1 - .5) * mat2(cos_factor1, sin_factor1, -sin_factor1, cos_factor1);
    noiseCoord1 += 0.5 + vec2(u_time*.1, 0);

    noiseCoord2 = (noiseCoord2 - .5) * mat2(cos_factor2, sin_factor2, -sin_factor2, cos_factor2);
    noiseCoord2 += 0.5 + vec2(u_time*.2, 0);

    vec4 noise1 = texture2D(u_texture, noiseCoord1);
    vec4 noise2 = texture2D(u_texture, noiseCoord2);

    float dist = distance(vec2(0.5), v_texCoord) * 2.0;
    float alpha = smoothstep(.4, 1., dist);
    if (dist > 1.) alpha = 0.;

    float noise = noise1.b * .2 + noise1.r * .5 + noise2.b * .3;

//    alpha *= noise1.b * 1.5;
//    alpha *= noise2.r * 1.5;
//    alpha *= noise2.b * 1.5;

    alpha *= noise;

    gl_FragColor = vec4(.3, .3, .8, alpha);
}