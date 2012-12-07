NRTOscRender {
	var <reciver;
	var <cmdName, <addr, <func, <>name, <>size;
	var <time,<>second,<>synthDefsFilePath,<>scoreFilePath,<>soundFilePath,>exist;

	*new{ |cmdN, funcN, add|
		^super.new.init(cmdN, funcN, add);
	}

	init { |argcmdName, argaddr|
		cmdName = argcmdName;
		addr = argaddr;
		size = 10.0;
		synthDefsFilePath = SynthDef.synthDefDir;
		soundFilePath = thisProcess.platform.recordingsDir ++ "/";
		scoreFilePath = File.getcwd;

		//change synthDefsFilePath
		"SC_SYNTHDEF_PATH".setenv(synthDefsFilePath);

		time = Date.getDate.format("%Y%m%d%H%M%S");
		second = Date.getDate.format("%M%S")++(10.rand).asString;
		name = time;
		func = { |msg|
			var graphFunc;
			var skip = false;
			var source,scoreFile,file,command;
			source = "{Out.ar(0," + msg[1] +")}";
			msg[1].postln;

			try{
				graphFunc = source.compile.value;
				SynthDef(name:name,ugenGraphFunc:graphFunc).writeDefFile(synthDefsFilePath);
				("write def file ... :" + name ).postln;
			} { |error|
				error.postln;
				"fale to compilse source :".postln;
				source.postln;
				skip = true;
			};

			if ( skip != true, {
				scoreFile = scoreFilePath ++ time ++".osc";
				file = File( scoreFile ,"w");

				command = [ 0.2, [9, time, second.asInteger, 0, 0]].asRawOSC;
				file.write(command.size);
				file.write(command);

				command = [ size, [11, second.asInteger]].asRawOSC;
				file.write(command.size);
				file.write(command);

				command = [ 10.2, [0]].asRawOSC;
				file.write(command.size);
				file.write(command);

				file.close;

				try{
					("SuperCollider.app/Contents/Resources/scsynth -N '"++ scoreFile ++ "' _ '" ++ soundFilePath ++ "/" ++ name ++ ".wav' 44100 WAVE int24 -o 2").unixCmd;
					("write sound file ... " ++ soundFilePath ++ name ++ ".wav" ).postln;
				}{|error|
					"fale to save sound file".postln;
					error.postln;
				}
			};);

		};
		reciver  = OSCFunc(func,cmdName,addr);
	}

	synthDefsPath_{arg path;
		"SC_SYNTHDEF_PATH".setenv(path);
		synthDefsFilePath = path;
	}

	enable {
		reciver.enable;
	}

	disable {
		reciver.disable;
	}

	free {
		reciver.free;
	}

	//clear { reciverfunc.all = IdentityDictionary.new }

	exist {
		var isEx;
		isEx = File.exists( soundFilePath ++ time++".aiff");
		^isEx;
	}
}

