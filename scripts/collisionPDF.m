data = load('../output/collisionFrequency/collision_frequency.txt');
N = 100;
count = numel(data)
dataMean = mean(data)
maxTime = max(data)
sprintf("Frecuencia de Colisiones = %d", 1/dataMean)
xRange = 0:0.001:maxTime;

figure(1)
[nn, xx] = hist(data, 2*N); %# Bin the data
bar(xx, nn);
xlabel("Tiempo entre colisiones(s)", 'fontsize', 16);
ylabel("Cantidad colisiones por unidad de tiempo", 'fontsize', 16);
set(gca, 'fontsize', 18);
axis([0 0.15])
grid on

print(sprintf("../output/collisionFrequency/Histogram-collision-frequency-N=%d.jpg", N), "-djpg")

figure(2)
[nn, xx] = hist(data, 2*N); %# Bin the data
bar(xx, nn ./ (count * (xx(2) - xx(1))));
xlabel("Tiempo entre colisiones(s)", 'fontsize', 16);
ylabel("Densidad de probabilidad", 'fontsize', 16);
set(gca, 'fontsize', 18);
axis([0 0.15])
grid on

print(sprintf("../output/collisionFrequency/PDF-collision-frequency-N=%d.jpg", N), "-djpg")