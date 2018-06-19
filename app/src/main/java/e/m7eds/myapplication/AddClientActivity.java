package e.m7eds.myapplication;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import e.m7eds.myapplication.Database.AppDatabase;

public class AddClientActivity extends AppCompatActivity {

    private final String TAG = AddClientActivity.class.getSimpleName();
    private EditText et_name;
    private EditText et_company;
    private EditText et_value;
    private Button btn_register;

    private AppDatabase mDb;

    public static final String EXTRA_CLIENT_ID = "extraClientId";

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_CLIENT_ID = -1;
    private int mClientID = DEFAULT_CLIENT_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        et_name = findViewById(R.id.et_name);
        et_company = findViewById(R.id.et_company);
        et_value = findViewById(R.id.et_value);
        btn_register = findViewById(R.id.btn_register);

        mDb = AppDatabase.getInstance(getApplicationContext());

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClient();
            }
        });

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_CLIENT_ID)){
            btn_register.setText("Update");
            if(mClientID == DEFAULT_CLIENT_ID){
                mClientID = intent.getIntExtra(EXTRA_CLIENT_ID, DEFAULT_CLIENT_ID);
                AddClientViewModelFactory factory = new AddClientViewModelFactory(mDb, mClientID);
                final AddClientViewModel viewModel = ViewModelProviders.of(this, factory).get(AddClientViewModel.class);
                viewModel.getClient().observe(this, new Observer<Client>() {
                    @Override
                    public void onChanged(@Nullable Client clientEntry) {
                        //Usually, we do not need to remove the observer as we want LiveData to reflect the state of the underlying data. In our case we are doing a one-time load, and we don’t want to listen to changes in the database.
                        viewModel.getClient().removeObserver(this);
                        Log.d(TAG, "Receiving database update from LiveData");
                        populateUI(clientEntry);
                    }
                });
            }
        }
    }

    private void addClient(){
        String name = et_name.getText().toString();
        String company = et_company.getText().toString();
        int value = Integer.parseInt(et_value.getText().toString());
        Date date = new Date();
        final Client client = new Client(name, company, value, date);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(mClientID == DEFAULT_CLIENT_ID){
                    mDb.clientDao().insertClient(client);
                }else{
                    client.setId(mClientID);
                    mDb.clientDao().updateClient(client);
                }
                finish();
            }
        });
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param client the taskEntry to populate the UI
     */
    private void populateUI(Client client) {
        if (client == null) {
            return;
        }
        et_name.setText(client.getName());
        et_company.setText(client.getCompany());
        et_value.setText(String.valueOf(client.getValue()));

    }
}
