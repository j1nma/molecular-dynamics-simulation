function particleDiffusion(index)
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

	fclose(fid);

	#color='rmbcgyrmbc';
    markers = '+o*.xsd^v><ph';
    #props = {"color", color(index+1), "marker", markers(index+1), 'LineStyle', 'none'};
    props = {"marker", markers(index+1), 'LineStyle', 'none'};
    h = plot(xRange, d);
    set(h, props{:})
    xlabel("Tiempo (s)", 'fontsize', 16);
    ylabel("DCM (m^2)", 'fontsize', 16);
    set(gca,'xtick',xRange);
    set(gca, 'fontsize', 18);
    axis([0 limitTime])
    grid on

    hold all

	print(sprintf("./output/bigParticleDiffusion/BigParticleDiffusion-Time=%d.jpg", limitTime), "-djpg")
end

