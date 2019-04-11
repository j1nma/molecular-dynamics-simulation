function bigParticleTrajectory(index, temperature)
fid = fopen('./output/bigParticleTrajectory/trajectory.txt');
x = [];
y = [];

# Read limit time
limitTime = fgetl(fid);

# Read file
while (!feof(fid))
    # Parse position
    position = fgetl(fid);
    [positionX positionY] = strsplit(position(1:end), " "){1,:};
    x = [x, str2num(positionX)];
    y = [y, str2num(positionY)];
endwhile

x = x(2:end);
y = y(2:end);

color='rmbc';
markers = '.o*+x';
props = {"color", color(index+1), "marker", markers(index+1),'LineStyle','none'};

h = plot(x, y, sprintf(";T %d = %.10e K;", index, temperature));
set (h, props{:})
xlabel("Coordenada X", 'fontsize', 16);
ylabel("Coordenada Y", 'fontsize', 16);
set(gca, 'fontsize', 18);
axis([0 0.5 0 0.5])
grid on

# hold previous trajectory plots
hold all

print(sprintf("./output/bigParticleTrajectory/BigParticleTrajectory-Time=%s.jpg", limitTime), "-djpg")
end