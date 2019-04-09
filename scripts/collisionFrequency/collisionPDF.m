data = load('./collision_frequency.txt');
N = 100;
count = numel(data)
dataMean = mean(data)
sprintf("Frecuencia de Colisiones =  %d", 1/dataMean)
xRange = 0:0.001:0.1;
H = hist(data, N + 1); %# Bin the data
bar(xRange, H./count);
xlabel("Tiempo entre colisiones(s)", 'fontsize', 16);
ylabel("Probabilidad", 'fontsize', 16);
set(gca, 'fontsize', 18);
axis([0 0.15])
grid on