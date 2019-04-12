function d = particleDiffusion
    fid = fopen('./output/bigParticleDiffusion/big_particle_diffusion.txt');

	d = [0.0];

	# Read limit time
	limitTime = str2num(fgetl(fid));
	xRange = 0:floor(0.1 * limitTime):limitTime;

	# Read initial positions
	initialPosition = fgetl(fid);
	[initialX initialY] = strsplit(initialPosition(1:end), " "){1,:};
	initialX = str2num(initialX);
	initialY = str2num(initialY);

	# Read file
	while (!feof(fid))
	    # Parse position
	    position = fgetl(fid);
	    [positionX positionY] = strsplit(position(1:end), " "){1,:};
	    d = [d, norm([str2num(positionX) str2num(positionY)] - [initialX initialY], 2)];
	endwhile

	#color='rmbc';
    #markers = '.o*+x';
    #props = {"color", 'r', "marker", '.', 'LineStyle', 'none'};
    #h = plot(xRange, d);
    #set(h, props{:})
    #axis([0 limitTime])
    #xlabel("Tiempo (s)", 'fontsize', 16);
   # ylabel("DCM (m/s2)", 'fontsize', 16);
   # set(gca, 'fontsize', 18);
   # set(gca,'xtick',xRange);
   # grid on

	#print(sprintf("./output/bigParticleDiffusion/BigParticleDiffusion-Time=%s.jpg", limitTime), "-djpg")
end

