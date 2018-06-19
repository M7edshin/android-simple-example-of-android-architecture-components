package e.m7eds.myapplication;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import e.m7eds.myapplication.Database.AppDatabase;

public class AddClientViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mClientID;

    public AddClientViewModelFactory(AppDatabase mDb, int mClientID) {
        this.mDb = mDb;
        this.mClientID = mClientID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddClientViewModel(mDb, mClientID);
    }


}
