package stocksniffer.view;

public interface URLAnalyzer {

    public boolean checkAvailability(String url);

    public String nameExtractor();

    public String priceExtractor();
}
