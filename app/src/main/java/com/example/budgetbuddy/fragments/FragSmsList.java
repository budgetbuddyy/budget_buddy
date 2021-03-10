package com.example.budgetbuddy.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.model.SmsExpenses;
import com.example.budgetbuddy.model.Sms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FragSmsList extends FragBase {

    private Cursor c;
    private SmsExpenses smsExpenses;
    private ArrayList<SmsExpenses> expensesList;
    private ArrayList<Sms> smsList;




    @Override
    int getResourceLayout() {
        return R.layout.frag_sms_list;
    }

    @Override
    void setUpView() {

        init();
        getPermission();
    }

    private void init() {
        expensesList = new ArrayList<>();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (c != null){

            c.close();
        }
    }

    private int getMonth() {
        String currentDate = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
//        Log.e("month", currentDate);
        return Integer.parseInt(currentDate);
    }


    private int getDate() {
        String currentDate = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
//        Log.e("month", currentDate);
        return Integer.parseInt(currentDate);

    }

    private int getYear() {
        String currentDate = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
//        Log.e("month", currentDate);
        return Integer.parseInt(currentDate);

    }

    private void getPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(MainActivity.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, 1);
            } else {
                Toast.makeText(baseContext, "Already have a permission", Toast.LENGTH_SHORT).show();

//                ArrayList<String> allList = getAllSms();
                ArrayList<String> bankList = new ArrayList<>();

                /*for (int i = 0; i < allList.size(); i++) {
                    if (allList.get(i).contains("HDFCBK")) {
                        bankList.add(allList.get(i));
                    } else {

                    }

                }*/


//                Log.e("SMSList: bankList", temp.getTime().toString());



                smsList = getSmsData();

                Log.e("SMSList", smsList.toString());

                /*if (smsList != null) {
                    adapterSMS = new AdapterSMSData(MainActivity.this, smsList);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(baseContext);
                    rcvSms.setLayoutManager(mLayoutManager);
                    rcvSms.setAdapter(adapterSMS);
                } else {
                    Toast.makeText(baseContext, "No message to show", Toast.LENGTH_SHORT).show();
                }*/

               /* Sms temp = getSmsData().get(0);

                Log.e("SMSList1", temp.getAddress());
                Log.e("SMSList2", temp.getMessage());
                Log.e("SMSList3", temp.getTime());*/

//                adapterSMS.notifyDataSetChanged();


            }
        } else {
//            adapterSMS.notifyDataSetChanged();

        }

    }

    public ArrayList<Sms> getSmsData() {

        Activity mActivity = getActivity();

        ArrayList<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Telephony.Sms.Inbox.CONTENT_URI;
        ContentResolver cr = null;
        if (mActivity != null) {
            cr = mActivity.getContentResolver();
        }

        c = cr.query(message, null, null, null, null);
        mActivity.startManagingCursor(c);
        int totalSMS = c.getCount();
        int j = 0;

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {


                String smsAdd = c.getString(c.getColumnIndexOrThrow("address"));
                String smsTxt = c.getString(c.getColumnIndexOrThrow("body"));
                String smsDate = c.getString(c.getColumnIndexOrThrow("date"));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date currentTimeZone = new Date(Long.parseLong(smsDate));
                String msgWholeDate = sdf.format(currentTimeZone);
                String[] splitDate = msgWholeDate.split("-");
                int msgYear = Integer.parseInt(splitDate[0]);
                int msgMonth = Integer.parseInt(splitDate[1]);
                int msgDate = Integer.parseInt(splitDate[2]);



                if (smsTxt.toLowerCase().contains("debited")
//                        && getDate() == msgDate
//                        && getMonth() == msgMonth
                        && getYear() == msgYear) {
                    objSms = new Sms();

                    objSms.setAddress(smsAdd);
                    objSms.setMessage(smsTxt);
                    objSms.setTime(smsDate);
                    lstSms.add(objSms);
//                    j++;
//                    Log.e("getAmount_call",""+j);
                    getAmount(smsTxt);
                }
                /*if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }*/


                c.moveToNext();
            }
        }

        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();


        return lstSms;
    }



    private void getAmount(String smsTxt) {


//        String[] splitTxt = smsTxt.split(" ");
        smsExpenses = new SmsExpenses();

        Pattern p = Pattern.compile("\\d+");

        Matcher m = p.matcher(smsTxt.replace(",",""));
        if(m.find()) {
            smsExpenses.setAmount(m.group(0));
            expensesList.add(smsExpenses);
            Log.e("expense",gson.toJson(smsExpenses));
//            System.out.println(m.group());
        }
        /*for (int i = 0; i <= splitTxt.length - 1; i++) {
            expenses = new Expenses();
            if (*//*(Pattern.matches("[0-9]+,[0-9]+\\.[0-9]+", splitTxt[i])
                    || Pattern.matches("[0-9]+\\.[0-9]+", splitTxt[i]))
                    || Pattern.matches("[Rs]\\.[0-9],[0-9]+\\.[0-9]+", splitTxt[i])
                    || Pattern.matches("[Rs]\\.[0-9]+\\.[0-9]+", splitTxt[i])*//*)
            {
                expenses.setAmount(splitTxt[i]);
                expensesList.add(expenses);
                if(expenses.getAmount() != null)
                Log.e("expenseList", expenses.getAmount());
                break;
            }


        }*/

        /*if(amount != null){

            Log.e("numbers", amount.get(0));
        }*/

    }


}