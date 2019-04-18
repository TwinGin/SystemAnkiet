
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/* 
 * System zarz¹dzania ankietami
 * @author Piotr Bajorek 
 * @version 1.0
 * @since 08.01.2019
 * */
public class Chart extends JFrame {
    String []text;
    int []count;
    public Chart(String data[],int counters[]) {
        text = data;
        count = counters;
        initUI();
    }

    private void initUI() {

        CategoryDataset dataset = createDataset();

        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle(text[0]);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private CategoryDataset createDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(count[0], text[0], text[1]);
        dataset.setValue(count[1], text[0], text[2]);
        dataset.setValue(count[2], text[0], text[3]);
        dataset.setValue(count[3], text[0], text[4]);
        return dataset;
    }

    private JFreeChart createChart(CategoryDataset dataset) {

        JFreeChart barChart = ChartFactory.createBarChart(
        		text[0],
                "",
                "Iloœæ odpowiedzi",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        return barChart;
    }

}