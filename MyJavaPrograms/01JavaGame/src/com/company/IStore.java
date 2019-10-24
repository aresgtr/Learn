package com.company;

import java.util.ArrayList;
import java.util.List;

public interface IStore {

    void initializeStore();

    void printItemList();

    void printOptions();

    int getItemPrice(int i);

    String getItemName(int i);

    int getItemCharacteristics(int i);
}
