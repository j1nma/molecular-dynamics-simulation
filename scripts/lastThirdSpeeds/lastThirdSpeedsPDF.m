data = load('./last_third_speeds.txt');
N = 100;
count = numel(data)
dataMean = mean(data)
maxTime = max(data)
sprintf("Promedio del módulo de las velocidades = %d +- %d", 1/dataMean, std(data))
xRange = 0:0.001:maxTime;

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