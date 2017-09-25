package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;


/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    private int numOfCoffees = 0;
    private boolean hasWhippedCream = false;
    private boolean hasChocolate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefaultTotal();
    }

    /**
     *Sets the Order Summary text to the User's chosen currency
     */
    public void setDefaultTotal (){
        TextView textView = (TextView) findViewById(R.id.order_summary_text_view);
        textView.setText(NumberFormat.getCurrencyInstance().format(0));
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        int cost = calculatePrice();
        Log.v("MainActivity", "The Total cost is " + cost); //Debugging purposes
        displayMessage(createOrderSummary(cost));
    }

    public void sendOrder (View view){
        submitOrder(view);

        TextView textView = (TextView) findViewById(R.id.order_summary_text_view);
        composeEmail(getResources().getString(R.string.send_subject), textView.getText().toString());
    }

    /**
     * An intent to send the coffee order via email with the order summary as the message
     * @param subject The subject of the email
     * @param message The body of the email
     */
    public void composeEmail(String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("*/*"); //Text Plain
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null){//Check if there is app that can "catch" the intent
            startActivity(intent);
        }
        else{ //If no relevant app installed - toast error message
            Context context = getApplicationContext();
            CharSequence errorText = getResources().getString(R.string.error_email_app);
            int duration = Toast.LENGTH_SHORT;

            Toast.makeText(context, errorText, duration).show();
        }

    }

    /**
     * Calculates the Price of the total number of coffees ordered
     * @return the price payable
     */
    private int calculatePrice(){
        final int PRICE = 5;
        int totalPrice = numOfCoffees * PRICE;
        if (hasWhippedCream){
            totalPrice += numOfCoffees;
        }
        if (hasChocolate){
            totalPrice += numOfCoffees*2;
        }
        return totalPrice;
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        numOfCoffees++;
        display(numOfCoffees);
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        if (numOfCoffees != 0){
            numOfCoffees--;
            display(numOfCoffees);
        }
        else{
            Context context = getApplicationContext();
            CharSequence text = getResources().getString(R.string.negative_cups);
            int duration = Toast.LENGTH_SHORT;

            Toast.makeText(context, text, duration).show();
        }

    }

    /**
     * Checks the current state of the checkbox and updates the hasWhippedCream boolean
     * Displays a toast, notifying the user of the extra related cost
     * @param view the view of the checkbox
     */
    public void checkWhippedCream (View view){
        CheckBox tick = (CheckBox) findViewById(R.id.checkBox_cream);

        if (tick.isChecked()){
            Context context = getApplicationContext();
            CharSequence text = getResources().getString(R.string.toast_whipped_cream) + " " + NumberFormat.getCurrencyInstance().format(1);
            int duration = Toast.LENGTH_SHORT;

            Toast.makeText(context, text, duration).show();
        }
        hasWhippedCream = tick.isChecked();
    }

    /**
     * Checks the current state of the checkbox and updates the hasChocolate boolean
     * Displays a toast, notifying the user of the extra related cost
     * @param view the view of the checkbox
     */
    public void checkChocolate (View view){
        CheckBox tick = (CheckBox) findViewById(R.id.checkBox_chocolate);

        if (tick.isChecked()){
            Context context = getApplicationContext();
            CharSequence text = getResources().getString(R.string.toast_chocolate) + " " + NumberFormat.getCurrencyInstance().format(2);
            int duration = Toast.LENGTH_SHORT;

            Toast.makeText(context, text, duration).show();
        }
        hasChocolate = tick.isChecked();
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(getString(R.string.ammended_price, number));
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    /**
     * A Welcome message to the custom user name of the customer
     * @return the greeting sent to the name of the customer
     */
    private String createCustomGreeting(){
        EditText editText = (EditText) findViewById(R.id.editText);
        String name = editText.getText().toString();
        return getResources().getString(R.string.summary_welcome) + " " + name;
    }

    /**
     *A comprehensive summary of the order
     * @param cost the total cost of the order
     * @return The order summary including a welcome message, include whipped cream,
     * include chocolate, the quantity of coffees the total price in the users currency,
     * a thank you message.
     */
    private String createOrderSummary(int cost){
        return createCustomGreeting() + "\n" +
                getString(R.string.summary_add) + " " + getString(R.string.whipped_cream) + "? " + hasWhippedCream + "\n" +
                getString(R.string.summary_add) + " " + getString(R.string.chocolate) + "? " + hasChocolate + "\n" +
                getString(R.string.quantity) + ": " + numOfCoffees + "\n" +
                getString(R.string.summary_total) + ": " + NumberFormat.getCurrencyInstance().format(cost) + "\n" +
                getString(R.string.thank_you) + "!";
    }


}
