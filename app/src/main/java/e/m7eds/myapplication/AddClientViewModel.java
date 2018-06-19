package e.m7eds.myapplication;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import e.m7eds.myapplication.Database.AppDatabase;

public class AddClientViewModel extends ViewModel {

    private LiveData<Client> client;

    public AddClientViewModel(AppDatabase database, int clientId) {
        client = database.clientDao().loadClientById(clientId);
    }

    public LiveData<Client> getClient() {
        return client;
    }
}
