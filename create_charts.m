A = importdata("results_pushpull.txt")
%B = importdata("results_axis.txt")




%boxplot(A,'Labels',{'0.05','0.10','0.2','0.3','0.4','0.5','0.6','0.7','0.8','0.9','0.92','0.95'})

boxplot(A)
%boxplot(A,'PlotStyle','compact')
set(gca,'XTick',0:1:100)
set(gca,'XTickLabel',0:10:100)
xlabel('Disseminated Nodes (%)')
ylabel('Time taken to propagate(ms)')
title('PushPull Algorithm')

