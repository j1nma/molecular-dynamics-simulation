fid = fopen('../output/bigParticleTrajectory/trajectory.txt');
x = [];
y = [];
# Read file
while (!feof(fid))
    # Parse position
    position = fgetl(fid);
    [positionX positionY] = strsplit(position(2:end-1), "; "){1,:};
    x = [x, str2num(positionX)];
    y = [y, str2num(positionY)];
endwhile

x = x(2:end);
y = y(2:end);

clf;
hold on;
h = plot(x,y);
hold off;


#print(sprintf("../output/bigParticleTrajectory/BigParticleTrajectory-Times=%d.jpg", times), "-djpg")