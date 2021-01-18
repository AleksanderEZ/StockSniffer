package stocksniffer.view;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DefaultURLAnalyzer implements URLAnalyzer {

    String ultPag = "";
    Document doc = null;

    public DefaultURLAnalyzer(String url) {
        doc = docGetter(url);
    }

    /**
     * Genera el documento.
     *
     * @version 1.0, 25/11/2020
     * @author Aleksander Borysov Ravelo
     */
    private Document docGetter(String url) {
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ex) {
            System.out.println("ERROR DefaultURLAnalyzer:: IO: " + ex.getMessage());
        }
        return doc;
    }

    /**
     * Comprueba la disponibilidad de un producto.
     *
     * @version 1.0, 25/11/2020
     * @author Aleksander Borysov Ravelo
     * @param url
     * @return AVAILABLE(true), N-AVAILABLE(false)
     */
    @Override
    public boolean checkAvailability(String url) {
        //VARIABLES
        boolean result = false;
        String domain = "";
        
        //GET DOMAIN
        try {
            domain = new URL(url).getHost();
        } catch (MalformedURLException ex) {
            System.out.println("ERROR DefaultURLAnalyzer:: checkAvailability(MalformedURL)" + ex.getMessage());
        } catch (NullPointerException ex) {
            System.out.println("ERROR DefaultURLAnalyzer:: checkAvailability(TimeOut)" + ex.getMessage());
        }
        
        //PARSING
        Elements row = null;
        switch (domain) {
            case "www.coolmod.com" -> {
                row = doc.select("span.product-availability");
                if (!"Sin Stock".equals(row.text())) {
                    result = true;
                }
            }

            case "www.amazon.es" -> {
                row = doc.select("div.a-section.a-spacing-base span.a-size-medium.a-color-price");
                if (!row.text().trim().equals("No disponible.")) {
                    row = doc.select("div.a-section.a-spacing-mini");
                    if (row.text().trim().equals("Vendido y enviado por Amazon.")) result = true;
                }
            }

            case "www.pccomponentes.com" -> {
                row = doc.select("div.row.envio div.col-xs-12.col-sm-9");
                if (!"Sin fecha exacta de entrada".equals(row.text())) {
                    result = true;
                }
            }
            default ->
                System.out.println("Unsupported website");
        }
        ultPag = domain;
        return result;
    }

    /**
     * Devuelve el nombre de un producto.
     *
     * @version 1.0, 25/11/2020
     * @author Aleksander Borysov Ravelo
     * @return Nombre del producto
     */
    @Override
    public String nameExtractor() {
        String title = doc.title();
        return title;
    }

    @Override
    public String priceExtractor() {
        Elements row;
        String result = "";
        switch (ultPag) {
            case "www.coolmod.com" -> {
                row = doc.select("div.container-price-total");
                for (Element e : row.select("span")) {
                    result += e.text();
                }
            }
            case "www.amazon.es" -> {
                row = doc.select("td.a-span12 span.priceBlockBuyingPriceString");
                result = row.text();
            }
            case "www.pccomponentes.com" -> {
                row = doc.select("div.precioMain");
                for (Element e : row.select("span")) {
                    result += e.text();
                }
                result = result.substring(0, result.length() - 4);
            }
            default ->
                System.out.println("Unsupported website");
        }
        return result;
    }
}
