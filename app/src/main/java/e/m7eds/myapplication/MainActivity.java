package e.m7eds.myapplication;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.List;

import e.m7eds.myapplication.Database.AppDatabase;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity implements ClientRecyclerAdapter.ItemClickListener{

    private final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView rv_clients;
    private ClientRecyclerAdapter clientRecyclerAdapter;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddClientActivity.class));
            }
        });

        rv_clients = findViewById(R.id.rv_clients);
        rv_clients.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        rv_clients.addItemDecoration(decoration);
        clientRecyclerAdapter = new ClientRecyclerAdapter(this, this);
        rv_clients.setAdapter(clientRecyclerAdapter);
        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Client> clientList = clientRecyclerAdapter.getClientList();
                        mDb.clientDao().deleteClient(clientList.get(position));
                    }
                });

            }
        }).attachToRecyclerView(rv_clients);

        mDb = AppDatabase.getInstance(getApplicationContext());

        retrieveClients();
    }


    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(this, AddClientActivity.class);
        intent.putExtra(AddClientActivity.EXTRA_CLIENT_ID, itemId);
        startActivity(intent);
    }

    private void retrieveClients(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getClients().observe(this, new Observer<List<Client>>() {
            @Override
            public void onChanged(@Nullable List<Client> clients) {
                Log.d(TAG, "Updating list of the clients from LiveData in ViewModel");
                clientRecyclerAdapter.setClients(clients);
            }
        });
    }
}
