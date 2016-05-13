//Envelope GUI.
//Control-click adds a node
//Delete key deletes the node
//Option=click and drag changes the slope.
//Double click sets the release node.

//This is a work in progress.  Jeremy Van Buskirk - jeremyvanb@yahoo.com

e = EnvEdit();

e.asEnv.postcs;

e.asEnv.test;


(
w = Window.new;
w.addFlowLayout();

c = EnvEdit(parent: w.view, bounds: 180@125);

d = EnvEdit(parent: w.view, bounds: 180@125);

w.front;
)

c.asEnv.duration_(8).test;

d.asEnv.duration_(8).test;


//You can add a releaseNode
b.releaseNode_(2);

//You can also set the env.
g = EnvEdit(Env([ 0.0, 1.0, 0.0 ], [ 0.5, 0.5 ]));

g.asEnv.test;

g.asEnv.postcs;


//Here is everything together.

(
w = Window.new.front;

g = EnvEdit(Env([ 0.0, 1.0, 0.0 ], [ 0.5, 0.5 ]), w, 180@125);
)

g.asEnv.test;


(
{
    var env = Env([0, 1, 0.5, 1, 0], [0.01, 0.5, 0.02, 0.5]);
	    SinOsc.ar(470) * EnvGen.kr(g.asEnv, doneAction: 2)
}.play
)

//You can set an range and duration.  If you set give a Env as an argument, range will scale the env.

h = EnvEdit(Env([0, 0.5, 0], [3.5, 3.5]), range: [100, 400], duration: 7);

