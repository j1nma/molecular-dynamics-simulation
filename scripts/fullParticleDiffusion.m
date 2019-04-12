function fullParticleDiffusion
    fid = fopen('./output/bigParticleDiffusion/Mean-and-Std-N=100-times=10.txt');

	mean_values = [0.0];
	std_values = [0.0];

	k = 0;

	limitTime = 120;

	xRange = 0:floor(0.1 * limitTime):limitTime;

	# Read file
	while (!feof(fid) && k < 10)
	    mean = fgetl(fid);
	    mean_values = [mean_values, str2num(mean)];
	    k = k + 1;
	endwhile

	while (!feof(fid))
	    std = fgetl(fid);
	    std_values = [std_values, str2num(std)];
	endwhile

	color='rmbc';
    markers = '.o*+x';
    props = {"color", 'r', "marker", '.', 'LineStyle', 'none'};

    plot = errorbar(xRange, mean_values, std_values);

    set(plot, props{:})
    axis([0 limitTime])
    xlabel("Tiempo (s)", 'fontsize', 16);
    ylabel("DCM (m/s2)", 'fontsize', 16);
    set(gca, 'fontsize', 18);
    set(gca,'xtick',xRange);
    grid on

    fclose(fid);

	print(sprintf("./output/bigParticleDiffusion/FullBigParticleDiffusion-Time=%d.jpg", limitTime), "-djpg")
end

