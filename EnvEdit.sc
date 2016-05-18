//Envelope GUI.
//Control-click adds a node
//Delete key deletes the node
//Option-click and drag changes the slope.
//Double-click sets the release node.

//This is a work in progress.  Jeremy Van Buskirk - jeremyvanb@yahoo.com




EnvEdit {
	var window, spec, envView, envelope, slope, node=nil, newNode, releaseNode=nil, container, <rangeSet, <durationSet;

	*new {arg env, parent, bounds, range = [0, 1], duration=1;
		^super.new.init(env, parent, bounds, range, duration);
	}

	init {arg env, parent, bounds, range, duration;
		var p1, p2, envAdd, order, time, level, slopeOrder;
		var slopePoint, slopeSpec, levelsArray;

		rangeSet=range;
		durationSet=duration;
		if(parent.isNil){
			window = Window("envelope",
				Rect(150 , Window.screenBounds.height - 250, 250, 100)).front;
			window.view.decorator = FlowLayout(window.bounds);
			window.view.palette = QPalette.dark;
		    }{
			window=CompositeView(parent, bounds).resize_(5);
		    window.decorator = FlowLayout(window.bounds);
			window.palette = QPalette.dark;
		};

		spec = ControlSpec(window.bounds.height-15, 0, step: 0.001);
		envView = EnvelopeView(window, window.bounds-15);


		if(env.isNil, {
			envelope = [[0.0,0.5,1],[0.0, 1.0, 0.0 ]];
			slope = [0, 0, 0];
			envView.value_(envelope)
			},{
				//set GUI range and duration from env
				levelsArray = env.levels;
				rangeSet = [levelsArray[levelsArray.minIndex], levelsArray[levelsArray.maxIndex]];
				durationSet = env.duration;
				//EnvelopeView needs levels normalized to set range properly.
				env = env.levels_(levelsArray.normalize);
				envView.setEnv(env);
				envelope = envView.value; //EnvelopeView seems to return values scaled between 0-1
				slope = [0, 0, 0, 0];

			}
		);


		envView.drawLines_(true)
		     .keepHorizontalOrder_(true)
             .selectionColor_(Color.red)
             .strokeColor_(Color.white)
		     //.fillColor_(Color.blue)
             .drawRects_(true)
		     //.style_(0) //this seems backward
		     .thumbSize_(13)
             .resize_(5)
             .step_(0.001)
		     .action_({arg envView;
			envView.setString(envView.selection[0],
				[envView.y.round(0.001).linlin(0, 1, rangeSet[0], rangeSet[1]),
					envView.x.round(0.001).linlin(0, 1, 0, durationSet)].asString);
			    envelope=envView.value;
		})
             .mouseDownAction_({|view, x, y, modifiers, buttonNumber, clickCount|
			     if(buttonNumber == 1){
		             spec = ControlSpec(window.bounds.height-15, 0, step: 0.001);
		             p1 = x/(window.bounds.width-15);
		             p2 = spec.unmap(y);
		             envAdd = [[], []];
				     slopeOrder = [];
		             envAdd[0] = envelope[0].add(p1);
		             envAdd[1]= envelope[1].add(p2.value);
				     slopeOrder = slope.add(0);
		             order=envAdd[0].order;
		             time = envAdd[0][order];
		             level = envAdd[1][order];
				     slope = slopeOrder[order];
				     envView.curves = slope;
		             envAdd = [time, level];
		             envView.value_(envAdd);
				     envelope = envAdd;
				     newNode = List.newFrom(time).detectIndex({arg item; item==p1});
				     envView.selectIndex(newNode);
				     if(releaseNode.notNil){
					     if(newNode<=releaseNode[0]){
					          releaseNode[0] = releaseNode[0]+1;
					          releaseNode.postln;
					          envView.fillColor_(Color.white);
				              envView.setFillColor(releaseNode[0], Color.red);
					              }
				             }
			         };
			     if(modifiers == 524288){
				     slopeSpec = [20, -20].asSpec;
				     node = envView.selection;
				     envView.setEditable(node[0], false);
				     envView.mouseMoveAction_({|view, x, y, modifiers|
					     slopePoint = (y/(window.bounds.height-15)).wrap(0, 1);
					     slopePoint = slopeSpec.map(slopePoint);
					     slope[node[0]] = slopePoint;
					     envView.curves = slope;

				})
			};
			     if(modifiers.isShift){
				      if(envView.selection.notNil){
					       node=envView.selection;
				           slope[node[0]] = 0;
				           envView.curves = slope;
				}
			};
			    if(clickCount==2){
				   if(envView.selection==releaseNode){
					   releaseNode = envView.selection;
					   envView.setFillColor(releaseNode[0], Color.white);
					   releaseNode = nil;
				     }{
				     releaseNode = envView.selection;
					 envView.fillColor_(Color.white);
				     envView.setFillColor(releaseNode[0], Color.red);
				     }

			    }


		})
		     .mouseUpAction_({envView.mouseMoveAction_({\nil}); //look at removeAction
		          if(node.notNil){envView.setEditable(node[0], true)};
			      if(envView.selection[0].notNil){envView.setString(envView.selection[0],"")} })
		     .keyUpAction_({|view, char, modifiers, unicode, keycode, key|
	               if(key==16777219){envelope[0].removeAt(envView.selection[0]);
				     envelope[1].removeAt(envView.selection[0]);
				     slope.removeAt(envView.selection[0]);
				     envView.value_(envelope)}});

	}

	asEnv {
		var envSet, envOrder;
		envOrder = (envelope ++ [slope]).flop;
		if(releaseNode.isNil){
			envSet = Env.xyc(envOrder);
			envSet.levels.do({arg item, i; envSet.levels[i]=item.linlin(0, 1, rangeSet[0], rangeSet[1])});
			^envSet.duration_(durationSet);
		}{
			envSet = Env.xyc(envOrder);
			envSet.levels.do({arg item, i; envSet.levels[i]=item.linlin(0, 1, rangeSet[0], rangeSet[1])});
			^envSet.releaseNode_(releaseNode[0]).duration_(durationSet);
		}
	}
}


//I am using node and envView.selection.  Pick one.
//I need a check if bounds is nil.
//I need a method to change the resize.
