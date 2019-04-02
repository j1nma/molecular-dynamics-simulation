#! /bin/octave -qf

defaultVelocity = 0.03;
duration=2000;

rc=1;
simulation_data_values={40, 3.1, 's', 3, 'r'; 100, 5, 'd', 4, 'b'; 400, 10, 'x', 9, 'g'};

etha_values=0:0.5:5;

hold on;

for i=1:rows(simulation_data_values)
       N=simulation_data_values{i,1};
       L=simulation_data_values{i,2};
       M=simulation_data_values{i,4};
       marker=simulation_data_values{i,3};

       outputFileName = sprintf("./output/duration=%d/N=%d-L=%d-M=%d.txt", duration, N, L, M);
       outputFile = fopen(outputFileName, 'r');

       va_plot_values = zeros(size(etha_values));
       std_plot_values = zeros(size(etha_values));

      % Read va
      tline = fgetl(outputFile);
      va_plot_values = str2num(tline);

      % Read std
      tline = fgetl(outputFile);
      std_plot_values = str2num(tline);

      fmt = sprintf(".%s", simulation_data_values{i,5});
      plot = errorbar(etha_values, va_plot_values, std_plot_values, fmt);
      set(plot, "linestyle", "none");
      set(plot, "marker", marker);
      fclose(outputFile);
endfor;

xlabel('etha');
ylabel('Va');
axis([0 5.0 0 1.0])
title("El valor absoluto de la velocidad media frente al ruido para una densidad fija")
grid on
legend('N=40','N=100','N=400');
hold off;

print(sprintf("./graphics/va_curves_duration=%d.jpg", duration), "-djpg")
