//Envelope GUI.
//Add Node - Control-click
//Delete node - delete key
//Change slope - Select Node. Then Option-click and drag.
//Delete slope - Select Node.  Shift-click on node.
//Add or remove a release node - Double click.

//This is a work in progress.  Jeremy Van Buskirk - jeremyvanb@yahoo.com

e = EnvEdit();

e.asEnv.postcs;

e.asEnv.test;

//Add to a window.
(
w = Window.new;
w.addFlowLayout();

c = EnvEdit(parent: w.view, bounds: 180@125);

d = EnvEdit(parent: w.view, bounds: 180@125);

w.front;
)



//You can also set the env.
//You can not set releaseNode via Env currently. ReleaseNode must be set using the GUI

g = EnvEdit(Env([ 0.0, 1.0, 0.5, 0.25, 0.0 ], [ 0.25, 0.25, 0.25, 0.25 ], [-5, -5, -5, -5]));

g.asEnv.postcs;

g.asEnv.test;


//GUI range will automatically be set between the min. and max. levels.
//GUI duration is set to total duration

n = EnvEdit(Env([ 20.0, 800.0, 400, 20.0 ], [ 1.5, 0.5, 1 ], [3, -3, 3]));



//Examples with sound.

//amplitude Env.

g = EnvEdit(Env([ 0.0, 1.0, 0.0 ], [ 0.5, 0.5 ], \sin));

(
{
	    SinOsc.ar(470) * EnvGen.kr(g.asEnv, doneAction: 2)
}.play
)


//Frequency Env.

f = EnvEdit(Env([ 100.0, 400.0, 250.0, 100 ], [ 4.5, 3.5, 2.3], [3, 3, 3]));
f.asEnv.postcs;

(
{
	    SinOsc.ar(EnvGen.kr(f.asEnv, doneAction: 2))
}.play
)
