data = load('../output/collision_frequency.txt');
count = numel(data)
dataMean = mean(data)
freqStr = sprintf("Frecuencia de Colisiones =  %d", 1/dataMean)
intervals = 2*max(data)/dataMean;
H = hist(data(:,1), 101);%,"facecolor", "m"
xRange = 0:0.001:0.1;
bar(xRange, H./numel(data));
xlabel ("Tiempo entre colisiones(s)", 'fontsize',16);
ylabel ("Probabilidad", 'fontsize',16);
set(gca,'fontsize',18);