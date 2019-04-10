fid = fopen('../output/bigParticleTrajectory/trajectory.txt');
x = [];
y = [];

# Read limit time
limitTime = fgetl(fid);

# Read file
while (!feof(fid))
    # Parse position
    position = fgetl(fid);
    [positionX positionY] = strsplit(position(2:end-1), " "){1,:};
    x = [x, str2num(positionX)];
    y = [y, str2num(positionY)];
endwhile

x = x(2:end);
y = y(2:end);

h = plot(x,y);
xlabel("Coordenada X", 'fontsize', 16);
ylabel("Coordenada Y", 'fontsize', 16);
set(gca, 'fontsize', 18);
axis([0 0.5 0 0.5])
grid on

print(sprintf("../output/bigParticleTrajectory/BigParticleTrajectory-Time=%s.jpg", limitTime), "-djpg")