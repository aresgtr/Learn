package com.company;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class StockList {

    private final Map<String, StockItem> list;  //  Create a Map to store Stock Items

    public StockList() {
        this.list = new LinkedHashMap<>();  //  Alphabetic order
    }

    public int addStock(StockItem item) {
        if (item != null) {
            //  check if already have quantities of this item
            StockItem inStock = list.getOrDefault(item.getName(), item);    //  不论list里有没有，到最后都会加上
            //  If there are already stocks on this item, adjust the quantity
            if (inStock != item) {
                item.adjustStock(inStock.quantityInStock());
            }

            list.put(item.getName(), item);
            return item.quantityInStock();
        }
        return 0;
    }

    public int sellStock(String item, int quantity) {
        StockItem inStock = list.getOrDefault(item, null);  //  没有的话就是null

        if ((inStock != null) && (inStock.quantityInStock() >= quantity) && (quantity > 0)) {
            inStock.adjustStock(-quantity);
            return quantity;
        }

        return 0;
    }

    public StockItem get(String key) {
        return list.get(key);
    }

    public Map<String, StockItem> Items() {
        return Collections.unmodifiableMap(list);
    }

    //  returns all the items in stock
    @Override
    public String toString() {
        String s = "\nStock List\n";
        double totalCost = 0.0;
        for (Map.Entry<String, StockItem> item : list.entrySet()) {
            StockItem stockItem = item.getValue();

            double itemValue = stockItem.getPrice() * stockItem.quantityInStock();

            s = s + stockItem + ". There are " + stockItem.quantityInStock() + " in stock. Value of items: ";
            s = s + String.format("%.2f", itemValue) + "\n";
            totalCost += itemValue;
        }

        return s + "Total stock value " + String.format("%.2f", totalCost);
    }
}
