data = load('./output/lastThirdSpeeds/initial_speeds.txt');
count = numel(data)
dataMean = mean(data)
maxSpeed = max(data)
sprintf("Promedio del módulo de las velocidades (en t=0) = %5.3f ± %5.3f", dataMean, std(data))
xRange = 0:0.001:maxSpeed;

figure(1)
[nn, xx] = hist(data, xRange); %# Bin the data
bar(xx, nn ./ (count * (xx(2) - xx(1))));
xlabel("Módulo de velocidad (m/s)", 'fontsize', 16);
ylabel("Densidad de probabilidad", 'fontsize', 16);
set(gca, 'fontsize', 18);
axis([0 maxSpeed])
grid on
print("./output/lastThirdSpeeds/PDF-speeds-initial.jpg", "-djpg")

data2 = load('./output/lastThirdSpeeds/last_third_speeds.txt');
count2 = numel(data2)
dataMean2 = mean(data2)
maxSpeed2 = max(data2)
sprintf("Promedio del módulo de las velocidades (en el último tercio) = %5.3f ± %5.3f", dataMean2, std(data2))
xRange2 = 0:0.001:maxSpeed2;

figure(2)
[nn2, xx2] = hist(data2, xRange2); %# Bin the data
bar(xx2, nn2);
xlabel("Módulo de velocidad", 'fontsize', 16);
ylabel("Densidad de probabilidad", 'fontsize', 16);
set(gca, 'fontsize', 18);
axis([0 maxSpeed2])
grid on
print("./output/lastThirdSpeeds/PDF-speeds-last-thirds.jpg", "-djpg")