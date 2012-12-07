NRTOscRender {
	var <reciver,<reciverfunc;
	var <cmdName, <addr, <func, <>name,<funcName, <>size;
	var <time,<>second,<>synthDefsFilePath,<>scoreFilePath,<>soundFilePath,>exist;

	*new{|cmdN, funcN, add|
		^super.new.init(cmdN, funcN, add);
	}

	init {|argcmdName, argfuncName, argaddr|
		cmdName = argcmdName;
		funcName = argfuncName;
		addr = argaddr;
		size = 10.0;
		reciver = OSCReceiver(cmdName,addr);
		synthDefsFilePath = File.getcwd;
		soundFilePath = File.getcwd;
		scoreFilePath = File.getcwd;

		//change synthDefsFilePath
		"SC_SYNTHDEF_PATH".setenv(synthDefsFilePath);

		time = Date.getDate.format("%Y%m%d%H%M%S");
		second = Date.getDate.format("%M%S")++(10.rand).asString;
		name = time;
		func = {	|msg|
				var graphFunc;
				var skip = false;
				var source,scoreFile,file,command;
				source = "{Out.ar(0," + msg +")}";
						msg.postln;

				try{
						graphFunc = source.compile.value;
						SynthDef(name:time,ugenGraphFunc:graphFunc).writeDefFile(synthDefsFilePath);
					}{|error|
						"fale to compilse source :".postln;
						source.postln;
						skip = true;
					 };

				if(skip!=true,{
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
					("./scsynth -N "++ scoreFile ++ " _ " ++ soundFilePath ++ time ++ ".wav 44100 WAVE int24 -o 2").unixCmd;

					}{|error|
						"fale to save soundFile".postln;
						error.postln;
					 };

				});

			};
		reciverfunc  = OSCReceiverFunction(cmdName,funcName,func);
		reciver.start;
	}

	synthDefsPath_{arg path;
		"SC_SYNTHDEF_PATH".setenv(path);
		synthDefsFilePath = path;
	}

	start {
		reciver.start;
	}

	stop {
		reciver.stop;
	}

	//clear { reciverfunc.all = IdentityDictionary.new }

	exist {
		var isEx;
		isEx = File.exists( soundFilePath ++ time++".aiff");
		^isEx;
	}
}

