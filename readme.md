#NRTOscRender

Nonrealtime Sound File Render
-----------

ThisClass responces optional OSC Message and renders sound file to a directory.


Class Methods
-----------


**new(cmdName, funcMane, addr)**

	Create a NRTOscRender instance.

	cmdName - A String or Symbol to recive messages. This as addres of OSC message.
			
	funcName - A String to head of messages.
	
	addr - An instance of NetAddr to recive messages.


Instance Methods
-----------

**size**

	length of Sound File. Unit is Second. default is 10.0 seconds.
		
**start**

	start listeninng.
		
**stop**
	
	stop listenning.

**exist**
	
	Check sound file's existance.



Examples
-----------

	r = NRTOscRender("/writeFile","w",NetAddr("127.0.0.1",57120));
	
	//If you wants to set name of files. You set name with setter. default name is current date and time.
	
	r.name_("test_sound");
	
	//Also you can set directroy where to write files. Reciver generates three files, SynthDef File, Score File and SoundFile.
	//Please attention to write directory with last slash('/') whitch means directory
	
	r.synthDefsFilePath_("/Applications/SuperCollider/");
	r.soundFilePath_("/Applications/SuperCollider/");
	r.scoreFilePath_("/Applications/SuperCollider/");
	
	//Start listen.
	
	r.start;
	
	//Send OSC Message with cmdName, funcName and Strings of code that will be compiled as grafFunction of SynthDef.
	
	NetAddr("127.0.0.1",57120).sendMsg("/writeFile","w", "SinOsc.ar(440,0,0.3).dup" );
	
	//stop listen.
	r.stop;
	
	r.exist;



Installation
-----------


copy /NRTOscRender to Library/Application Support/SuperCollider/Extensions



Licence
-----------
NRTOscRender is licensed under the GNU GENERAL PUBLIC LICENSE Version 3.
