precision highp float;
uniform vec2      resolution;
uniform float     time;

float rand(vec2 n) {
    return fract(cos(dot(n, vec2(2.9898, 20.1414))) * 5.5453);
    // todo actually make the shaders not suck lol
}

float noise(vec2 n) {
    const vec2 d = vec2(0.0, 1.0);
    vec2 b = floor(n), f = smoothstep(vec2(0.0), vec2(1.000002), fract(n));
    return mix(mix(rand(b), rand(b + d.yx), f.x), mix(rand(b + d.xy), rand(b + d.yy), f.x), f.y);
}

float fbm(vec2 n){
    float total=0., amplitude=2.5;
    for (int i=0;i<18;i++){
        total+=noise(n)*amplitude;
        n+=n;
        float t = sin(time) * .07 + .49;
        amplitude*=t;
    }
    return total;
}


void main(void){
    const vec3 c1=vec3(0.502, 0.1059, 0.1059);
    const vec3 c2=vec3(167./255., 93./255., 110./255.);
    const vec3 c3=vec3(0.4902, 0.5333, 0.4902);
    const vec3 c4=vec3(0.2118, 0.3451, 0.2706);
    const vec3 c5=vec3(0.3176, 0.2549, 0.4);
    const vec3 c6=vec3(0.8, 0.3569, 0.3569);

    vec2 p=gl_FragCoord.xy*10./resolution.xx;
    float q=fbm(p-time*.07);
    vec2 r=vec2(fbm(p+q+time*-0.4-p.x-p.y), fbm(p+q-time*0.3));
    vec3 c=mix(c1, c2, fbm(p+r))+mix(c3, c4, r.x)-mix(c5, c6, r.y);
    float grad=gl_FragCoord.y/resolution.y;
    gl_FragColor=vec4(c, 255.);
}