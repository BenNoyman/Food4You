package com.example.food4you.Helper;

import android.content.Context;
import android.widget.Toast;
import com.example.food4you.Callbacks.ChangeNumberItemsListener;
import com.example.food4you.Models.Foods;

import java.util.ArrayList;


public class ManagmentCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = TinyDB.getInstance();
    }

    public void clearCart() {
        tinyDB.remove("CartList"); // This will clear the cart list from TinyDB
    }

    //insert a food item into the cart
    public void insertFood(Foods item) {
        ArrayList<Foods> listpop = getListCart();   //retrieve the current list
        boolean existAlready = false;   //flag to check if the item already exists
        int n = 0;
        for (int i = 0; i < listpop.size(); i++) {  //check if the item already exists in the cart
            if (listpop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;    //item exist
                n = i;  //store the index
                break;  //exit
            }
        }
        //if the item exist update its quantity else add it to the cart
        if(existAlready){
            listpop.get(n).setNumberInCart(item.getNumberInCart()); //update quantity
        }
        else{
            listpop.add(item);  //add new item to the cart
        }
        tinyDB.putListObject("CartList",listpop);   //save the updated cart list in the TinyDB
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Foods> getListCart() { //get the list of items currently in the cart

        return tinyDB.getListObject("CartList");

    }

    //calculate the total fee of items in the cart
    public Double getTotalFee(){
        ArrayList<Foods> listItem=getListCart();    //get the items

        double fee=0;   //init total fee

        //calculate the total fee by summing the price of each item multiply by his quantity
        for (int i = 0; i < listItem.size(); i++) {
            fee=fee+(listItem.get(i).getPrice()*listItem.get(i).getNumberInCart());
        }
        return fee;
    }

    //decrease the quantity of an item in the cart
    public void minusNumberItem(ArrayList<Foods> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener){
        if(listItem.get(position).getNumberInCart()==1){    //check if the quantity is 1 if so remove the item from the cart
            listItem.remove(position);  //remove item from cart
        }
        else{   //decrease the quantity of the item by 1
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()-1);
        }
        tinyDB.putListObject("CartList",listItem);  //save the updated cart list in TinyDB
        changeNumberItemsListener.change(); //notify the listener about the change
    }

    //increase the quantity of the item in the cart
    public  void plusNumberItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()+1);
        tinyDB.putListObject("CartList",listItem);  //save the updated cart list in TinyDB
        changeNumberItemsListener.change(); //notify the listener about the change
    }
}
