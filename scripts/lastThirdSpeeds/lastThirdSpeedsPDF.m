data = load('./initial_speeds.txt');
count = numel(data)
dataMean = mean(data)
maxSpeed = max(data)
sprintf("Promedio del módulo de las velocidades (en t=0) = %5.3f ± %5.3f", dataMean, std(data))

figure(1)
[nn, xx] = hist(data, 2*N); %# Bin the data
bar(xx, nn);
xlabel("Módulo de las velocidades", 'fontsize', 16);
ylabel("Cantidad velocidades por valor", 'fontsize', 16);
set(gca, 'fontsize', 18);
axis([0 0.15])
grid on

figure(2)
[nn, xx] = hist(data, 2*N); %# Bin the data
bar(xx, nn ./ (count * (xx(2) - xx(1))));
xlabel("Módulo de las velocidades", 'fontsize', 16);
ylabel("Densidad de probabilidad", 'fontsize', 16);
set(gca, 'fontsize', 18);
axis([0 0.15])
grid on